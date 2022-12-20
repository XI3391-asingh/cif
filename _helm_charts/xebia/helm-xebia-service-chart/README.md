# app

![Version: 1.0.10](https://img.shields.io/badge/Version-1.0.10-informational?style=flat-square)  ![AppVersion: 1.0.0](https://img.shields.io/badge/AppVersion-1.0.0-informational?style=flat-square)

## Description

A Helm chart for deploying microservices at Xebia

## Maintainers

| Name | Email | Url |
| ---- | ------ | --- |
| XI3391-asingh | <abhishek.singh5@xebia.com> |  |

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| autoscaling.enabled | bool | `false` |  |
| autoscaling.maxReplicas | int | `10` |  |
| autoscaling.minReplicas | int | `2` |  |
| autoscaling.targetCPUUtilizationPercentage | int | `80` |  |
| config | object | `{}` |  |
| deployment.affinity | object | `{}` |  |
| deployment.annotations | object | `{}` |  |
| deployment.component | string | `"api"` |  |
| deployment.containerPort.http | int | `8080` |  |
| deployment.envFrom | list | `[]` |  |
| deployment.extraEnvs | list | `[]` |  |
| deployment.image.pullPolicy | string | `"IfNotPresent"` |  |
| deployment.image.registry | string | `""` |  |
| deployment.image.repository | string | `"nginx"` |  |
| deployment.image.tag | string | `"latest"` |  |
| deployment.imagePullSecrets | list | `[]` |  |
| deployment.livenessProbe | object | `{}` |  |
| deployment.nodeSelector | object | `{}` |  |
| deployment.podAnnotations | object | `{}` |  |
| deployment.podSecurityContext | object | `{}` |  |
| deployment.readinessProbe | object | `{}` |  |
| deployment.replicaCount | int | `2` |  |
| deployment.resources.limits.cpu | string | `"100m"` |  |
| deployment.resources.limits.memory | string | `"128Mi"` |  |
| deployment.resources.requests.cpu | string | `"50m"` |  |
| deployment.resources.requests.memory | string | `"100Mi"` |  |
| deployment.securityContext.privileged | bool | `false` |  |
| deployment.startupProbe | object | `{}` |  |
| deployment.tolerations | list | `[]` |  |
| deployment.volumeMounts | list | `[]` |  |
| deployment.volumes | list | `[]` |  |
| environment | string | `"dev"` |  |
| fullnameOverride | string | `""` |  |
| ingress.annotations."alb.ingress.kubernetes.io/scheme" | string | `"internal"` |  |
| ingress.annotations."kubernetes.io/ingress.class" | string | `"alb"` |  |
| ingress.enabled | bool | `false` |  |
| ingress.rules[0].http.paths[0].backend.service.name | string | `"istio-ingressgateway"` |  |
| ingress.rules[0].http.paths[0].backend.service.port.number | int | `80` |  |
| ingress.rules[0].http.paths[0].path | string | `"/"` |  |
| ingress.rules[0].http.paths[0].pathType | string | `"Prefix"` |  |
| istio.authorizationPolicy.allowedNaemspaces | list | `[]` |  |
| istio.authorizationPolicy.enabled | bool | `false` |  |
| istio.destinationRule.enabled | bool | `false` |  |
| istio.enabled | bool | `true` |  |
| istio.gateway.enabled | bool | `false` |  |
| istio.gateway.ingressHost | string | `"*"` |  |
| istio.gateway.portName | string | `"http"` |  |
| istio.gateway.portNumber | int | `80` |  |
| istio.gateway.protocol | string | `"HTTP"` |  |
| istio.virtualService.enabled | bool | `true` |  |
| istio.virtualService.gateway | string | `"mesh"` |  |
| istio.virtualService.host | string | `"*"` |  |
| istio.virtualService.timeout | string | `"1m"` |  |
| istio.virtualService.uriPrefix[0] | string | `"/"` |  |
| istio.virtualService.uriPrefix[1] | string | `"/home"` |  |
| namespace | string | `"test"` |  |
| project | string | `"project-name"` |  |
| secretProviderClass.enabled | bool | `false` |  |
| secretProviderClass.parameters.objects | string | `"- objectName: \"nginx-secret-db\"\n  objectType: \"secretsmanager\"\n"` |  |
| secretProviderClass.provider | string | `"aws"` |  |
| service.annotations | object | `{}` |  |
| service.ports[0].name | string | `"http"` |  |
| service.ports[0].port | int | `8080` |  |
| service.ports[0].targetPort | int | `8080` |  |
| service.type | string | `"ClusterIP"` |  |
| serviceAccount.annotations | object | `{}` |  |
| serviceAccount.name | string | `"test"` |  |
| serviceMonitor.additionalLabels | object | `{}` |  |
| serviceMonitor.enabled | bool | `false` |  |
| serviceMonitor.path | string | `"/actuator/prometheus"` |  |
| serviceMonitor.port | string | `"http"` |  |
| test.enabled | bool | `false` |  |
| test.testPath | string | `"/health"` |  |