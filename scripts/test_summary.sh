#!/bin/bash

set -euo pipefail

echo "Running test suite..."
if bazel test //src/test/... --test_output=errors --test_summary=detailed 2>&1 | tee test_output.txt; then
    TEST_STATUS="success"
    TEST_ICON=":white_check_mark:"
else
    TEST_STATUS="error"
    TEST_ICON=":x:"
fi

# Count test results
TOTAL_TESTS=$(grep -c "PASSED\|FAILED" test_output.txt || echo "0")
PASSED_TESTS=$(grep -c "PASSED" test_output.txt || echo "0")
FAILED_TESTS=$(grep -c "FAILED" test_output.txt || echo "0")

# Create test summary annotation
buildkite-agent annotate --style "$TEST_STATUS" --context "test-summary" <<EOF
### ${TEST_ICON} Test Summary

- **Total Tests:** ${TOTAL_TESTS}
- **Passed:** ${PASSED_TESTS} ✅
- **Failed:** ${FAILED_TESTS} ❌

---

\`\`\`
$(tail -20 test_output.txt)
\`\`\`
EOF

echo "Test summary annotation created"
rm -f test_output.txt
