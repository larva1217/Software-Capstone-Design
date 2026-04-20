package stock.cpastonedesign.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stock.cpastonedesign.domain.User;
import stock.cpastonedesign.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void signup(String username, String password) {
        // [추가된 부분] 1. 이미 존재하는 아이디인지 검사 (중복 방지)
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("이미 사용 중인 아이디입니다.");
        }

        // 2. 새로운 User 생성
        User newUser = new User();

        // 3. 사용자 아이디와 비밀번호
        newUser.setUsername(username);
        newUser.setPassword(password);

        // 4. 모의투자 초기 지원금 1,000만원
        newUser.setBalance(10000000.0);

        // 5. DB에 영구 저장
        userRepository.save(newUser);
    }

    public User login(String username, String password) {
        // 1. DB에서 아이디로 유저 찾기
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("가입되지 않은 아이디입니다."));

        // 2. 비밀번호가 맞는지 확인
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

        // 3. 통과시 유저 정보 반환
        return user;
    }
}