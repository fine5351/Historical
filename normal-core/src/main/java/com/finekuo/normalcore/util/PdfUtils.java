package com.finekuo.normalcore.util;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class PdfUtils {

    // URI of embedded font file extracted at runtime (file://...)
    private static String embeddedFontUri = null;
    // List of all successfully loaded embedded font names
    private static final List<String> loadedFontNames = new ArrayList<>();

    /**
     * Convert HTML to PDF with support for Chinese characters and images
     *
     * @param html         HTML content to convert
     * @param outputStream Output stream to write PDF content
     * @throws IOException       if I/O error occurs
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
            configureFonts(renderer.getFontResolver());

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
     * @throws IOException       if I/O error occurs
     * @throws DocumentException if PDF generation error occurs
     */
    public static byte[] htmlToPdf(String html) throws IOException, DocumentException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            htmlToPdf(html, outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * Configure fonts with fallback mechanism for Chinese character support
     */
    private static void configureFonts(ITextFontResolver fontResolver) {
        // Try to load all bundled TTF files from resources (src/main/resources/ttf)
        boolean anyEmbeddedFontLoaded = loadEmbeddedFontsFromResources(fontResolver);

        if (anyEmbeddedFontLoaded) {
            log.info("Successfully loaded {} embedded font(s) from resources", loadedFontNames.size());
            return; // embedded fonts loaded, skip system font fallback
        }

        // Fallback to system fonts if no embedded fonts were loaded
        log.debug("No embedded fonts loaded, trying system fonts...");

        // List of potential Chinese fonts to try (system font names)
        String[] chineseFonts = {
                "STSong-Light",
                "STSongStd-Light",
                "MSung-Light",
                "MHei-Medium",
                "SimSun",
                "NSimSun",
                "Microsoft JhengHei",
                "Microsoft YaHei",
                "PingFang SC",
                "Hiragino Sans GB",
                "Source Han Sans CN",
                "Noto Sans CJK SC"
        };

        boolean chineseFontLoaded = false;

        // Try to load Chinese fonts by name
        for (String fontName : chineseFonts) {
            try {
                fontResolver.addFont(fontName, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                log.info("Successfully loaded Chinese font: {}", fontName);
                chineseFontLoaded = true;
                break; // Use the first successfully loaded font
            } catch (Exception e) {
                log.debug("Failed to load font: {} - {}", fontName, e.getMessage());
            }
        }

        if (!chineseFontLoaded) {
            log.warn("No Chinese fonts could be loaded. Chinese characters may not display correctly.");
            log.info("Consider installing Chinese fonts or using embedded font files for better compatibility.");
        }

        // Try to load common system fonts as fallback
        String[] fallbackFonts = {
                "Arial",
                "Times New Roman",
                "Helvetica",
                "Courier New"
        };

        for (String fontName : fallbackFonts) {
            try {
                fontResolver.addFont(fontName, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                log.debug("Loaded fallback font: {}", fontName);
            } catch (Exception e) {
                log.debug("Failed to load fallback font: {} - {}", fontName, e.getMessage());
            }
        }
    }

    /**
     * Load all TTF files from resources/ttf directory and register them
     *
     * @param fontResolver the font resolver to register fonts with
     * @return true if at least one font was successfully loaded
     */
    private static boolean loadEmbeddedFontsFromResources(ITextFontResolver fontResolver) {
        int successCount = 0;

        try {
            // Get the /ttf directory URL from classpath
            URL ttfDirUrl = PdfUtils.class.getResource("/ttf");
            if (ttfDirUrl == null) {
                log.debug("Embedded font directory /ttf not found in classpath");
                return false;
            }

            List<String> fontFiles = listTtfFiles(ttfDirUrl);

            if (fontFiles.isEmpty()) {
                log.debug("No .ttf files found in /ttf directory");
                return false;
            }

            log.debug("Found {} TTF file(s) in /ttf directory", fontFiles.size());

            // Load and register each TTF file
            for (String fontFileName : fontFiles) {
                try (InputStream is = PdfUtils.class.getResourceAsStream("/ttf/" + fontFileName)) {
                    if (is != null) {
                        Path tempFile = Files.createTempFile("embedded-font-" + fontFileName.replace(".ttf", "") + "-", ".ttf");
                        Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
                        tempFile.toFile().deleteOnExit();

                        try {
                            // Register font with ITextFontResolver
                            // Use "NotoSerifCJK" as the common family name for all CJK fonts
                            String fontPath = tempFile.toAbsolutePath().toString();
                            fontResolver.addFont(fontPath, "NotoSerifCJK", BaseFont.IDENTITY_H, true, null);

                            String fontFamilyName = fontFileName.replace("-VF.ttf", "").replace(".ttf", "");
                            loadedFontNames.add(fontFamilyName);

                            // Store the first font path for reference
                            if (embeddedFontUri == null) {
                                embeddedFontUri = tempFile.toUri().toString();
                            }

                            log.info("Registered embedded font: {} as 'NotoSerifCJK' from {}", fontFamilyName, tempFile);
                            successCount++;
                        } catch (Exception e) {
                            log.warn("Failed to register embedded font {}: {}", fontFileName, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    log.warn("Failed to load font file {}: {}", fontFileName, e.getMessage());
                }
            }
        } catch (Exception e) {
            log.debug("Error loading embedded fonts from resources: {}", e.getMessage());
        }

        return successCount > 0;
    }

    /**
     * List all .ttf files in the given directory URL
     *
     * @param dirUrl the directory URL (may be file:// or jar:file://)
     * @return list of .ttf filenames
     */
    private static List<String> listTtfFiles(URL dirUrl) throws IOException, URISyntaxException {
        List<String> ttfFiles = new ArrayList<>();

        URI uri = dirUrl.toURI();

        // Handle both file system and JAR resources
        if (uri.getScheme().equals("jar")) {
            // Inside JAR
            try (FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                Path dirPath = fs.getPath("/ttf");
                try (Stream<Path> walk = Files.walk(dirPath, 1)) {
                    walk.filter(Files::isRegularFile)
                            .filter(p -> p.toString().toLowerCase().endsWith(".ttf"))
                            .forEach(p -> ttfFiles.add(p.getFileName().toString()));
                }
            }
        } else {
            // Regular file system
            Path dirPath = Paths.get(uri);
            try (Stream<Path> walk = Files.walk(dirPath, 1)) {
                walk.filter(Files::isRegularFile)
                        .filter(p -> p.toString().toLowerCase().endsWith(".ttf"))
                        .forEach(p -> ttfFiles.add(p.getFileName().toString()));
            }
        }

        return ttfFiles;
    }

    /**
     * Ensure HTML is in proper XHTML format
     */
    private static String ensureXhtmlFormat(String html) {
        // Check if HTML already has XML declaration
        if (!html.trim().startsWith("<?xml")) {
            // Check if HTML has html tag
            if (!html.toLowerCase().contains("<html")) {
                // Wrap content in basic HTML structure with fallback font family
                String fontFaceCss = "";
                if (!loadedFontNames.isEmpty()) {
                    // Use the registered font family name directly (no @font-face needed for Flying Saucer)
                    fontFaceCss = "<style>body { font-family: 'NotoSerifCJK', 'STSong-Light', 'Microsoft JhengHei', Arial, sans-serif; }</style>";
                } else {
                    fontFaceCss = "<style>body { font-family: Arial, sans-serif; }</style>";
                }

                html = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"/>" + fontFaceCss + "</head><body>" + html + "</body></html>";
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