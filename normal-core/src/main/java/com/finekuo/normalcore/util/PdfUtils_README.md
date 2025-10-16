# PdfUtils - HTML to PDF Converter

A utility class for converting HTML to PDF with support for Chinese characters and embedded images.

## Features

- ✅ Convert HTML strings to PDF
- ✅ Support for Chinese characters (繁體中文 & 简体中文)
- ✅ Support for embedded images (base64 data URLs)
- ✅ CSS styling support
- ✅ Table and layout support
- ✅ Two output methods: byte array or output stream

## Dependencies

This utility uses Flying Saucer (OpenPDF) for HTML to PDF conversion:

```xml
<dependency>
    <groupId>org.xhtmlrenderer</groupId>
    <artifactId>flying-saucer-pdf</artifactId>
    <version>9.9.3</version>
</dependency>
```

## Usage

### Basic Usage

```java
import com.finekuo.normalcore.util.PdfUtils;
import java.io.FileOutputStream;

// Method 1: Convert to byte array
String html = "<html><body><h1>Hello World</h1></body></html>";
byte[] pdfBytes = PdfUtils.htmlToPdf(html);

// Method 2: Write to output stream
try (FileOutputStream fos = new FileOutputStream("output.pdf")) {
    PdfUtils.htmlToPdf(html, fos);
}
```

### With Chinese Characters

```java
String html = "<!DOCTYPE html>" +
        "<html>" +
        "<head><meta charset=\"UTF-8\"/></head>" +
        "<body>" +
        "<h1>測試中文標題</h1>" +
        "<p>這是一個包含中文字的測試文件。</p>" +
        "</body>" +
        "</html>";

byte[] pdfBytes = PdfUtils.htmlToPdf(html);
```

### With Embedded Images

```java
// Using base64 encoded images
String html = "<!DOCTYPE html>" +
        "<html>" +
        "<body>" +
        "<h1>Image Example</h1>" +
        "<img src=\"data:image/png;base64,iVBORw0KG...\" alt=\"Logo\"/>" +
        "</body>" +
        "</html>";

byte[] pdfBytes = PdfUtils.htmlToPdf(html);
```

### With CSS Styling

```java
String html = "<!DOCTYPE html>" +
        "<html>" +
        "<head>" +
        "<style>" +
        "body { font-family: serif; margin: 30px; }" +
        "h1 { color: #333; border-bottom: 2px solid #3498db; }" +
        "table { border-collapse: collapse; width: 100%; }" +
        "td, th { border: 1px solid #ddd; padding: 8px; }" +
        "</style>" +
        "</head>" +
        "<body>" +
        "<h1>Styled Document</h1>" +
        "<table>" +
        "<tr><th>Name</th><th>Age</th></tr>" +
        "<tr><td>John</td><td>30</td></tr>" +
        "</table>" +
        "</body>" +
        "</html>";

byte[] pdfBytes = PdfUtils.htmlToPdf(html);
```

## Method Signatures

```java
// Convert HTML to PDF and return as byte array
public static byte[] htmlToPdf(String html) throws IOException, DocumentException

// Convert HTML to PDF and write to output stream
public static void htmlToPdf(String html, OutputStream outputStream) throws IOException, DocumentException
```

## Parameters

- `html` - HTML content to convert. Must be valid XHTML. If not well-formed, the method will attempt to wrap it in a basic HTML structure.
- `outputStream` - (Optional) Output stream to write PDF content to

## Exceptions

- `IllegalArgumentException` - If HTML is null or empty, or if output stream is null
- `IOException` - If an I/O error occurs during conversion
- `DocumentException` - If a PDF generation error occurs

## Notes

### HTML Requirements

- HTML should be valid XHTML (well-formed XML)
- If your HTML doesn't include `<html>` tags, the utility will wrap it automatically
- Include `<meta charset="UTF-8"/>` in the head section for proper character encoding

### Chinese Font Support

The utility attempts to use these Chinese fonts in order:
1. STSong-Light
2. STSongStd-Light
3. MSung-Light
4. MHei-Medium

If these fonts are not available on the system, it falls back to default fonts. While the Chinese characters may not display perfectly with default fonts, the PDF will still be generated.

For better Chinese character support in production:
- Ensure Chinese fonts are installed on the server
- Or use embedded font files (requires font file resources)

### Image Support

- Base64 encoded images (data URLs) are supported
- External image URLs may have limitations depending on network access
- Recommended format: `data:image/png;base64,{base64_data}`

## Examples

See `PdfUtilsExample.java` in the test directory for complete working examples including:
- Simple HTML conversion
- Chinese character support
- Embedded images
- Complex HTML with tables and styling

## Testing

Run the test suite:
```bash
mvn test -Dtest=PdfUtilsTest
```

Run the examples:
```bash
mvn test-compile
java -cp "target/classes:target/test-classes:$(mvn dependency:build-classpath -q)" \
  com.finekuo.normalcore.util.PdfUtilsExample
```

## License

Part of the Historical project by finekuo.
