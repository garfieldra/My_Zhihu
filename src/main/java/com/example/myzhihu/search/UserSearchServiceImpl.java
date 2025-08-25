package com.example.myzhihu.search;

import com.example.myzhihu.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserSearchServiceImpl implements UserSearchService {

    private final UserSearchRepository userSearchRepository;

    public UserSearchServiceImpl(UserSearchRepository userSearchRepository) {
        this.userSearchRepository = userSearchRepository;
    }

    @Override
    public List<UserDocument> searchByUsername(String username) {
        return userSearchRepository.findByUsername(username);
    }

    @Override
    public UserDocument searchById(Long id) {
        Optional<UserDocument> userDocument = userSearchRepository.findById(id);
        if(userDocument.isEmpty()){
            throw new ResourceNotFoundException("未找到id为" + id + "的用户");
        }
        return userDocument.get();
    }

    @Override
    public void saveUserDocument(UserDocument userDocument)
    {
        userSearchRepository.save(userDocument);
    }

    @Override
    public void deleteUserDocumentById(Long id)
    {
        userSearchRepository.deleteById(id);
    }
}
