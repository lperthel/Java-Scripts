package com.lperthel.HTMLReader;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLDocumentParserByHeading{
	private static String HEADING_TAG = "h3";
	private static String OUTPUT_DIRECTORY = "C:\\Users\\lpert\\OneDrive\\Documents\\ITCareer\\Hibernate Reference Documents\\User Guide";
	public static String HTML_FILE = null;

public static void main(String[] args) {
    final Path outputDir = Paths.get(OUTPUT_DIRECTORY);
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter the heading name to extract contents: ");
    String headingName = scanner.nextLine();

    try {
        Document doc = Jsoup.parse(Paths.get(HTML_FILE).toFile(), "UTF-8", "");
        Elements headings = doc.select(HEADING_TAG);

        Map<String, String> sections = new HashMap<>();

        for (Element heading : headings) {
            String sectionTitle = heading.text();
            if (sectionTitle.equalsIgnoreCase(headingName)) {
                StringBuilder sectionContent = new StringBuilder();
                Element next = heading.nextElementSibling();
                while (next != null && !next.tagName().equals(HEADING_TAG)) {
                    sectionContent.append(next.outerHtml());
                    next = next.nextElementSibling();
                }
                String sectionText = Jsoup.parse(sectionContent.toString()).text();
                sections.put(sectionTitle, sectionText);
                break; // Stop parsing when the desired heading is found
            }
        }

        if (sections.isEmpty()) {
            System.out.println("No sections found with the specified heading name.");
        } else {
            for (Map.Entry<String, String> section : sections.entrySet()) {
                String title = section.getKey();
                String content = section.getValue();

                // create a new docx file with section content
                XWPFDocument docx = new XWPFDocument();
                XWPFParagraph para = docx.createParagraph();
                para.setAlignment(ParagraphAlignment.LEFT);

                XWPFRun run = para.createRun();
                run.setFontSize(35); // Set the font size to 35
                run.setText(content);

                Path outputPath = outputDir.resolve(title + ".docx");

                System.out.println("Opening file for writing: " + outputPath.toString());

                Files.createDirectories(outputPath.getParent());
                try (BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(outputPath))) {
                    docx.write(out);
                }

                System.out.println("File written successfully: " + outputPath.toString());
            }
        }

    } catch (IOException e) {
        System.err.println("Error while parsing HTML file: " + e.getMessage());
        e.printStackTrace();
    } finally {
        scanner.close();
    }
}

}