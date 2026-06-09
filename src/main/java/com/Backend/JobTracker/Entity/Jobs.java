package com.Backend.JobTracker.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Jobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String CompanyName;
    private String Role;
    private LocalDate ApplyDate;
    private String status;
    private String Notes;
    private String resumeUrl;

    @Override
    public String toString() {
        return "Jobs{" +
                "id=" + id +
                ", CompanyName='" + CompanyName + '\'' +
                ", Role='" + Role + '\'' +
                ", ApplyDate=" + ApplyDate +
                ", status='" + status + '\'' +
                ", Notes='" + Notes + '\'' +
                ", resumeUrl='" + resumeUrl + '\'' +
                '}';
    }
}
