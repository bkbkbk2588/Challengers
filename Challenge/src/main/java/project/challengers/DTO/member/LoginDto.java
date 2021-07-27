package project.challengers.DTO.member;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("사용자 로그인")
public class LoginDto {
    @NotNull
    @ApiModelProperty(value="아이디", example="user001")
    private String id;

    @NotNull
    @ApiModelProperty(value="비밀번호", example="1234")
    private String pw;
}
