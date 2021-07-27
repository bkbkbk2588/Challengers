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
@ApiModel("사용자 정보 수정")
public class UpdateMemberDto {
    @ApiModelProperty(value="이름", example="홍길동")
    private String name;

    @ApiModelProperty(value="닉네임", example="abc")
    private String nickname;

    @ApiModelProperty(value="이메일", example="abc@abc.abc")
    private String email;

    @ApiModelProperty(value="핸드폰 번호", example="01011111111")
    private String phone;
}
