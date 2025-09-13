#!/bin/bash
# /**
# * App: Customer Registration
# * Package: ai.project-parser
# * File: ai-append.sh
# * Version: 0.1.0
# * Turns: 1
# * Author: codex
# * Date: 2025-09-13T02:16:18Z
# * Exports: main
# * Description: Aggregates repository text files into ai/project-parser/output/ai-files.md
# */
set -e
BASE_DIR=$(git rev-parse --show-toplevel)
OUTPUT_DIR="$BASE_DIR/ai/project-parser/output"
OUT_FILE="$OUTPUT_DIR/ai-files.md"
mkdir -p "$OUTPUT_DIR"
echo "# AI Collected Files" > "$OUT_FILE"
find "$BASE_DIR" -type f \
  ! -path "$BASE_DIR/.git/*" \
  ! -path "$OUTPUT_DIR/*" \
  ! -path "$BASE_DIR/target/*" \
  ! -path "$BASE_DIR/ai/project-parser/output/*" \
  ! -name "ai-files.md" \
  | while read file; do
  if file --mime-type "$file" | grep -q text; then
    echo "\n## $file\n" >> "$OUT_FILE"
    cat "$file" >> "$OUT_FILE"
    echo "\n" >> "$OUT_FILE"
  fi
done
wc -l "$OUT_FILE"
