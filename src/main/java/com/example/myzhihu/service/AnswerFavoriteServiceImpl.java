package com.example.myzhihu.service;

import com.example.myzhihu.dto.AnswerFavoriteRequest;
import com.example.myzhihu.entity.ActionType;
import com.example.myzhihu.entity.Answer;
import com.example.myzhihu.entity.AnswerFavorite;
import com.example.myzhihu.entity.User;
import com.example.myzhihu.exception.BusinessException;
import com.example.myzhihu.exception.OwnershipMismatchException;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.AnswerFavoriteRepository;
import com.example.myzhihu.repository.AnswerRepository;
import com.example.myzhihu.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.List;

@Service
public class AnswerFavoriteServiceImpl implements AnswerFavoriteService {

    private final AnswerFavoriteRepository answerFavoriteRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final FeedService feedService;

    public AnswerFavoriteServiceImpl(AnswerFavoriteRepository answerFavoriteRepository, UserRepository userRepository, AnswerRepository answerRepository, FeedService feedService) {
        this.answerFavoriteRepository = answerFavoriteRepository;
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
        this.feedService = feedService;
    }

    @Override
    public AnswerFavorite addAnswerFavorite(AnswerFavoriteRequest answerFavoriteRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = ( authentication == null ? null : authentication.getName() );

        if( currentUsername == null ) {
            throw new OwnershipMismatchException("请先登录账号，再收藏回答");
        }

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("当前用户不存在"));

        if(!answerFavoriteRequest.getUserId().equals(user.getId()))
        {
            throw new OwnershipMismatchException("无权以他人身份收藏回答");
        }

        if(answerFavoriteRepository.existsByUserIdAndAnswerId(answerFavoriteRequest.getUserId(), answerFavoriteRequest.getAnswerId())) {
            throw new BusinessException("已收藏该回答");
        }
//        User user = userRepository.findById(answerFavoriteRequest.getUserId())
//                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + answerFavoriteRequest.getUserId() + "的用户"));
        Answer answer = answerRepository.findById(answerFavoriteRequest.getAnswerId())
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + answerFavoriteRequest.getAnswerId() + "的回答"));
        AnswerFavorite answerFavorite = new AnswerFavorite();
        answerFavorite.setUser(user);
        answerFavorite.setAnswer(answer);
        answerFavoriteRepository.save(answerFavorite);

        feedService.createFeed(user.getId(), ActionType.ANSWER_FAVORITE, answer.getId());

        return answerFavorite;
    }

    @Override
    @Transactional
    public void deleteAnswerFavorite(Long userId, Long answerId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = ( authentication == null ? null : authentication.getName() );

        if( currentUsername == null ) {
            throw new OwnershipMismatchException("未登录，无法取消收藏");
        }

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("当前用户不存在"));

        if(!userId.equals(user.getId()))
        {
            throw new OwnershipMismatchException("无权删除他人收藏内容");
        }



        if(!answerFavoriteRepository.existsByUserIdAndAnswerId(userId, answerId)) {
            throw new ResourceNotFoundException("未找到要删除的已收藏回答");
        }
        answerFavoriteRepository.deleteByUserIdAndAnswerId(userId, answerId);
        feedService.removeFeed(userId, ActionType.ANSWER_FAVORITE, answerId);
        return;
    }

    @Override
    public List<AnswerFavorite> getAnswerFavoriteByUserId(Long userId) {

        return answerFavoriteRepository.findByUserId(userId);
    }

    @Override
    public List<AnswerFavorite> getAnswerFavoriteByAnswerId(Long answerId) {
        return answerFavoriteRepository.findByAnswerId(answerId);
    }

    @Override
    public boolean isFavorite(Long userId, Long answerId) {
        return answerFavoriteRepository.existsByUserIdAndAnswerId(userId, answerId);
    }

    @Override
    public int countAnswerFavoriteByUserId(Long userId) {
        return answerFavoriteRepository.countByUserId(userId);
    }

    @Override
    public int countAnswerFavoriteByAnswerId(Long answerId) {
        return answerFavoriteRepository.countByAnswerId(answerId);
    }

}
