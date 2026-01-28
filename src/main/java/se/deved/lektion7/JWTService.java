package se.deved.lektion7;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

// Service för att hantera JWT (JSON Web Tokens)
// JWT är tokens som används för att identifiera inloggade användare
@Service
public class JWTService {

    // Algoritmen som används för att signera tokens (HMAC256 med en hemlig nyckel)
    // OBS: I produktion ska denna nyckel vara säker och inte hårdkodad!
    private static final Algorithm algorithm = Algorithm.HMAC256("frehqvirheiuoqhvruioewhuio");

    // Verifierare som kontrollerar att tokens är giltiga och skapade av oss
    private static final JWTVerifier verifier = JWT.require(algorithm).withIssuer("lektion7").build();

    // Skapar en ny JWT-token för en användare
    public String generateToken(UUID userId) {
        return JWT.create()
                .withIssuer("lektion7")                                           // Vem som skapat token
                .withIssuedAt(Instant.now())                                     // När token skapades
                .withExpiresAt(Instant.now().plus(2, ChronoUnit.MINUTES))       // Token gäller i 2 minuter
                .withSubject(userId.toString())                                  // Användarens ID
                .withClaim("pannkakor", "är gott")                              // Extra data (bara för demo)
                .sign(algorithm);                                                // Signera med vår hemliga nyckel
    }

    // Validerar en JWT-token och returnerar användarens ID
    // Kastar ett exception om token är ogiltig eller utgången
    public UUID validateToken(String token) {
        // Verifiera att token är giltig och inte manipulerad
        DecodedJWT jwt = verifier.verify(token);
        // Hämta och returnera användarens ID från token
        return UUID.fromString(jwt.getSubject());
    }
}
