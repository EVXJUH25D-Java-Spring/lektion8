package se.deved.lektion7;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

// Detta är ett Filter som körs för varje HTTP-förfrågan
// Filtret kontrollerar om användaren har en giltig JWT-token
// OncePerRequestFilter betyder att filtret körs exakt en gång per förfrågan
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final IUserRepository userRepository;

    // Denna metod körs för varje HTTP-förfrågan som kommer in
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // Hämta Authorization-headern från HTTP-förfrågan
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isBlank()) {
            // Ingen token finns - låt förfrågan fortsätta (vissa endpoints är publika)
            filterChain.doFilter(request, response);
            return;
        }

        // Plocka ut token från "Bearer <token>"-formatet
        String token = authHeader.substring("Bearer ".length());

        // Försök validera token och hämta användarens ID
        UUID userId;
        try {
            userId = jwtService.validateToken(token);
        } catch (JWTVerificationException exception) {
            // Token var ogiltig - skicka tillbaka 401 Unauthorized
            response.setStatus(401);
            return;
        }

        // Försök hitta användaren i databasen
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            // Användaren finns inte (trots att token var giltig) - skicka tillbaka 401
            response.setStatus(401);
            return;
        }

        // Token var giltig och användaren finns!
        User user = userOptional.get();
        // Spara användaren i Spring Security's context så att controllers kan komma åt den
        SecurityContextHolder
                .getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        user, user.getPasswordHash(), user.getAuthorities()
                ));
        // Låt förfrågan fortsätta till nästa filter/controller
        filterChain.doFilter(request, response);
    }
}
