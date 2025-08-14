package com.example.myzhihu.repository;

import com.example.myzhihu.entity.Follow;
import com.example.myzhihu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    int countByFollowerId(Long userId); //按照关注者id查找（找到的是该用户关注的用户的数量）

    int countByFollowingId(Long userId); //按照被关注者id查找（找到的是该用户的粉丝数量）

    @Query("SELECT f.follower FROM Follow f WHERE f.following.id = :userId")
    List<User> findFollowersByUserId(Long userId);

    @Query("SELECT f.following FROM Follow f WHERE f.follower.id = :userId")
    List<User> findFollowingsByUserId(Long userId);

    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
