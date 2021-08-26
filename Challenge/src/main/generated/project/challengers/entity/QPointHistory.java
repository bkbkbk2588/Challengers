package project.challengers.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPointHistory is a Querydsl query type for PointHistory
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPointHistory extends EntityPathBase<PointHistory> {

    private static final long serialVersionUID = 1534186320L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPointHistory pointHistory = new QPointHistory("pointHistory");

    public final DateTimePath<java.time.LocalDateTime> insertTime = createDateTime("insertTime", java.time.LocalDateTime.class);

    public final QMember member;

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final NumberPath<Long> pointHistorySeq = createNumber("pointHistorySeq", Long.class);

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public QPointHistory(String variable) {
        this(PointHistory.class, forVariable(variable), INITS);
    }

    public QPointHistory(Path<? extends PointHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPointHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPointHistory(PathMetadata metadata, PathInits inits) {
        this(PointHistory.class, metadata, inits);
    }

    public QPointHistory(Class<? extends PointHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
    }

}

