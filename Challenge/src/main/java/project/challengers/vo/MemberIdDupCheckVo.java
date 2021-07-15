package project.challengers.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberIdDupCheckVo {
    //@ApiModelProperty(value="중복여부", example="Y")
    private String dupYn;
}
