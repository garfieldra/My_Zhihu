package com.example.myzhihu.controller;

import com.example.myzhihu.dto.CommentRequest;
import com.example.myzhihu.entity.Comment;
import com.example.myzhihu.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService)
    {
        this.commentService = commentService;
    }

    @PostMapping
    public Comment addComment(@RequestBody CommentRequest commentRequest)
    {
        return commentService.addComment(commentRequest);
    }

    @GetMapping("/answer/{answerId}")
    public List<Comment> getTopLevelCommentsByAnswerId(@PathVariable Long answerId)
    {
        return commentService.getTopLevelCommentsByAnswerId(answerId);
    }

    @GetMapping("/{commentId}/replies")
    public List<Comment> getChildCommentsByParentId(@PathVariable Long commentId)
    {
        return commentService.getChildCommentsByParentId(commentId);
    }

    @GetMapping("/user/{userId}")
    public List<Comment> getCommentsByUserId(@PathVariable Long userId)
    {
        return commentService.getCommentsByUserId(userId);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId)
    {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
