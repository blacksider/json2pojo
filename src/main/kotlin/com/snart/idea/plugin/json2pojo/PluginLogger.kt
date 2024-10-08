package com.snart.idea.plugin.json2pojo

import com.intellij.openapi.diagnostic.Logger
import org.jsonschema2pojo.AbstractRuleLogger

class PluginLogger : AbstractRuleLogger() {
    private val log: Logger = Logger.getInstance(PluginLogger::class.java)

    override fun isDebugEnabled(): Boolean {
        return log.isDebugEnabled
    }

    override fun isErrorEnabled(): Boolean {
        return true
    }

    override fun isInfoEnabled(): Boolean {
        return true
    }

    override fun isTraceEnabled(): Boolean {
        return log.isTraceEnabled
    }

    override fun isWarnEnabled(): Boolean {
        return true
    }

    override fun doDebug(msg: String?) {
        log.debug(msg)
    }

    override fun doError(msg: String?, e: Throwable?) {
        log.error(msg, e)
    }

    override fun doInfo(msg: String?) {
        log.info(msg)
    }

    override fun doTrace(msg: String?) {
        log.trace(msg)
    }

    override fun doWarn(msg: String?, e: Throwable?) {
        log.warn(msg, e)
    }

}