package vinh.pro.book.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import vinh.pro.book.constant.AppConstant;
import vinh.pro.book.exception.BookAppException;

import java.util.Date;


@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String jwtSecret;

    @Value("${jwt.expiration-in-second}")
    private int jwtExpirationInSecond;

    public String generateToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInSecond * 1000);

        return Jwts.builder()
                   .setSubject(userPrincipal.getEmail())
                   .setIssuedAt(new Date())
                   .setExpiration(expiryDate)
                   .signWith(SignatureAlgorithm.HS512, jwtSecret)
                   .compact();
    }

    public String getUserEmailFromJWT(String token) {
        Claims claims = Jwts.parser()
                            .setSigningKey(jwtSecret)
                            .parseClaimsJws(token)
                            .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            throw new BookAppException(AppConstant.Code.AUTH_SERVICE_CODE_PREFIX, "Invalid JWT signature", HttpStatus.BAD_REQUEST);
        } catch (MalformedJwtException ex) {
            throw new BookAppException(AppConstant.Code.AUTH_SERVICE_CODE_PREFIX, "Invalid JWT token", HttpStatus.BAD_REQUEST);
        } catch (ExpiredJwtException ex) {
            throw new BookAppException(AppConstant.Code.AUTH_SERVICE_CODE_PREFIX, "Expired JWT token", HttpStatus.BAD_REQUEST);
        } catch (UnsupportedJwtException ex) {
            throw new BookAppException(AppConstant.Code.AUTH_SERVICE_CODE_PREFIX, "Unsupported JWT token", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException ex) {
            throw new BookAppException(AppConstant.Code.AUTH_SERVICE_CODE_PREFIX, "JWT claims string is empty.", HttpStatus.BAD_REQUEST);
        }
    }
}
