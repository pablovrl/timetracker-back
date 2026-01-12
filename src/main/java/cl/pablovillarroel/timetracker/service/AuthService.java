package cl.pablovillarroel.timetracker.service;

import cl.pablovillarroel.timetracker.config.JwtUtil;
import cl.pablovillarroel.timetracker.dto.LoginRequest;
import cl.pablovillarroel.timetracker.dto.LoginResponse;
import cl.pablovillarroel.timetracker.model.User;
import cl.pablovillarroel.timetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!user.getEnabled()) {
            throw new IllegalArgumentException("User account is disabled");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return mapToLoginResponse(user, token);
    }

    private LoginResponse mapToLoginResponse(User user, String token) {
        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
