package org.wenant.web.servlet;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.wenant.annotations.Audit;
import org.wenant.annotations.Loggable;
import org.wenant.domain.dto.RegistrationDto;
import org.wenant.domain.repository.JdbcUserRepository;
import org.wenant.domain.repository.UserRepository;
import org.wenant.service.in.RegistrationService;

import java.io.IOException;
import java.io.PrintWriter;

@Loggable
@Audit
@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RegistrationService registrationService;

    public RegistrationServlet() {
        UserRepository userRepository = new JdbcUserRepository();
        this.registrationService = new RegistrationService(userRepository);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            RegistrationDto registrationDto = objectMapper.readValue(req.getReader(), RegistrationDto.class);
            RegistrationService.RegistrationResult registrationResult =
                    registrationService.registerUser(registrationDto);
            String jsonResponse = objectMapper.writeValueAsString(registrationResult);

            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();

            switch (registrationResult) {
                case SUCCESS:
                    resp.setStatus(HttpServletResponse.SC_OK);
                    writer.write(jsonResponse);
                    break;
                case INVALID_USERNAME, INVALID_PASSWORD:
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    writer.write(jsonResponse);
                    break;
                case USERNAME_ALREADY_EXISTS:
                    resp.setStatus(HttpServletResponse.SC_CONFLICT);
                    writer.write(jsonResponse);
                    break;
            }

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(e.getMessage() + "{\"error\": \"Invalid JSON format\"}");

        }

    }
}

