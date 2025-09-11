package com.example.myzhihu.service;

import com.example.myzhihu.dto.AnswerRequest;
import com.example.myzhihu.entity.ActionType;
import com.example.myzhihu.entity.Answer;
import com.example.myzhihu.entity.Question;
import com.example.myzhihu.entity.User;
import com.example.myzhihu.exception.OwnershipMismatchException;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.AnswerRepository;
import com.example.myzhihu.repository.FeedRepository;
import com.example.myzhihu.repository.QuestionRepository;
import com.example.myzhihu.repository.UserRepository;
import com.example.myzhihu.search.AnswerDocument;
import com.example.myzhihu.search.AnswerSearchRepository;
import com.example.myzhihu.search.AnswerSearchService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService{

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final FeedService feedService;
    private final AnswerSearchService answerSearchService;
    private final RedisTemplate<String, Object> redisTemplate;


    public AnswerServiceImpl(AnswerRepository answerRepository, QuestionRepository questionRepository, UserRepository userRepository, FeedService feedService, AnswerSearchService answerSearchService, RedisTemplate<String, Object> redisTemplate)
    {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.feedService = feedService;
        this.answerSearchService = answerSearchService;
        this.redisTemplate = redisTemplate;
    }

    public List<Answer> getAllAnswers()
    {
        return answerRepository.findAll();
    }

    @Override
    public Answer getAnswerById(Long id)
    {
        String key = "answer:" + id;

        Answer cachedAnswer = (Answer) redisTemplate.opsForValue().get(key);
        if(cachedAnswer != null)
        {
            return cachedAnswer;
        }

        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + id + "的回答"));

        redisTemplate.opsForValue().set(key, answer, Duration.ofMinutes(20));

        return answer;

    }

    public Answer saveAnswer(AnswerRequest answerRequest)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = (authentication != null ? authentication.getName() : null);

        if(currentUsername == null)
        {
            throw new OwnershipMismatchException("未登录，无法发布回答");
        }

        User author = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("当前用户不存在"));

        if(answerRequest.getAuthorId() != null && !answerRequest.getAuthorId().equals(author.getId()))
        {
            throw new OwnershipMismatchException("无法以他人身份发布回答");
        }

        Question question = questionRepository.findById(answerRequest.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + answerRequest.getQuestionId() + "的问题"));


//        Optional<Question> questionOptional = questionRepository.findById(answerRequest.getQuestionId());
//        Optional<User> userOptional = userRepository.findById(answerRequest.getAuthorId());
//        if(questionOptional.isEmpty())
//        {
//            throw new ResourceNotFoundException("未找到id为" + answerRequest.getQuestionId() + "的问题");
//        } else if (userOptional.isEmpty()) {
//            throw new ResourceNotFoundException("未找到id为" + answerRequest.getAuthorId() + "的用户");
//        }
//        User author = userOptional.get();
//        Question question = questionOptional.get();
        Answer answer = new Answer();
        answer.setAuthor(author);
        answer.setContent(answerRequest.getContent());
        answer.setQuestion(question);
        answer = answerRepository.save(answer);

        String key = "answer:" + answer.getId();
        redisTemplate.opsForValue().set(key, answer, Duration.ofMinutes(20));

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
//        if(!answerRepository.existsById(id))
//        {
//            throw new ResourceNotFoundException("未找到id为" + id + "的回答");
//        }
//        answerRepository.deleteById(id);
//
//        answerSearchService.deleteAnswerDocumentById(id);
//        return;
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + id + "的回答"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = (authentication != null ? authentication.getName() : null);

        if(currentUsername == null || !answer.getAuthor().getUsername().equals(currentUsername))
        {
            throw new OwnershipMismatchException("没有删除该回答的权限");
        }
        answerRepository.deleteById(id);

        String key = "answer:" + id;
        redisTemplate.delete(key);

        answerSearchService.deleteAnswerDocumentById(id);
        return;
    }
}
