package com.example.techeerbackend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Question {
    @Id
    private Long id;

    @Column
    private String imgUrl;

    @Column
    private String answer;

    @Column
    private String solution;
}
