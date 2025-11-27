package work.maiotanet.MAIOTANetwork.controller.tools;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
public class IpCheckerController {

  @GetMapping("/tools/ip-checker")
  public String showIpChecker(HttpServletRequest request, Model model) {

    // 1. IPアドレス取得
    String clientIp = getClientIpAddress(request);

    // 2. ホスト名取得
    String clientHost = resolveHostname(clientIp);

    // 3. ブラウザ情報取得
    String userAgent = request.getHeader("User-Agent");

    // Thymeleafにデータを渡す
    model.addAttribute("clientIp", clientIp);
    model.addAttribute("clientHost", clientHost);
    model.addAttribute("userAgent", userAgent);

    return "tools/ip-checker"; // templates/tools/ip-checker.html を表示
  }

  // --- ユーティリティメソッド (以前と同じ) ---

  private String getClientIpAddress(HttpServletRequest request) {
    String[] headersToCheck = {
            "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR", "HTTP_FORWARDED",
            "HTTP_VIA", "REMOTE_ADDR"
    };
    for (String header : headersToCheck) {
      String ip = request.getHeader(header);
      if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
        if (ip.contains(",")) {
          return ip.split(",")[0].trim();
        }
        return ip;
      }
    }
    return request.getRemoteAddr();
  }

  private String resolveHostname(String ip) {
    try {
      return InetAddress.getByName(ip).getHostName();
    } catch (UnknownHostException e) {
      return "Unknown";
    }
  }
}