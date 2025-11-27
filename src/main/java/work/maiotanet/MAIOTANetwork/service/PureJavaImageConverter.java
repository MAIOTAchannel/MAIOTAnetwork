package work.maiotanet.MAIOTANetwork.service;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.UUID;

@Service
public class PureJavaImageConverter {

  private static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

  /**
   * MultipartFile (アップロード) からの変換
   */
  public File convert(MultipartFile sourceFile, String targetFormat) throws IOException {
    String originalFilename = sourceFile.getOriginalFilename();
    if (originalFilename == null) originalFilename = "unknown.tmp";
    String ext = getExtension(originalFilename);

    File inputFile = new File(TEMP_DIR, UUID.randomUUID() + "." + ext);
    sourceFile.transferTo(inputFile);

    return convertFile(inputFile, targetFormat);
  }

  /**
   * URLからの変換 (New!)
   */
  public File convertFromUrl(String urlString, String targetFormat) throws IOException {
    URL url = new URL(urlString);

    // 拡張子をURLから推測 (パラメータ除去)
    String path = url.getPath();
    String ext = getExtension(path);
    if (ext.isEmpty()) ext = "tmp"; // 拡張子がない場合はtmp

    File inputFile = new File(TEMP_DIR, UUID.randomUUID() + "." + ext);

    // ダウンロード処理 (User-Agentを設定して拒否を防ぐ)
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestProperty("User-Agent", "Mozilla/5.0");

    try (InputStream in = connection.getInputStream()) {
      Files.copy(in, inputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    return convertFile(inputFile, targetFormat);
  }

  /**
   * 共通変換ロジック (ファイルを受け取って変換)
   */
  private File convertFile(File inputFile, String targetFormat) throws IOException {
    BufferedImage image;
    String ext = getExtension(inputFile.getName()).toLowerCase(Locale.ROOT);

    try {
      // 読み込み分岐
      if (ext.equals("pdf") || ext.equals("ai")) {
        image = readPdfAsImage(inputFile);
      } else {
        image = ImageIO.read(inputFile);
      }

      if (image == null) {
        throw new IOException("サポートされていないフォーマットか、ファイルが破損しています: " + ext);
      }

      // 書き出し
      File outputFile = new File(TEMP_DIR, UUID.randomUUID() + "_converted." + targetFormat);
      writeImage(image, targetFormat, outputFile);
      return outputFile;

    } finally {
      if (inputFile.exists()) inputFile.delete();
    }
  }

  // --- 以下、前回と同じプライベートメソッド (省略せず記述します) ---

  private BufferedImage readPdfAsImage(File pdfFile) throws IOException {
    try (PDDocument document = Loader.loadPDF(pdfFile)) {
      PDFRenderer pdfRenderer = new PDFRenderer(document);
      return pdfRenderer.renderImageWithDPI(0, 150, ImageType.RGB);
    } catch (IOException e) {
      throw new IOException("PDF/AIの読み込みに失敗しました。", e);
    }
  }

  private void writeImage(BufferedImage image, String format, File output) throws IOException {
    if ("svg".equalsIgnoreCase(format)) {
      writeSvg(image, output);
      return;
    }
    // 背景透過処理
    if (format.equalsIgnoreCase("jpg") || format.equalsIgnoreCase("jpeg") || format.equalsIgnoreCase("bmp")) {
      BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
      Graphics2D g = newImage.createGraphics();
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, image.getWidth(), image.getHeight());
      g.drawImage(image, 0, 0, null);
      g.dispose();
      image = newImage;
    }
    if (!ImageIO.write(image, format, output)) {
      throw new IOException("書き込みに対応していないフォーマットです: " + format);
    }
  }

  private void writeSvg(BufferedImage image, File output) throws IOException {
    DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
    Document document = domImpl.createDocument("http://www.w3.org/2000/svg", "svg", null);
    SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
    svgGenerator.drawImage(image, 0, 0, null);
    try (Writer out = new OutputStreamWriter(new FileOutputStream(output), StandardCharsets.UTF_8)) {
      svgGenerator.stream(out, true);
    }
  }

  private String getExtension(String filename) {
    if (filename == null) return "";
    int dotIndex = filename.lastIndexOf('.');
    if (dotIndex == -1) return "";
    return filename.substring(dotIndex + 1);
  }
}