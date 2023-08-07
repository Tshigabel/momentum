package com.momentum.investments.momentformgeneratorservice.service.filestore;

import com.momentum.investments.momentformgeneratorservice.dto.FileStoreType;
import com.momentum.investments.momentformgeneratorservice.dto.FileType;
import com.momentum.investments.momentformgeneratorservice.exception.GenericException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.sftp.session.SftpSession;

import java.io.*;
import java.util.Arrays;
import java.util.List;


@Slf4j
public class AtmozService implements IFileStoreManager {

    private final SftpSession sftpSession;
    public AtmozService(final SftpSession sftpSession) {
        this.sftpSession = sftpSession;
    }

    @Override
    public byte[] getFileContent(FileType fileType, String storageId) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        sftpSession.read(storageId, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public String uploadFile(File file) {

        try {
            InputStream inputStream = new FileInputStream(file);
            sftpSession.write(inputStream, file.getName());
        } catch (FileNotFoundException e) {
            log.error("failed to read file", e);
            throw new GenericException("Failed to read file (570)");
        } catch (IOException e) {
            log.error("failed to write to sftp", e);
            throw new GenericException("Failed to write to sftp, please try again later (571)");
        } finally {
            sftpSession.close();
        }


        return file.getName();
    }

    @Override
    public List<String> getListOfExistingFiles() throws GenericException {
        try {
            return Arrays.asList(sftpSession.listNames(""));
        } catch (IOException ioException)  {
            throw new GenericException("failed to get list of existing files");
        }
    }

    @Override
    public FileStoreType getType() {
        return FileStoreType.ATMOZ_SFTP;
    }
}
