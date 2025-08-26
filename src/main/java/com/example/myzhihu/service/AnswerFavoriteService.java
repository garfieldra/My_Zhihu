package com.example.myzhihu.service;

import com.example.myzhihu.dto.AnswerFavoriteRequest;
import com.example.myzhihu.entity.AnswerFavorite;

import java.util.List;

public interface AnswerFavoriteService {

    public AnswerFavorite addAnswerFavorite(AnswerFavoriteRequest answerFavoriteRequest);

    public void deleteAnswerFavorite(Long userId, Long answerId);

    public List<AnswerFavorite> getAnswerFavoriteByUserId(Long userId);

    public List<AnswerFavorite> getAnswerFavoriteByAnswerId(Long answerId);

    public boolean isFavorite(Long userId, Long answerId);

    public int countAnswerFavoriteByUserId(Long userId);

    public int countAnswerFavoriteByAnswerId(Long answerId);
}
