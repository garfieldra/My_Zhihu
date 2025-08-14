package com.example.myzhihu.service;

import com.example.myzhihu.dto.CommentLikeRequest;
import com.example.myzhihu.entity.CommentLike;

public interface CommentLikeService {

    CommentLike addCommentLike(CommentLikeRequest commentLikeRequest);

    int countCommentLikesByCommentId(Long commentId);

    void removeLike(Long commentId, Long userId);

    boolean hasUserLiked(Long commentId, Long userId);
}
