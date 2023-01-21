package com.example.pdfload.service;

import com.example.pdfload.model.RegisterForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class FileStorageService {

    @Value("${pdf.file.path}")
    private String pdfFilePath;

    private static final String SEPARATOR = "&";
    private static final DateTimeFormatter FILE_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter OUTPUT_DATE_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy");
    private static final String JACS_CODE_START_INDEX_STR = "JACS CODE (";
    private static final String JACS_CODE_END_INDEX_STR = ")";
    private static final String PDF_START_LINE_STR = "Course name";
    private static final String PDF_LINE_END_STR = "Terms and Conditions";

    public RegisterForm getStudentFormValuesFromPdf() {

        try (PDDocument pdDocument = PDDocument.load(new File(pdfFilePath))) {

            /*
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setWordSeparator("|");
            stripper.setSortByPosition(true);
            //Hard coded positions for now
            Rectangle rect = new Rectangle(0, 225, 1000, 80);
            stripper.addRegion("class1", rect);
            stripper.extractRegions(pdDocument.getPage(pdDocument.getNumberOfPages() - 2));
            stripper.getTextForRegion("class1");
             */

            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            pdfTextStripper.setWordSeparator(SEPARATOR);
            pdfTextStripper.setStartPage(pdDocument.getNumberOfPages() - 1);
            pdfTextStripper.setEndPage(pdDocument.getNumberOfPages() - 1);
            String text = pdfTextStripper.getText(pdDocument);

            String[] lines = text.substring(text.indexOf(PDF_START_LINE_STR),
                    text.indexOf(PDF_LINE_END_STR)).split("\n");

            log.info("Extracted lines : {}", lines);

            String[] courseLine = lines[1].split(SEPARATOR);
            String jacsCodeLine = lines[2].substring((lines[2].indexOf(JACS_CODE_START_INDEX_STR) + JACS_CODE_START_INDEX_STR.length()),
                    lines[2].lastIndexOf(JACS_CODE_END_INDEX_STR));

            return RegisterForm.builder()
                    .course(courseLine[0])
                    .startDate(LocalDate.parse(courseLine[1], FILE_DATE_FORMATTER).format(OUTPUT_DATE_FORMATTER))
                    .endDate(LocalDate.parse(courseLine[2], FILE_DATE_FORMATTER).format(OUTPUT_DATE_FORMATTER))
                    .jacsCode(jacsCodeLine)
                    .build();


        } catch (IOException e) {
            log.error("Error Occurred while reading Pdf", e);
        } catch (Exception e) {
            log.error("Error Occurred while fetching the data from Pdf", e);
        }

        return null;

    }
}
