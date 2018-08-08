package com.plivo.base;

import com.plivo.pages.AccountDetails;
import com.plivo.pages.Message;
import com.plivo.pages.Numbers;
import com.plivo.pages.Pricing;

public class PageFacotory {

	public Numbers numbers() {
		return new Numbers();
	}
	
	public AccountDetails accountDetails() {
		return new AccountDetails();
	}
	
	public Message message() {
		return new Message();
	}
	
	public Pricing pricing() {
		return new Pricing();
	}
}
