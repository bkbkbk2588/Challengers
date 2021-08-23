package project.challengers.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuth is a Querydsl query type for Auth
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAuth extends EntityPathBase<Auth> {

    private static final long serialVersionUID = -5171180L;

    public static final QAuth auth = new QAuth("auth");

    public final NumberPath<Long> authSeq = createNumber("authSeq", Long.class);

    public final StringPath fileName = createString("fileName");

    public final StringPath filePath = createString("filePath");

    public final StringPath id = createString("id");

    public final NumberPath<Long> noticeSeq = createNumber("noticeSeq", Long.class);

    public QAuth(String variable) {
        super(Auth.class, forVariable(variable));
    }

    public QAuth(Path<? extends Auth> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuth(PathMetadata metadata) {
        super(Auth.class, metadata);
    }

}

