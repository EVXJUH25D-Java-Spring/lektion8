package se.deved.lektion7;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Detta är konfigurationen för Spring Security
// @Configuration säger att denna klass innehåller Spring-konfiguration
// @EnableWebSecurity aktiverar Spring Security för vår webbapplikation
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // @Bean betyder att Spring ska använda denna metod för att skapa en "bean"
    // En bean är ett objekt som Spring hanterar och kan injicera i andra klasser
    // SecurityFilterChain bestämmer vilka endpoints som är skyddade och hur autentisering fungerar
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JWTService jwtService,
            IUserRepository userRepository,
            UserService userService
    ) {
        // Konfigurera säkerhetsregler
        http.csrf(AbstractHttpConfigurer::disable)                          // Stäng av CSRF (används ofta för API:er)
                .userDetailsService(userService)
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers("/register").permitAll()       // /register är tillgänglig för alla
                            .requestMatchers("/login").permitAll()          // /login är tillgänglig för alla
                            .requestMatchers("/authorized-endpoint").hasRole("ADMIN")
                            .anyRequest().authenticated();                  // Alla andra endpoints kräver inloggning
                })
                .addFilterBefore(
                        new AuthenticationFilter(jwtService, userRepository),
                        UsernamePasswordAuthenticationFilter.class           // Lägg till vårt JWT-filter före Springs standardfilter
                );

        // Bygg och returnera konfigurationen
        return http.build();
    }

    // Skapar en PasswordEncoder som används för att kryptera lösenord
    // BCrypt är en stark och säker krypteringsalgoritm
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
