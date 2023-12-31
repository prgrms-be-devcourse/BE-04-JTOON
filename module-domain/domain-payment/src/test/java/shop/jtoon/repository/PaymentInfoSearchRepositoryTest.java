package shop.jtoon.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shop.jtoon.config.JpaConfig;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.PaymentInfo;
import shop.jtoon.factory.CreatorFactory;

import java.util.ArrayList;
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

    private Member member;
    private PaymentInfo paymentInfo1;
    private PaymentInfo paymentInfo2;

    @BeforeEach
    void beforeEach() {
        member = CreatorFactory.createMember("example123@naver.com");
        memberRepository.save(member);
        paymentInfo1 = CreatorFactory.createPaymentInfo("imp123", "mer123", member);
        paymentInfoRepository.save(paymentInfo1);
        paymentInfo2 = CreatorFactory.createPaymentInfo("imp789", "mer789", member);
        paymentInfoRepository.save(paymentInfo2);
    }

    @DisplayName("paymentInfoSearchRepository - Bean 등록 여부 테스트 - NotNull")
    @Test
    void paymentInfoSearchRepository_NotNull() {
        // Then
        assertThat(paymentInfoSearchRepository).isNotNull();
    }

    @DisplayName("searchByMerchantsUidAndEmail - 해당 이메일과 일치하는 회원의 결제 정보 조회 - 모든 결제 정보")
    @Test
    void searchByMerchantsUidAndEmail_PaymentInfo_List1() {
        // When
        List<PaymentInfo> actual = paymentInfoSearchRepository
            .searchByMerchantsUidAndEmail(null, member.getEmail());

        // Then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("searchByMerchantsUidAndEmail - 해당 이메일과 일치하고 주문번호들과 일치하는 결제 정보 조회 - 결제 정보 2건")
    @Test
    void searchByMerchantsUidAndEmail_PaymentInfo_List2() {
        //Given
        List<String> merchantsUid = new ArrayList<>();
        merchantsUid.add(paymentInfo1.getMerchantUid());
        merchantsUid.add(paymentInfo2.getMerchantUid());

        // When
        List<PaymentInfo> actual = paymentInfoSearchRepository
            .searchByMerchantsUidAndEmail(merchantsUid, member.getEmail());

        // Then
        assertThat(actual).hasSize(2);
    }

    @DisplayName("searchByMerchantsUidAndEmail - 해당 이메일과 일치하고 주문번호와 일치하는 결제 정보 조회 - 결제 정보 1건")
    @Test
    void searchByMerchantsUidAndEmail_PaymentInfo() {
        //Given
        List<String> merchantsUid = new ArrayList<>();
        merchantsUid.add(paymentInfo1.getMerchantUid());

        // When
        List<PaymentInfo> actual = paymentInfoSearchRepository
            .searchByMerchantsUidAndEmail(merchantsUid, member.getEmail());

        // Then
        assertThat(actual).hasSize(1);
    }

    @DisplayName("searchByMerchantsUidAndEmail - 해당 이메일과 일치하지만 주문번호가 일치하지 않는 경우 - 0건")
    @Test
    void searchByMerchantsUidAndEmail_PaymentInfo_Null1() {
        //Given
        List<String> merchantsUid = new ArrayList<>();
        merchantsUid.add(paymentInfo1.getMerchantUid());

        // When
        List<PaymentInfo> actual = paymentInfoSearchRepository
            .searchByMerchantsUidAndEmail(merchantsUid, "notfoundemail@naver.com");

        // Then
        assertThat(actual).isEmpty();
    }

    @DisplayName("searchByMerchantsUidAndEmail - 해당 이메일과 일치하지 않지만 주문번호가 일치하는 경우 - 0건")
    @Test
    void searchByMerchantsUidAndEmail_PaymentInfo_Null2() {
        //Given
        List<String> merchantsUid = new ArrayList<>();
        merchantsUid.add("notfoundmerchantuid");

        // When
        List<PaymentInfo> actual = paymentInfoSearchRepository
            .searchByMerchantsUidAndEmail(merchantsUid, member.getEmail());

        // Then
        assertThat(actual).isEmpty();
    }
}
