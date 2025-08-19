package com.example.myzhihu.service;

import com.example.myzhihu.dto.ProfileRequest;
import com.example.myzhihu.entity.Profile;
import com.example.myzhihu.entity.User;
import com.example.myzhihu.exception.BusinessException;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.ProfileRepository;
import com.example.myzhihu.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService{

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository, UserRepository userRepository)
    {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
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
        return profileRepository.save(profile);
    }

    public Profile updateProfile(Long userId, ProfileRequest profileRequest)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + userId + "的用户"));
        if(profileRepository.findByUserId(userId).isEmpty())
        {
            throw new BusinessException("该用户的个人主页不存在");
        }
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("该用户的个人主页不存在"));
        //profile.setUser(user);
        profile.setBio(profileRequest.getBio());
        profile.setAvatarUrl(profileRequest.getAvatarUrl());
        profile.setGender(profileRequest.getGender());
        profile.setWebsite(profileRequest.getWebsite());
        return profileRepository.save(profile);
    }

    public Profile getProfileByUserId(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("该用户的个人主页不存在"));
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
        return;
    }
}
