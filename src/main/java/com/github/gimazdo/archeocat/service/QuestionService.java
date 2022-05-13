package com.github.gimazdo.archeocat.service;

import com.github.gimazdo.archeocat.entity.Answer;
import com.github.gimazdo.archeocat.entity.Question;
import com.github.gimazdo.archeocat.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final AnswerService answerService;

    public Question add(Question question){
        return  questionRepository.save(question);
    }
    @Transactional
    public List<Question> getAll(){
        return  questionRepository.findAll();
    }
    @Transactional
    public Question getById(Long id){
        return  questionRepository.getById(id);
    }

    public Question getByIndex(Long index){
        return  questionRepository.findFirstByIndex(index);
    }
    @Transactional
    public Question getNext(Long prev) {
        Long last = questionRepository.findLastIndex();
        Question question = null;
            while (question == null && prev<=last) {
            question = questionRepository.findFirstByIndex(prev);
            prev++;
        }
        return question;
    }

    public Long getLastIndex(){
        return questionRepository.findLastIndex();
    }

    public void delete(Question question){
        for(Answer answer:question.getAnswerSet()){
            answerService.delete(answer);
        }
        questionRepository.delete(question);
    }
}
