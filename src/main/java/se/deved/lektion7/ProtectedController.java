package se.deved.lektion7;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// En enkel REST Controller med en skyddad endpoint
// Denna endpoint kräver att användaren är inloggad (har en giltig JWT-token)
@RestController
public class ProtectedController {

    // GET /protected - en endpoint som bara inloggade användare kan nå
    // @GetMapping betyder att detta är en GET-endpoint
    // @AuthenticationPrincipal hämtar den inloggade användaren automatiskt
    @GetMapping("/protected")
    public String protectedEndpoint(
            @AuthenticationPrincipal User user
    ) {
        // Returnera ett hälsningsmeddelande med användarens namn
        return "Hello " + user.getUsername();
    }

    @GetMapping("/authorized-endpoint")
    public String authorizedEndpoint(
            @AuthenticationPrincipal User user
    ) {
        // Returnera ett hälsningsmeddelande med användarens namn
        return "Hello " + user.getUsername();
    }
}
