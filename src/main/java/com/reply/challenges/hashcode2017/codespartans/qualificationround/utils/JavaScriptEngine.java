package com.reply.challenges.hashcode2017.codespartans.qualificationround.utils;

import java.io.InputStreamReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class JavaScriptEngine {

	private static Logger logger;
	private static Invocable jsEngine;

	static {
		logger = LoggerFactory.getLogger(JavaScriptEngine.class);
		
		final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		final ScriptEngine baseEngine = scriptEngineManager.getEngineByName("nashorn");
		try {
			baseEngine.eval(
					new InputStreamReader(JavaScriptEngine.class.getClassLoader().getResourceAsStream("main.js")));
			JavaScriptEngine.jsEngine = (Invocable) baseEngine;
		} catch (ScriptException e) {
			logger.error("Cannot initialize javascript engine");
		}
	}

	@SuppressWarnings("unchecked")
	public static final <T> T call(final String functionName, Object... parameters) throws NoSuchMethodException, ScriptException {
		return (T) jsEngine.invokeFunction(functionName, parameters);
	}
}
