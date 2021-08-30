package project.challengers.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "APPLY")
@Builder
@ApiModel("참가 신청")
public class Apply {

    @Id
    @Column(name = "apply_seq")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long applySeq;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "notice_seq", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Notice notice;

    @Column(nullable = false)
    private int deposit;

}
