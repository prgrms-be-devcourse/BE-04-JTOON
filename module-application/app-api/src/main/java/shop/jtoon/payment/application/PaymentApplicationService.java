package shop.jtoon.payment.application;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siot.IamportRestClient.exception.IamportResponseException;

import lombok.RequiredArgsConstructor;
import shop.jtoon.dto.PaymentDto;
import shop.jtoon.payment.request.PaymentReq;
import shop.jtoon.service.IamportService;
import shop.jtoon.service.MemberCookieDomainService;
import shop.jtoon.service.PaymentInfoDomainService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentApplicationService {

	private final IamportService iamportService;
	private final PaymentInfoDomainService paymentInfoDomainService;
	private final MemberCookieDomainService memberCookieDomainService;

	@Transactional
	public BigDecimal validatePayment(PaymentReq paymentReq) throws IamportResponseException, IOException {
		PaymentDto paymentDto = paymentReq.toDto();
		iamportService.validateIamport(paymentDto.impUid(), paymentDto.amount());
		paymentInfoDomainService.validatePayment(paymentDto);
		paymentInfoDomainService.createPayment(paymentDto);
		memberCookieDomainService.createMemberCookie(paymentDto.cookieItem());

		return paymentDto.amount();
	}
}
