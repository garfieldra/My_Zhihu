package com.example.myzhihu.controller;


import com.example.myzhihu.search.QuestionDocument;
import com.example.myzhihu.search.QuestionSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QuestionSearchController {

    private final QuestionSearchService questionSearchService;

    public QuestionSearchController(QuestionSearchService questionSearchService)
    {
        this.questionSearchService = questionSearchService;
    }

    @GetMapping("/search/questions/{keyword}")
    public List<QuestionDocument> searchByKeyword(@PathVariable String keyword)
    {
        return questionSearchService.searchByKeyword(keyword);
    }

    @GetMapping("/search/questions/user/{username}")
    public List<QuestionDocument> searchByUsername(@PathVariable String username)
    {
        return questionSearchService.searchByUsername(username);
    }
}
