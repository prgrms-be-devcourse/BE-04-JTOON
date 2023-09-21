package shop.jtoon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberCookie is a Querydsl query type for MemberCookie
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberCookie extends EntityPathBase<MemberCookie> {

    private static final long serialVersionUID = 919555335L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberCookie memberCookie = new QMemberCookie("memberCookie");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final NumberPath<Integer> cookieCount = createNumber("cookieCount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMemberCookie(String variable) {
        this(MemberCookie.class, forVariable(variable), INITS);
    }

    public QMemberCookie(Path<? extends MemberCookie> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberCookie(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberCookie(PathMetadata metadata, PathInits inits) {
        this(MemberCookie.class, metadata, inits);
    }

    public QMemberCookie(Class<? extends MemberCookie> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

