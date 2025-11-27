package work.maiotanet.MAIOTANetwork.controller.tools;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import work.maiotanet.MAIOTANetwork.service.QrCodeService;

@Controller
public class QrCodeController {

  private final QrCodeService qrCodeService;

  public QrCodeController(QrCodeService qrCodeService) {
    this.qrCodeService = qrCodeService;
  }

  // 画面表示
  @GetMapping("/tools/qrcode")
  public String showPage() {
    return "tools/qrcode";
  }

  // 生成処理
  @PostMapping("/tools/qrcode")
  public String generate(@RequestParam("inputText") String inputText, Model model) {
    // 250x250ピクセルのQRコードを生成
    String qrImage = qrCodeService.generateQrCodeBase64(inputText, 250, 250);

    model.addAttribute("inputText", inputText);
    model.addAttribute("qrImage", qrImage);

    return "tools/qrcode";
  }
}