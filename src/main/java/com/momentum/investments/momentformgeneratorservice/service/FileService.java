package com.momentum.investments.momentformgeneratorservice.service;

import com.momentum.investments.momentformgeneratorservice.dto.FileLogDto;
import com.momentum.investments.momentformgeneratorservice.mapper.FileLogMapper;
import com.momentum.investments.momentformgeneratorservice.repository.FileLogRepository;
import com.momentum.investments.momentformgeneratorservice.repository.entity.FileLog;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileService {

    private final FileLogMapper fileLogMapper;

    private final FileLogRepository fileLogRepository;
    public FileService(@Autowired FileLogMapper fileLogMapper, @Autowired FileLogRepository fileLogRepository) {
        this.fileLogMapper = fileLogMapper;
        this.fileLogRepository = fileLogRepository;
    }

    public List<FileLogDto> getListOfFiles() {
        List<FileLog> flies = fileLogRepository.findAll();
        return flies.stream().map(fileLogMapper::mapFileLogEntitytoFileLogDto)
                .collect(Collectors.toList());
    }
}
