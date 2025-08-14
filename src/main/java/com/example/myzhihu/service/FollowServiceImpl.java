package com.example.myzhihu.service;

import com.example.myzhihu.dto.FollowRequest;
import com.example.myzhihu.entity.Follow;
import com.example.myzhihu.entity.User;
import com.example.myzhihu.exception.BusinessException;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.FollowRepository;
import com.example.myzhihu.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FollowServiceImpl implements FollowService{

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowServiceImpl(FollowRepository followRepository, UserRepository userRepository)
    {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public boolean hasUserFollowed(Long followerId, Long followingId)
    {
        return followRepository.findByFollowerIdAndFollowingId(followerId, followingId).isPresent();
    }

    public Follow addFollow(FollowRequest followRequest) {
        User follower = userRepository.findById(followRequest.getFollowerId())
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + followRequest.getFollowerId() + "的用户"));
        User following = userRepository.findById(followRequest.getFollowingId())
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + followRequest.getFollowingId() + "的用户"));
        if(followRepository.findByFollowerIdAndFollowingId(followRequest.getFollowerId(), followRequest.getFollowingId()).isPresent())
        {
            throw new BusinessException("已经关注该用户，不能重复关注");
        }
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        return followRepository.save(follow);
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
        if(!followRepository.existsByFollowerIdAndFollowingId(followerId, followingId))
        {
            throw new ResourceNotFoundException("不存在改关注关系");
        }
        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
        return;
    }
}
