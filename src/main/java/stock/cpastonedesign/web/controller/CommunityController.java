package stock.cpastonedesign.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
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
    public String community(Model model, HttpSession session) {
        // ID 기준 내림차순으로 게시글 목록 조회
        model.addAttribute("posts", postRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
        model.addAttribute("loginUserId", session.getAttribute("loginUser"));
        return "community";
    }

    /**
     * [글쓰기]
     * 비회원이 접근 시 알림창을 띄우고 차단하며,
     * 로그인한 유저의 경우 작성자 정보를 연동하여 저장합니다.
     */
    @PostMapping("/write")
    public void write(Post post, HttpSession session, HttpServletResponse response) throws Exception {
        Long loginUserId = (Long) session.getAttribute("loginUser");

        // 1. 비회원 차단 로직
        if (loginUserId == null) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('로그인이 필요한 기능입니다.'); location.href='/community';</script>");
            out.flush();
            out.close();
            return;
        }

        // 2. 작성자(Author) 정보 매칭 후 저장
        User user = userRepository.findById(loginUserId).orElse(null);
        if (user != null) {
            post.setAuthor(user); // 팀장님이 Post.java에 만든 author 필드에 유저 주입!
            postRepository.save(post);
        }

        response.sendRedirect("/community");
    }

    /**
     * [삭제]
     * 본인이 작성한 글인지 확인한 후 일치할 경우에만 삭제를 진행합니다.
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        Long loginUserId = (Long) session.getAttribute("loginUser");

        if (loginUserId != null) {
            Post post = postRepository.findById(id).orElse(null);

            // 게시글이 존재하고, 작성자 ID가 현재 로그인한 유저 ID와 같은지 검사
            if (post != null && post.getAuthor() != null && post.getAuthor().getId().equals(loginUserId)) {
                postRepository.deleteById(id);
            }
        }
        return "redirect:/community";
    }
}