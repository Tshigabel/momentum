package com.momentum.investments.momentformgeneratorservice.service.filestore;

import com.momentum.investments.momentformgeneratorservice.dto.FileStoreType;
import com.momentum.investments.momentformgeneratorservice.dto.FileType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface IFileStoreManager {

    byte[] getFileContent(FileType fileType, String storageId) throws IOException;

    String uploadFile(File file) throws FileNotFoundException;

    List<String> getListOfExistingFiles() throws IOException;

    FileStoreType getType();
}
