package event.ticket.booking.identity;

import event.ticket.booking.identity.internal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserContract.RegisterReq dto) {
        userService.register(dto);
        return ResponseEntity.ok(Map.of("message", "Đăng ký thành công"));
    }

    @PostMapping("/login")
    public ResponseEntity<UserContract.LoginRes> login(@RequestBody UserContract.LoginReq dto) {
        return ResponseEntity.ok(userService.login(dto));
    }
}
