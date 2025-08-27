package com.example.myzhihu.service;

import com.example.myzhihu.dto.FollowRequest;
import com.example.myzhihu.entity.ActionType;
import com.example.myzhihu.entity.Follow;
import com.example.myzhihu.entity.User;
import com.example.myzhihu.exception.BusinessException;
import com.example.myzhihu.exception.OwnershipMismatchException;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.FollowRepository;
import com.example.myzhihu.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Service
public class FollowServiceImpl implements FollowService{

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final FeedService feedService;

    public FollowServiceImpl(FollowRepository followRepository, UserRepository userRepository, FeedService feedService)
    {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.feedService = feedService;
    }

    public boolean hasUserFollowed(Long followerId, Long followingId)
    {
        return followRepository.findByFollowerIdAndFollowingId(followerId, followingId).isPresent();
    }

    @Transactional
    public Follow addFollow(FollowRequest followRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = (authentication == null ? null : authentication.getName());
        if(currentUsername == null)
        {
            throw new OwnershipMismatchException("未登录，无法关注用户");
        }
        User follower = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("未找到当前用户"));

        if(!followRequest.getFollowerId().equals(follower.getId()))
        {
            throw new OwnershipMismatchException("无法以他人账号关注用户");
        }

//        User follower = userRepository.findById(followRequest.getFollowerId())
//                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + followRequest.getFollowerId() + "的用户"));
        User following = userRepository.findById(followRequest.getFollowingId())
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + followRequest.getFollowingId() + "的用户"));
        if(followRepository.findByFollowerIdAndFollowingId(followRequest.getFollowerId(), followRequest.getFollowingId()).isPresent())
        {
            throw new BusinessException("已经关注该用户，不能重复关注");
        }
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        follow = followRepository.save(follow);

        feedService.createFeed(follow.getFollower().getId(), ActionType.FOLLOW_USER,  follow.getFollowing().getId());

        return follow;
    }

    public int countFollowers(Long userId)
    {
        return followRepository.countByFollowingId(userId);
    }

    public int countFollowings(Long userId)
    {
        return followRepository.countByFollowerId(userId);
    }

    public List<User> findFollowers(Long userId) {
        return followRepository.findFollowersByUserId(userId); //查找userId的所有粉丝的信息
    }

    public List<User> findFollowings(Long userId) {
        return followRepository.findFollowingsByUserId(userId); //查找userId关注的所有用户的信息
    }

    @Transactional
    public void removeFollow(Long followerId, Long followingId)
    {
//        if(!followRepository.findByFollowerIdAndFollowingId(followerId, followingId).isPresent())
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = (authentication == null ? null : authentication.getName());

        if(currentUsername == null)
        {
            throw new OwnershipMismatchException("未登录，无法取消关注");
        }
        User follower = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("当前用户不存在"));

        if(!followerId.equals(follower.getId()))
        {
            throw new OwnershipMismatchException("无法以他人身份取消关注");
        }


        if(!followRepository.existsByFollowerIdAndFollowingId(followerId, followingId))
        {
            throw new ResourceNotFoundException("不存在该关注关系");
        }
        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
        return;
    }
}
