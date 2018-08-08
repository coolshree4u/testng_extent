package com.plivo.base;

import java.util.Map;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

public class RestAssuredUtility {
	
	private RestAssuredUtility() {}
	
	private static RequestSpecification reqSpec = null;
	
	public static RequestSpecification setBasicAuthentication(){
		
		if(reqSpec==null) {
		String authID = CommonUtility.getProperty("test.properties", "authId");
		String authToken = CommonUtility.getProperty("test.properties", "authToken");
		
		RestAssured.baseURI = CommonUtility.getProperty("test.properties", "baseURI").replaceAll("auth_id", authID);
		
		reqSpec =  RestAssured.given().auth().basic(authID, authToken);
		}
		
		return reqSpec;
	}
	
	public static Response get() {
		return setBasicAuthentication().get();
	}
	
	public static Response get(String uri) {
		return setBasicAuthentication().get(uri);
	}

	public static Response get(String uri, Map<String, String> queryParam) {
		return setBasicAuthentication().queryParameters(queryParam).get(uri);
	}

	public static Response post(String uri, Map<String, String> formParam) {
		return setBasicAuthentication().formParameters(formParam).post(uri);
	}
	
	public static Object getNodeValue(String response, String node) {
		return JsonPath.with(response).get(node);
	}

}
