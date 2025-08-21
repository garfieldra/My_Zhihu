package com.example.myzhihu.controller;

import com.example.myzhihu.entity.Feed;
import com.example.myzhihu.service.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feeds")
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/user/{userId}")
    public List<Feed> getFeedsByUserId(@PathVariable Long userId)
    {
        return feedService.getFeedsByUserId(userId);
    }

    @DeleteMapping("/{feedId}")
    public ResponseEntity<Void> deleteFeedById(@PathVariable Long feedId)
    {
        feedService.deleteFeedByFeedId(feedId);
        return  ResponseEntity.noContent().build();
    }
}
