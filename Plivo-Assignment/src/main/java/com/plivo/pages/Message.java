package com.plivo.pages;

import java.util.Map;

import com.jayway.restassured.response.Response;
import com.plivo.base.CommonUtility;
import com.plivo.base.RestAssuredUtility;

public class Message {
	
	public Response sendMessage(Map<String, String> formParam) {
		String uri = CommonUtility.getProperty("test.properties", "msgUri");
		return RestAssuredUtility.post(uri, formParam);
	}
	
	public Response getMsgDetails(String msgUUID) {
		String uri = CommonUtility.getProperty("test.properties", "msgUri");
		return RestAssuredUtility.get(uri + msgUUID);
	}
}
