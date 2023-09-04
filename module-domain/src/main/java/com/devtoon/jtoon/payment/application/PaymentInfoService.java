package com.devtoon.jtoon.payment.application;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devtoon.jtoon.payment.repository.PaymentInfoRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

@Service
@Transactional(readOnly = true)
public class PaymentInfoService {

	@Value("${pg.kg-inicis.rest-api-key}")
	private String REST_API_KEY;

	@Value("${pg.kg-inicis.rest-api-secret}")
	private String REST_API_SECRET;

	private final IamportClient iamportClient;
	private final PaymentInfoRepository paymentInfoRepository;

	public PaymentInfoService(PaymentInfoRepository paymentInfoRepository) {
		this.iamportClient = new IamportClient(REST_API_KEY, REST_API_SECRET);
		this.paymentInfoRepository = paymentInfoRepository;
	}

	public IamportResponse<Payment> paymentLookup(String impUid) throws IamportResponseException, IOException {
		return iamportClient.paymentByImpUid(impUid);
	}
}
