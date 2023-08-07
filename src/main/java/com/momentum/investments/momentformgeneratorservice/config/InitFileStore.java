package com.momentum.investments.momentformgeneratorservice.config;

import com.momentum.investments.momentformgeneratorservice.dto.FileStoreType;
import com.momentum.investments.momentformgeneratorservice.dto.FileType;
import com.momentum.investments.momentformgeneratorservice.repository.FileLogRepository;
import com.momentum.investments.momentformgeneratorservice.repository.entity.FileLog;
import com.momentum.investments.momentformgeneratorservice.service.filestore.IFileStoreManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Configuration
@Slf4j
public class InitFileStore {

    private final FileLogRepository fileLogRepository;
    private final List<IFileStoreManager> fileManager;

    public InitFileStore(@Autowired FileLogRepository fileLogRepository, @Autowired List<IFileStoreManager> fileManager) {
        this.fileManager = fileManager;
        this.fileLogRepository = fileLogRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void saveCvsFiles() {

        log.info("beginning of initial upload");
        fileManager.forEach(fileManager -> {
            try {
                fileManager.getListOfExistingFiles()
                        .stream()
                        .filter(storageId -> fileLogRepository.findFileLogByStorageIdAAndAndFileType(storageId, FileType.CSV.name()).isEmpty())
                        .map(storageId -> createFiles(storageId, fileManager.getType()))
                        .forEach(fileLogRepository::saveAndFlush);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        log.info("initial upload complete");
        fileLogRepository.flush();
    }

    private FileLog createFiles(String fileStorageId, FileStoreType fileStoreType) {
        log.info("***********************************************************");
        log.info(fileStoreType.toString());
        log.info("***********************************************************");
        return FileLog
                .builder()
                .id(UUID.randomUUID())
                .storageId(fileStorageId)
                .fileType(FileType.CSV)
                .fileStoreType(fileStoreType)
                .originalFileName(UUID.randomUUID() + ".csv")
                .build();
    }
}
