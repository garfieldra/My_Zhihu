package com.example.myzhihu.service;

import com.example.myzhihu.dto.CommentRequest;
import com.example.myzhihu.dto.NotificationRequest;
import com.example.myzhihu.entity.Answer;
import com.example.myzhihu.entity.Comment;
import com.example.myzhihu.entity.NotificationType;
import com.example.myzhihu.entity.User;
import com.example.myzhihu.exception.OwnershipMismatchException;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.AnswerRepository;
import com.example.myzhihu.repository.CommentRepository;
import com.example.myzhihu.repository.UserRepository;
import com.example.myzhihu.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final NotificationService notificationService;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, AnswerRepository answerRepository, NotificationService notificationService)
    {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public Comment addComment(CommentRequest commentRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = (authentication == null ? null : authentication.getName());

        if(currentUsername == null){
            throw new OwnershipMismatchException("未登录，无法发布评论");
        }

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("当前用户不存在"));

        if(commentRequest.getUserId() != null && !commentRequest.getUserId().equals(user.getId()))
        {
            throw new OwnershipMismatchException("无法以他人身份发布评论");
        }


//        User user = userRepository.findById(commentRequest.getUserId())
//                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + commentRequest.getUserId() + "的用户"));
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

        commentRepository.save(comment);

        if(!commentRequest.getUserId().equals(answer.getAuthor().getId()))
        {
            NotificationRequest notificationRequest = new NotificationRequest();
            notificationRequest.setUserId(answer.getAuthor().getId());
            notificationRequest.setNotificationType(NotificationType.ANSWER_BE_COMMENTED);
            notificationRequest.setMessage("您在" + answer.getQuestion().getTitle() + "问题下的回答有新的评论");
            notificationService.sendNotification(notificationRequest);
        }
        if(comment.getParentComment() != null && !commentRequest.getUserId().equals(comment.getParentComment().getUser().getId()))
        {
            NotificationRequest parentNotificationRequest = new NotificationRequest();
            parentNotificationRequest.setUserId(comment.getParentComment().getUser().getId());
            parentNotificationRequest.setNotificationType(NotificationType.COMMENT_BE_COMMENTED);
            parentNotificationRequest.setMessage("您在" + answer.getQuestion().getTitle() + "问题下的评论有新的回复");
            notificationService.sendNotification(parentNotificationRequest);
        }

        return comment;
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

    @Transactional
    public void deleteComment(Long commentId)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = (authentication == null ? null : authentication.getName());

        if(currentUsername == null){
            throw new OwnershipMismatchException("未登录，无法删除评论");
        }

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("当前用户不存在"));
//        if(!commentRepository.existsById(commentId))
//        {
//            throw new ResourceNotFoundException("未找到id为" + commentId + "的评论");
//        }

        Comment comment = commentRepository.findById(commentId)
                        .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + commentId + "的评论"));

        if(!comment.getUser().equals(user))
        {
            throw new OwnershipMismatchException("无权删除他人发布的评论");
        }

        commentRepository.deleteById(commentId);
        return;
    }
}

