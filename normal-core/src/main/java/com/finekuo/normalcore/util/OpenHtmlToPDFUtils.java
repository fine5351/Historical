package com.finekuo.normalcore.util;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class OpenHtmlToPDFUtils {

    /**
     * Convert HTML to PDF with support for Chinese characters and images using OpenHTMLToPDF library
     * 
     * @param html HTML content to convert
     * @param outputStream Output stream to write PDF content
     * @throws IOException if I/O error occurs
     */
    public static void htmlToPdf(String html, OutputStream outputStream) throws IOException {
        if (html == null || html.isEmpty()) {
            throw new IllegalArgumentException("HTML content cannot be null or empty");
        }
        
        if (outputStream == null) {
            throw new IllegalArgumentException("Output stream cannot be null");
        }

        try {
            // Ensure HTML is in proper XHTML format
            String xhtml = ensureXhtmlFormat(html);
            
            // Create PDF renderer builder
            PdfRendererBuilder builder = new PdfRendererBuilder();
            
            // Set HTML content
            builder.withHtmlContent(xhtml, null);
            
            // Set output stream
            builder.toStream(outputStream);
            
            // Use fast mode for better performance
            builder.useFastMode();
            
            // Enable Unicode support for Chinese characters
            try {
                builder.useUnicodeBidiSplitter(new com.openhtmltopdf.bidi.support.ICUBidiSplitter.ICUBidiSplitterFactory());
                builder.useUnicodeBidiReorderer(new com.openhtmltopdf.bidi.support.ICUBidiReorderer());
            } catch (Exception e) {
                log.warn("Failed to enable Unicode BiDi support, continuing without it", e);
            }
            
            // Build and create PDF
            builder.run();
            
            log.info("Successfully converted HTML to PDF using OpenHTMLToPDF");
        } catch (Exception e) {
            log.error("Error converting HTML to PDF", e);
            throw new IOException("Failed to convert HTML to PDF", e);
        }
    }

    /**
     * Convert HTML to PDF and return as byte array
     * 
     * @param html HTML content to convert
     * @return PDF content as byte array
     * @throws IOException if I/O error occurs
     */
    public static byte[] htmlToPdf(String html) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            htmlToPdf(html, outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * Ensure HTML is in proper XHTML format
     */
    private static String ensureXhtmlFormat(String html) {
        // Check if HTML already has DOCTYPE
        if (!html.trim().toLowerCase().startsWith("<!doctype")) {
            // Check if HTML has html tag
            if (!html.toLowerCase().contains("<html")) {
                // Wrap content in basic HTML structure
                html = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"/></head><body>" + html + "</body></html>";
            } else if (!html.toLowerCase().contains("<!doctype")) {
                // Add DOCTYPE
                html = "<!DOCTYPE html>" + html;
            }
        }
        
        return html;
    }
}
