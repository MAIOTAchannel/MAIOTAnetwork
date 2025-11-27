package work.maiotanet.MAIOTANetwork.controller.tools;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import work.maiotanet.MAIOTANetwork.service.PureJavaImageConverter;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Controller
public class PureConverterController {

  private final PureJavaImageConverter converterService;

  private final List<String> outputFormats = Arrays.asList(
          "jpg", "png", "gif", "bmp", "tif", "tga", "ico",
          "svg", "pcx", "pict", "sgi", "wbmp"
  );

  public PureConverterController(PureJavaImageConverter converterService) {
    this.converterService = converterService;
  }

  @GetMapping("/tools/converter")
  public String showPage(Model model) {
    model.addAttribute("formats", outputFormats);
    return "tools/converter";
  }

  @PostMapping("/tools/converter")
  public ResponseEntity<Resource> convert(
          @RequestParam(value = "file", required = false) MultipartFile file,
          @RequestParam(value = "url", required = false) String url,
          @RequestParam("format") String format,
          Model model) {

    try {
      File resultFile;
      String downloadName;

      // 分岐: ファイルアップロードか、URLか
      if (file != null && !file.isEmpty()) {
        resultFile = converterService.convert(file, format);
        String original = file.getOriginalFilename();
        downloadName = (original != null && original.contains("."))
                ? original.substring(0, original.lastIndexOf('.'))
                : "converted";
      } else if (url != null && !url.isEmpty()) {
        resultFile = converterService.convertFromUrl(url, format);
        downloadName = "url_image";
      } else {
        throw new IllegalArgumentException("File or URL is required.");
      }

      downloadName += "." + format;

      // 日本語ファイル名等の文字化け対策
      String encodedName = URLEncoder.encode(downloadName, StandardCharsets.UTF_8).replace("+", "%20");

      return ResponseEntity.ok()
              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedName + "\"; filename*=UTF-8''" + encodedName)
              .contentType(MediaType.APPLICATION_OCTET_STREAM)
              .body(new FileSystemResource(resultFile));

    } catch (Exception e) {
      e.printStackTrace();
      // エラー時はHTMLに戻すのが理想ですが、ResponseEntityを返す仕様上、ここでは500エラーにします
      // 実装を凝るなら @ExceptionHandler で処理します
      return ResponseEntity.internalServerError().build();
    }
  }
}