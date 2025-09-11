package com.example.myzhihu.service;


import com.example.myzhihu.dto.QuestionRequest;
import com.example.myzhihu.entity.ActionType;
import com.example.myzhihu.entity.Question;
import com.example.myzhihu.exception.OwnershipMismatchException;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.QuestionRepository;
import com.example.myzhihu.repository.UserRepository;
import com.example.myzhihu.search.QuestionDocument;
import com.example.myzhihu.search.QuestionSearchService;
import com.example.myzhihu.search.QuestionSearchServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.myzhihu.entity.User;

import org.springframework.security.core.Authentication;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService{
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final FeedService feedService;
    private final QuestionSearchService questionSearchService;
    private final RedisTemplate<String, Object> redisTemplate;

    public QuestionServiceImpl(QuestionRepository questionRepository, UserRepository userRepository, FeedService feedService, QuestionSearchService questionSearchService, RedisTemplate<String, Object> redisTemplate)
    {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.feedService = feedService;
        this.questionSearchService = questionSearchService;
        this.redisTemplate = redisTemplate;
    }

    public List<Question> getAllQuestions()
    {
        return questionRepository.findAll();
    }

    public Question getQuestionById(Long id)
    {
        String key = "question:" + id;
        Question cachedquestion = (Question) redisTemplate.opsForValue().get(key);
        if(cachedquestion != null)
        {
            return cachedquestion;
        }

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + id + "的问题"));
        redisTemplate.opsForValue().set(key, question, Duration.ofMinutes(20));
        return question;
    }

//    public Question saveQuestion(Question question)
//    {
//        return questionRepository.save(question);
//    }

    public Question saveQuestion(QuestionRequest questionRequest)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = (authentication != null ? authentication.getName() : null);
        if(currentUsername == null)
        {
            throw new OwnershipMismatchException("需要登录才能发布问题");
        }

        User user= userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("该用户不存在"));

        if(questionRequest.getUserId() != null && !questionRequest.getUserId().equals(user.getId()))
        {
            throw new OwnershipMismatchException("无法以他人账号发布问题");
        }

//        Optional<User> userOptional = userRepository.findById(questionRequest.getUserId());
//        if(userOptional.isEmpty())
//        {
////            return null;
//            throw new ResourceNotFoundException("未找到id为" + questionRequest.getUserId() + "的用户");
//        }
//        User user = userOptional.get();
        Question question = new Question();
        question.setTitle(questionRequest.getTitle());
        question.setContent(questionRequest.getContent());
        question.setUser(user);
        question = questionRepository.save(question);

        feedService.createFeed(question.getUser().getId(), ActionType.CREATE_QUESTION, question.getId());

        QuestionDocument questionDocument = new QuestionDocument(
                question.getId(),
                question.getTitle(),
                question.getContent(),
                question.getUser().getUsername(),
                question.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        );
        questionSearchService.saveQuestionDocument(questionDocument);

        String key = "question:" + question.getId();
        redisTemplate.opsForValue().set(key, question, Duration.ofMinutes(20));

        return question;
    }

    public void deleteQuestion(Long id)
    {
//        if(!questionRepository.existsById(id))
//        {
//            throw new ResourceNotFoundException("未找到id为" + id + "的问题");
//        }
//        questionRepository.deleteById(id);
//        questionSearchService.deleteQuestionDocumentById(id);
//        return;
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + id + "的问题"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = (authentication != null ? authentication.getName() : null);

        if(currentUsername == null || !question.getUser().getUsername().equals(currentUsername))
        {
            throw new OwnershipMismatchException("没有删除该问题的权限");
        }
        questionRepository.deleteById(id);

        String key = "question:" + id;
        redisTemplate.delete(key);

        questionSearchService.deleteQuestionDocumentById(id);
    }
}
