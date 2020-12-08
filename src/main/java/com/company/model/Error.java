package com.company.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Error {

    private int status;
    private String message;
    private String urlDocumentation;
}
