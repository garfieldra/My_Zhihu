package com.example.myzhihu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.myzhihu.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
