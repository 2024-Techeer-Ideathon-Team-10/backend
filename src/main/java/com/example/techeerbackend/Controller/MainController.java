package com.example.techeerbackend.Controller;

import com.example.techeerbackend.Service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {
    @Autowired
    MainService service;
}
