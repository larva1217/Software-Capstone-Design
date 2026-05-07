package stock.cpastonedesign.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    //@GetMapping("/")
    public String index(){
        return "index";
    }

    //@GetMapping("/community")
    public String community(){
        return "community";
    }

    //@GetMapping("/news")
    public String news(){
        return "news";
    }

    //@GetMapping("/chart")
    public String shart(){
        return "chart";
    }

}
