package com.example.myzhihu.service;


import com.example.myzhihu.dto.QuestionRequest;
import com.example.myzhihu.entity.ActionType;
import com.example.myzhihu.entity.Question;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.QuestionRepository;
import com.example.myzhihu.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.myzhihu.entity.User;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService{
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final FeedService feedService;

    public QuestionServiceImpl(QuestionRepository questionRepository, UserRepository userRepository, FeedService feedService)
    {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.feedService = feedService;
    }

    public List<Question> getAllQuestions()
    {
        return questionRepository.findAll();
    }

    public Question getQuestionById(Long id)
    {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("未找到id为" + id + "的问题"));
    }

//    public Question saveQuestion(Question question)
//    {
//        return questionRepository.save(question);
//    }

    public Question saveQuestion(QuestionRequest questionRequest)
    {
        Optional<User> userOptional = userRepository.findById(questionRequest.getUserId());
        if(userOptional.isEmpty())
        {
//            return null;
            throw new ResourceNotFoundException("未找到id为" + questionRequest.getUserId() + "的用户");
        }
        User user = userOptional.get();
        Question question = new Question();
        question.setTitle(questionRequest.getTitle());
        question.setContent(questionRequest.getContent());
        question.setUser(user);
        question = questionRepository.save(question);

        feedService.createFeed(question.getUser().getId(), ActionType.CREATE_QUESTION, question.getId());

        return question;
    }

    public void deleteQuestion(Long id)
    {
        if(!questionRepository.existsById(id))
        {
            throw new ResourceNotFoundException("未找到id为" + id + "的问题");
        }
        questionRepository.deleteById(id);
        return;
    }
}
