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
@ApiModel("로그인 응답")
public class LoginResponseDto {
    @ApiModelProperty(value = "access_token", example = "b8f4ec80-b11b-3d27-9377-e41d5167077a")
    private String accessToken;

    @ApiModelProperty(value="", example="")
    private Member member;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ApiModel("로그인 정보")
    public static class Member {
        @ApiModelProperty(value="로그인Id", example="user076")
        private String id;

        @ApiModelProperty(value="이름", example="홍길동")
        private String name;

        @ApiModelProperty(value="닉네임", example="닉네임이다")
        private String nickname;

        @ApiModelProperty(value="이메일", example="aaa@aaa.aaa")
        private String email;

        @ApiModelProperty(value="핸드폰번호", example="010123456789")
        private String phone;
    }
}
