package com.example.myzhihu.repository;

import com.example.myzhihu.entity.ActionType;
import com.example.myzhihu.entity.Feed;
import com.example.myzhihu.entity.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    List<Feed>  findFeedByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsFeedByUserIdAndActionTypeAndTargetId(Long userId, ActionType actionType, Long targetId);
}
