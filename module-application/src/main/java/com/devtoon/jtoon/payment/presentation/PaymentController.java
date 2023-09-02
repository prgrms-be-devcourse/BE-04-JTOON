package com.devtoon.jtoon.payment.presentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.siot.IamportRestClient.IamportClient;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

	@Value("${pg.kg-inicis.rest-api-key}")
	private String REST_API_KEY;

	@Value("${pg.kg-inicis.rest-api-secret}")
	private String REST_API_SECRET;

	private final IamportClient iamportClient;

	public PaymentController() {
		this.iamportClient = new IamportClient(REST_API_KEY, REST_API_SECRET);
	}
}
