package com.example.myzhihu.repository;

import com.example.myzhihu.dto.ProfileRequest;
import com.example.myzhihu.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
