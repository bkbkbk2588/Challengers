package project.challengers.DTO.member;

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
@ApiModel("사용자 로그인")
public class LoginDTO {
    @ApiModelProperty(value="아이디", example="user001")
    private String id;

    @ApiModelProperty(value="비밀번호", example="1234")
    private String pw;
}
