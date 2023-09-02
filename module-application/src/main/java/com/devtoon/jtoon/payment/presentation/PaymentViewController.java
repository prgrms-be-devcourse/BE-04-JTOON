package com.devtoon.jtoon.payment.presentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.siot.IamportRestClient.IamportClient;

@Controller
public class PaymentViewController {

	@Value("${pg.kg-inicis.rest-api-key}")
	private String REST_API_KEY;

	@Value("${pg.kg-inicis.rest-api-secret}")
	private String REST_API_SECRET;

	private final IamportClient iamportClient;

	public PaymentViewController() {
		this.iamportClient = new IamportClient(REST_API_KEY, REST_API_SECRET);
	}
}
