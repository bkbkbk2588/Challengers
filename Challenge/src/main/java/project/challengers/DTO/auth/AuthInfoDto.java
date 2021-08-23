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
@ApiModel("인증 정보")
public class AuthInfoDto {
    @ApiModelProperty(value="참가자 id", example="user001")
    String id;

    @ApiModelProperty(value = "[localhost:8080/downloadFile/1.jpg]")
    List<String> fileUrl;
}
