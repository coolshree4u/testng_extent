package com.plivo.tests;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.response.Response;
import com.plivo.base.PageFacotory;
import com.plivo.base.RestAssuredUtility;

public class MessageTest extends PageFacotory{
	
	String number1, number2, msgUUID;
	Float msgCharge, outboundRate, cashCredits;
	
	
	@Test(description="To find any two numbers from get all Numbers api")
	public void getNumbers() {
		Response res = numbers().getAllRentedNumbers();
		Assert.assertEquals(res.getStatusCode(), 200, "Couldn't get the list of numbers");
		
		//Fetching two numbers
		number1 = (String) RestAssuredUtility.getNodeValue(res.asString(), "objects.number[0]");
		number2 = (String) RestAssuredUtility.getNodeValue(res.asString(), "objects.number[1]");
		
		System.out.println("Two numbers identified for messaging: "+ number1+", "+number2+".");
	}
	
	
	
	@Test(description="To send a message from one number to another identified in first test",dependsOnMethods= {"getNumbers"})
	public void sendMessage() {
		//Prepare message
		Map<String, String> msg = new HashMap<>();
		msg.put("src", number1);
		msg.put("dst", number2);
		msg.put("text", "Sample Message");
		
		//Send message
		Response res = message().sendMessage(msg);
		Assert.assertEquals(res.getStatusCode(), 202, "Couldn't send the message");
		
		//Read UUID from response
		msgUUID = (String) RestAssuredUtility.getNodeValue(res.asString(), "message_uuid[0]");
		
		System.out.println("msgUUID: "+msgUUID);
	}
	
	
	
	@Test(description="To read message charge for above message",dependsOnMethods= {"sendMessage"})
	public void readMsgCharge() {
		//Fire message details api
		Response res = message().getMsgDetails(msgUUID);
		Assert.assertEquals(res.getStatusCode(), 200, "Couldn't get the message details");
		
		//Read amount incurred for message
		msgCharge = Float.valueOf((String)RestAssuredUtility.getNodeValue(res.asString(), "total_amount"));
		
		System.out.println("msgCharge: "+msgCharge);
	}
	
	
	
	@Test(description="To get outbound rate for above message",dependsOnMethods= {"readMsgCharge"})
	public void getOutboundRate() {
		//Prepare param to find pricing details for a specific country
		Map<String, String> queryParam = new HashMap<>();
		queryParam.put("country_iso", "US");
		
		//Fire pricing api 
		Response res = pricing().getPricing(queryParam);
		Assert.assertEquals(res.getStatusCode(), 200, "Couldn't get the pricing details");
		
		//Read outboundRate from response
		outboundRate = Float.valueOf((String)RestAssuredUtility.getNodeValue(res.asString(), "message.outbound.rate"));
		
		System.out.println("outboundRate:"+outboundRate);
	}
	
	
	
	@Test(description="Verify the rate and the price deducted for the sending message, should be same",dependsOnMethods= {"getOutboundRate"})
	public void verifyMsgAmount() {
		Assert.assertEquals(outboundRate, msgCharge, "Outbound rate and amount deducted for message is not same");
	}
	
	
	
	@Test(description="Verify that account cash credit should be less than by the deducted amount",dependsOnMethods= {"readMsgCharge"})
	public void verifyCashCredit() {
		//Fire Account Details api
		Response res = accountDetails().getAccountDetails();
		Assert.assertEquals(res.getStatusCode(), 200, "Couldn't get the account details");
		
		//Get cash credits from response
		cashCredits = Float.valueOf((String)RestAssuredUtility.getNodeValue(res.asString(), "cash_credits"));
		
		System.out.println("cashCredits:"+cashCredits);
		
		//verify that account cash credit should be less than by the deducted amount
		Assert.assertTrue(cashCredits < msgCharge, "Unexpectedly cashCredits is greater than msgCharge as cashCredits: "+cashCredits+" and msgCharge: "+msgCharge);
	}
}
