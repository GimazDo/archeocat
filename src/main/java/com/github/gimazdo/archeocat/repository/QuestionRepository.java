package com.github.gimazdo.archeocat.repository;

import com.github.gimazdo.archeocat.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

@Transactional
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "SELECT index FROM question ORDER BY index DESC LIMIT 1",
            nativeQuery = true)
    Long findLastIndex();
    Question findFirstByIndex(Long index);
}