package com.smoothstack.userservice.service;

import com.smoothstack.userservice.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DuplicateCheckService {
    private final ConcurrentHashMap<String, String> duplicateCache = new ConcurrentHashMap<>();

    @Autowired
    private AppUserRepository appUserRepository;

    public boolean checkAndPrepareDuplicates(Path file) {
        final boolean[] hasDuplicates = {false};
        try (Stream<String> lines = Files.lines(file)) {
            lines.skip(1)
                    .map(line -> line.split(",")[0])
                    .forEach(username -> {
                        System.out.println(username);
                        if (appUserRepository.findByUsername(username).isPresent() || duplicateCache.containsKey(username)) {
                            System.out.println(duplicateCache);
                            duplicateCache.putIfAbsent(username, "PENDING");
                            hasDuplicates[0] = true;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasDuplicates[0];
    }

    // Fetches all duplicates with "PENDING" status
    public Map<String, String> getPendingDuplicates() {
        return duplicateCache.entrySet().stream()
                .filter(entry -> "PENDING".equals(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // Check for duplicates in the cache and database
    public boolean checkDuplicate(String username) {
        return duplicateCache.containsKey(username) || appUserRepository.findByUsername(username).isPresent();
    }

    // Retrieves the decision for a username
    public String getDecision(String username) {
        return duplicateCache.getOrDefault(username, "PENDING");
    }

    // Resolves the decision on a duplicate
    public void resolveDuplicate(String username, String decision) {
        duplicateCache.put(username, decision);
        System.out.println("Updated decision for " + username + ": " + decision);
        System.out.println("Current cache state: " + duplicateCache.toString());
    }
    // Clears all entries from the duplicate cache
    public void clearDuplicateCache() {
        duplicateCache.clear();
    }
}

