#!/bin/bash
set -euo pipefail

# Code fully written by Claude

# ---------------------------------------------------------------------------
# Script: modify_lexer.sh
# Purpose: Apply a set of source transformations to a generated lexer Java
#          file (e.g. produced by JFlex for org.intellij.markdown.lexer).
#
# Usage:   ./modify_lexer.sh <path-to-java-file>
# ---------------------------------------------------------------------------

# ----- CONFIG --------------------------------------------------------------
# Path to the target Java file. Pass as first arg, or hardcode below.
FILE="${1:-./_RFMLexer.java}"   # <-- BLANK: fill in a default path here if you don't want
                #     to pass it as an argument, e.g.:
                # FILE="${1:-/path/to/_MarkdownLexer.java}"

if [[ -z "$FILE" ]]; then
    echo "Usage: $0 <path-to-java-file>"
    exit 1
fi

if [[ ! -f "$FILE" ]]; then
    echo "Error: file not found: $FILE"
    exit 1
fi

# The name of the stack field/variable used for push/pop -> add/remove.
# Adjust this if the generated lexer uses a different variable name.
STACK_VAR="stateStack"   # <-- BLANK: confirm this matches the actual field name in the file

# Backup before editing
cp "$FILE" "${FILE}.bak"
echo "Backup created at ${FILE}.bak"

# ----- 1. Add imports -------------------------------------------------------
# Insert the new imports right after the package declaration line.
# If any of these imports already exist, they will be skipped.

IMPORTS=(
  "import org.intellij.markdown.lexer.GeneratedLexer;"
  "import java.util.ArrayList;"
  "import java.util.BitSet;"
  "import java.util.HashSet;"
  "import java.util.List;"
  "import java.util.Set;"
)

PACKAGE_LINE=$(grep -n '^package ' "$FILE" | head -1 | cut -d: -f1)

if [[ -z "$PACKAGE_LINE" ]]; then
    echo "Warning: no 'package' line found; prepending imports to top of file."
    PACKAGE_LINE=0
fi

for IMPORT in "${IMPORTS[@]}"; do
    if ! grep -qF "$IMPORT" "$FILE"; then
        sed -i "${PACKAGE_LINE}a\\${IMPORT}" "$FILE"
        # Move insertion point down as lines are added, and keep imports
        # grouped right after the package line.
        PACKAGE_LINE=$((PACKAGE_LINE + 1))
    else
        echo "Import already present, skipping: $IMPORT"
    fi
done

# ----- 2. Replace .push(...) with .add(...) and .pop() with .remove(size-1) -
# .push(x)  ->  .add(x)
sed -i -E "s/\.push\(/.add(/g" "$FILE"

# .pop()    ->  .remove(<STACK_VAR>.size() - 1)
# Handles both `something.pop()` generically by replacing the trailing
# `.pop()` call. Assumes the stack variable name matches STACK_VAR above.
sed -i -E "s/\.pop\(\)/.remove(${STACK_VAR}.size() - 1)/g" "$FILE"

sed -i -E "s/throws java.io.IOException//g" "$FILE"

# ----- 3. Remove the constructor that takes an `in` parameter --------------
# This looks for a single-argument constructor of the form:
#   public <ClassName>(java.io.Reader in) { ... }
# matched by parameter TYPE (java.io.Reader) rather than parameter NAME,
# which is more robust and avoids relying on \b word-boundaries (unsupported
# in mawk, the default awk in this environment).
#
# NOTE: This is a best-effort removal using awk to track brace depth,
# since constructors can span multiple lines. Adjust CLASS_NAME below
# if the heuristic doesn't pick up the right constructor.

CLASS_NAME="_RFMLexer"   # hardcoded based on confirmed constructor signature

# Fallback auto-detection, only used if the hardcoded name above is ever cleared
if [[ -z "$CLASS_NAME" ]]; then
    CLASS_NAME=$(grep -oP '(?<=class )\w+' "$FILE" | head -1)
    if [[ -z "$CLASS_NAME" ]]; then
        echo "Warning: could not determine class name automatically."
    fi
fi

awk -v class="$CLASS_NAME" '
BEGIN { skip=0; depth=0; found=0 }
{
    # Match: public <ClassName>(java.io.Reader in) {  (or any Reader-typed
    # single-arg constructor). Avoids \b word-boundary since mawk does not
    # support it.
    if (!found && $0 ~ class"\\(java\\.io\\.Reader[ \t]+[A-Za-z_][A-Za-z0-9_]*\\)" && $0 ~ /\{/) {
        skip=1
        found=1
        depth=1
        next
    }
    if (skip) {
        # count braces to know when constructor block ends
        n_open = gsub(/\{/, "{")
        n_close = gsub(/\}/, "}")
        depth += n_open - n_close
        if (depth <= 0) {
            skip=0
        }
        next
    }
    print
}
' "$FILE" > "${FILE}.tmp" && mv "${FILE}.tmp" "$FILE"

# ----- 4. Add the default override for getState ----------------------------
# _RFMLexer has no existing getState() method, so this method is INSERTED
# (not replaced). If an existing getState() is ever found, its body is
# replaced in place instead, to avoid creating a duplicate method.

RETURN_TYPE="int"      # confirmed against actual _RFMLexer.getState() override
DEFAULT_VALUE="0"      # confirmed against actual _RFMLexer.getState() override

if grep -qE 'getState[ \t]*\(' "$FILE"; then
    # Existing getState() found -> replace its body in place.
    awk -v rtype="$RETURN_TYPE" -v defval="$DEFAULT_VALUE" '
    BEGIN { skip=0; depth=0; found=0 }
    {
        if (!found && $0 ~ /getState[ \t]*\(/ && $0 ~ /\{/) {
            skip=1
            found=1
            depth=1
            print "  @Override"
            print "  public " rtype " getState() {"
            print "    return " defval ";"
            print "  }"
            next
        }
        if (skip) {
            n_open = gsub(/\{/, "{")
            n_close = gsub(/\}/, "}")
            depth += n_open - n_close
            if (depth <= 0) {
                skip=0
            }
            next
        }
        print
    }
    ' "$FILE" > "${FILE}.tmp" && mv "${FILE}.tmp" "$FILE"
else
    # No existing getState() -> insert it just before the class's final
    # closing brace (assumed to be the last line containing only "}").
    awk -v rtype="$RETURN_TYPE" -v defval="$DEFAULT_VALUE" '
    { lines[NR] = $0 }
    END {
        last_brace = 0
        for (i = NR; i >= 1; i--) {
            if (lines[i] ~ /^[ \t]*}[ \t]*$/) {
                last_brace = i
                break
            }
        }
        for (i = 1; i <= NR; i++) {
            if (i == last_brace) {
                print "  @Override"
                print "  public " rtype " getState() {"
                print "    return " defval ";"
                print "  }"
                print ""
            }
            print lines[i]
        }
    }
    ' "$FILE" > "${FILE}.tmp" && mv "${FILE}.tmp" "$FILE"
fi

echo "Done. Modified file: $FILE"
echo "Original backed up at: ${FILE}.bak"