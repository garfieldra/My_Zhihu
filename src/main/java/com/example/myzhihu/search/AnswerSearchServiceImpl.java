package com.example.myzhihu.search;

import com.example.myzhihu.repository.AnswerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerSearchServiceImpl implements AnswerSearchService {

    private final AnswerSearchRepository answerSearchRepository;
    public AnswerSearchServiceImpl(AnswerSearchRepository answerSearchRepository) {
        this.answerSearchRepository = answerSearchRepository;
    }

    @Override
    public List<AnswerDocument> searchByKeyword(String keyword)
    {
        return answerSearchRepository.findByQuestionTitleContainingOrContentContaining(keyword, keyword);
    }

    public List<AnswerDocument> searchByUsername(String username)
    {
        return answerSearchRepository.findByUsernameContaining(username);
    }

    public void saveAnswerDocument(AnswerDocument answerDocument)
    {
        answerSearchRepository.save(answerDocument);
    }

    public void deleteAnswerDocumentById(Long id)
    {
        answerSearchRepository.deleteById(id);
        return;
    }
}
