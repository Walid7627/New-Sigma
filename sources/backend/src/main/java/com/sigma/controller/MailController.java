package com.sigma.controller;

import com.sigma.model.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sigma.service.impl.EmailServiceImpl;

@Controller
@RequestMapping("/mail")
public class MailController {

    @RequestMapping("/test")
    @ResponseBody
    public String test() throws com.fasterxml.jackson.core.JsonProcessingException {
      emailService.sendSimpleMessage("pi.mace@free.fr", "spring mail test", "test");

      return objectMapper.writeValueAsString(
      new ApiResponse(HttpStatus.OK,
      "Test mail sent")
      );
    }

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Autowired
    private EmailServiceImpl emailService;
}
