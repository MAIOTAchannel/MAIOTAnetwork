package work.maiotanet.MAIOTANetwork.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class QrCodeService {

  public String generateQrCodeBase64(String text, int width, int height) {
    if (text == null || text.trim().isEmpty()) {
      return null;
    }

    try {
      QRCodeWriter qrCodeWriter = new QRCodeWriter();
      // QRコードのビット行列を作成
      BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

      // 画像(PNG)としてバイト配列に書き出し
      ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
      MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
      byte[] pngData = pngOutputStream.toByteArray();

      // HTMLで表示しやすいようにBase64文字列に変換して返す
      return Base64.getEncoder().encodeToString(pngData);

    } catch (Exception e) {
      e.printStackTrace();
      return null; // エラーハンドリングは適宜調整してください
    }
  }
}