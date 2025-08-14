package com.example.myzhihu.controller;

import com.example.myzhihu.dto.VoteRequest;
import com.example.myzhihu.entity.Vote;
import com.example.myzhihu.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votes")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService)
    {
        this.voteService = voteService;
    }

    @PostMapping
    public Vote addOrUpdateVote(@RequestBody VoteRequest voteRequest)
    {
        return voteService.addOrUpdateVote(voteRequest);
    }

    @GetMapping("/likes/{answerId}")
    public int countLikesByAnswerId(@PathVariable Long answerId)
    {
        return voteService.countLikesByAnswer(answerId);
    }

    @GetMapping("/dislikes/{answerId}")
    public int countDislikesByAnswerId(@PathVariable Long answerId)
    {
        return voteService.countDislikesByAnswer(answerId);
    }

    @DeleteMapping("/{userId}/{answerId}")
    public ResponseEntity<Void> removeVote(@PathVariable Long userId, @PathVariable Long answerId)
    {
        voteService.removeVote(userId, answerId);
        return ResponseEntity.noContent().build();
    }
}
