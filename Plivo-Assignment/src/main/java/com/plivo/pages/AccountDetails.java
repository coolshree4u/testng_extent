package com.plivo.pages;

import com.jayway.restassured.response.Response;
import com.plivo.base.RestAssuredUtility;

public class AccountDetails {

	public Response getAccountDetails() {
		return RestAssuredUtility.get();
	}
}
