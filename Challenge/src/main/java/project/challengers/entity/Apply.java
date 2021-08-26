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
@Entity(name = "APPLY")
@Builder
@ApiModel("참가 신청")
public class Apply {

    @Id
    @Column(name = "apply_seq")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long applySeq;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", nullable = false)
    private Member member;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "notice_seq", nullable = false)
    private Notice notice;

    @Column(nullable = false)
    private int deposit;

}
