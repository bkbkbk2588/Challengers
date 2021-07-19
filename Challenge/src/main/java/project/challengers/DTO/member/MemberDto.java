package project.challengers.DTO.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("사용자 회원가입")
public class MemberDto {
    @ApiModelProperty(value="아이디", example="user001")
    private String id;

    @JsonIgnore
    @ApiModelProperty(value="비밀번호", example="1234")
    private String pw;

    @ApiModelProperty(value="이름", example="홍길동")
    private String name;

    @ApiModelProperty(value="닉네임", example="abc")
    private String nickname;

    @ApiModelProperty(value="이메일", example="abc@abc.abc")
    private String email;

    @ApiModelProperty(value="핸드폰 번호", example="01011111111")
    private String phone;
}
