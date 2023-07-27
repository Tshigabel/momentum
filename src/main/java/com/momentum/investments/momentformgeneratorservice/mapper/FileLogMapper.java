package com.momentum.investments.momentformgeneratorservice.mapper;

import com.momentum.investments.momentformgeneratorservice.dto.FileLogDto;
import com.momentum.investments.momentformgeneratorservice.repository.entity.FileLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileLogMapper {

    FileLogDto mapFileLogEntitytoFileLogDto(FileLog fileLog);
}
