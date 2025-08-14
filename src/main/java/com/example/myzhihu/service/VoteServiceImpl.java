package com.example.myzhihu.service;

import com.example.myzhihu.dto.VoteRequest;
import com.example.myzhihu.entity.Answer;
import com.example.myzhihu.entity.User;
import com.example.myzhihu.entity.Vote;
import com.example.myzhihu.entity.VoteType;
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

    public VoteServiceImpl(VoteRepository voteRepository, AnswerRepository answerRepository, UserRepository userRepository)
    {
        this.voteRepository = voteRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
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
            return voteRepository.save(vote);
        }
        else {
            Vote vote = voteOptional.get();
            if (!vote.getVoteType().equals(voteRequest.getVoteType())) {
                vote.setVoteType(voteRequest.getVoteType());
                vote.refreshCreatedAt(); //从点赞改到反对或者从反对改到点赞后需要更新创建时间
            }
            return voteRepository.save(vote);
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
