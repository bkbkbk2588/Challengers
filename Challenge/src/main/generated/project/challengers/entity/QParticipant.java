package project.challengers.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QParticipant is a Querydsl query type for Participant
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QParticipant extends EntityPathBase<Participant> {

    private static final long serialVersionUID = 470049607L;

    public static final QParticipant participant = new QParticipant("participant");

    public final NumberPath<Integer> credit = createNumber("credit", Integer.class);

    public final StringPath masterId = createString("masterId");

    public final NumberPath<Long> noticeSeq = createNumber("noticeSeq", Long.class);

    public final StringPath participantId = createString("participantId");

    public final NumberPath<Long> participantSeq = createNumber("participantSeq", Long.class);

    public final NumberPath<Integer> participantType = createNumber("participantType", Integer.class);

    public final NumberPath<Integer> warning = createNumber("warning", Integer.class);

    public QParticipant(String variable) {
        super(Participant.class, forVariable(variable));
    }

    public QParticipant(Path<? extends Participant> path) {
        super(path.getType(), path.getMetadata());
    }

    public QParticipant(PathMetadata metadata) {
        super(Participant.class, metadata);
    }

}

