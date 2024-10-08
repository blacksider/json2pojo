package com.snart.idea.plugin.json2pojo.utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

object NotificationUtils {
    fun warning(project: Project, message: String) {
        Messages.showWarningDialog(project, message, "Warning")
    }
}