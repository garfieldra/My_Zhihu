package com.example.myzhihu.service;

import com.example.myzhihu.dto.ProfileRequest;
import com.example.myzhihu.entity.Profile;

import java.util.List;

public interface ProfileService {

    Profile createProfile(Long userId, ProfileRequest profileRequest);

    Profile updateProfile(Long userId, ProfileRequest profileRequest);

    Profile getProfileByUserId(Long userId);

    List<Profile> getAllProfiles();

    void deleteProfileByUserId(Long userId);
}
