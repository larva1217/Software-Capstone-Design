package stock.cpastonedesign.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import stock.cpastonedesign.domain.User;
import stock.cpastonedesign.repository.PostRepository;
import stock.cpastonedesign.repository.UserRepository;
import stock.cpastonedesign.web.dto.Post;

import java.io.PrintWriter;

@Controller
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @GetMapping
    @Transactional(readOnly = true)
    public String community(Model model, HttpSession session) {
        // ID 내림차순으로 게시글 목록 조회
        model.addAttribute("posts", postRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
        //로그인한 사용자 ID값
        model.addAttribute("loginUserId", session.getAttribute("loginUser"));
        return "community";
    }


    @PostMapping("/write")
    public void write(Post post, HttpSession session, HttpServletResponse response) throws Exception {
        //세션에서 로그인한 유저의 ID를 꺼내옴
        Long loginUserId = (Long) session.getAttribute("loginUser");

        //비회원인 경우 글쓰기를 차단, 팝업창 띄우기
        if (loginUserId == null) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('로그인이 필요한 기능입니다.'); location.href='/community';</script>");
            out.flush();
            out.close();
            return;
        }

        //DB에서 현재 로그인한 사용자 정보를 조회
        User user = userRepository.findById(loginUserId).orElse(null);

        if (user != null) {
            post.setAuthor(user);
            postRepository.save(post);
        }

        response.sendRedirect("/community");
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        //세션에서 로그인한 유저의 ID를 꺼내옴
        Long loginUserId = (Long) session.getAttribute("loginUser");

        if (loginUserId != null) {
            Post post = postRepository.findById(id).orElse(null);

            //게시글이 존재하고, 작성자 ID가 현재 로그인한 유저 ID와 같은지 검사
            if (post != null && post.getAuthor() != null && post.getAuthor().getId().equals(loginUserId)) {
                postRepository.deleteById(id);
            }
        }

        return "redirect:/community";
    }

}