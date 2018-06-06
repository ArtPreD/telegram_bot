package ua.com.meraya.database.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
public class MyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter @Getter private long id;

    @Column(name = "user_id")
    @Getter @Setter private int userId;

    @Getter @Setter private String username;

    @Column(name = "first_name")
    @Getter @Setter private String firstName;

    @Column(name = "last_name")
    @Getter @Setter private String lastName;

    @Getter @Setter private long score;
}
