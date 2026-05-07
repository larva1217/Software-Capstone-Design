package stock.cpastonedesign.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import stock.cpastonedesign.repository.PostRepository;
import stock.cpastonedesign.web.dto.Post;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {

    private final PostRepository postRepository;

    @GetMapping
    public String community(Model model) {
        model.addAttribute("posts", postRepository.findAll());
        return "community";
    }

    //쓰기
    @PostMapping("/write")
    public String write(Post post) {
        postRepository.save(post);
        return "redirect:/community";
    }

    //삭제
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        postRepository.deleteById(id);
        return "redirect:/community";
    }

}
