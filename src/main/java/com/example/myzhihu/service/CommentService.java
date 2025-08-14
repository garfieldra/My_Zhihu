package com.example.myzhihu.service;

import com.example.myzhihu.dto.CommentRequest;
import com.example.myzhihu.entity.Comment;

import java.util.List;

public interface CommentService {

    Comment addComment(CommentRequest commentRequest);

//    List<Comment> getCommentsByAnswerId(Long answerId);

    List<Comment> getTopLevelCommentsByAnswerId(Long answerId);

    List<Comment> getChildCommentsByParentId(Long parentId);

    List<Comment> getCommentsByUserId(Long userId);

    void deleteComment(Long commentId);
}
