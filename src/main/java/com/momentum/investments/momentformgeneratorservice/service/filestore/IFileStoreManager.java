package com.momentum.investments.momentformgeneratorservice.service.filestore;

import com.momentum.investments.momentformgeneratorservice.dto.FileType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface IFileStoreManager {

    InputStream getFileInputStream(FileType fileType, String storageId) throws IOException;

    String uploadFile(File file);

    List<String> getListOfExistingFiles();
}
