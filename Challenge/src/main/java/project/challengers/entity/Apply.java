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

    @Column(nullable = false)
    private String id;

    @Column(nullable = false, name = "notice_seq")
    private long noticeSeq;

    @Column(nullable = false)
    private int deposit;

}
