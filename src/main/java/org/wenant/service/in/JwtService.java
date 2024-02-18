package org.wenant.service.in;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.wenant.domain.dto.AuthenticationDto;
import org.wenant.domain.dto.UserDto;

public class JwtService {
    private static final String SECRET_KEY = "p4+ah8Op5+GXShuqV6ZWGF4Go9S0UKtVbSbHy/RzFi8=";

    public String generateToken(AuthenticationDto authenticationDto) {
        return Jwts.builder()
                .setSubject(authenticationDto.getUsername())
                .claim("role", authenticationDto.getRole().name())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public UserDto getUserFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            String role = (String) claims.get("role");


            return new UserDto(username, UserDto.Role.valueOf(role));

        } catch (Exception e) {

            return null;
        }
    }

}
