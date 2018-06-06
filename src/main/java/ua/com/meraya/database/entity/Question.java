package ua.com.meraya.database.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter @Getter private long id;

    @Column(name = "question")
    @Setter @Getter private String question;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "answer_id")
    @Setter @Getter private Option answer;

    @ElementCollection(targetClass = Option.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "answer_options", joinColumns = @JoinColumn(name = "question_id"))
    @Setter @Getter List<Option> options;
}
