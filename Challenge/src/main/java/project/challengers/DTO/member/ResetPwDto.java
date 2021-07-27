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
@ApiModel("비밀번호 초기화")
public class ResetPwDto {
    @ApiModelProperty(value="아이디", example="user001")
    private String id;

    @ApiModelProperty(value="이름", example="홍길동")
    private String name;

    @ApiModelProperty(value="핸드폰 번호", example="01011111111")
    private String phone;
}
