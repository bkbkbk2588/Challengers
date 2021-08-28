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

    public final QNotice _super;

    //inherited
    public final StringPath content;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> endTime;

    //inherited
    public final NumberPath<Integer> maxPeople;

    // inherited
    public final QMember member;

    public final NumberPath<Integer> money = createNumber("money", Integer.class);

    //inherited
    public final NumberPath<Long> noticeSeq;

    //inherited
    public final NumberPath<Integer> price;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> startTime;

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    //inherited
    public final StringPath title;

    //inherited
    public final StringPath type;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateTime;

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
        this._super = new QNotice(type, metadata, inits);
        this.content = _super.content;
        this.endTime = _super.endTime;
        this.maxPeople = _super.maxPeople;
        this.member = _super.member;
        this.noticeSeq = _super.noticeSeq;
        this.price = _super.price;
        this.startTime = _super.startTime;
        this.title = _super.title;
        this.type = _super.type;
        this.updateTime = _super.updateTime;
    }

}

