package project.challengers.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "CHALLENGE")
@DiscriminatorValue("Challenge")
@SuperBuilder
@OnDelete(action = OnDeleteAction.CASCADE)
public class Challenge extends Notice {

    @Column
    int money;

    @Column
    int status;
}
