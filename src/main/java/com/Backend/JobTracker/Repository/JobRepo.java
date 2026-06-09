package com.Backend.JobTracker.Repository;

import com.Backend.JobTracker.Entity.Jobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface JobRepo extends JpaRepository<Jobs , Long> {

    List<Jobs> findByStatus(String applied);
}
