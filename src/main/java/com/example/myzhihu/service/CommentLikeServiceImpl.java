package com.example.myzhihu.service;

import com.example.myzhihu.dto.CommentLikeRequest;
import com.example.myzhihu.dto.NotificationRequest;
import com.example.myzhihu.entity.Comment;
import com.example.myzhihu.entity.CommentLike;
import com.example.myzhihu.entity.NotificationType;
import com.example.myzhihu.entity.User;
import com.example.myzhihu.exception.BusinessException;
import com.example.myzhihu.exception.OwnershipMismatchException;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.CommentLikeRepository;
import com.example.myzhihu.repository.CommentRepository;
import com.example.myzhihu.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class CommentLikeServiceImpl implements CommentLikeService{

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public CommentLikeServiceImpl(CommentLikeRepository commentLikeRepository, CommentRepository commentRepository, UserRepository userRepository, NotificationService notificationService)
    {
        this.commentLikeRepository = commentLikeRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public boolean hasUserLiked(Long commentId, Long userId) {
//        if(!commentLikeRepository.findByCommentIdAndUserId(commentId, userId).isEmpty())
//        {
//            return true;
//        }
//        else
//        {
//            return false;
//        }
        return commentLikeRepository.findByCommentIdAndUserId(commentId, userId).isPresent();
    }

    @Transactional
    public CommentLike addCommentLike(CommentLikeRequest commentLikeRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = (authentication == null ? null : authentication.getName());
        if(currentUsername == null)
        {
            throw new OwnershipMismatchException("未登录，无法点赞评论");
        }
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("当前用户不存在"));

        if(!commentLikeRequest.getUserId().equals(user.getId()))
        {
            throw new OwnershipMismatchException("无权以他人身份点赞评论");
        }

        Comment comment = commentRepository.findById(commentLikeRequest.getCommentId())
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + commentLikeRequest.getCommentId() + "的评论"));
//        User user = userRepository.findById(commentLikeRequest.getUserId())
//                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + commentLikeRequest.getUserId() + "的用户"));
//        if(this.hasUserLiked(commentLikeRequest.getCommentId(), commentLikeRequest.getUserId()))
        if(commentLikeRepository.findByCommentIdAndUserId(commentLikeRequest.getCommentId(), commentLikeRequest.getUserId()).isPresent())
        {
            throw new BusinessException("用户已经点赞过该评论");
        }
        CommentLike commentLike = new CommentLike();
        commentLike.setComment(comment);
        commentLike.setUser(user);
        commentLikeRepository.save(commentLike);

        if(!commentLikeRequest.getUserId().equals(comment.getUser().getId()))
        {
            NotificationRequest notificationRequest = new NotificationRequest();
            notificationRequest.setUserId(comment.getUser().getId());
            notificationRequest.setNotificationType(NotificationType.COMMENT_BE_LIKED);
            notificationRequest.setMessage("您在问题" + commentLike.getComment().getAnswer().getQuestion().getTitle() + "下的评论收到了新的点赞");
            notificationService.sendNotification(notificationRequest);
        }

        return commentLike;
    }

    public int countCommentLikesByCommentId(Long commentId) {
        return commentLikeRepository.countByCommentId(commentId);
    }

    @Transactional
    public void removeLike(Long commentId, Long userId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = (authentication == null ? null : authentication.getName());
        if(currentUsername == null)
        {
            throw new OwnershipMismatchException("未登录，无法取消点赞");
        }
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("当前用户不存在"));

        if(userId != null && !userId.equals(user.getId()))
        {
            throw new OwnershipMismatchException("无权取消该点赞");
        }
        if(commentLikeRepository.findByCommentIdAndUserId(commentId, userId).isEmpty())
        {
            throw new ResourceNotFoundException("未找到用户id为" + userId + "评论id为" + commentId + "的点赞信息");
        }
        commentLikeRepository.deleteByCommentIdAndUserId(commentId, userId);
        return;
    }
}
