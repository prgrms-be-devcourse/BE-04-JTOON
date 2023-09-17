package shop.jtoon.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.jtoon.entity.PaymentInfo;
import shop.jtoon.util.DynamicQuery;

import java.util.List;

import static shop.jtoon.entity.QMember.*;
import static shop.jtoon.entity.QPaymentInfo.*;

@Repository
@RequiredArgsConstructor
public class PaymentInfoSearchRepository {

    private final JPAQueryFactory queryFactory;

    public List<PaymentInfo> searchByMerchantsUidAndEmail(List<String> merchantsUid, String email) {
        return queryFactory.selectFrom(paymentInfo)
                .join(paymentInfo.member, member).fetchJoin()
                .where(
                        DynamicQuery.filterCondition(merchantsUid, paymentInfo.merchantUid::in),
                        DynamicQuery.generateEq(email, paymentInfo.member.email::eq)
                )
                .fetch();
    }
}
