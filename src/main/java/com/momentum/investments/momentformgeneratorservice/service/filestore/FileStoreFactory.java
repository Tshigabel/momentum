package com.momentum.investments.momentformgeneratorservice.service.filestore;

import com.momentum.investments.momentformgeneratorservice.dto.FileStoreType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileStoreFactory {

    private final Map<FileStoreType, IFileStoreManager> fileStoreManagerMap;
    public FileStoreFactory(List<IFileStoreManager> fileStoreManagerList) {
        fileStoreManagerMap = new HashMap<>();
        fileStoreManagerList.forEach(c -> fileStoreManagerMap.put(c.getType(), c));
    }

    public IFileStoreManager getFileStoreBy(FileStoreType fileStoreType) {
        return fileStoreManagerMap.get(fileStoreType);
    }
}
