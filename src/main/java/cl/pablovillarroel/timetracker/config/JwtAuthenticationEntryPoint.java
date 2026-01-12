package cl.pablovillarroel.timetracker.config;

import cl.pablovillarroel.timetracker.dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String timestamp = LocalDateTime.now().format(FORMATTER);
        String jsonResponse = String.format(
                "{\"status\":%d,\"error\":\"%s\",\"code\":\"%s\",\"message\":\"%s\",\"timestamp\":\"%s\"}",
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized",
                "UNAUTHORIZED",
                "Authentication is required to access this resource",
                timestamp
        );

        PrintWriter writer = response.getWriter();
        writer.write(jsonResponse);
        writer.flush();
    }
}
