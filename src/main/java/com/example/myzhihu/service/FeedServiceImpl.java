package com.example.myzhihu.service;


import com.example.myzhihu.entity.ActionType;
import com.example.myzhihu.entity.Feed;
import com.example.myzhihu.entity.TargetType;
import com.example.myzhihu.entity.User;
import com.example.myzhihu.exception.BusinessException;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.FeedRepository;
import com.example.myzhihu.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;

    private final UserRepository userRepository;

    public FeedServiceImpl(FeedRepository feedRepository, UserRepository userRepository) {
        this.feedRepository = feedRepository;
        this.userRepository = userRepository;
    }

    public List<Feed> getFeedsByUserId(Long userId)
    {
        return feedRepository.findFeedByUserIdOrderByCreatedAtDesc(userId);
    }

    public Feed createFeed(Long userId, ActionType actionType, Long targetId)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + userId + "的用户"));
        if(feedRepository.existsFeedByUserIdAndActionTypeAndTargetId(userId, actionType,  targetId))
        {
            throw new BusinessException("该Feed已经存在");
        }
        Feed feed = new Feed();
        feed.setUser(user);
        feed.setActionType(actionType);
        feed.setTargetId(targetId);
        return feedRepository.save(feed);
    }

    public void deleteFeedByFeedId(Long feedId)
    {
        if(!feedRepository.existsById(feedId))
        {
            throw new ResourceNotFoundException("未找到该Feed");
        }
        feedRepository.deleteById(feedId);
        return;
    }

    @Transactional
    public void removeFeed(Long userId, ActionType actionType, Long targetId)
    {
        if(!feedRepository.existsFeedByUserIdAndActionTypeAndTargetId(userId, actionType, targetId))
        {
            throw new ResourceNotFoundException("未找到该Feed");
        }
        feedRepository.deleteFeedByUserIdAndActionTypeAndTargetId(userId, actionType, targetId);
        return;
    }
}
