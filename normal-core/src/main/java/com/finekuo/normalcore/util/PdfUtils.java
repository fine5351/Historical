package com.finekuo.normalcore.util;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class PdfUtils {

    /**
     * Convert HTML to PDF with support for Chinese characters and images
     * 
     * @param html HTML content to convert
     * @param outputStream Output stream to write PDF content
     * @throws IOException if I/O error occurs
     * @throws DocumentException if PDF generation error occurs
     */
    public static void htmlToPdf(String html, OutputStream outputStream) throws IOException, DocumentException {
        if (html == null || html.isEmpty()) {
            throw new IllegalArgumentException("HTML content cannot be null or empty");
        }
        
        if (outputStream == null) {
            throw new IllegalArgumentException("Output stream cannot be null");
        }

        try {
            // Wrap HTML with proper XML declaration and ensure it's well-formed XHTML
            String xhtml = ensureXhtmlFormat(html);
            
            // Create renderer
            ITextRenderer renderer = new ITextRenderer();
            
            // Configure font resolver for Chinese characters support
            ITextFontResolver fontResolver = renderer.getFontResolver();
            
            // Try to add common Chinese fonts that might be available on the system
            // Using embedded fonts would be more reliable but requires font files
            try {
                // Try to use system fonts for Chinese
                fontResolver.addFont("STSong-Light", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                fontResolver.addFont("STSongStd-Light", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                fontResolver.addFont("MSung-Light", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                fontResolver.addFont("MHei-Medium", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            } catch (Exception e) {
                log.warn("Failed to load some Chinese fonts, falling back to default fonts", e);
                // Continue with default fonts if Chinese fonts are not available
            }
            
            // Set document from HTML string
            try (InputStream inputStream = new ByteArrayInputStream(xhtml.getBytes(StandardCharsets.UTF_8))) {
                Document doc = parseXhtml(inputStream);
                renderer.setDocument(doc, null);
            }
            
            // Render to PDF
            renderer.layout();
            renderer.createPDF(outputStream);
            
            log.info("Successfully converted HTML to PDF");
        } catch (Exception e) {
            log.error("Error converting HTML to PDF", e);
            throw e;
        }
    }

    /**
     * Convert HTML to PDF and return as byte array
     * 
     * @param html HTML content to convert
     * @return PDF content as byte array
     * @throws IOException if I/O error occurs
     * @throws DocumentException if PDF generation error occurs
     */
    public static byte[] htmlToPdf(String html) throws IOException, DocumentException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            htmlToPdf(html, outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * Ensure HTML is in proper XHTML format
     */
    private static String ensureXhtmlFormat(String html) {
        // Check if HTML already has XML declaration
        if (!html.trim().startsWith("<?xml")) {
            // Check if HTML has html tag
            if (!html.toLowerCase().contains("<html")) {
                // Wrap content in basic HTML structure
                html = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"/></head><body>" + html + "</body></html>";
            }
            
            // Add XML declaration
            html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + html;
        }
        
        return html;
    }

    /**
     * Parse XHTML content into DOM Document
     */
    private static Document parseXhtml(InputStream inputStream) throws IOException {
        org.xhtmlrenderer.resource.XMLResource xmlResource = 
            org.xhtmlrenderer.resource.XMLResource.load(inputStream);
        return xmlResource.getDocument();
    }
}
