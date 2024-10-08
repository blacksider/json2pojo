package com.snart.idea.plugin.json2pojo

import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.sun.codemodel.JCodeModel
import org.jsonschema2pojo.ContentResolver
import org.jsonschema2pojo.GenerationConfig
import org.jsonschema2pojo.Jackson2Annotator
import org.jsonschema2pojo.SchemaGenerator
import org.jsonschema2pojo.SchemaMapper
import org.jsonschema2pojo.SchemaStore
import org.jsonschema2pojo.rules.RuleFactory
import java.io.File


class PojoGenerationTask(
    private val project: Project,
    private val directory: PsiDirectory,
    private val sourceRoot: VirtualFile,
    private val packageName: String,
    private val jsonInputDialog: JsonInputDialog
) : Task.Backgroundable(project, "Java class generation", false) {
    override fun run(indicator: ProgressIndicator) {
        val className = jsonInputDialog.className
        val jsonInput = jsonInputDialog.content

        val config: GenerationConfig = PojoGenerationConfig(
            jsonInputDialog.sourceType,
            jsonInputDialog.generateHashCodeAndEquals,
            jsonInputDialog.generateToString
        )

        val logger = PluginLogger()
        val ruleFactory = RuleFactory(config, Jackson2Annotator(config), SchemaStore(ContentResolver(), logger))
        ruleFactory.logger = logger

        val mapper = SchemaMapper(ruleFactory, SchemaGenerator())

        val codeModel = JCodeModel()

        mapper.generate(codeModel, className, packageName, jsonInput)

        codeModel.build(File(sourceRoot.path))

        try {
            Thread.sleep(100)
            ProjectView.getInstance(project).refresh()
            directory.virtualFile.refresh(false, true)
        } catch (ignored: InterruptedException) {
        }
    }

}