package com.example.myzhihu.controller;

import com.example.myzhihu.dto.AnswerRequest;
import com.example.myzhihu.entity.Answer;
import com.example.myzhihu.service.AnswerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService)
    {
        this.answerService = answerService;
    }

    @GetMapping
    public List<Answer> getAllAnswers()
    {
        return answerService.getAllAnswers();
    }

    @GetMapping("/{id}")
    public Answer getAnswerById(@PathVariable Long id)
    {
        return answerService.getAnswerById(id);
    }

    @PostMapping
    public Answer saveAnswer(@RequestBody AnswerRequest answerRequest)
    {
        return answerService.saveAnswer(answerRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteAnswer(@PathVariable Long id)
    {
        answerService.deleteAnswer(id);
        return;
    }

}
