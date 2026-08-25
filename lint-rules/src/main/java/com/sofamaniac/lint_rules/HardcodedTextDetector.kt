package com.sofamaniac.lint_rules

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.ULiteralExpression
import org.jetbrains.uast.UNamedExpression
import org.jetbrains.uast.kotlin.KotlinStringTemplateUPolyadicExpression

class HardcodedTextDetector : Detector(), SourceCodeScanner {
    override fun getApplicableMethodNames() =
        listOf("Text")

    override fun visitMethodCall(
        context: JavaContext,
        node: UCallExpression,
        method: PsiMethod,
    ) {
        val qualifiedName =
            method.containingClass?.qualifiedName

        if (qualifiedName?.startsWith("androidx.compose.material.Text") == false && !qualifiedName.startsWith(
                "androidx.compose.material3.Text"
            )
        ) {
            return
        }

        val textIndex = method.parameterList.parameters
            .indexOfFirst { it.name == "text" }

        if (textIndex == -1) {
            return
        }

        // Named argument: Text(text = "Hello")
        val textArgument = node.valueArguments
            .firstOrNull { argument ->
                argument is UNamedExpression &&
                        argument.name == "text"
            }
            ?.let { (it as UNamedExpression).expression }
            ?: node.valueArguments.getOrNull(textIndex)
            ?: node.valueArguments.firstOrNull()

        if (textArgument is ULiteralExpression &&
            textArgument.value is String
        ) {
            context.report(
                ISSUE,
                node,
                context.getLocation(textArgument),
                "Hardcoded text should use a string resource"
            )
        } else if (textArgument is KotlinStringTemplateUPolyadicExpression) {
            if (textArgument.operands.all { it is ULiteralExpression && it.value is String }) {
                context.report(
                    ISSUE,
                    node,
                    context.getLocation(textArgument),
                    "Hardcoded text should use a string resource"
                )
            }
        }

    }

    companion object {
        val ISSUE = Issue.create(
            id = "HardcodedComposeText",
            briefDescription = "Hardcoded text in Compose Text",
            explanation = """
        User-visible strings should be defined in string resources
        rather than hardcoded directly in Compose UI.
    """.trimIndent(),
            category = Category.I18N,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                HardcodedTextDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}
