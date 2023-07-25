package com.example.project.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Overview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String fileKey;

    private String description;

    private LocalDateTime date;

    public Overview(String fileKey, String description, LocalDateTime date) {
        this.fileKey = fileKey;
        this.description = description;
        this.date=date;
    }

}
