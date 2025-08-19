package com.example.myzhihu.controller;

import com.example.myzhihu.dto.ProfileRequest;
import com.example.myzhihu.entity.Profile;
import com.example.myzhihu.service.ProfileService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService)
    {
        this.profileService = profileService;
    }

    @PostMapping("/{userId}")
    public Profile createProfile(@PathVariable Long userId, @RequestBody ProfileRequest profileRequest)
    {
        return profileService.createProfile(userId, profileRequest);
    }

    @PutMapping("/{userId}")
    public Profile updateProfile(@PathVariable Long userId, @RequestBody ProfileRequest profileRequest)
    {
        return profileService.updateProfile(userId, profileRequest);
    }

    @GetMapping
    public List<Profile> getAllProfiles()
    {
        return profileService.getAllProfiles();
    }

    @GetMapping("/{userId}")
    public Profile getProfileByUserId(@PathVariable Long userId)
    {
        return profileService.getProfileByUserId(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteProfileByUserId(@PathVariable Long userId)
    {
        profileService.deleteProfileByUserId(userId);
    }
}
