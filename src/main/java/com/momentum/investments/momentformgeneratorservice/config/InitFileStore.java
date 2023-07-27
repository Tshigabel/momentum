package com.momentum.investments.momentformgeneratorservice.config;

import com.momentum.investments.momentformgeneratorservice.dto.FileType;
import com.momentum.investments.momentformgeneratorservice.repository.FileLogRepository;
import com.momentum.investments.momentformgeneratorservice.repository.entity.FileLog;
import com.momentum.investments.momentformgeneratorservice.service.IFileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import java.util.UUID;

@Configuration
@Slf4j
public class InitFileStore {

    private final FileLogRepository fileLogRepository;
    private final IFileManager fileManager;

    public InitFileStore(@Autowired FileLogRepository fileLogRepository, @Autowired IFileManager fileManager) {
        this.fileManager = fileManager;
        this.fileLogRepository = fileLogRepository;
    }

    //@PostConstruct
    @EventListener(ApplicationReadyEvent.class)
    public void saveCvsFiles() {

        log.info("beginning of initial upload");
        fileManager.getListOfExistingFiles()
                .stream()
                .filter(storageId -> !fileLogRepository.findFileLogByStorageIdAAndAndFileType(storageId, FileType.CSV.name()).isPresent())
                .map(this::createFiles)
                .forEach(fileLogRepository::saveAndFlush);

        log.info("initial upload complete");
        fileLogRepository.flush();
    }

    private FileLog createFiles(String fileStorageId) {
        return FileLog
                .builder()
                .id(UUID.randomUUID())
                .storageId(fileStorageId)
                .fileType(FileType.CSV)
                .originalFileName(UUID.randomUUID() + ".csv")
                .build();
    }
}
