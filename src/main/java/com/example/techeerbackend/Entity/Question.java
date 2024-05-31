package com.example.techeerbackend.Entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String base64Image;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String answer;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String solution;

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
