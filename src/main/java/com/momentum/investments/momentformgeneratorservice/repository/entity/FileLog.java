package com.momentum.investments.momentformgeneratorservice.repository.entity;

import com.momentum.investments.momentformgeneratorservice.dto.FileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "file_log", schema = "momentum_investments")
public class FileLog {


    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {@org.hibernate.annotations.Parameter(
                    name = "uuid_gen_strategy_class",
                    value = "org.hibernate.id.uuid.CustomVersionOneStrategy")
            }
    )
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp creationDate;

    @Column(name = "modified_date")
    @UpdateTimestamp
    private Timestamp modifiedDate;

    @Column(name = "original_file_name", nullable = true)
    private String originalFileName;

    @Column(name = "checksum", nullable = true)
    private String checksum;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type")
    private FileType fileType;

    @Column(name = "storage_id", unique = true)
    private String storageId;
}