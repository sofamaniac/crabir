package com.sofamaniac.lint_rules

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.Issue

class CrabirIssueRegistry : IssueRegistry() {
    override val issues: List<Issue> = listOf(
        HardcodedTextDetector.ISSUE
    )

    override val api: Int = 8

    override val vendor: Vendor = Vendor(vendorName = "crabir")
}