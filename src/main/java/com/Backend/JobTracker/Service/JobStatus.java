package com.Backend.JobTracker.Service;

import com.Backend.JobTracker.Entity.Jobs;
import com.Backend.JobTracker.Repository.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JobStatus {
    @Autowired
    private JobRepo jobRepo;

    public Map<String , Object> status(){

        List<Jobs> allJobs = jobRepo.findAll();

        long totalJobs = allJobs.size();
        long totalInterviews = allJobs.stream().filter((a)-> a.getStatus().equals("Interview")).count();
        long totalOffers = allJobs.stream().filter((a)-> a.getStatus().equals("Offers")).count();
        double responseRate = totalJobs > 0
                ? Math.round((double)(totalJobs - jobRepo.findByStatus("Applied").size())
                             / totalJobs * 100) : 0;
        return Map.of(
                "totalJobs" , totalJobs,
                "totalInterviews" , totalInterviews,
                "totalOffers" , totalOffers,
                "responseRate" , responseRate +"%"
        );
    }
}
