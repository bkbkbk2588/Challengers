package project.challengers.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "notice_seq", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Notice notice;

    @Column(nullable = false, name = "master_id")
    private String masterId;

    @Column(name = "participant_id")
    private String participantId;

    @Column(name = "participant_type")
    private int participantType;

    @Column
    @ColumnDefault("0")
    private int warning;

    @Column
    @ColumnDefault("0")
    private int credit;
}
