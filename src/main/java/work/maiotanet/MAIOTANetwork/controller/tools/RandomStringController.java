package work.maiotanet.MAIOTANetwork.controller.tools;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import work.maiotanet.MAIOTANetwork.service.RandomStringService;

@Controller
public class RandomStringController {

  private final RandomStringService randomStringService;

  public RandomStringController(RandomStringService randomStringService) {
    this.randomStringService = randomStringService;
  }

  @GetMapping("/tools/random-string")
  public String showPage(Model model) {
    model.addAttribute("length", 16);
    model.addAttribute("useUpper", true);
    model.addAttribute("useLower", true);
    model.addAttribute("useNumber", true);
    model.addAttribute("useSymbol", false);
    model.addAttribute("customSymbols", "");
    return "tools/random-string";
  }

  @PostMapping("/tools/random-string")
  public String generate(
          @RequestParam(value = "length", defaultValue = "16") int length,
          @RequestParam(value = "useUpper", defaultValue = "false") boolean useUpper,
          @RequestParam(value = "useLower", defaultValue = "false") boolean useLower,
          @RequestParam(value = "useNumber", defaultValue = "false") boolean useNumber,
          @RequestParam(value = "useSymbol", defaultValue = "false") boolean useSymbol,
          @RequestParam(value = "customSymbols", defaultValue = "") String customSymbols,
          Model model) {

    // バリデーション修正: 1024文字まで許可
    if (length < 1) length = 1;
    if (length > 1024) length = 1024;

    String result = randomStringService.generate(length, useUpper, useLower, useNumber, useSymbol, customSymbols);

    model.addAttribute("result", result);

    model.addAttribute("length", length);
    model.addAttribute("useUpper", useUpper);
    model.addAttribute("useLower", useLower);
    model.addAttribute("useNumber", useNumber);
    model.addAttribute("useSymbol", useSymbol);
    model.addAttribute("customSymbols", customSymbols);

    return "tools/random-string";
  }
}