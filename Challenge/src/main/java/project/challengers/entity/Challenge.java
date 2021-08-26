package project.challengers.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "CHALLENGE")
@Builder
public class Challenge implements Serializable {

    @Id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "challenge_seq")
    Notice notice;

    @Column
    int money;

    @Column
    int status;

    @Override
    public boolean equals(Object o) {
        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
