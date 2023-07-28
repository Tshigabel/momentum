package com.momentum.investments.momentformgeneratorservice.service.converter;


import com.momentum.investments.momentformgeneratorservice.dto.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileConverterFactory {

    private final Map<FileType, CsvFileConverter> csvFileConverterList;

    public FileConverterFactory (@Autowired List<CsvFileConverter> fileConverters) {
        csvFileConverterList = new HashMap<>();
        fileConverters.forEach(c -> csvFileConverterList.put(c.getType(), c));
    }

    public CsvFileConverter getConverterBy( FileType fileType) {
        return csvFileConverterList.get(fileType);
    }
}
