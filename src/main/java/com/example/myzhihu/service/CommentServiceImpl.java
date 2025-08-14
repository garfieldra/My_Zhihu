package com.example.myzhihu.service;

import com.example.myzhihu.dto.CommentRequest;
import com.example.myzhihu.entity.Answer;
import com.example.myzhihu.entity.Comment;
import com.example.myzhihu.entity.User;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.AnswerRepository;
import com.example.myzhihu.repository.CommentRepository;
import com.example.myzhihu.repository.UserRepository;
import com.example.myzhihu.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, AnswerRepository answerRepository)
    {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
    }

    public Comment addComment(CommentRequest commentRequest) {
        User user = userRepository.findById(commentRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + commentRequest.getUserId() + "的用户"));
        Answer answer = answerRepository.findById(commentRequest.getAnswerId())
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + commentRequest.getAnswerId() + "的回答"));
        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setUser(user);
        comment.setAnswer(answer);
        if(commentRequest.getParentCommentId() != null)
        {
            Comment parentComment = commentRepository.findById(commentRequest.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + commentRequest.getParentCommentId() + "的父评论"));
            comment.setParentComment(parentComment);
        }
        return commentRepository.save(comment);
    }

    public List<Comment> getTopLevelCommentsByAnswerId(Long answerId) {
        return commentRepository.findByAnswerIdAndParentCommentIsNullOrderByCreatedAtAsc(answerId);
    }

    public List<Comment> getChildCommentsByParentId(Long parentId) {
        return commentRepository.findByParentCommentIdOrderByCreatedAtAsc(parentId);
    }

    public List<Comment> getCommentsByUserId(Long userId) {
        return commentRepository.findByUserIdOrderByCreatedAtAsc(userId);
    }

    public void deleteComment(Long commentId)
    {
        if(!commentRepository.existsById(commentId))
        {
            throw new ResourceNotFoundException("未找到id为" + commentId + "的评论");
        }
        commentRepository.deleteById(commentId);
        return;
    }
}

