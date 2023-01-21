package com.example.pdfload.controller;

import com.example.pdfload.model.RegisterForm;
import com.example.pdfload.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@Slf4j
public class PdfRestController {

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/valuesFromPdf")
    @CrossOrigin(origins = {"*"})
    public ResponseEntity<RegisterForm> getValuesFromPdf() {

        RegisterForm formValuesFromPdf = fileStorageService.getStudentFormValuesFromPdf();
        log.info("Values Retrieved form Pdf : {}", formValuesFromPdf);

        if (Objects.isNull(formValuesFromPdf)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

        return ResponseEntity.ok()
                .body(formValuesFromPdf);

    }

}
