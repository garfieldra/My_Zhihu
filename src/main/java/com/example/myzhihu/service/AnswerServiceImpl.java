package com.example.myzhihu.service;

import com.example.myzhihu.dto.AnswerRequest;
import com.example.myzhihu.entity.ActionType;
import com.example.myzhihu.entity.Answer;
import com.example.myzhihu.entity.Question;
import com.example.myzhihu.entity.User;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.AnswerRepository;
import com.example.myzhihu.repository.FeedRepository;
import com.example.myzhihu.repository.QuestionRepository;
import com.example.myzhihu.repository.UserRepository;
import com.example.myzhihu.search.AnswerDocument;
import com.example.myzhihu.search.AnswerSearchRepository;
import com.example.myzhihu.search.AnswerSearchService;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService{

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final FeedService feedService;
    private final AnswerSearchService answerSearchService;


    public AnswerServiceImpl(AnswerRepository answerRepository, QuestionRepository questionRepository, UserRepository userRepository, FeedService feedService, AnswerSearchService answerSearchService)
    {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.feedService = feedService;
        this.answerSearchService = answerSearchService;
    }

    public List<Answer> getAllAnswers()
    {
        return answerRepository.findAll();
    }

    public Answer getAnswerById(Long id)
    {
        return answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + id + "的回答"));
    }

    public Answer saveAnswer(AnswerRequest answerRequest)
    {
        Optional<Question> questionOptional = questionRepository.findById(answerRequest.getQuestionId());
        Optional<User> userOptional = userRepository.findById(answerRequest.getAuthorId());
        if(questionOptional.isEmpty())
        {
            throw new ResourceNotFoundException("未找到id为" + answerRequest.getQuestionId() + "的问题");
        } else if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("未找到id为" + answerRequest.getAuthorId() + "的用户");
        }
        User author = userOptional.get();
        Question question = questionOptional.get();
        Answer answer = new Answer();
        answer.setAuthor(author);
        answer.setContent(answerRequest.getContent());
        answer.setQuestion(question);
        answer = answerRepository.save(answer);


        AnswerDocument answerDocument = new AnswerDocument(
                answer.getId(),
                answer.getQuestion().getTitle(),
                answer.getContent(),
                answer.getAuthor().getUsername(),
                answer.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        );
        answerSearchService.saveAnswerDocument(answerDocument);


        feedService.createFeed(author.getId(), ActionType.CREATE_ANSWER, answer.getId());

        return answer;
    }

    public void deleteAnswer(Long id)
    {
        if(!answerRepository.existsById(id))
        {
            throw new ResourceNotFoundException("未找到id为" + id + "的回答");
        }
        answerRepository.deleteById(id);

        answerSearchService.deleteAnswerDocumentById(id);
        return;
    }
}
