package com.example.myzhihu.repository;

import com.example.myzhihu.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByCommentIdAndUserId(Long commentId, Long userId);

    int countByCommentId(Long commentId);

    void deleteByCommentIdAndUserId(Long commentId, Long userId);
}
