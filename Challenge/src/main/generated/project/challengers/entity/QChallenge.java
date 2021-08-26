package project.challengers.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChallenge is a Querydsl query type for Challenge
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QChallenge extends EntityPathBase<Challenge> {

    private static final long serialVersionUID = 1062659447L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChallenge challenge = new QChallenge("challenge");

    public final NumberPath<Integer> money = createNumber("money", Integer.class);

    public final QNotice notice;

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public QChallenge(String variable) {
        this(Challenge.class, forVariable(variable), INITS);
    }

    public QChallenge(Path<? extends Challenge> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChallenge(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChallenge(PathMetadata metadata, PathInits inits) {
        this(Challenge.class, metadata, inits);
    }

    public QChallenge(Class<? extends Challenge> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.notice = inits.isInitialized("notice") ? new QNotice(forProperty("notice"), inits.get("notice")) : null;
    }

}

