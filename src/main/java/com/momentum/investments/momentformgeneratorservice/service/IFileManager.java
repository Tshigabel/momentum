package com.momentum.investments.momentformgeneratorservice.service;

import com.momentum.investments.momentformgeneratorservice.repository.entity.FileLog;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IFileManager {

    InputStream getFileInputStream(final FileLog fileLog) throws IOException;

    void uploadFile(File file);

    List<String> getListOfExistingFiles();
}
