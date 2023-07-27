package com.momentum.investments.momentformgeneratorservice.controller;

import com.itextpdf.text.DocumentException;
import com.momentum.investments.momentformgeneratorservice.dto.FileType;
import com.momentum.investments.momentformgeneratorservice.service.ConvertCsvToPdf;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/upload/")

@Slf4j
public class UploadFileController {
    private final ConvertCsvToPdf convertCsvToPdf;

    public UploadFileController(@Autowired ConvertCsvToPdf convertCsvToPdf) {
        this.convertCsvToPdf = convertCsvToPdf;
    }

    @GetMapping("/convert")
    @Operation(summary = "Get list of files", description = "Returns a list of files ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    public ResponseEntity<byte[]>
    getListOfFiles(@RequestParam UUID fileId)  throws IOException {

        log.info("starting conversion");
        var filename = convertCsvToPdf.convert(fileId);

        log.info("completed conversion of ");
        byte[] fileContent = Files.readAllBytes(new File(filename).toPath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="
                        + filename)
                .body(fileContent);
    }
}
