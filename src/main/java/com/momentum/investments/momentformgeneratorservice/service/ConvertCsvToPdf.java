package com.momentum.investments.momentformgeneratorservice.service;

import au.com.bytecode.opencsv.CSVReader;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.momentum.investments.momentformgeneratorservice.dto.FileType;
import com.momentum.investments.momentformgeneratorservice.exception.GenericException;
import com.momentum.investments.momentformgeneratorservice.repository.FileLogRepository;
import com.momentum.investments.momentformgeneratorservice.repository.entity.FileLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.UUID;

@Slf4j
@Service
public class ConvertCsvToPdf {

    private final IFileManager fileManager;
    private final FileLogRepository fileLogRepository;

    public ConvertCsvToPdf(@Autowired IFileManager fileManager, @Autowired  FileLogRepository fileLogRepository) {
        this.fileManager = fileManager;
        this.fileLogRepository = fileLogRepository;
    }

    public String convert(UUID id) throws IOException {

        String pdfFileName;
        InputStream csvFileContentStream = null;

        try {

            var fileLog = fileLogRepository.getFileLogById(id);
            csvFileContentStream = fileManager.getFileInputStream(fileLog);

            pdfFileName = streamCvsContentToPdfFile(fileLog, csvFileContentStream);
            fileManager.uploadFile(new File(pdfFileName));

            FileLog pdfFile = FileLog
                    .builder()
                    .storageId(id.toString())
                    .originalFileName(pdfFileName)
                    .checksum(null)
                    .fileType(FileType.PDF)
                    .build();
            fileLogRepository.save(pdfFile);

        } catch (IOException e) {
            log.error("IO exception was thrown for file " +  id, e);
            throw new GenericException("We can't process your file right now. Please try again later.(558)");
        } catch (DocumentException e) {
            log.error("Document exception was thrown for file " +  id, e);
            throw new GenericException("We can't process your file right now. Please try again later.(559)");
        }
        finally {
            if (csvFileContentStream != null) {
                csvFileContentStream.close();
            }
        }

        return pdfFileName;
    }

    public String streamCvsContentToPdfFile(final FileLog fileLog, InputStream contentStream) throws IOException, DocumentException {

        /* Step -1 : Read input CSV file in */
        CSVReader reader = new CSVReader(new InputStreamReader(contentStream));
        /* Variables to loop through the CSV File */
        String[] nextLine; /* for every line in the file */
        /* Step-2: Initialize PDF documents - logical objects */
        Document my_pdf_data = new Document();
        String pfdFileName = fileLog.getId() + ".pdf";
        PdfWriter.getInstance(my_pdf_data, new FileOutputStream(pfdFileName));
        my_pdf_data.open();
        PdfPTable pdfPTable = new PdfPTable(2);
        /* Step -3: Loop through CSV file and populate data to PDF table */
        while ((nextLine = reader.readNext()) != null) {
            pdfPTable = mapCsvCellToPdf(nextLine, 0, pdfPTable);
            pdfPTable = mapCsvCellToPdf(nextLine, 1, pdfPTable);
        }
        /* Step -4: Attach table to PDF and close the document */
        my_pdf_data.add(pdfPTable);
        my_pdf_data.close();
        return pfdFileName;
    }

    private PdfPTable mapCsvCellToPdf(String [] nextLine, int index, PdfPTable pdfPTable) {
        if (index < nextLine.length) {
            var table_cell = new PdfPCell(new Phrase(nextLine[index]));
            pdfPTable.addCell(table_cell);
        }
        return pdfPTable;
    }
}
