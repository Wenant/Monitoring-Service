package org.wenant.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.wenant.annotations.Audit;
import org.wenant.annotations.Loggable;
import org.wenant.domain.dto.AuthenticationDto;
import org.wenant.domain.repository.JdbcUserRepository;
import org.wenant.domain.repository.UserRepository;
import org.wenant.service.in.AuthService;
import org.wenant.service.in.JwtService;

import java.io.IOException;

@Loggable
@Audit
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtService jwtService;
    private final AuthService authService;

    public LoginServlet() {
        UserRepository userRepository = new JdbcUserRepository();
        this.jwtService = new JwtService();
        this.authService = new AuthService(userRepository);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        AuthenticationDto authenticationDto = objectMapper.readValue(req.getReader(), AuthenticationDto.class);
        authenticationDto = authService.authenticateUser(authenticationDto.getUsername(), authenticationDto.getPassword());

        if (authenticationDto != null) {
            String token = jwtService.generateToken(authenticationDto);
            String jsonToken = objectMapper.writeValueAsString(token);
            resp.setContentType("application/json");
            resp.getWriter().write(jsonToken);
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Authentication failed\"}");
        }
    }
}
