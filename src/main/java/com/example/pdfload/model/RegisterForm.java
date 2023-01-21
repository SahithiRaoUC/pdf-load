package com.example.pdfload.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterForm {
    private String course;
    private String startDate;
    private String endDate;
    private String jacsCode;
}
