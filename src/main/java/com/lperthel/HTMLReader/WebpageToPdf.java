package com.lperthel.HTMLReader;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebpageToPdf {
    public static void main(String[] args) throws IOException, DocumentException {
        // Get the URL from the console input
        System.out.print("Enter the URL of the webpage to scrape: ");
        String url = System.console().readLine();

        // Use Jsoup to connect to the webpage and get the page content
        Document document = Jsoup.connect(url).get();

        // Create a new PDF document using iText
        Document pdfDocument = new Document();
        
        // Create a new BufferedOutputStream to wrap the FileOutputStream
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream("output.pdf"));
        PdfWriter.getInstance(pdfDocument, outputStream);
        pdfDocument.open();

        // Extract the text content from the webpage and add it to the PDF document
        String pageContent = document.text();
        pdfDocument.add(new Paragraph(pageContent));

        // Close the PDF document and the BufferedOutputStream
        pdfDocument.close();
        outputStream.close();

        System.out.println("PDF file created successfully.");
    }
}
