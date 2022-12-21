{{/* vim: set filetype=mustache: */}}
{{/*
namespace
*/}}
{{- define "app.namespace" -}}
{{- default .Release.Namespace .Values.namespace | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "app.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- if contains .Chart.Name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name .Chart.Name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "app.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "app.labels" -}}
helm.sh/chart: {{ include "app.chart" . }}
app.kubernetes.io/version: {{ .Values.deployment.image.tag | quote}}
app.kubernetes.io/component: {{ .Values.deployment.component }}
app.kubernetes.io/part-of: {{ .Values.project}}
app.kubernetes.io/managed-by: helm
{{ include "app.selectorLabels" . }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "app.selectorLabels" -}}
app: {{include "app.fullname" .}}
app.kubernetes.io/name: {{ .Release.Name }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "app.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "app.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{/*
Prepare container image name
*/}}
{{- define "app.image"}}
{{- if eq .Values.deployment.image.registry ""}}
{{- .Values.deployment.image.repository }}:{{ .Values.deployment.image.tag | default .Chart.AppVersion }}
{{- else}}
{{- .Values.deployment.image.registry }}/{{ .Values.deployment.image.repository }}:{{ .Values.deployment.image.tag | default .Chart.AppVersion }}
{{- end}}
{{- end}}


{{/*
Prepare namespaces for Authorization Policy
*/}}
{{- define "istio.authorizationPolicyNs" }}
{{- if eq (len .Values.istio.authorizationPolicy.allowedNaemspaces) 0}}
{{- toYaml (list .Values.namespace) }}
{{- else }}
{{- toYaml .Values.istio.authorizationPolicy.allowedNaemspaces }}
{{- end }}
{{- end }}