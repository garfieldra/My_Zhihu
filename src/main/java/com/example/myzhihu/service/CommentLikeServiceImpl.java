package com.example.myzhihu.service;

import com.example.myzhihu.dto.CommentLikeRequest;
import com.example.myzhihu.entity.Comment;
import com.example.myzhihu.entity.CommentLike;
import com.example.myzhihu.entity.User;
import com.example.myzhihu.exception.BusinessException;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.CommentLikeRepository;
import com.example.myzhihu.repository.CommentRepository;
import com.example.myzhihu.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentLikeServiceImpl implements CommentLikeService{

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentLikeServiceImpl(CommentLikeRepository commentLikeRepository, CommentRepository commentRepository, UserRepository userRepository)
    {
        this.commentLikeRepository = commentLikeRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
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

    public CommentLike addCommentLike(CommentLikeRequest commentLikeRequest) {
        Comment comment = commentRepository.findById(commentLikeRequest.getCommentId())
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + commentLikeRequest.getCommentId() + "的评论"));
        User user = userRepository.findById(commentLikeRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + commentLikeRequest.getUserId() + "的用户"));
//        if(this.hasUserLiked(commentLikeRequest.getCommentId(), commentLikeRequest.getUserId()))
        if(commentLikeRepository.findByCommentIdAndUserId(commentLikeRequest.getCommentId(), commentLikeRequest.getUserId()).isPresent())
        {
            throw new BusinessException("用户已经点赞过该评论");
        }
        CommentLike commentLike = new CommentLike();
        commentLike.setComment(comment);
        commentLike.setUser(user);
        return commentLikeRepository.save(commentLike);
    }

    public int countCommentLikesByCommentId(Long commentId) {
        return commentLikeRepository.countByCommentId(commentId);
    }

    @Transactional
    public void removeLike(Long commentId, Long userId) {
        if(commentLikeRepository.findByCommentIdAndUserId(commentId, userId).isEmpty())
        {
            throw new ResourceNotFoundException("未找到用户id为" + userId + "评论id为" + commentId + "的点赞信息");
        }
        commentLikeRepository.deleteByCommentIdAndUserId(commentId, userId);
        return;
    }
}
