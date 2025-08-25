package com.example.myzhihu.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface AnswerSearchRepository extends ElasticsearchRepository<AnswerDocument, Long> {

    List<AnswerDocument> findByQuestionTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);

    List<AnswerDocument> findByUsernameContaining(String username);
}
