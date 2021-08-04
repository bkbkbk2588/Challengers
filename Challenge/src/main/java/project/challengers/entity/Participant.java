package project.challengers.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "PARTICIPANT")
@Builder
@ApiModel("챌린저 참가자")
public class Participant {

    @Id
    @Column(name = "patricipant_seq")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long participantSeq;

    @Column(nullable = false, name = "notice_seq")
    private long noticeSeq;

    @Column(nullable = false, name = "master_id")
    private String masterId;

    @Column(nullable = true, name = "participant_id")
    private String participantId;

    @Column(name = "participant_type")
    private int participantType;
}
