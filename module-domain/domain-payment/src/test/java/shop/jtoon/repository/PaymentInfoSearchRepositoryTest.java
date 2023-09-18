package shop.jtoon.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shop.jtoon.config.JpaConfig;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.PaymentInfo;
import shop.jtoon.factory.CreatorFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfig.class, PaymentInfoSearchRepository.class})
class PaymentInfoSearchRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PaymentInfoRepository paymentInfoRepository;

    @Autowired
    private PaymentInfoSearchRepository paymentInfoSearchRepository;

    @DisplayName("paymentInfoSearchRepository - Bean 등록 여부 테스트 - NotNull")
    @Test
    void paymentInfoSearchRepository_NotNull() {
        // Then
        assertThat(paymentInfoSearchRepository).isNotNull();
    }

    @DisplayName("searchByMerchantsUidAndEmail - 해당 이메일과 일치하는 회원의 결제 정보 조회")
    @Test
    void searchByMerchantsUidAndEmail_PaymentInfo_List() {
        // Given
        Member member = CreatorFactory.createMember("example123@naver.com");
        memberRepository.save(member);
        PaymentInfo paymentInfo1 = CreatorFactory.createPaymentInfo("imp123", "mer123", member);
        PaymentInfo paymentInfo2 = CreatorFactory.createPaymentInfo("imp789", "mer789", member);
        paymentInfoRepository.save(paymentInfo1);
        paymentInfoRepository.save(paymentInfo2);

        // When
        List<PaymentInfo> actual = paymentInfoSearchRepository
            .searchByMerchantsUidAndEmail(null, member.getEmail());

        // Then
        assertThat(actual).hasSize(2);
    }
}
