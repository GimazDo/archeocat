package com.github.gimazdo.archeocat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String text;

    private boolean correct;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Question.class, optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

}
