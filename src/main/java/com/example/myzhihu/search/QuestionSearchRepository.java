package com.example.myzhihu.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface QuestionSearchRepository extends ElasticsearchRepository<QuestionDocument, Long> {

    List<QuestionDocument> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);

    List<QuestionDocument> findByUsernameContaining(String username);
}
