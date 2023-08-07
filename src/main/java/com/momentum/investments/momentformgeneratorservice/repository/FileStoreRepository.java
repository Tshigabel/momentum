package com.momentum.investments.momentformgeneratorservice.repository;

import com.momentum.investments.momentformgeneratorservice.repository.entity.FileStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface FileStoreRepository extends JpaRepository<FileStore, UUID> {
}
