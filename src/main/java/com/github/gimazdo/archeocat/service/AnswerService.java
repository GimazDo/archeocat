package com.github.gimazdo.archeocat.service;

import com.github.gimazdo.archeocat.entity.Answer;
import com.github.gimazdo.archeocat.entity.Question;
import com.github.gimazdo.archeocat.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer add(Answer answer){
        return answerRepository.save(answer);
    }

    public List<Answer> findAllByQuestion(Question question){
        return answerRepository.findByQuestion(question);
    }

    public void delete(Answer answer){
        answerRepository.delete(answer);
    }
    public Answer getById(Long id){ return answerRepository.getById(id);}
}
