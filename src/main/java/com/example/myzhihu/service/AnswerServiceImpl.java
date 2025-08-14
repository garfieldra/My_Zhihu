package com.example.myzhihu.service;

import com.example.myzhihu.dto.AnswerRequest;
import com.example.myzhihu.entity.Answer;
import com.example.myzhihu.entity.Question;
import com.example.myzhihu.entity.User;
import com.example.myzhihu.exception.ResourceNotFoundException;
import com.example.myzhihu.repository.AnswerRepository;
import com.example.myzhihu.repository.QuestionRepository;
import com.example.myzhihu.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService{

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;


    public AnswerServiceImpl(AnswerRepository answerRepository, QuestionRepository questionRepository, UserRepository userRepository)
    {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
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
        return answerRepository.save(answer);
    }

    public void deleteAnswer(Long id)
    {
        if(!answerRepository.existsById(id))
        {
            throw new ResourceNotFoundException("未找到id为" + id + "的回答");
        }
        answerRepository.deleteById(id);
        return;
    }
}
