package com.devtoon.jtoon.iamport.application;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

@Service
public class IamportService {

	@Value("${pg.kg-inicis.rest-api-key}")
	private String REST_API_KEY;

	@Value("${pg.kg-inicis.rest-api-secret}")
	private String REST_API_SECRET;

	private final IamportClient iamportClient;

	public IamportService() {
		this.iamportClient = new IamportClient(REST_API_KEY, REST_API_SECRET);
	}

	public IamportResponse<Payment> validateIamport(String impUid, int amount)
		throws IamportResponseException, IOException {
		IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(impUid);
		validateAmount(iamportResponse, amount);

		return iamportResponse;
	}

	private void validateAmount(IamportResponse<Payment> iamportResponse, int amount) {
		if (iamportResponse.getResponse().getAmount().intValue() != amount) {
			throw new RuntimeException("verify iamport exception");
		}
	}
}
