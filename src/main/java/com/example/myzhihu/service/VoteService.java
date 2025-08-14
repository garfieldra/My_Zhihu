package com.example.myzhihu.service;

import com.example.myzhihu.dto.VoteRequest;
import com.example.myzhihu.entity.Vote;

public interface VoteService {

    Vote addOrUpdateVote(VoteRequest voteRequest);

    int countLikesByAnswer(Long answerId); //统计某一回答的赞同数

    int countDislikesByAnswer(Long answerId);

    void removeVote(Long userId, Long answerId);
}
