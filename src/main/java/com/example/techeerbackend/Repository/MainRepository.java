package com.example.techeerbackend.Repository;

import com.example.techeerbackend.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainRepository extends JpaRepository<Question, Long> {
}
