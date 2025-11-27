package work.maiotanet.MAIOTANetwork.service;

import org.springframework.stereotype.Service;
import java.security.SecureRandom;

@Service
public class RandomStringService {

  private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
  private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
  private static final String CHAR_NUMBER = "0123456789";
  // デフォルトの記号セット
  private static final String DEFAULT_SYMBOLS = "!@#$%^&*()_+-=[]{}|;:,.<>?";

  private final SecureRandom random = new SecureRandom();

  /**
   * 文字列生成メソッド
   * @param customSymbols ユーザーが指定した記号（空の場合はデフォルトを使用）
   */
  public String generate(int length, boolean useUpper, boolean useLower, boolean useNumber, boolean useSymbol, String customSymbols) {
    StringBuilder charPool = new StringBuilder();
    if (useUpper) charPool.append(CHAR_UPPER);
    if (useLower) charPool.append(CHAR_LOWER);
    if (useNumber) charPool.append(CHAR_NUMBER);

    if (useSymbol) {
      // カスタム記号が指定されていればそれを使い、空ならデフォルトを使う
      if (customSymbols != null && !customSymbols.isEmpty()) {
        charPool.append(customSymbols);
      } else {
        charPool.append(DEFAULT_SYMBOLS);
      }
    }

    // 何も選択されていない場合の安全策（小文字と数字）
    if (charPool.isEmpty()) {
      charPool.append(CHAR_LOWER).append(CHAR_NUMBER);
    }

    StringBuilder result = new StringBuilder(length);
    String pool = charPool.toString();

    for (int i = 0; i < length; i++) {
      int index = random.nextInt(pool.length());
      result.append(pool.charAt(index));
    }

    return result.toString();
  }
}