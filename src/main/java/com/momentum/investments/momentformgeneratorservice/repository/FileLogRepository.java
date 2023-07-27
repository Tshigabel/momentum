package com.momentum.investments.momentformgeneratorservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.momentum.investments.momentformgeneratorservice.dto.FileType;
import com.momentum.investments.momentformgeneratorservice.repository.entity.FileLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileLogRepository extends JpaRepository<FileLog, UUID> {


    @Query(value =  "select * from momentum_investments.file_log f where f.id = :id", nativeQuery = true)
    FileLog getFileLogById(UUID id);

    @Query(value =  "select * from momentum_investments.file_log f where f.storage_id = :storageId and f.file_type = :fileType", nativeQuery = true)
    Optional<FileLog> findFileLogByStorageIdAAndAndFileType(String storageId, String fileType);
}
