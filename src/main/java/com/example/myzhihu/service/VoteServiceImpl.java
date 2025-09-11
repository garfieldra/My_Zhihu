package com.example.myzhihu.service;

import com.example.myzhihu.dto.NotificationRequest;
import com.example.myzhihu.dto.VoteRequest;
import com.example.myzhihu.entity.*;
import com.example.myzhihu.exception.OwnershipMismatchException;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.AnswerRepository;
import com.example.myzhihu.repository.UserRepository;
import com.example.myzhihu.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService{

    private final VoteRepository voteRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final FeedService feedService;
    private final NotificationService notificationService;

    public VoteServiceImpl(VoteRepository voteRepository, AnswerRepository answerRepository, UserRepository userRepository, FeedService feedService, NotificationService notificationService)
    {
        this.voteRepository = voteRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.feedService = feedService;
        this.notificationService = notificationService;
    }

    public Vote addOrUpdateVote(VoteRequest voteRequest)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = (authentication != null ? authentication.getName() : null);
        if(currentUsername == null)
        {
            throw new OwnershipMismatchException("未登录，无法点赞/点踩");
        }
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("当前用户不存在"));

        Answer answer = answerRepository.findById(voteRequest.getAnswerId())
                .orElseThrow(() -> new ResourceNotFoundException("当前问题不存在"));

        if(!voteRequest.getUserId().equals(user.getId()))
        {
            throw new OwnershipMismatchException("无权以他人身份点赞/点踩");
        }
//        Optional<User> userOptional = userRepository.findById(voteRequest.getUserId());
//        Optional<Answer> answerOptional = answerRepository.findById(voteRequest.getAnswerId());
//        if(userOptional.isEmpty())
//        {
//            throw new ResourceNotFoundException("未找到id为" + voteRequest.getUserId() + "的用户");
//        }
//        else if(answerOptional.isEmpty())
//        {
//            throw new ResourceNotFoundException("未找到id为" + voteRequest.getAnswerId() + "的回答");
//        }
        Optional<Vote> voteOptional = voteRepository.findByUserIdAndAnswerId(voteRequest.getUserId(), voteRequest.getAnswerId());
        if(voteOptional.isEmpty())
        {
            Vote vote = new Vote();
            vote.setUser(user);
            vote.setAnswer(answer);
            vote.setVoteType(voteRequest.getVoteType());
            if(voteRequest.getVoteType() == VoteType.LIKE)
            {
                vote = voteRepository.save(vote);

                feedService.createFeed(user.getId(), ActionType.LIKE_ANSWER, answer.getId());

                NotificationRequest notificationRequest = new NotificationRequest();
                notificationRequest.setUserId(answer.getAuthor().getId());
                notificationRequest.setNotificationType(NotificationType.ANSWER_BE_LIKED);
                notificationRequest.setMessage("您在“" + answer.getQuestion().getTitle() + "“问题下的回答收到了新的点赞");
                notificationService.sendNotification(notificationRequest);


                return vote;
            }
            return voteRepository.save(vote);
        }
        else {
            Vote vote = voteOptional.get();
            if (!vote.getVoteType().equals(voteRequest.getVoteType())) {
                vote.setVoteType(voteRequest.getVoteType());
                vote.refreshCreatedAt();//从点赞改到反对或者从反对改到点赞后需要更新创建时间
                if(voteRequest.getVoteType() == VoteType.LIKE)
                {
                    vote = voteRepository.save(vote);
                    feedService.createFeed(vote.getUser().getId(), ActionType.LIKE_ANSWER, vote.getAnswer().getId());

                    NotificationRequest notificationRequest = new NotificationRequest();
                    notificationRequest.setUserId(vote.getAnswer().getAuthor().getId());
                    notificationRequest.setNotificationType(NotificationType.ANSWER_BE_LIKED);
                    notificationRequest.setMessage("您在“" + answer.getQuestion().getTitle() + "“问题下的回答收到了新的点赞");
                    notificationService.sendNotification(notificationRequest);
                    return vote;
                }
                return voteRepository.save(vote);
            }

//            if(!vote.getVoteType().equals(voteRequest.getVoteType()) && voteRequest.getVoteType() == VoteType.LIKE)
//            {
//                vote = voteRepository.save(vote);
//                feedService.createFeed(vote.getUser().getId(), ActionType.LIKE_ANSWER, vote.getId());
//                return vote;
//            }

            return vote;
        }
    }

    public int countLikesByAnswer(Long answerId)
    {
        return voteRepository.countByAnswerIdAndVoteType(answerId, VoteType.LIKE);
    }

    public int countDislikesByAnswer(Long answerId)
    {
        return voteRepository.countByAnswerIdAndVoteType(answerId, VoteType.DISLIKE);
    }


    public int  countTotalLikesByAnswer(Long answerId)
    {
        return countLikesByAnswer(answerId) - countDislikesByAnswer(answerId);
    }

    @Transactional
    public void removeVote(Long userId, Long answerId)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = (authentication != null ? authentication.getName() : null);

        if(currentUsername == null)
        {
            throw new OwnershipMismatchException("未登录账号");
        }
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("当前用户不存在"));

        if(userId != null && !userId.equals(currentUser.getId()))
        {
            throw new OwnershipMismatchException("没有删除这一点赞/点踩的权限");
        }

        answerRepository.findById(answerId)
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + answerId + "的回答"));

        voteRepository.deleteByUserIdAndAnswerId(userId, answerId);
        return;
    }
}
