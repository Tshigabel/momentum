package com.momentum.investments.momentformgeneratorservice.config;

import com.momentum.investments.momentformgeneratorservice.dto.FileStoreType;
import com.momentum.investments.momentformgeneratorservice.dto.FileType;
import com.momentum.investments.momentformgeneratorservice.repository.FileLogRepository;
import com.momentum.investments.momentformgeneratorservice.repository.entity.FileLog;
import com.momentum.investments.momentformgeneratorservice.service.filestore.IFileStoreManager;
import com.momentum.investments.momentformgeneratorservice.service.filestore.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class InitFileStoreTests {


    @MockBean
    private FileLogRepository fileLogRepository;


    @MockBean
    private S3Service fileManager;

    private InitFileStore initFileStore;

    @BeforeEach
    public void setup() {
        initFileStore = new InitFileStore(fileLogRepository, List.of(fileManager));
    }

    @Test
    public void shouldSaveAllExisingFilesInAWSBucketToDB() {
        when(fileManager.getListOfExistingFiles()).thenReturn(List.of("A", "B", "C", "D"));
        when(fileManager.getType()).thenReturn(FileStoreType.AWS_S3);
        when(fileLogRepository.findFileLogByStorageIdAAndAndFileType("A", FileType.CSV.name())).thenReturn(Optional.of(new FileLog()));

        initFileStore.saveCvsFiles();

        verify(fileLogRepository, times(3)).saveAndFlush(any());
    }
}
