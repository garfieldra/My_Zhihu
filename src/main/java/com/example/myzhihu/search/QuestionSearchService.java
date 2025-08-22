package com.example.myzhihu.search;

import java.util.List;

public interface QuestionSearchService {

    List<QuestionDocument> searchByKeyword(String keyword);

    List<QuestionDocument> searchByUsername(String username);

    void saveQuestionDocument(QuestionDocument questionDocument);

    void deleteQuestionDocumentById(Long id);
}
