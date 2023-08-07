package com.momentum.investments.momentformgeneratorservice.controller;

import com.momentum.investments.momentformgeneratorservice.dto.FileLogDto;
import com.momentum.investments.momentformgeneratorservice.dto.FileStoreType;
import com.momentum.investments.momentformgeneratorservice.dto.FileType;
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
@CrossOrigin("*")
public class FileController {
    private final FileService fileService;

    public FileController(@Autowired FileService fileService) {
        this.fileService = fileService;
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
    @Operation(summary = "convert csv file to pdf", description = "give a unique file id for a csv file, covert the file and return file ref")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    public UUID
    convert(@RequestParam UUID fileId,@RequestParam FileType targetFileType,@RequestParam FileStoreType fileStoreType)  throws IOException {

        log.info("starting conversion for " + fileId);
        var newFileId = fileService.convert(fileId, targetFileType, fileStoreType);
        log.info("completed conversion for " + fileId);

        //@todo add validation for already exising files
        return newFileId;
    }

    @GetMapping ("/download")
    @Operation(summary = "convert csv file to pdf", description = "give a unique file id for a csv file, covert and download pdf file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
    })
    public ResponseEntity<byte[]>
    download(@RequestParam UUID fileId)  throws IOException {

        log.info("beginning download of file " + fileId);
        var fileNameAndBytes = fileService.getFileNameAndData(fileId);

        log.info("completed download of file " + fileId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="
                        + fileNameAndBytes.getFirst())
                .body(fileNameAndBytes.getSecond());
    }
}
