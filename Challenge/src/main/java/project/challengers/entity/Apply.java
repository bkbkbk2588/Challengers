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
@Entity(name = "apply")
@Builder
@ApiModel("참가 신청")
public class Apply {

    @Id
    @Column(name = "apply_seq")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value="참가신청 순번", example="1")
    private int applySeq;

    @Column(nullable = false)
    @ApiModelProperty(value="참가 신청 아이디", example="user001")
    private String id;

    @Column(nullable = false, name = "notice_seq")
    @ApiModelProperty(value="챌린저 공지 번호", example="1")
    private int noticeSeq;

    @Column(nullable = false)
    @ApiModelProperty(value="보증금 유형", example="0")
    private int deposit;

}
