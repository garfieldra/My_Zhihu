package com.example.myzhihu.service;

import com.example.myzhihu.dto.QuestionRequest;
import com.example.myzhihu.entity.Question;
import java.util.List;

public interface QuestionService {

    List<Question> getAllQuestions();

    Question getQuestionById(Long id);

    //Question saveQuestion(Question question);

    Question saveQuestion(QuestionRequest questionRequest);

    void deleteQuestion(Long id);
}
