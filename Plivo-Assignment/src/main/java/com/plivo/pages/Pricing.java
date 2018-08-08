package com.plivo.pages;

import java.util.Map;

import com.jayway.restassured.response.Response;
import com.plivo.base.CommonUtility;
import com.plivo.base.RestAssuredUtility;

public class Pricing {

	public Response getPricing(Map<String, String> queryParam) {
		String uri = CommonUtility.getProperty("test.properties", "pricingUri");
		return RestAssuredUtility.get(uri, queryParam);
	}
}
