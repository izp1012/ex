package com.study.ex.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Retention;

@RestController
/*RestController : 별도의 화면없이 데이터를 전송*/
public class SampleController {
    @GetMapping("/hello")
    public String[] hello(){
        return new String[]{"Hello","World"};
    }
    @GetMapping("/error")
    public String[] error(){
        return new String[]{"Error","World"};
    }
}
