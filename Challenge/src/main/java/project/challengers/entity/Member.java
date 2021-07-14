package project.challengers.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "member")
@Builder
@ApiModel("사용자")
public class Member {

    @Id
    @ApiModelProperty(value="아이디", example="user001")
    private String id;

    @Column(nullable = false)
    @ApiModelProperty(value="비밀번호", example="1234")
    private String pw;

    @Column(nullable = false)
    @ApiModelProperty(value="이름", example="홍길동")
    private String name;

    @Column(nullable = false)
    @ApiModelProperty(value="닉네임", example="abc")
    private String nickname;

    @Column(nullable = false)
    @ApiModelProperty(value="이메일", example="abc@abc.abc")
    private String email;

    @Column(nullable = false)
    @ApiModelProperty(value="핸드폰 번호", example="01011111111")
    private String phone;
}
