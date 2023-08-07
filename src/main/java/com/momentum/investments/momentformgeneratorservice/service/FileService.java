package com.momentum.investments.momentformgeneratorservice.service;

import com.momentum.investments.momentformgeneratorservice.dto.FileLogDto;
import com.momentum.investments.momentformgeneratorservice.dto.FileStoreType;
import com.momentum.investments.momentformgeneratorservice.dto.FileType;
import com.momentum.investments.momentformgeneratorservice.exception.GenericException;
import com.momentum.investments.momentformgeneratorservice.mapper.FileLogMapper;
import com.momentum.investments.momentformgeneratorservice.repository.FileLogRepository;
import com.momentum.investments.momentformgeneratorservice.repository.FileStoreRepository;
import com.momentum.investments.momentformgeneratorservice.repository.entity.FileLog;
import com.momentum.investments.momentformgeneratorservice.repository.entity.FileStore;
import com.momentum.investments.momentformgeneratorservice.service.converter.FileConverterFactory;
import com.momentum.investments.momentformgeneratorservice.service.filestore.FileStoreFactory;
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

    private final FileStoreFactory fileStoreFactory;
    private final FileLogRepository fileLogRepository;
    private final FileConverterFactory converterFactory;

    private final FileLogMapper fileLogMapper;

    public FileService(@Autowired FileStoreFactory fileStoreFactory,
                       @Autowired FileConverterFactory converterFactory,
                       @Autowired FileLogMapper fileLogMapper,
                       @Autowired FileStoreRepository repository,
                       @Autowired  FileLogRepository fileLogRepository) {
        this.fileStoreFactory = fileStoreFactory;
        this.converterFactory = converterFactory;
        this.fileLogRepository = fileLogRepository;
        this.fileLogMapper = fileLogMapper;
    }


    public Pair<String, byte[]> getFileNameAndData(UUID fileId) throws IOException {
        var fileLog =  fileLogRepository.getFileLogById(fileId);
        var fileStoreManager =  fileStoreFactory.getFileStoreBy(fileLog.getFileStoreType());
        var content = fileStoreManager.getFileContent(fileLog.getFileType(), fileLog.getStorageId());
        return  Pair.of(fileLog.getOriginalFileName(), content);
    }

    public List<FileLogDto> getListOfFiles() {
        List<FileLog> flies = fileLogRepository.findAll();
        return flies.stream().map(fileLogMapper::mapFileLogEntitytoFileLogDto)
                .collect(Collectors.toList());
    }

    public UUID convert(UUID id, FileType targetFileType, FileStoreType fileStoreType) {

        String newFileName;
        FileLog pdfFile;

        try {
            var fileLog = fileLogRepository.getFileLogById(id);
            var fileStoreManager =  fileStoreFactory.getFileStoreBy(fileStoreType);
            byte[] content = fileStoreManager.getFileContent(fileLog.getFileType(), fileLog.getStorageId());

            var converter =  converterFactory.getConverterBy(targetFileType);
            newFileName = converter.execute(fileLog, content);
            var storageId = fileStoreManager.uploadFile(new File(newFileName));

             pdfFile = FileLog
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

        return pdfFile.getId();
    }
}
