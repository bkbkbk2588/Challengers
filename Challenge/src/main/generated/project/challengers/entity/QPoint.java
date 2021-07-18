package project.challengers.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QPoint is a Querydsl query type for Point
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPoint extends EntityPathBase<Point> {

    private static final long serialVersionUID = -146642780L;

    public static final QPoint point1 = new QPoint("point1");

    public final StringPath id = createString("id");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final NumberPath<Integer> pointSeq = createNumber("pointSeq", Integer.class);

    public QPoint(String variable) {
        super(Point.class, forVariable(variable));
    }

    public QPoint(Path<? extends Point> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPoint(PathMetadata metadata) {
        super(Point.class, metadata);
    }

}

