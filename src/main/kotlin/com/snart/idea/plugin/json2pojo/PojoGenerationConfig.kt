package com.snart.idea.plugin.json2pojo

import org.jsonschema2pojo.DefaultGenerationConfig
import org.jsonschema2pojo.SourceType

class PojoGenerationConfig(
    private val sourceType: SourceType = SourceType.JSON,
    private val generateHashCodeAndEquals: Boolean = false,
    private val generateToString: Boolean = false,

    ) : DefaultGenerationConfig() {
    override fun getSourceType(): SourceType {
        return sourceType
    }

    override fun isIncludeGeneratedAnnotation(): Boolean {
        return false
    }

    override fun isIncludeAdditionalProperties(): Boolean {
        return false
    }

    override fun isIncludeHashcodeAndEquals(): Boolean {
        return generateHashCodeAndEquals
    }

    override fun isIncludeToString(): Boolean {
        return generateToString
    }
}