package com.example.myzhihu.search;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionSearchServiceImpl implements QuestionSearchService {

    private final QuestionSearchRepository questionSearchRepository;

    public QuestionSearchServiceImpl(QuestionSearchRepository questionSearchRepository) {
        this.questionSearchRepository = questionSearchRepository;
    }

    @Override
    public List<QuestionDocument> searchByKeyword(String keyword)
    {
        return questionSearchRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }

    @Override
    public List<QuestionDocument> searchByUsername(String username)
    {
        return questionSearchRepository.findByUsernameContaining(username);
    }

    @Override
    public void saveQuestionDocument(QuestionDocument questionDocument) {
        questionSearchRepository.save(questionDocument);
    }

    @Override
    public void deleteQuestionDocumentById(Long id) {
        questionSearchRepository.deleteById(id);
    }
}
