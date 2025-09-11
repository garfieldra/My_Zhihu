package com.example.myzhihu.service;

import com.example.myzhihu.dto.ProfileRequest;
import com.example.myzhihu.entity.Profile;
import com.example.myzhihu.entity.User;
import com.example.myzhihu.exception.BusinessException;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.ProfileRepository;
import com.example.myzhihu.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService{

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public ProfileServiceImpl(ProfileRepository profileRepository, UserRepository userRepository, RedisTemplate<String, Object> redisTemplate)
    {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    public Profile createProfile(Long userId, ProfileRequest profileRequest)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + userId + "的用户"));
        if(profileRepository.findByUserId(userId).isPresent())
        {
            throw new BusinessException("该用户的个人主页已存在");
        }
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setBio(profileRequest.getBio());
        profile.setAvatarUrl(profileRequest.getAvatarUrl());
        profile.setGender(profileRequest.getGender());
        profile.setWebsite(profileRequest.getWebsite());
        profileRepository.save(profile);

        String key = "profile:" + userId;
        redisTemplate.opsForValue().set(key, profile, Duration.ofMinutes(20));

        return profile;
    }

    public Profile updateProfile(Long userId, ProfileRequest profileRequest)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + userId + "的用户"));
//        if(profileRepository.findByUserId(userId).isEmpty())
//        {
//            throw new BusinessException("该用户的个人主页不存在");
//        }
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("该用户的个人主页不存在"));
        //profile.setUser(user);
        profile.setBio(profileRequest.getBio());
        profile.setAvatarUrl(profileRequest.getAvatarUrl());
        profile.setGender(profileRequest.getGender());
        profile.setWebsite(profileRequest.getWebsite());
        profileRepository.save(profile);

        String key = "profile:" + userId;
        redisTemplate.opsForValue().set(key, profile, Duration.ofMinutes(20));

        return profile;
    }

    public Profile getProfileByUserId(Long userId) {

        String key = "profile:" + userId;
        Profile cachedProfile = (Profile) redisTemplate.opsForValue().get(key);
        if(cachedProfile != null)
        {
            return cachedProfile;
        }

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("该用户的个人主页不存在"));

        redisTemplate.opsForValue().set(key, profile, Duration.ofMinutes(20));

        return profile;

    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public void deleteProfileByUserId(Long userId) {
        if(profileRepository.findByUserId(userId).isEmpty())
        {
            throw new ResourceNotFoundException("不存在该用户对应的用户主页");
        }
        profileRepository.deleteByUserId(userId);

        String key = "profile:" + userId;
        redisTemplate.delete(key);

        return;
    }
}
