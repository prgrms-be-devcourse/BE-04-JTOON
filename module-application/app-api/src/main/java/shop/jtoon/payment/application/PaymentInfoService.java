package shop.jtoon.payment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.PaymentInfo;
import shop.jtoon.exception.DuplicatedException;
import shop.jtoon.payment.request.PaymentReq;
import shop.jtoon.payment.response.PaymentRes;
import shop.jtoon.repository.PaymentInfoRepository;
import shop.jtoon.repository.PaymentInfoSearchRepository;
import shop.jtoon.service.PaymentInfoDomainService;
import shop.jtoon.type.ErrorStatus;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentInfoService {

    private final PaymentInfoDomainService paymentInfoDomainService;
    private final PaymentInfoRepository paymentInfoRepository;
    private final PaymentInfoSearchRepository paymentInfoSearchRepository;

    @Transactional
    public void createPaymentInfo(PaymentReq paymentReq, Member member) {
        PaymentInfo paymentInfo = paymentReq.toEntity(member);
        paymentInfoRepository.save(paymentInfo);
    }

    public List<PaymentRes> getPaymentsInfo(List<String> merchantsUid, Member member) {
        List<PaymentInfo> paymentsInfo = paymentInfoSearchRepository.searchByMerchantsUidAndEmail(
                merchantsUid,
                member.getEmail()
        );

        return paymentsInfo.stream()
                .map(PaymentRes::toDto)
                .toList();
    }

    public void validatePaymentInfo(PaymentReq paymentReq) {
        paymentInfoDomainService.validatePaymentInfo(paymentReq.itemName(), paymentReq.amount());
        validateImpUid(paymentReq.impUid());
        validateMerchantUid(paymentReq.merchantUid());
    }


    private void validateImpUid(String impUid) {
        if (paymentInfoRepository.existsByImpUid(impUid)) {
            throw new DuplicatedException(ErrorStatus.PAYMENT_IMP_UID_DUPLICATED);
        }
    }

    private void validateMerchantUid(String merchantUid) {
        if (paymentInfoRepository.existsByMerchantUid(merchantUid)) {
            throw new DuplicatedException(ErrorStatus.PAYMENT_MERCHANT_UID_DUPLICATED);
        }
    }
}
