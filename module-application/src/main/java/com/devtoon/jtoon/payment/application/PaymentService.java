package com.devtoon.jtoon.payment.application;

import com.devtoon.jtoon.paymentinfo.request.CancelReq;
import com.devtoon.jtoon.paymentinfo.request.PaymentReq;
import com.devtoon.jtoon.paymentinfo.service.PaymentInfoService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentInfoService paymentInfoService;

    public IamportResponse<Payment> validatePayment(PaymentReq paymentReq)
            throws IamportResponseException, IOException {
        IamportResponse<Payment> irsp = paymentInfoService.paymentLookUp(paymentReq.impUid());
        paymentInfoService.validateIamport(irsp, paymentReq);
        paymentInfoService.createPaymentInfo(paymentReq);

        return irsp;
    }

    public IamportResponse<Payment> cancelPayment(CancelReq cancelReq)
            throws IamportResponseException, IOException {
        IamportResponse<Payment> irsp = paymentInfoService.paymentLookUp(cancelReq.impUid());

        return paymentInfoService.cancelPayment(irsp, cancelReq);
    }
}
