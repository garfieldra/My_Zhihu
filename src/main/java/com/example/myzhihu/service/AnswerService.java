package com.example.myzhihu.service;

import com.example.myzhihu.dto.AnswerRequest;
import com.example.myzhihu.entity.Answer;

import java.util.List;

public interface AnswerService {

    List<Answer> getAllAnswers();

    Answer getAnswerById(Long id);

    Answer saveAnswer(AnswerRequest answerRequest);

    void deleteAnswer(Long id);

}
