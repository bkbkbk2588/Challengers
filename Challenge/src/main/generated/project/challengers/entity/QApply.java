package project.challengers.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QApply is a Querydsl query type for Apply
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QApply extends EntityPathBase<Apply> {

    private static final long serialVersionUID = -160459134L;

    public static final QApply apply = new QApply("apply");

    public final NumberPath<Long> applySeq = createNumber("applySeq", Long.class);

    public final NumberPath<Integer> deposit = createNumber("deposit", Integer.class);

    public final StringPath id = createString("id");

    public final NumberPath<Long> noticeSeq = createNumber("noticeSeq", Long.class);

    public QApply(String variable) {
        super(Apply.class, forVariable(variable));
    }

    public QApply(Path<? extends Apply> path) {
        super(path.getType(), path.getMetadata());
    }

    public QApply(PathMetadata metadata) {
        super(Apply.class, metadata);
    }

}

