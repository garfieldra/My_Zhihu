package com.example.myzhihu.controller;

import com.example.myzhihu.dto.QuestionRequest;
import com.example.myzhihu.entity.Question;
import com.example.myzhihu.service.QuestionService;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService)
    {
        this.questionService = questionService;
    }

    @GetMapping
    public List<Question> getAllQuestions()
    {
        return questionService.getAllQuestions();
    }

    @GetMapping("/{id}")
    public  Question getQuestionById(@PathVariable Long id)
    {
        return questionService.getQuestionById(id);
    }

    @PostMapping
    public Question saveQuestion(@RequestBody QuestionRequest questionRequest)
    {
//        return questionService.saveQuestion(question);
        return questionService.saveQuestion(questionRequest);
    }



    @DeleteMapping("/{id}")
    public void deleteQuestion(@PathVariable Long id)
    {
        questionService.deleteQuestion(id);
        return;
    }
}
