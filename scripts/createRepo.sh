#!/bin/bash

if [ -z "$REPO" ] || [ -z "$REGION" ] || [ -z "$PROJECT" ]
  then
    echo "Error: missing neccessary argument"
    exit 1
fi
ECR_ARN=$(aws ecr create-repository --repository-name ${REPO} --image-tag-mutability IMMUTABLE --image-scanning-configuration scanOnPush=true --region ${REGION}|jq -r '.repository .repositoryArn')
if [ ! -z ${ECR_ARN} ];
then
    aws ecr set-repository-policy \
    --repository-name ${REPO} \
    --policy-text file://ecr-permissions-policy.json \
    --region ${REGION} && {
        aws ecr put-lifecycle-policy \
        --repository-name ${REPO} \
        --lifecycle-policy-text file://ecr-lifecycle-policy.json \
        --region ${REGION} && {
            aws ecr tag-resource \
            --resource-arn ${ECR_ARN} \
            --region ${REGION} \
            --tags Key=Name,Value=${REPO} Key=Owner,Value=${PROJECT} Key=Type,Value=backend
        } || exit 1
    } || exit 1
fi 
exit 0
