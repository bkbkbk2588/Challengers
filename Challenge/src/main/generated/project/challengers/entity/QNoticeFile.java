package project.challengers.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QNoticeFile is a Querydsl query type for NoticeFile
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QNoticeFile extends EntityPathBase<NoticeFile> {

    private static final long serialVersionUID = 1076006368L;

    public static final QNoticeFile noticeFile = new QNoticeFile("noticeFile");

    public final StringPath fileName = createString("fileName");

    public final StringPath filePath = createString("filePath");

    public final NumberPath<Long> fileSeq = createNumber("fileSeq", Long.class);

    public final NumberPath<Long> noticeSeq = createNumber("noticeSeq", Long.class);

    public QNoticeFile(String variable) {
        super(NoticeFile.class, forVariable(variable));
    }

    public QNoticeFile(Path<? extends NoticeFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNoticeFile(PathMetadata metadata) {
        super(NoticeFile.class, metadata);
    }

}

