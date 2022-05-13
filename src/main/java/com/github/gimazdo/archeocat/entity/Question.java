package com.github.gimazdo.archeocat.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String description;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "question")
    private Set<Answer> answerSet;

    private Long index;

    @OneToOne
    private Image image;

    @Lob
    private String textIfWin;

    @Column(columnDefinition = "boolean default false")
    private boolean textAnswer;

    @OneToOne
    private Image imageWin;
    public Set<Answer> getAnswerSet() {
        if(answerSet==null)
            answerSet = new HashSet<>();
        return answerSet;
    }
}
