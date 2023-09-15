package shop.jtoon.payment.presentation;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.siot.IamportRestClient.exception.IamportResponseException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import shop.jtoon.payment.application.PaymentApplicationService;
import shop.jtoon.payment.request.PaymentReq;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

	private final PaymentApplicationService paymentApplicationService;

	@PostMapping("/validation")
	@ResponseStatus(HttpStatus.CREATED)
	public BigDecimal validatePayment(@RequestBody @Valid PaymentReq paymentReq)
		throws IamportResponseException, IOException {
		return paymentApplicationService.validatePayment(paymentReq);
	}
}
