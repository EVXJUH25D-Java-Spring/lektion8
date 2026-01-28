package se.deved.lektion7;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// Detta är en REST Controller - hanterar HTTP-förfrågningar från klienter
// @RestController säger till Spring att detta är en REST API-klass
// @RequiredArgsConstructor skapar automatiskt en konstruktor för "final" fält
@RestController
@RequiredArgsConstructor
public class UserController {

    // UserService injiceras automatiskt av Spring
    private final UserService userService;

    // POST /register - skapar en ny användare
    // @PostMapping betyder att detta är en POST-endpoint
    // @RequestBody tar emot JSON-data från klienten och omvandlar den till CreateUserRequest
    @PostMapping("/register")
    public void createUser(@RequestBody CreateUserRequest request) {
        // Skapa användaren via UserService
        userService.createUser(request.getUsername(), request.getPassword(), false);
    }

    // POST /login - loggar in en användare och returnerar en JWT-token
    // ResponseEntity<?> låter oss skicka olika HTTP-statuskoder (200 OK, 401 Unauthorized, etc.)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserRequest request) {
        // Försök logga in användaren
        String token = userService.login(request.getUsername(), request.getPassword());
        if (token == null) {
            // Inloggningen misslyckades - skicka tillbaka 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Inloggningen lyckades - skicka tillbaka token med 200 OK
        return ResponseEntity.ok(token);
    }

    // En hjälpklass som representerar JSON-data för registrering
    // Spring omvandlar automatiskt JSON till denna klass
    @Getter
    @Setter
    public static class CreateUserRequest {
        private String username;
        private String password;
    }

    // En hjälpklass som representerar JSON-data för inloggning
    @Getter
    @Setter
    public static class LoginUserRequest {
        private String username;
        private String password;
    }
}
