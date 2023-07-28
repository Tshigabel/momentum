package com.momentum.investments.momentformgeneratorservice.service.converter;

import au.com.bytecode.opencsv.CSVReader;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.momentum.investments.momentformgeneratorservice.dto.FileType;
import com.momentum.investments.momentformgeneratorservice.exception.GenericException;
import com.momentum.investments.momentformgeneratorservice.repository.entity.FileLog;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class PdfConverterCsv implements CsvFileConverter {
    @Override
    public String execute(FileLog fileLog, InputStream contentStream) throws IOException {

        try {
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

        } catch (DocumentException exception) {
            throw new GenericException("We can't process your file right now. Please try again later.(560)");
        }
    }

    @Override
    public FileType getType() {
        return FileType.PDF;
    }

    private PdfPTable mapCsvCellToPdf(String[] nextLine, int index, PdfPTable pdfPTable) {
        if (index < nextLine.length) {
            var table_cell = new PdfPCell(new Phrase(nextLine[index]));
            pdfPTable.addCell(table_cell);
        }
        return pdfPTable;
    }
}
