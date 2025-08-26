package com.example.myzhihu.repository;

import com.example.myzhihu.entity.AnswerFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerFavoriteRepository extends JpaRepository<AnswerFavorite, Long> {

    List<AnswerFavorite> findByUserId(Long userId);

    List<AnswerFavorite> findByAnswerId(Long answerId);

    boolean existsByUserIdAndAnswerId(Long userId, Long answerId);

    void deleteByUserIdAndAnswerId(Long userId, Long answerId);

    int countByUserId(Long userId);

    int countByAnswerId(Long answerId);
}
