package pr.jku.backend.jump.run.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtils {
    public static final String PRIVATE_KEY = "super-secret-key";

    public static String generateToken(String username, String userId) {
        return Jwts.builder()
                .claim("username", username)
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS512, PRIVATE_KEY)
                .compact();
    }

    public static String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(PRIVATE_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("username")
                .toString();
    }

    public static String getUserIdFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(PRIVATE_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("userId")
                .toString();
    }

    public static boolean isJwtTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(PRIVATE_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
