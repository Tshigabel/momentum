package com.momentum.investments.momentformgeneratorservice.service.converter;


import com.momentum.investments.momentformgeneratorservice.dto.FileType;
import com.momentum.investments.momentformgeneratorservice.repository.entity.FileLog;

import java.io.IOException;
import java.io.InputStream;

public interface CsvFileConverter {

    String execute(final FileLog fileLog, InputStream contentStream) throws IOException;

    FileType getType();
}
