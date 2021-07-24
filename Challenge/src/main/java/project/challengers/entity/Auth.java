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
@Entity(name = "AUTH")
@Builder
@ApiModel("인증수단")
public class Auth {

    @Id
    @Column(name = "auth_seq")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value="인증 순번", example="1")
    private long authSeq;

    @Column(nullable = false)
    @ApiModelProperty(value="아이디", example="user001")
    private String id;

    @Column(nullable = true)
    @ApiModelProperty(value="인증 사진 경로", example="C://a.png")
    private String photo;

    @Column(nullable = true)
    @ApiModelProperty(value="인증 동영상 경로", example="C://a.mp4")
    private String video;
}
