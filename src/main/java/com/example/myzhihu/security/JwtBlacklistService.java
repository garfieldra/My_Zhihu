package com.example.myzhihu.security;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class JwtBlacklistService {

    private final Map<String, Long> blacklist = new ConcurrentHashMap<>();

    public void addToBlacklist(String token, long expirationTimeMillis)
    {
        blacklist.put(token, expirationTimeMillis);
    }

    public boolean isBlacklisted(String token)
    {
        Long exp = blacklist.get(token);
        if (exp == null) return false;
        if (System.currentTimeMillis() > exp)
        {
            blacklist.remove(token);
            return false;
        }
        return true;
    }
}
