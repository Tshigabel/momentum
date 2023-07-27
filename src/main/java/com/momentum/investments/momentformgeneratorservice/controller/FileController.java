package com.momentum.investments.momentformgeneratorservice.controller;

import com.momentum.investments.momentformgeneratorservice.dto.FileLogDto;
import com.momentum.investments.momentformgeneratorservice.service.ConvertCsvToPdf;
import com.momentum.investments.momentformgeneratorservice.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/file/")
@Slf4j
public class FileController {
    private final FileService fileService;
    private final ConvertCsvToPdf convertCsvToPdf;

    public FileController(@Autowired FileService fileService, @Autowired ConvertCsvToPdf convertCsvToPdf) {
        this.fileService = fileService;
        this.convertCsvToPdf = convertCsvToPdf;
    }

    @GetMapping("/files")
    @Operation(summary = "Get list of files", description = "Returns a list of files ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    public List<FileLogDto> getListOfFiles() {
        return fileService.getListOfFiles();
    }


    @PostMapping ("/convert")
    @Operation(summary = "convert csv file to pdf", description = "give a unique file id for a csv file, covert and download pdf file")
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
