package com.example.myzhihu.controller;

import com.example.myzhihu.search.UserDocument;
import com.example.myzhihu.search.UserSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserSearchController {

    private final UserSearchService userSearchService;

    public UserSearchController(UserSearchService userSearchService) {
        this.userSearchService = userSearchService;
    }

    @GetMapping("/search/users/{username}")
    public List<UserDocument> searchByUsername(@PathVariable String username){
        return userSearchService.searchByUsername(username);
    }

    @GetMapping("/search/users/id/{id}")
    public UserDocument searchById(@PathVariable Long id){
        return userSearchService.searchById(id);
    }
}
