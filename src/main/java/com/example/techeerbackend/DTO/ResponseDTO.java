package com.example.techeerbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ResponseDTO {
    private int status; // 200: OK, 400: ERROR
    private Object response;
}
