package com.example.myzhihu.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UserSearchRepository extends ElasticsearchRepository<UserDocument, Long> {
    List<UserDocument> findByUsername(String username);
}
