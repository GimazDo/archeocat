package com.github.gimazdo.archeocat.repository;

import com.github.gimazdo.archeocat.entity.Answer;
import com.github.gimazdo.archeocat.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
@Transactional
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByQuestion(Question question);
}