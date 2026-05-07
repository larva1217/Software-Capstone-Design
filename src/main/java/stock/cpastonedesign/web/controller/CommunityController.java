package stock.cpastonedesign.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import stock.cpastonedesign.web.dto.Post;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/community")
public class CommunityController {

    private List<Post> postList = new ArrayList<>();

    @GetMapping
    public String community(Model model) {
        model.addAttribute("posts", postList);
        return "community";
    }

    @PostMapping("/write")
    public String write(Post post) {
        postList.add(post);
        return "redirect:/community";
    }

}
