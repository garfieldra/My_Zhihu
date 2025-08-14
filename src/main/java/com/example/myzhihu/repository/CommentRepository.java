package com.example.myzhihu.repository;

import com.example.myzhihu.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByAnswerIdAndParentCommentIsNullOrderByCreatedAtAsc(Long answerId);

    List<Comment> findByParentCommentIdOrderByCreatedAtAsc(Long parentCommentId);

    List<Comment> findByUserIdOrderByCreatedAtAsc(Long userId);
}
