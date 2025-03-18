package com.example.foodordering.exception;

import com.example.foodordering.dto.request.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = RuntimeException.class)
//    ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e) {
//        ApiResponse response = new ApiResponse();
//        response.setCode(1001);
//        response.setMessage(e.getMessage());
//
//        return ResponseEntity.badRequest().body(response);
//    }
//
//    @ExceptionHandler(value = AppException.class)
//    ResponseEntity<ApiResponse> handleAppException(AppException e) {
//
//        ApiResponse response = new ApiResponse();
//
//        ErrorCode errorCode = e.getErrorCode();
//
//        response.setCode(errorCode.getCode());
//        response.setMessage(errorCode.getMessage());
//
//        return ResponseEntity.status(errorCode.getCode()).body(response);
//    }



}
