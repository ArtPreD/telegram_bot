package ua.com.meraya.database.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter @Getter private long id;

    @Column(name = "option")
    @Setter @Getter private String option;
}
