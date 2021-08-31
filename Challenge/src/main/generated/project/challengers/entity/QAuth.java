package project.challengers.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAuth is a Querydsl query type for Auth
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAuth extends EntityPathBase<Auth> {

    private static final long serialVersionUID = -5171180L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAuth auth = new QAuth("auth");

    public final DatePath<java.time.LocalDate> authDate = createDate("authDate", java.time.LocalDate.class);

    public final NumberPath<Long> authSeq = createNumber("authSeq", Long.class);

    public final StringPath fileName = createString("fileName");

    public final StringPath filePath = createString("filePath");

    public final QMember member;

    public final QNotice notice;

    public QAuth(String variable) {
        this(Auth.class, forVariable(variable), INITS);
    }

    public QAuth(Path<? extends Auth> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAuth(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAuth(PathMetadata metadata, PathInits inits) {
        this(Auth.class, metadata, inits);
    }

    public QAuth(Class<? extends Auth> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
        this.notice = inits.isInitialized("notice") ? new QNotice(forProperty("notice"), inits.get("notice")) : null;
    }

}

