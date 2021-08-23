package project.challengers.DTO.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("인증 리스트")
public class AuthDto {
    @ApiModelProperty(value = "챌린저 공지 번호", example = "1")
    long noticeSeq;

    @ApiModelProperty(value = "[localhost:8080/downloadFile/1.jpg]")
    List<AuthInfoDto> authInfo;
}
