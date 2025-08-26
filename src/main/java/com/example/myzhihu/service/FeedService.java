package com.example.myzhihu.service;

import com.example.myzhihu.entity.ActionType;
import com.example.myzhihu.entity.Feed;
import com.example.myzhihu.entity.TargetType;
import com.example.myzhihu.entity.User;

import java.util.List;

public interface FeedService {

    List<Feed>  getFeedsByUserId(Long userId);

    Feed createFeed(Long userId, ActionType actionType, Long targetId);

    void deleteFeedByFeedId(Long feedId);

    void removeFeed(Long userId, ActionType actionType, Long targetId);
}
