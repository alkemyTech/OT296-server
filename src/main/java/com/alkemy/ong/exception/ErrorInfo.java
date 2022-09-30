package com.alkemy.ong.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
@AllArgsConstructor
public class ErrorInfo {

    private final String url;
    private final String message;

    public ErrorInfo(HttpServletRequest request, Exception ex) {
        this.url = request.getRequestURL().toString();
        this.message = ex.getMessage();
    }
}