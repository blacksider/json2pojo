package com.snart.idea.plugin.json2pojo

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.psi.PsiDirectory
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.bind
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.rows
import com.snart.idea.plugin.json2pojo.utils.NotificationUtils
import org.jsonschema2pojo.SourceType

class JsonInputDialog(
    private val project: Project,
    private val directory: PsiDirectory
) : DialogWrapper(project, true) {
    companion object {
        private const val DEFAULT_CONTENT_ROWS = 10
    }

    var className = ""
    var content = ""
    var generateHashCodeAndEquals = false
    var generateToString = false
    var sourceType = SourceType.JSON

    init {
        title = "Json Input"
        init()
    }

    override fun createCenterPanel() = panel {
        row("Class name:") {
            textField()
                .bindText(::className)
                .align(Align.FILL)
        }
        buttonsGroup("Source type:", indent = false) {
            row {
                radioButton("Json", SourceType.JSON)
                radioButton("Json schema", SourceType.JSONSCHEMA)
            }
        }.bind({ sourceType }, { sourceType = it })
        row("Content:") {
        }
        row {
            textArea().bindText(::content)
                .rows(DEFAULT_CONTENT_ROWS)
                .align(Align.FILL)
        }
        collapsibleGroup("Options") {
            row {
                checkBox("Generate hashCode() & equals()")
                    .bindSelected(::generateHashCodeAndEquals)
            }
            row {
                checkBox("Generate toString()")
                    .bindSelected(::generateToString)
            }
        }.expanded = true
    }

    override fun doOKAction() {
        if (okAction.isEnabled) {
            applyFields()
        }

        if (!validateFields()) {
            return
        }

        close(OK_EXIT_CODE)
    }

    private fun validateFields(): Boolean {
        return validateClassName() && validateJson()
    }

    private fun validateClassName(): Boolean {
        if (className.isBlank()) {
            displayWarning("Please input class name.")
            return false
        }

        val fileName = "$className.java"
        if (directory.findFile(fileName) != null) {
            displayWarning("File '$fileName' already exists.")
            return false
        }

        return true
    }

    private fun validateJson(): Boolean {
        if (content.isBlank()) {
            displayWarning("Please input json.")
            return false
        }
        return true
    }

    private fun displayWarning(message: String) {
        NotificationUtils.warning(project, message)
    }
}