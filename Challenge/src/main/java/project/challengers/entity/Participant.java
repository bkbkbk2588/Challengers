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
@Entity(name = "participant")
@Builder
@ApiModel("챌린저 참가자")
public class Participant {

    @Id
    @Column(name = "patricipant_seq")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value="참가자 순번", example="1")
    private int participantSeq;

    @Column(nullable = false, name = "notice_seq")
    @ApiModelProperty(value="챌린저 공지 번호", example="1")
    private int noticeSeq;

    @Column(nullable = false, name = "master_id")
    @ApiModelProperty(value="방장 아이디", example="user001")
    private String masterId;

    @Column(nullable = true, name = "participant_id")
    @ApiModelProperty(value="참가자 명단", example="")
    private String participantId;

    @Column(name = "participant_type")
    @ApiModelProperty(value="참가자 타입", example="0")
    private int participantType;
}
