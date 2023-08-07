package com.momentum.investments.momentformgeneratorservice.service.filestore;

import com.momentum.investments.momentformgeneratorservice.dto.FileStoreType;
import com.momentum.investments.momentformgeneratorservice.dto.FileType;
import com.momentum.investments.momentformgeneratorservice.exception.FileUploadException;
import com.momentum.investments.momentformgeneratorservice.exception.GenericException;
import com.momentum.investments.momentformgeneratorservice.repository.FileStoreRepository;
import com.momentum.investments.momentformgeneratorservice.repository.entity.FileStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.sftp.session.SftpSession;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Slf4j
public class DbService implements IFileStoreManager {

    private final FileStoreRepository fileStoreRepository;
    public DbService(final FileStoreRepository fileStoreRepository) {
        this.fileStoreRepository = fileStoreRepository;
    }

    @Override
    public byte[] getFileContent(FileType fileType, String storageId) throws IOException {

        var fileStoreId = UUID.fromString(storageId);
        FileStore fileStore = fileStoreRepository.findById(fileStoreId).orElseThrow();
        return fileStore.getFileData();
    }

    @Override
    public String uploadFile(File file) {

        try {
            var bytes = Files.readAllBytes(file.toPath());

            FileStore
                    .builder()
                    .fileData(bytes)
                    .build();

        } catch (IOException e) {
            log.error("failed to read file bytes ", e);
            throw new FileUploadException("failed to read file bytes ");
        }

        return file.getName();
    }

    @Override
    public List<String> getListOfExistingFiles() throws GenericException {
        try {
            return List.of("");
        } catch (Exception ioException)  {
            throw new GenericException("failed to get list of existing files");
        }
    }

    @Override
    public FileStoreType getType() {
        return FileStoreType.ATMOZ_SFTP;
    }
}
