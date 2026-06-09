package com.Backend.JobTracker.Config;

import com.Backend.JobTracker.Repository.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DbKeepAlive {
    @Autowired
    private JobRepo jobRepo;

    // Ping DB every 4 minutes — keeps Clever Cloud awake!
    @Scheduled(fixedRate = 240000)
    public void keepAlive() {
        try {
            long count = jobRepo.count();
            System.out.println("DB keep-alive ✅ total jobs: " + count);
        } catch (Exception e) {
            System.out.println("DB ping failed ❌ " + e.getMessage());
        }
    }
}
