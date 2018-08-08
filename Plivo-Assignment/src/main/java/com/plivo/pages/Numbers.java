package com.plivo.pages;

import com.jayway.restassured.response.Response;
import com.plivo.base.CommonUtility;
import com.plivo.base.RestAssuredUtility;

public class Numbers {

	public Response getAllRentedNumbers() {
		String uri = CommonUtility.getProperty("test.properties", "listAllRentedNumbersUri");
		return RestAssuredUtility.get(uri);
	}
}
