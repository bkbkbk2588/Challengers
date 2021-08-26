package project.challengers.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QApply is a Querydsl query type for Apply
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QApply extends EntityPathBase<Apply> {

    private static final long serialVersionUID = -160459134L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QApply apply = new QApply("apply");

    public final NumberPath<Long> applySeq = createNumber("applySeq", Long.class);

    public final NumberPath<Integer> deposit = createNumber("deposit", Integer.class);

    public final QMember member;

    public final QNotice notice;

    public QApply(String variable) {
        this(Apply.class, forVariable(variable), INITS);
    }

    public QApply(Path<? extends Apply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QApply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QApply(PathMetadata metadata, PathInits inits) {
        this(Apply.class, metadata, inits);
    }

    public QApply(Class<? extends Apply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
        this.notice = inits.isInitialized("notice") ? new QNotice(forProperty("notice"), inits.get("notice")) : null;
    }

}

