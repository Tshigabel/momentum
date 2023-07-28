package com.momentum.investments.momentformgeneratorservice.service;

import com.momentum.investments.momentformgeneratorservice.dto.FileLogDto;
import com.momentum.investments.momentformgeneratorservice.dto.FileType;
import com.momentum.investments.momentformgeneratorservice.exception.GenericException;
import com.momentum.investments.momentformgeneratorservice.mapper.FileLogMapper;
import com.momentum.investments.momentformgeneratorservice.repository.FileLogRepository;
import com.momentum.investments.momentformgeneratorservice.repository.entity.FileLog;
import com.momentum.investments.momentformgeneratorservice.service.converter.FileConverterFactory;
import com.momentum.investments.momentformgeneratorservice.service.filestore.IFileStoreManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileService {

    private final IFileStoreManager fileStoreManager;
    private final FileLogRepository fileLogRepository;
    private final FileConverterFactory converterFactory;

    private final FileLogMapper fileLogMapper;

    public FileService(@Autowired IFileStoreManager fileStoreManager,
                       @Autowired FileConverterFactory converterFactory,
                       @Autowired FileLogMapper fileLogMapper,
                       @Autowired  FileLogRepository fileLogRepository) {
        this.fileStoreManager = fileStoreManager;
        this.converterFactory = converterFactory;
        this.fileLogRepository = fileLogRepository;
        this.fileLogMapper = fileLogMapper;
    }


    public Pair<String, byte[]> getFileNameAndData(UUID fileId) throws IOException {
        var fileLog =  fileLogRepository.getFileLogById(fileId);
        InputStream fileInputStream = fileStoreManager.getFileInputStream(fileLog.getFileType(), fileLog.getStorageId());
        return  Pair.of(fileLog.getOriginalFileName(), fileInputStream.readAllBytes());
    }

    public List<FileLogDto> getListOfFiles() {
        List<FileLog> flies = fileLogRepository.findAll();
        return flies.stream().map(fileLogMapper::mapFileLogEntitytoFileLogDto)
                .collect(Collectors.toList());
    }

    public String convert(UUID id, FileType targetFileType) throws IOException {

        String newFileName;
        InputStream csvFileContentStream = null;

        try {
            var fileLog = fileLogRepository.getFileLogById(id);
            csvFileContentStream = fileStoreManager.getFileInputStream(fileLog.getFileType(), fileLog.getStorageId());

            var converter =  converterFactory.getConverterBy(targetFileType);
            newFileName = converter.execute(fileLog, csvFileContentStream);
            var storageId = fileStoreManager.uploadFile(new File(newFileName));

            FileLog pdfFile = FileLog
                    .builder()
                    .storageId(storageId)
                    .originalFileName(newFileName)
                    .fileType(FileType.PDF)
                    .build();
            fileLogRepository.save(pdfFile);

        } catch (IOException e) {
            log.error("IO exception was thrown for file " +  id, e);
            throw new GenericException("We can't process your file right now. Please try again later.(558)");
        }
        finally {
            if (csvFileContentStream != null) {
                csvFileContentStream.close();
            }
        }

        return newFileName;
    }
}
