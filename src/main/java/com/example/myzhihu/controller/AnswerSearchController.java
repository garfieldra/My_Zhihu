package com.example.myzhihu.controller;

import com.example.myzhihu.search.AnswerDocument;
import com.example.myzhihu.search.AnswerSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AnswerSearchController {

    private final AnswerSearchService answerSearchService;

    public AnswerSearchController(AnswerSearchService answerSearchService) {
        this.answerSearchService = answerSearchService;
    }

    @GetMapping("/search/answers/{keyword}")
    public List<AnswerDocument> searchByKeyword(@PathVariable String keyword)
    {
        return answerSearchService.searchByKeyword(keyword);
    }

    @GetMapping("/search/answers/user/{username}")
    public List<AnswerDocument> searchByUsername(@PathVariable String username)
    {
        return answerSearchService.searchByUsername(username);
    }

}
