package com.labhesh.MeroMeal.advice;



import com.labhesh.MeroMeal.exception.BadRequestException;
import com.labhesh.MeroMeal.exception.ForbiddenException;
import com.labhesh.MeroMeal.exception.InternalServerException;
import com.labhesh.MeroMeal.exception.NotFoundException;
import com.labhesh.MeroMeal.utils.ErrorResponse;
import jakarta.servlet.ServletException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleInvalidArgument(MethodArgumentNotValidException e) {
        List<String> errorMessages = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errorMessages.add(fieldError.getDefaultMessage());
        });
        return ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(errorMessages)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ErrorResponse handleUnsupportedMediaType(HttpMediaTypeNotSupportedException e){
        return ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(List.of("Content-Type 'application/json' is only supported"))
                .build();
    };

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleUserNotFound(NotFoundException e){
        return ErrorResponse.builder()
                .code(404)
                .error("Not Found")
                .message(List.of(e.getMessage()))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ErrorResponse handleUserNameNotFound(UsernameNotFoundException e){
        return ErrorResponse.builder()
                .code(400)
                .error("Bad Request")
                .message(List.of(e.getMessage()))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServletException.class)
    public ErrorResponse handleServlet(ServletException e){
        return ErrorResponse.builder()
                .code(400)
                .error("Bad Request")
                .message(List.of(e.getMessage()))
                .build();
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse handleUserExist(DataIntegrityViolationException e){
        return ErrorResponse.builder()
                .code(400)
                .error("Bad Request")
                .message(List.of(e.getMessage()))
                .build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerException.class)
    public ErrorResponse handleInternalServerException(InternalServerException e){
        return ErrorResponse.builder()
                .code(500)
                .error("Internal Server Error")
                .message(List.of(e.getMessage()))
                .build();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ErrorResponse handleForbiddenException(ForbiddenException e){
        return ErrorResponse.builder()
                .code(403)
                .error("Forbidden")
                .message(List.of(e.getMessage()))
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorResponse handleBadRequestException(BadRequestException e){
        return ErrorResponse.builder()
                .code(400)
                .error("Bad Request")
                .message(List.of(e.getMessage()))
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorResponse handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        return ErrorResponse.builder()
                .code(404)
                .error("Not Found")
                .message(List.of(e.getMessage()))
                .build();
    }
}
