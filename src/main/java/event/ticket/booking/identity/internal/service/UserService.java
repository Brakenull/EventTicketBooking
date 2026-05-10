package event.ticket.booking.identity.internal.service;

import event.ticket.booking.identity.UserContract;
import event.ticket.booking.identity.internal.entity.User;
import event.ticket.booking.identity.internal.repository.UserRepository;
import event.ticket.booking.shared.consts.UserRole;
import event.ticket.booking.shared.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void register(UserContract.RegisterReq dto) {
        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPasswordHash(passwordEncoder.encode(dto.password()));
        user.setRole(dto.role());
        userRepository.save(user);
    }

    public UserContract.LoginRes login(UserContract.LoginReq dto) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(dto.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }
        String jwtToken = jwtService.generateToken(user.getId().toString(), user.getEmail(), user.getRole().toString());
        return new UserContract.LoginRes(jwtToken, user.getEmail(), user.getUsername());
    }
}
