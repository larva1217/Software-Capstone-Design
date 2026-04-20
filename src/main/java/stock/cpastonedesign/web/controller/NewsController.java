package stock.cpastonedesign.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import stock.cpastonedesign.service.NewsService;

import java.util.List;
import java.util.Map;

@Controller
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/news")
    public String getNewsPage(Model model){
        List<Map<String,String>> headlines = newsService.getHeadlines();
        model.addAttribute("headlines", headlines);
        return "news";
    }
}
