package project.challengers.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "AUTH")
@Builder
@ApiModel("인증수단")
public class Auth {

    @Id
    @Column(name = "auth_seq")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long authSeq;

    @Column(nullable = false)
    private String id;

    @Column(name = "photo_name", nullable = true)
    private String photoName;

    @Column(name = "photo_path", nullable = true)
    private String photoPath;

    @Column(name = "video_name", nullable = true)
    private String videoName;

    @Column(name = "video_path", nullable = true)
    private String videoPath;
}
