package org.adclear.dbunit.json;

import java.lang.annotation.ElementType;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.adclear.dbunit.json.annotations.JsonData;
import org.junit.runners.model.FrameworkMethod;

/**
 * @author fit
 * 
 */
@Getter
@AllArgsConstructor
public class JsonDataResult {

	private JsonData jsonData;

	private ElementType elementType;

	private FrameworkMethod testMethod;

	public boolean isJsonDataPresent() {
		return jsonData != null;
	}

	public String getFileName() {
		if (this.isJsonDataPresent()) {
			return getFileName(this);
		}
		return null;
	}

	private static String getFileName(JsonDataResult jsonDataResult) {
		JsonData data = jsonDataResult.getJsonData();
		String dataSetFileName = data.fileName();
		if (dataSetFileName.isEmpty()) {
			dataSetFileName = getDefaultDataSetFileName(jsonDataResult, "json");
		}
		return dataSetFileName;
	}

	private static String getDefaultDataSetFileName(
			JsonDataResult jsonDataResult, String extension) {
		if (jsonDataResult.getElementType() == ElementType.METHOD) {

			String className = jsonDataResult.getTestMethod().getMethod()
					.getDeclaringClass().getSimpleName();
			return className + "-" + jsonDataResult.getTestMethod().getName()
					+ "." + extension;
		} else if (jsonDataResult.getElementType() == ElementType.TYPE) {

			String className = jsonDataResult.getTestMethod().getMethod()
					.getDeclaringClass().getSimpleName();
			return className + "." + extension;
		}
		return null;
	}
}
