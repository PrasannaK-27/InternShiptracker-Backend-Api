package com.Backend.JobTracker.Service;

import com.Backend.JobTracker.Entity.Jobs;
import com.Backend.JobTracker.Exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.Backend.JobTracker.Repository.JobRepo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    @Autowired
    private JobRepo jobRepo;

    //Fetching all jobs...
    public ResponseEntity<List<Jobs>> getAllJobs(){
        return ResponseEntity.ok(jobRepo.findAll());
    }
    //Creating a new jobs with resume file..!!
    public ResponseEntity<Jobs> saveJob(Jobs job , MultipartFile Resume){

        if(Resume != null && !Resume.isEmpty()) {

            String fileName = Resume.getOriginalFilename(); //

            Path path = Paths.get("uploads", fileName);
            try {
                Files.copy(
                        Resume.getInputStream(),
                        path,
                        StandardCopyOption.REPLACE_EXISTING
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new ResponseEntity<>(jobRepo.save(job) , HttpStatus.CREATED);
    }
   //Fetching a single job by there ID...
    public Jobs findByID(Long id){
        return jobRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("There is no Job found with this id..:"+id));
    }
    //Updating jobs by Using there Ids...!!
    public ResponseEntity<Jobs> updateJobById(Long id , Jobs job , MultipartFile Resume){

       Jobs editJob = jobRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("There is no Job found with this id..:"+id));
       editJob.setCompanyName(job.getCompanyName());
       editJob.setRole(job.getRole());
       editJob.setApplyDate(job.getApplyDate());
       editJob.setStatus(job.getStatus());
       editJob.setNotes(job.getNotes());
//       editJob.setResumeUrl(job.getResumeUrl());

       if(Resume != null && !Resume.isEmpty()){
           String oldFile = editJob.getResumeUrl();
           if(oldFile != null){
               try {
                   Files.deleteIfExists(Paths.get("uploads", oldFile));
                   String newFileName = Resume.getOriginalFilename();
                   Files.copy( Resume.getInputStream(),
                               Paths.get("uploads", newFileName),
                               StandardCopyOption.REPLACE_EXISTING );
                   editJob.setResumeUrl(newFileName);
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
           }else{
               try {
                   Files.copy( Resume.getInputStream(),
                           Paths.get("uploads", Resume.getOriginalFilename()),
                           StandardCopyOption.REPLACE_EXISTING );
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
               editJob.setResumeUrl(Resume.getOriginalFilename());
           }
       }

       jobRepo.save(editJob);
       return ResponseEntity.ok(editJob);
    }
    //Delete job by there Id...!!
    public ResponseEntity<?> deleteJobById(Long id){
        Jobs deleteJob = jobRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("There is no Job found with this id..:"+id));
        String hasToDeleteFile = deleteJob.getResumeUrl();
        if(hasToDeleteFile != null && !hasToDeleteFile.isEmpty()){
            try {
                Files.deleteIfExists(Paths.get("uploads", hasToDeleteFile));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        jobRepo.deleteById(deleteJob.getId());
        return ResponseEntity.ok("The record is deleted including Resume file also..!!");
    }

    public ResponseEntity<Jobs> updatingExitingStatusById(Long id, Jobs job) {
        Jobs editJobStatus = jobRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("There is no Job found with this id..:"+id));
        editJobStatus.setStatus(job.getStatus());
        return ResponseEntity.ok(jobRepo.save(editJobStatus));
    }

    public ResponseEntity<?> resumeUrlDel(Long id, String fileName) {
        Jobs hasToDelResumePath = jobRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("There is no Job found with this id..:"+id));
        if( hasToDelResumePath.getResumeUrl().equals(fileName) ){
            try {
                Files.deleteIfExists(Paths.get("uploads", fileName));
                hasToDelResumePath.setResumeUrl(null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(hasToDelResumePath);
        }
        return ResponseEntity.ok(jobRepo.save(hasToDelResumePath));
    }
}
