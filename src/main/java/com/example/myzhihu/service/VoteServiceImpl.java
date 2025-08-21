package com.example.myzhihu.service;

import com.example.myzhihu.dto.VoteRequest;
import com.example.myzhihu.entity.*;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.AnswerRepository;
import com.example.myzhihu.repository.UserRepository;
import com.example.myzhihu.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService{

    private final VoteRepository voteRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final FeedService feedService;

    public VoteServiceImpl(VoteRepository voteRepository, AnswerRepository answerRepository, UserRepository userRepository, FeedService feedService)
    {
        this.voteRepository = voteRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.feedService = feedService;
    }

    public Vote addOrUpdateVote(VoteRequest voteRequest)
    {
        Optional<User> userOptional = userRepository.findById(voteRequest.getUserId());
        Optional<Answer> answerOptional = answerRepository.findById(voteRequest.getAnswerId());
        if(userOptional.isEmpty())
        {
            throw new ResourceNotFoundException("未找到id为" + voteRequest.getUserId() + "的用户");
        }
        else if(answerOptional.isEmpty())
        {
            throw new ResourceNotFoundException("未找到id为" + voteRequest.getAnswerId() + "的回答");
        }
        Optional<Vote> voteOptional = voteRepository.findByUserIdAndAnswerId(voteRequest.getUserId(), voteRequest.getAnswerId());
        if(voteOptional.isEmpty())
        {
            User user = userOptional.get();
            Answer answer = answerOptional.get();
            Vote vote = new Vote();
            vote.setUser(user);
            vote.setAnswer(answer);
            vote.setVoteType(voteRequest.getVoteType());
            if(voteRequest.getVoteType() == VoteType.LIKE)
            {
                vote = voteRepository.save(vote);

                feedService.createFeed(user.getId(), ActionType.LIKE_ANSWER, answer.getId());

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

    @Transactional
    public void removeVote(Long userId, Long answerId)
    {
        voteRepository.deleteByUserIdAndAnswerId(userId, answerId);
        return;
    }
}
