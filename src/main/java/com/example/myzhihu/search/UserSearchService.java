package com.example.myzhihu.search;

import java.util.List;

public interface UserSearchService {

    List<UserDocument> searchByUsername(String username);

    UserDocument searchById(Long id);

    void saveUserDocument(UserDocument userDocument);

    void deleteUserDocumentById(Long id);
}
