package com.finekuo.normalcore.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Example usage of PdfUtils for converting HTML to PDF
 * This demonstrates the capabilities including Chinese character support and image embedding
 */
@Slf4j
public class PdfUtilsExample {

    public static void main(String[] args) {
        try {
            // Example 1: Simple HTML to PDF
            example1SimpleConversion();
            
            // Example 2: HTML with Chinese characters
            example2ChineseCharacters();
            
            // Example 3: HTML with images
            example3WithImages();
            
            // Example 4: Complex HTML with styling
            example4ComplexHtml();
            
            log.info("All examples completed successfully!");
            
        } catch (Exception e) {
            log.error("Error in examples", e);
        }
    }

    /**
     * Example 1: Simple HTML to PDF conversion
     */
    private static void example1SimpleConversion() throws Exception {
        log.info("=== Example 1: Simple HTML Conversion ===");
        
        String html = "<html><body>" +
                "<h1>Hello World</h1>" +
                "<p>This is a simple PDF generated from HTML.</p>" +
                "</body></html>";
        
        byte[] pdfBytes = PdfUtils.htmlToPdf(html);
        
        Path outputPath = Paths.get("/tmp/example1_simple.pdf");
        Files.write(outputPath, pdfBytes);
        
        log.info("Generated PDF: {} (size: {} bytes)", outputPath, pdfBytes.length);
    }

    /**
     * Example 2: HTML with Chinese characters
     */
    private static void example2ChineseCharacters() throws Exception {
        log.info("=== Example 2: Chinese Characters ===");
        
        String html = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\"/>" +
                "<style>" +
                "body { font-family: 'STSong-Light', 'STSongStd-Light', 'MSung-Light', serif; margin: 30px; }" +
                "h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }" +
                "p { line-height: 1.6; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h1>中文文檔範例</h1>" +
                "<p>這是一個支援中文的 PDF 文件。</p>" +
                "<p>繁體中文：台灣、香港、澳門</p>" +
                "<p>简体中文：中国大陆、新加坡、马来西亚</p>" +
                "<h2>功能特點</h2>" +
                "<ul>" +
                "<li>完整支援中文字符顯示</li>" +
                "<li>支援 HTML 標籤和 CSS 樣式</li>" +
                "<li>可以嵌入圖片</li>" +
                "</ul>" +
                "</body>" +
                "</html>";
        
        Path outputPath = Paths.get("/tmp/example2_chinese.pdf");
        try (FileOutputStream fos = new FileOutputStream(outputPath.toFile())) {
            PdfUtils.htmlToPdf(html, fos);
        }
        
        log.info("Generated PDF: {}", outputPath);
    }

    /**
     * Example 3: HTML with embedded images
     */
    private static void example3WithImages() throws Exception {
        log.info("=== Example 3: With Images ===");
        
        // Small base64 encoded red dot image
        String base64Image = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8DwHwAFBQIAX8jx0gAAAABJRU5ErkJggg==";
        
        String html = "<!DOCTYPE html>" +
                "<html>" +
                "<head><meta charset=\"UTF-8\"/>" +
                "<style>" +
                "body { margin: 30px; font-family: Arial, sans-serif; }" +
                "img { border: 2px solid #ddd; padding: 5px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h1>PDF with Embedded Image</h1>" +
                "<p>Below is an embedded base64 image:</p>" +
                "<img src=\"data:image/png;base64," + base64Image + "\" " +
                "alt=\"Embedded Image\" width=\"100\" height=\"100\"/>" +
                "<p>Images can be embedded using data URLs (base64 encoding).</p>" +
                "</body>" +
                "</html>";
        
        byte[] pdfBytes = PdfUtils.htmlToPdf(html);
        Path outputPath = Paths.get("/tmp/example3_images.pdf");
        Files.write(outputPath, pdfBytes);
        
        log.info("Generated PDF: {} (size: {} bytes)", outputPath, pdfBytes.length);
    }

    /**
     * Example 4: Complex HTML with styling and tables
     */
    private static void example4ComplexHtml() throws Exception {
        log.info("=== Example 4: Complex HTML ===");
        
        String html = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\"/>" +
                "<style>" +
                "body { " +
                "  font-family: 'STSong-Light', 'STSongStd-Light', 'MSung-Light', serif; " +
                "  margin: 40px; " +
                "  background-color: #f9f9f9; " +
                "}" +
                "h1 { " +
                "  color: #2c3e50; " +
                "  border-bottom: 3px solid #3498db; " +
                "  padding-bottom: 10px; " +
                "}" +
                "table { " +
                "  border-collapse: collapse; " +
                "  width: 100%; " +
                "  margin: 20px 0; " +
                "  background-color: white; " +
                "}" +
                "th { " +
                "  background-color: #3498db; " +
                "  color: white; " +
                "  padding: 12px; " +
                "  text-align: left; " +
                "}" +
                "td { " +
                "  border: 1px solid #ddd; " +
                "  padding: 10px; " +
                "}" +
                "tr:nth-child(even) { background-color: #f2f2f2; }" +
                ".info-box { " +
                "  background-color: #e8f4f8; " +
                "  border-left: 4px solid #3498db; " +
                "  padding: 15px; " +
                "  margin: 20px 0; " +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h1>員工資訊報表</h1>" +
                "<div class=\"info-box\">" +
                "<strong>報表日期：</strong> 2025年10月16日" +
                "</div>" +
                "<table>" +
                "<tr>" +
                "<th>員工編號</th>" +
                "<th>姓名</th>" +
                "<th>部門</th>" +
                "<th>職位</th>" +
                "<th>薪資</th>" +
                "</tr>" +
                "<tr>" +
                "<td>E001</td>" +
                "<td>張小明</td>" +
                "<td>技術部</td>" +
                "<td>高級工程師</td>" +
                "<td>NT$ 80,000</td>" +
                "</tr>" +
                "<tr>" +
                "<td>E002</td>" +
                "<td>李小華</td>" +
                "<td>行銷部</td>" +
                "<td>行銷經理</td>" +
                "<td>NT$ 75,000</td>" +
                "</tr>" +
                "<tr>" +
                "<td>E003</td>" +
                "<td>王小美</td>" +
                "<td>人資部</td>" +
                "<td>人資專員</td>" +
                "<td>NT$ 60,000</td>" +
                "</tr>" +
                "</table>" +
                "<div class=\"info-box\">" +
                "<strong>備註：</strong> 此報表為範例資料，僅供測試使用。" +
                "</div>" +
                "</body>" +
                "</html>";
        
        byte[] pdfBytes = PdfUtils.htmlToPdf(html);
        Path outputPath = Paths.get("/tmp/example4_complex.pdf");
        Files.write(outputPath, pdfBytes);
        
        log.info("Generated PDF: {} (size: {} bytes)", outputPath, pdfBytes.length);
    }
}
