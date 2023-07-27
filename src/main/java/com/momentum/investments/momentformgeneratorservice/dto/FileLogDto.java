package com.momentum.investments.momentformgeneratorservice.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.util.UUID;

@Data
public class FileLogDto {
    private UUID id;
    private Timestamp creationDate;
    private Timestamp modifiedDate;
    private String originalFileName;
    private String checksum;
    private FileType fileType;
    private String storageId;
    private String userId;
}
