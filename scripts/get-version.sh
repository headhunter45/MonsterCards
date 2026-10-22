#!/usr/bin/env bash
set -euo pipefail

# Determine current or incremented development version based on Git tags
# - If HEAD is on an exact tag (e.g., v1.2.3), uses that version.
# - If HEAD is untagged, finds the nearest ancestor tag (e.g., v1.2.3),
#   increments the patch version by 1 (+0.0.1), and appends "-dev" (e.g., 1.2.4-dev).

git fetch --tags --force 2>/dev/null || true

CURRENT_TAG=$(git describe --tags --exact-match 2>/dev/null || true)

if [ -n "$CURRENT_TAG" ]; then
  RAW_TAG="${CURRENT_TAG#v}"
  V_TAG="v${RAW_TAG}"
else
  NEAREST_TAG=$(git describe --tags --abbrev=0 2>/dev/null || true)
  if [ -z "$NEAREST_TAG" ]; then
    # Fallback to current project baseline if no git tags exist in history
    NEAREST_TAG="v0.6.0"
  fi

  CLEAN_TAG="${NEAREST_TAG#v}"

  IFS='.' read -r MAJOR MINOR PATCH <<< "$CLEAN_TAG"
  MAJOR="${MAJOR:-0}"
  MINOR="${MINOR:-0}"
  PATCH="${PATCH%%-*}"
  PATCH="${PATCH:-0}"

  NEXT_PATCH=$((PATCH + 1))

  RAW_TAG="${MAJOR}.${MINOR}.${NEXT_PATCH}-dev"
  V_TAG="v${RAW_TAG}"
fi

case "${1:-}" in
  --prefix|--tag)
    echo "$V_TAG"
    ;;
  --github-env)
    if [ -n "${GITHUB_OUTPUT:-}" ]; then
      echo "version_name=$RAW_TAG" >> "$GITHUB_OUTPUT"
      echo "version_tag=$V_TAG" >> "$GITHUB_OUTPUT"
    fi
    echo "Resolved version: $RAW_TAG (tag: $V_TAG)"
    ;;
  *)
    echo "$RAW_TAG"
    ;;
esac
