package com.snart.idea.plugin.json2pojo

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.roots.PackageIndex
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.impl.file.PsiJavaDirectoryFactory


class Json2PojoAction : AnAction() {
    override fun update(event: AnActionEvent) {
        val project = event.project ?: return
        val psiDirectory = getCurrentDirectory(event)
        if (psiDirectory != null) {
            // Check if the directory is a Java package
            event.presentation.isEnabledAndVisible = PsiJavaDirectoryFactory
                .getInstance(project)
                .isPackage(psiDirectory)
        } else {
            event.presentation.isEnabledAndVisible = false
        }
    }

    private fun getCurrentDirectory(event: AnActionEvent): PsiDirectory? {
        return event.getData(CommonDataKeys.PSI_ELEMENT) as? PsiDirectory
    }


    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project ?: return
        val directory = getCurrentDirectory(event) ?: return

        val jsonInputDialog = JsonInputDialog(project, directory)
        if (!jsonInputDialog.showAndGet()) {
            return
        }

        val sourceRoot =
            ProjectRootManager.getInstance(project).fileIndex.getSourceRootForFile(directory.virtualFile)
                ?: return
        val packageName =
            PackageIndex.getInstance(project).getPackageNameByDirectory(directory.virtualFile) ?: return
        ProgressManager.getInstance().run(
            PojoGenerationTask(
                project,
                directory,
                sourceRoot,
                packageName,
                jsonInputDialog
            )
        )
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}