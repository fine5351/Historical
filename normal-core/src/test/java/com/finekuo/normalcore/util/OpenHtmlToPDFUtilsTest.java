package com.finekuo.normalcore.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class OpenHtmlToPDFUtilsTest {

    @TempDir
    Path tempDir;

    @Test
    void testHtmlToPdfWithSimpleContent() throws Exception {
        String html = "<html><body><h1>Hello World</h1><p>This is a test.</p></body></html>";
        
        byte[] pdfBytes = OpenHtmlToPDFUtils.htmlToPdf(html);
        
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        
        // Verify it's a valid PDF using PDFBox
        try (PDDocument document = PDDocument.load(pdfBytes)) {
            assertTrue(document.getNumberOfPages() > 0);
        }
        
        log.info("Simple HTML to PDF conversion successful, PDF size: {} bytes", pdfBytes.length);
    }

    @Test
    void testHtmlToPdfWithChineseCharacters() throws Exception {
        String html = "<!DOCTYPE html>" +
                "<html>" +
                "<head><meta charset=\"UTF-8\"/>" +
                "<style>" +
                "body { font-family: sans-serif; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h1>測試中文標題</h1>" +
                "<p>這是一個包含中文字的測試文件。</p>" +
                "<p>支援繁體中文與簡体中文。</p>" +
                "</body>" +
                "</html>";
        
        byte[] pdfBytes = OpenHtmlToPDFUtils.htmlToPdf(html);
        
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        
        // Verify it's a valid PDF using PDFBox
        try (PDDocument document = PDDocument.load(pdfBytes)) {
            assertTrue(document.getNumberOfPages() > 0);
        }
        
        // Save to file for manual inspection
        Path outputFile = tempDir.resolve("chinese_test.pdf");
        Files.write(outputFile, pdfBytes);
        log.info("Chinese HTML to PDF conversion successful, saved to: {}", outputFile);
        log.info("PDF size: {} bytes", pdfBytes.length);
    }

    @Test
    void testHtmlToPdfWithImages() throws Exception {
        // Using base64 encoded small 1x1 pixel red PNG image for testing
        String base64Image = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8DwHwAFBQIAX8jx0gAAAABJRU5ErkJggg==";
        
        String html = "<!DOCTYPE html>" +
                "<html>" +
                "<head><meta charset=\"UTF-8\"/></head>" +
                "<body>" +
                "<h1>Image Test</h1>" +
                "<p>Below is an embedded image:</p>" +
                "<img src=\"data:image/png;base64," + base64Image + "\" alt=\"Test Image\" width=\"100\" height=\"100\"/>" +
                "</body>" +
                "</html>";
        
        byte[] pdfBytes = OpenHtmlToPDFUtils.htmlToPdf(html);
        
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        
        // Verify it's a valid PDF using PDFBox
        try (PDDocument document = PDDocument.load(pdfBytes)) {
            assertTrue(document.getNumberOfPages() > 0);
        }
        
        // Save to file for manual inspection
        Path outputFile = tempDir.resolve("image_test.pdf");
        Files.write(outputFile, pdfBytes);
        log.info("Image HTML to PDF conversion successful, saved to: {}", outputFile);
        log.info("PDF size: {} bytes", pdfBytes.length);
    }

    @Test
    void testHtmlToPdfWithOutputStream() throws Exception {
        String html = "<html><body><h1>Output Stream Test</h1></body></html>";
        
        Path outputFile = tempDir.resolve("output_stream_test.pdf");
        try (FileOutputStream fos = new FileOutputStream(outputFile.toFile())) {
            OpenHtmlToPDFUtils.htmlToPdf(html, fos);
        }
        
        assertTrue(Files.exists(outputFile));
        assertTrue(Files.size(outputFile) > 0);
        
        // Verify it's a valid PDF using PDFBox
        try (PDDocument document = PDDocument.load(outputFile.toFile())) {
            assertTrue(document.getNumberOfPages() > 0);
        }
        
        log.info("Output stream HTML to PDF conversion successful");
    }

    @Test
    void testHtmlToPdfWithComplexContent() throws Exception {
        String html = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\"/>" +
                "<style>" +
                "body { font-family: sans-serif; margin: 20px; }" +
                "h1 { color: #333; }" +
                "table { border-collapse: collapse; width: 100%; }" +
                "td, th { border: 1px solid #ddd; padding: 8px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h1>綜合測試文件</h1>" +
                "<p>這是一個包含中文和表格的測試文件。</p>" +
                "<table>" +
                "<tr><th>姓名</th><th>年齡</th><th>城市</th></tr>" +
                "<tr><td>張三</td><td>25</td><td>台北</td></tr>" +
                "<tr><td>李四</td><td>30</td><td>高雄</td></tr>" +
                "</table>" +
                "</body>" +
                "</html>";
        
        byte[] pdfBytes = OpenHtmlToPDFUtils.htmlToPdf(html);
        
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        
        // Verify it's a valid PDF using PDFBox
        try (PDDocument document = PDDocument.load(pdfBytes)) {
            assertTrue(document.getNumberOfPages() > 0);
        }
        
        // Save to file for manual inspection
        Path outputFile = tempDir.resolve("complex_test.pdf");
        Files.write(outputFile, pdfBytes);
        log.info("Complex HTML to PDF conversion successful, saved to: {}", outputFile);
        log.info("PDF size: {} bytes", pdfBytes.length);
    }

    @Test
    void testHtmlToPdfWithNullHtml() {
        assertThrows(IllegalArgumentException.class, () -> {
            OpenHtmlToPDFUtils.htmlToPdf(null);
        });
    }

    @Test
    void testHtmlToPdfWithEmptyHtml() {
        assertThrows(IllegalArgumentException.class, () -> {
            OpenHtmlToPDFUtils.htmlToPdf("");
        });
    }

    @Test
    void testHtmlToPdfWithNullOutputStream() {
        String html = "<html><body>Test</body></html>";
        assertThrows(IllegalArgumentException.class, () -> {
            OpenHtmlToPDFUtils.htmlToPdf(html, null);
        });
    }
}
