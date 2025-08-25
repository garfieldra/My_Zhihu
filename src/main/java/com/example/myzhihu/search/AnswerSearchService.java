package com.example.myzhihu.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface AnswerSearchService{

    List<AnswerDocument> searchByKeyword(String keyword);

    List<AnswerDocument> searchByUsername(String username);

    void saveAnswerDocument(AnswerDocument answerDocument);

    void deleteAnswerDocumentById(Long id);
}
