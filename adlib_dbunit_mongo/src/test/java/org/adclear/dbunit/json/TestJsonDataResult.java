package org.adclear.dbunit.json;

import java.lang.annotation.ElementType;
import java.lang.reflect.Method;

import junit.framework.Assert;

import org.adclear.dbunit.json.annotations.JsonData;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;

/**
 * The junit test class for the {@link JsonDataResult} class.
 * 
 * @author fit
 * 
 */
@JsonData
public class TestJsonDataResult {

	@Test
	public void testClassTarget() throws SecurityException,
			NoSuchMethodException {
		Method method = this.getClass().getMethod("testClassTarget",
				new Class[0]);
		JsonDataResult jsonDataResult = new JsonDataResult(this.getClass()
				.getAnnotation(JsonData.class), ElementType.TYPE,
				new FrameworkMethod(method));
		
		Assert.assertEquals("TestJsonDataResult.json",
				jsonDataResult.getFileName());
	}

	@Test
	@JsonData
	public void testMethodTarget() throws SecurityException,
			NoSuchMethodException {
		Method method = this.getClass().getMethod("testMethodTarget",
				new Class[0]);
		JsonDataResult jsonDataResult = new JsonDataResult(
				method.getAnnotation(JsonData.class), ElementType.METHOD,
				new FrameworkMethod(method));
		
		Assert.assertEquals("TestJsonDataResult-testMethodTarget.json",
				jsonDataResult.getFileName());
	}

	@Test
	@JsonData(fileName = "TestFile.json")
	public void testMethodTargetWithFileName() throws SecurityException,
			NoSuchMethodException {
		Method method = this.getClass().getMethod(
				"testMethodTargetWithFileName", new Class[0]);
		JsonDataResult jsonDataResult = new JsonDataResult(
				method.getAnnotation(JsonData.class), ElementType.METHOD,
				new FrameworkMethod(method));
		
		Assert.assertEquals("TestFile.json", jsonDataResult.getFileName());
	}
}
