package org.wenant.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.wenant.annotations.Audit;
import org.wenant.annotations.Loggable;
import org.wenant.domain.dto.MeterReadingDto;
import org.wenant.domain.dto.MeterTypeCatalogDto;
import org.wenant.domain.dto.UserDto;
import org.wenant.domain.repository.JdbcMeterReadingRepository;
import org.wenant.domain.repository.JdbcMeterTypeCatalogRepository;
import org.wenant.domain.repository.JdbcUserRepository;
import org.wenant.domain.repository.MeterReadingRepository;
import org.wenant.service.UserService;
import org.wenant.service.in.JwtService;
import org.wenant.service.in.MeterReadingService;
import org.wenant.service.in.MeterTypeCatalogService;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;

@Loggable
@Audit
@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MeterReadingService meterReadingService;
    private final MeterTypeCatalogService meterTypeCatalogService;
    private final UserService userService;
    JwtService jwtService = new JwtService();

    public AdminServlet() {
        this.userService = new UserService(new JdbcUserRepository());
        MeterTypeCatalogService meterTypeCatalogService = new MeterTypeCatalogService(new JdbcMeterTypeCatalogRepository());
        MeterReadingRepository meterReadingRepository = new JdbcMeterReadingRepository(new JdbcMeterTypeCatalogRepository());
        this.meterReadingService = new MeterReadingService(meterReadingRepository, meterTypeCatalogService, userService);
        this.meterTypeCatalogService = meterTypeCatalogService;

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String authorizationHeader = req.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);

                if (!jwtService.isValidToken(token) || !jwtService.getUserFromToken(token).getRole().equals(UserDto.Role.ADMIN)) {
                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    resp.getWriter().write("{\"error\": \"Unauthorized\"}");

                } else {
                    //прошел авторизацию
                    resp.setContentType("application/json");
                    String pathInfo = req.getPathInfo();

                    if (pathInfo != null) {
                        String[] pathSegments = pathInfo.split("/");

                        if (pathSegments.length == 3 && "meters".equals(pathSegments[1]) && "types".equals(pathSegments[2])) {
                            MeterTypeCatalogDto meterTypeCatalogDto = objectMapper.readValue(req.getReader(), MeterTypeCatalogDto.class);
                            String meterType = meterTypeCatalogDto.getMeterType();

                            if (meterType != null && meterType.length() >= 3) {
                                meterTypeCatalogService.addMeterType(meterType);
                                resp.setStatus(HttpServletResponse.SC_OK);
                                resp.getWriter().write("{\"SUCCESS\"}");
                            } else {
                                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                                resp.getWriter().write("{\"error\": \"Invalid meter type\"}");
                            }
                        } else {
                            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            resp.getWriter().write("Invalid path");
                        }
                    }
                }
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(e.getMessage() + " {\"error\": \"Invalid JSON format\"}");

        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String authorizationHeader = req.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);


                if (!jwtService.isValidToken(token) || !jwtService.getUserFromToken(token).getRole().equals(UserDto.Role.ADMIN)) {
                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    resp.getWriter().write("{\"error\": \"Unauthorized\"}");

                } else {

                    resp.setContentType("application/json");
                    PrintWriter writer = resp.getWriter();

                    String pathInfo = req.getPathInfo();
                    if (pathInfo != null) {
                        String[] pathSegments = pathInfo.split("/");
                        String username = pathSegments[1];

                        if (pathSegments.length == 5 && "readings".equals(pathSegments[2])) {
                            int year = Integer.parseInt(pathSegments[3]);
                            int month = Integer.parseInt(pathSegments[4]);
                            YearMonth date = YearMonth.of(year, month);
                            List<MeterReadingDto> meterReadingDtoList = meterReadingService.getByUserAndDate(username, date);

                            resp.setStatus(HttpServletResponse.SC_OK);
                            writer.write(objectMapper.writeValueAsString(meterReadingDtoList));

                        } else if (pathSegments.length == 4 && "readings".equals(pathSegments[2]) && "history".equals(pathSegments[3])) {
                            List<MeterReadingDto> meterReadingDtoList = meterReadingService.getAllForUser(username);

                            resp.setStatus(HttpServletResponse.SC_OK);
                            writer.write(objectMapper.writeValueAsString(meterReadingDtoList));

                        } else if (pathSegments.length == 4 && "readings".equals(pathSegments[2]) && "latest".equals(pathSegments[3])) {
                            List<MeterReadingDto> meterReadingDtoList = meterReadingService.getLatestMeterReadings(username);
                            resp.setStatus(HttpServletResponse.SC_OK);
                            writer.write(objectMapper.writeValueAsString(meterReadingDtoList));

                        } else if (pathSegments.length == 2 && "users".equals(pathSegments[1])) {
                            List<UserDto> userDtoList = userService.getAllUsers();
                            resp.setStatus(HttpServletResponse.SC_OK);
                            writer.write(objectMapper.writeValueAsString(userDtoList));

                        } else {
                            resp.getWriter().write("Invalid path");
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (e instanceof NumberFormatException || e instanceof DateTimeParseException) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Invalid year or month format");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(e.getMessage() + " {\"error\": \"Invalid JSON format\"}");
            }
        }
    }
}