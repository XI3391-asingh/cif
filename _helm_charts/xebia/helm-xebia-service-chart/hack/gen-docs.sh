#!/bin/bash
# ref: https://github.com/norwoodj/helm-docs
set -eux
CHART_DIR="$(cd "$(dirname "$0")/.." && pwd)"
echo "$CHART_DIR"

echo "Running Helm-Docs"
docker run --rm \
    -v "$CHART_DIR:/helm-docs" \
    -u $(id -u) \
    jnorwood/helm-docs:v1.10.0

if [[ $(git diff --stat) != '' ]]; then
    echo -e '\033[0;31mDocumentation outdated!\033[0m ❌'
    git --no-pager diff --color
    exit 1
else
    echo -e '\033[0;32mDocumentation up to date\033[0m ✔'
fi
