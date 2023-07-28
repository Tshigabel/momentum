package com.momentum.investments.momentformgeneratorservice.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.momentum.investments.momentformgeneratorservice.dto.FileType;
import com.momentum.investments.momentformgeneratorservice.repository.entity.FileLog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@Import({FileLogMapperImpl.class})
public class FileLogMapperTests {

    @Autowired
    private FileLogMapperImpl fileLogMapper;

    @Test
    public void shouldMapFileLogDtoFileLogDtoSuccessfully() {

        var source = FileLog
                .builder()
                .storageId("storageId")
                .fileType(FileType.CSV)
                .id(UUID.randomUUID())
                .creationDate(Timestamp.from(Instant.now()))
                .modifiedDate(Timestamp.from(Instant.now()))
                .originalFileName("test.csv")
                .build();

        var target = fileLogMapper.mapFileLogEntitytoFileLogDto(source);

        assertEquals(FileType.CSV, target.getFileType());
        assertEquals(source.getStorageId(), target.getStorageId());
        assertEquals(source.getOriginalFileName(), target.getOriginalFileName());
        assertEquals(source.getId(), target.getId());
        assertEquals(source.getCreationDate(), target.getCreationDate());
        assertEquals(source.getModifiedDate(), target.getModifiedDate());
    }
}
