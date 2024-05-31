package com.example.techeerbackend.Controller;

import com.example.techeerbackend.DTO.ResponseDTO;
import com.example.techeerbackend.Entity.Question;
import com.example.techeerbackend.Service.MainService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/questions")
public class MainController {
    @Autowired
    MainService service;

    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = service.getAllQuestions();
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> handleImageUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(new ResponseDTO(400, "file empty"), HttpStatus.BAD_REQUEST);
        }

        try {
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());

            ResponseDTO responseDTO = service.RequestChatGpt(base64Image);

            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
