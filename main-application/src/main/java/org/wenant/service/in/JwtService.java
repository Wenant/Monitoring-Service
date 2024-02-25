package org.wenant.service.in;


import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;
import org.wenant.config.YamlReader;
import org.wenant.domain.dto.AuthenticationDto;
import org.wenant.domain.dto.UserDto;
import org.wenant.domain.model.User;
import org.wenant.service.UserService;

@Service
public class JwtService {
    private final UserService userService;
    private static final String SECRET_KEY = YamlReader.getJwtSecret();
    private static final int BEARER_TOKEN_PREFIX_LENGTH = 7;
    public JwtService(UserService userService) {
        this.userService = userService;
    }



    private JwtParser getParser() {
        return Jwts.parser().setSigningKey(SECRET_KEY);
    }

    public String generateToken(AuthenticationDto authenticationDto) {
        User user = userService.getUserByUsername(authenticationDto.getUsername());
        return Jwts.builder()
                .setSubject(authenticationDto.getUsername())
                .claim("role", user.getRole().name())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean isValidToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return false;
        }
        try {
            String token = getTokenFromAuthorizationHeader(authorizationHeader);
            getParser().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            System.out.println("Ошибка при проверке токена " + e.getMessage());
            return false;
        }
    }

    public String getTokenFromAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid authorization header: " + authorizationHeader);
        }
        return authorizationHeader.substring(BEARER_TOKEN_PREFIX_LENGTH);
    }

    public boolean userIsAdmin(String authorizationHeader) {
        return isValidToken(authorizationHeader) &&
                "ADMIN".equals(getUserFromAuthorizationHeader(authorizationHeader).getRole().name());
    }


    public String getUsernameFromAuthorizationHeader(String authorizationHeader) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        Claims claims = getParser().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public UserDto getUserFromAuthorizationHeader(String authorizationHeader) {
        String token = getTokenFromAuthorizationHeader(authorizationHeader);
        try {
            Claims claims = getParser().parseClaimsJws(token).getBody();

            String username = claims.getSubject();
            String role = (String) claims.get("role");

            return new UserDto(username, UserDto.Role.valueOf(role));
        } catch (Exception e) {
            return null;
        }
    }

}