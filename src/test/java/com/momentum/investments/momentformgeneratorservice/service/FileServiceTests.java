package com.momentum.investments.momentformgeneratorservice.service;


import com.momentum.investments.momentformgeneratorservice.dto.FileStoreType;
import com.momentum.investments.momentformgeneratorservice.dto.FileType;
import com.momentum.investments.momentformgeneratorservice.mapper.FileLogMapper;
import com.momentum.investments.momentformgeneratorservice.mapper.FileLogMapperImpl;
import com.momentum.investments.momentformgeneratorservice.repository.FileLogRepository;
import com.momentum.investments.momentformgeneratorservice.repository.entity.FileLog;
import com.momentum.investments.momentformgeneratorservice.service.converter.CsvFileConverter;
import com.momentum.investments.momentformgeneratorservice.service.converter.FileConverterFactory;
import com.momentum.investments.momentformgeneratorservice.service.filestore.FileStoreFactory;
import com.momentum.investments.momentformgeneratorservice.service.filestore.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import({FileLogMapperImpl.class})
public class FileServiceTests {

    @MockBean
    FileStoreFactory fileStoreFactory;

    @MockBean
    CsvFileConverter csvFileConverter;

    @MockBean
    S3Service s3Service;

    @Autowired
    FileLogMapper fileLogMapper;
    @MockBean
    FileLogRepository fileLogRepository;

    FileConverterFactory converterFactory;
    FileService fileService;

    @BeforeEach
    public void setup() throws  IOException {
        when(csvFileConverter.getType()).thenReturn(FileType.PDF);
        when(s3Service.getFileContent(any(), any())).thenReturn(new byte[]{});
        when(s3Service.getType()).thenReturn(FileStoreType.AWS_S3);
        converterFactory = new FileConverterFactory(List.of(csvFileConverter));
        fileStoreFactory = new FileStoreFactory(List.of(s3Service));
        fileService = new FileService(fileStoreFactory, converterFactory, fileLogMapper, null, fileLogRepository);
    }

    @Test
    public void shouldMapAllFilesInRepositoryToDtoList() {

        FileLog fileLog = getFileLog();
        when(fileLogRepository.findAll()).thenReturn(List.of(fileLog));
        var fileLogDtos = fileService.getListOfFiles();

        assertEquals(fileLog.getId(), fileLogDtos.get(0).getId());
        verify(fileLogRepository, times(1)).findAll();
    }

    @Test
    public void shouldConvertFileSuccessfullyWhenConvertIsCalled() throws IOException {

        FileLog fileLog = getFileLog();

        when(fileLogRepository.getFileLogById(any())).thenReturn(fileLog);
        when(csvFileConverter.execute(any(), any())).thenReturn("newFileName.pdf");

        fileService.convert(UUID.randomUUID(), FileType.PDF, FileStoreType.AWS_S3);

        verify(fileLogRepository, times(1)).getFileLogById(any());
        verify(csvFileConverter, times(1)).execute(any(), any());
        verify(fileLogRepository, times(1)).save(any());
    }

    private FileLog getFileLog() {
        return FileLog.builder()
                .storageId("storageId")
                .fileType(FileType.CSV)
                .id(UUID.randomUUID())
                .creationDate(Timestamp.from(Instant.now()))
                .modifiedDate(Timestamp.from(Instant.now()))
                .originalFileName("test.csv")
                .build();
    }
}
