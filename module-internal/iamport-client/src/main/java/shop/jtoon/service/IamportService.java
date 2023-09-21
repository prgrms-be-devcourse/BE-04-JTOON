package shop.jtoon.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jtoon.exception.IamportException;
import shop.jtoon.exception.InvalidRequestException;
import shop.jtoon.type.ErrorStatus;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IamportService {

    private final IamportClient iamportClient;

    public void validateIamport(String impUid, BigDecimal amount) {
        try {
            IamportResponse<Payment> irsp = iamportClient.paymentByImpUid(impUid);
            BigDecimal realAmount = irsp.getResponse().getAmount();
            validateAmount(realAmount, amount);
        } catch (IamportResponseException | IOException e) {
            throw new IamportException(e.getMessage());
        }
    }

    @Transactional
    public void cancelIamport(String impUid, String reason, BigDecimal checksum, String refundHolder) {
        try {
            IamportResponse<Payment> irsp = iamportClient.paymentByImpUid(impUid);
            CancelData cancelData = new CancelData(irsp.getResponse().getImpUid(), true);
            cancelData.setReason(reason);
            cancelData.setChecksum(checksum);
            cancelData.setRefund_holder(refundHolder);
            iamportClient.cancelPaymentByImpUid(cancelData);
        } catch (IamportResponseException | IOException e) {
            throw new IamportException(e.getMessage());
        }
    }

    private void validateAmount(BigDecimal realAmount, BigDecimal amount) {
        if (!realAmount.equals(amount)) {
            throw new InvalidRequestException(ErrorStatus.PAYMENT_AMOUNT_INVALID);
        }
    }
}
