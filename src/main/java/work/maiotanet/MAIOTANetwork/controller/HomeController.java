package work.maiotanet.MAIOTANetwork.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping("/")
  public String index() {
    return "index"; // returns the view name "home"
  }

  @GetMapping("/tools")
  public String tools() {
    return "tools/index"; // returns the view name "tools"
  }

  @GetMapping("/about")
  public String about() {
    return "about"; // returns the view name "about"
  }

}
