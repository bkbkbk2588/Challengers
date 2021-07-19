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
@ApiModel("아이디 찾기")
public class FindIdDTO {
    @ApiModelProperty(value = "아이디", example = "user001")
    private String id;
}
