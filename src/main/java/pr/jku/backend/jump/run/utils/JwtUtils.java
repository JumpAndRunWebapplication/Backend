package pr.jku.backend.jump.run.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtils {
    public static final String PRIVATE_KEY = "super-secret-key";

    /**
     * generates a JWT by using a username and an id
     * @param username
     * @param userId
     * @return
     */
    public static String generateToken(String username, String userId) {
        return Jwts.builder()
                .claim("username", username)
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS512, PRIVATE_KEY)
                .compact();
    }

    /**
     * decrypts the username of a JWT
     * @param token
     * @return
     */
    public static String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(PRIVATE_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("username")
                .toString();
    }

    /**
     * decrypts the userID of a JWT
     * @param token
     * @return
     */
    public static String getUserIdFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(PRIVATE_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("userId")
                .toString();
    }

    /**
     * checks if the given JWT is valid or not and returns the appropriate boolean value
     * @param token
     * @return
     */
    public static boolean isJwtTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(PRIVATE_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
