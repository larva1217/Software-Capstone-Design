package stock.cpastonedesign.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stock.cpastonedesign.domain.User;
import stock.cpastonedesign.repository.UserRepository;

@Service
@RequiredArgsConstructor //Lombok final 변수의 생성자를 자동 생성
public class UserService {

    private final UserRepository userRepository;

    //회원가입
    public void signup(String username, String password) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("이미 사용 중인 아이디입니다.");
        }

        //새로운 User 생성
        User newUser = new User();

        //사용자 아이디와 비밀번호
        newUser.setUsername(username);
        newUser.setPassword(password);

        //모의투자 초기 지원금 1,000만원
        newUser.setBalance(10000000.0);

        //DB에 영구 저장
        userRepository.save(newUser);

    }

    //로그인
    public User login(String username, String password) {
        // 아이디 없음 / 비밀번호 틀림 → 같은 메시지로 통일
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return user;
    }
}