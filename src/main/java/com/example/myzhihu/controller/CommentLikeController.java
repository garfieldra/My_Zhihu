package com.example.myzhihu.controller;

import com.example.myzhihu.dto.CommentLikeRequest;
import com.example.myzhihu.entity.CommentLike;
import com.example.myzhihu.service.CommentLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment-likes")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    public CommentLikeController(CommentLikeService commentLikeService)
    {
        this.commentLikeService = commentLikeService;
    }

    @PostMapping
    public CommentLike addCommentLike(@RequestBody CommentLikeRequest commentLikeRequest)
    {
        return commentLikeService.addCommentLike(commentLikeRequest);
    }

    @GetMapping("/{commentId}/count")
    public int countCommentLikesByCommentId(@PathVariable Long commentId)
    {
        return commentLikeService.countCommentLikesByCommentId(commentId);
    }

    @GetMapping("/{commentId}/users/{userId}")
    public boolean hasUserLiked(@PathVariable Long commentId, @PathVariable Long userId)
    {
        return commentLikeService.hasUserLiked(commentId, userId);
    }

    @DeleteMapping("/{commentId}/users/{userId}")
    public ResponseEntity<Void> deleteCommentLike(@PathVariable Long commentId, @PathVariable Long userId)
    {
        commentLikeService.removeLike(commentId, userId);
        return ResponseEntity.noContent().build();
    }
}
