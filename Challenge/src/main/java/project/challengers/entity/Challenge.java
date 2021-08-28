package project.challengers.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "CHALLENGE")
@DiscriminatorValue("Challenge")
@SuperBuilder
public class Challenge extends Notice {

    @Column
    int money;

    @Column
    int status;
}
