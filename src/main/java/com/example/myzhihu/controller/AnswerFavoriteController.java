package com.example.myzhihu.controller;

import com.example.myzhihu.dto.AnswerFavoriteRequest;
import com.example.myzhihu.entity.AnswerFavorite;
import com.example.myzhihu.service.AnswerFavoriteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/answer-favorites")
public class AnswerFavoriteController {

    private AnswerFavoriteService answerFavoriteService;

    public AnswerFavoriteController(AnswerFavoriteService answerFavoriteService) {
        this.answerFavoriteService = answerFavoriteService;
    }

    @PostMapping
    public AnswerFavorite addAnswerFavorite(@RequestBody AnswerFavoriteRequest answerFavoriteRequest) {
        return answerFavoriteService.addAnswerFavorite(answerFavoriteRequest);
    }

    @DeleteMapping("/{userId}/{answerId}")
    public void deleteAnswerFavorite(@PathVariable Long userId, @PathVariable Long answerId) {
        answerFavoriteService.deleteAnswerFavorite(userId, answerId);
        return;
    }

    @GetMapping("/user/{userId}")
    public List<AnswerFavorite> getAnswerFavoriteByUserId(@PathVariable Long userId) {
        return answerFavoriteService.getAnswerFavoriteByUserId(userId);
    }

    @GetMapping("/answer/{answerId}")
    public List<AnswerFavorite> getAnswerFavoriteByAnswerId(@PathVariable Long answerId) {
        return answerFavoriteService.getAnswerFavoriteByAnswerId(answerId);
    }

    @GetMapping("/count/user/{userId}")
    public int countAnswerFavoriteByUserId(@PathVariable Long userId) {
        return answerFavoriteService.countAnswerFavoriteByUserId(userId);
    }

    @GetMapping("/count/answer/{answerId}")
    public int countAnswerFavoriteByAnswerId(@PathVariable Long answerId) {
        return answerFavoriteService.countAnswerFavoriteByAnswerId(answerId);
    }

    @GetMapping("/user/{userId}/answer/{answerId}")
    public boolean isFavorite(@PathVariable Long userId, @PathVariable Long answerId) {
        return answerFavoriteService.isFavorite(userId, answerId);
    }
}
