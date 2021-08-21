package project.challengers.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QPointHistory is a Querydsl query type for PointHistory
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPointHistory extends EntityPathBase<PointHistory> {

    private static final long serialVersionUID = 1534186320L;

    public static final QPointHistory pointHistory = new QPointHistory("pointHistory");

    public final StringPath id = createString("id");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final NumberPath<Long> pointHistorySeq = createNumber("pointHistorySeq", Long.class);

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public QPointHistory(String variable) {
        super(PointHistory.class, forVariable(variable));
    }

    public QPointHistory(Path<? extends PointHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPointHistory(PathMetadata metadata) {
        super(PointHistory.class, metadata);
    }

}

