package com.devtoon.jtoon.paymentinfo.service;

import com.devtoon.jtoon.error.exception.DuplicatedException;
import com.devtoon.jtoon.error.exception.InvalidRequestException;
import com.devtoon.jtoon.error.model.ErrorStatus;
import com.devtoon.jtoon.member.entity.Member;
import com.devtoon.jtoon.paymentinfo.entity.CookieItem;
import com.devtoon.jtoon.paymentinfo.entity.PaymentInfo;
import com.devtoon.jtoon.paymentinfo.repository.PaymentInfoRepository;
import com.devtoon.jtoon.paymentinfo.request.CancelReq;
import com.devtoon.jtoon.paymentinfo.request.PaymentReq;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class PaymentInfoService {

    private final IamportClient iamportClient;
    private final PaymentInfoRepository paymentInfoRepository;

    public PaymentInfoService(
            @Value("${pg.kg-inicis.rest-api-key}") String restApiKey,
            @Value("${pg.kg-inicis.rest-api-secret}") String restSecretKey,
            PaymentInfoRepository paymentInfoRepository
    ) {
        this.iamportClient = new IamportClient(restApiKey, restSecretKey);
        this.paymentInfoRepository = paymentInfoRepository;
    }

    public IamportResponse<Payment> paymentLookUp(String impUid) throws IamportResponseException, IOException {
        return iamportClient.paymentByImpUid(impUid);
    }

    public void validateIamport(IamportResponse<Payment> irsp, PaymentReq paymentReq) {
        CookieItem cookieItem = CookieItem.from(paymentReq.cookieItem());
        validateAmount(irsp, cookieItem.getAmount());
        validateAmount(irsp, paymentReq.amount());
        validateImpUid(paymentReq);
        validateMerchantUid(paymentReq);
    }

    @Transactional
    public void createPaymentInfo(PaymentReq paymentReq) {
        Member member = null;
        PaymentInfo paymentInfo = paymentReq.toEntity(member);
        paymentInfoRepository.save(paymentInfo);
    }

    public IamportResponse<Payment> cancelPayment(IamportResponse<Payment> irsp, CancelReq cancelReq)
            throws IamportResponseException, IOException {
        validateAmount(irsp, cancelReq.checksum());
        CancelData cancelData = cancelReq.toCancelData(irsp);

        return iamportClient.cancelPaymentByImpUid(cancelData);
    }

    private void validateAmount(IamportResponse<Payment> iamportResponse, BigDecimal amount) {
        BigDecimal realAmount = iamportResponse.getResponse().getAmount();

        if (!realAmount.equals(amount)) {
            throw new InvalidRequestException(ErrorStatus.PAYMENT_AMOUNT_INVALID);
        }
    }

    private void validateMerchantUid(PaymentReq paymentReq) {
        if (paymentInfoRepository.existsByMerchantUid(paymentReq.merchantUid())) {
            throw new DuplicatedException(ErrorStatus.PAYMENT_MERCHANT_UID_DUPLICATED);
        }
    }

    private void validateImpUid(PaymentReq paymentReq) {
        if (paymentInfoRepository.existsByImpUid(paymentReq.impUid())) {
            throw new DuplicatedException(ErrorStatus.PAYMENT_IMP_UID_DUPLICATED);
        }
    }
}
