package com.example.myzhihu.repository;

import com.example.myzhihu.entity.Vote;
import com.example.myzhihu.entity.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    int countByAnswerIdAndVoteType(Long answerId, VoteType voteType);

    int deleteByUserIdAndAnswerId(Long userId, Long answerId);

    Optional<Vote> findByUserIdAndAnswerId(Long userId, Long answerId);
}
