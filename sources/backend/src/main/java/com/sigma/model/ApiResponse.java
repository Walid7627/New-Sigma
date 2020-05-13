package com.sigma.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiResponse {

   public HttpStatus status;
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
   public LocalDateTime timestamp;
   public String message;
   public String debugMessage;

   public ApiResponse(HttpStatus status) {
     this.timestamp = LocalDateTime.now();
     this.status = status;
   }

   public ApiResponse(HttpStatus status, Throwable ex) {
     this.timestamp = LocalDateTime.now();
     this.status = status;
     this.message = "Unexpected error";
     this.debugMessage = ex.getLocalizedMessage();
   }

   public ApiResponse(HttpStatus status, String message, Throwable ex) {
     this.timestamp = LocalDateTime.now();
     this.status = status;
     this.message = message;
     this.debugMessage = ex.getLocalizedMessage();
   }

   public ApiResponse(HttpStatus status, String message) {
     this.timestamp = LocalDateTime.now();
     this.status = status;
     this.message = message;
   }
}
