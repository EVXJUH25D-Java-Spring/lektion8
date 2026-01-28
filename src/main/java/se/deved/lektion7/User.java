package se.deved.lektion7;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

// Detta är en databasmodell för användare
// @Entity säger att denna klass ska sparas i databasen som en tabell med namnet "users"
// @NoArgsConstructor skapar en tom konstruktor automatiskt (krävs av JPA)
// @Getter och @Setter skapar get- och set-metoder automatiskt för alla fält
@Entity(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails {

    // @Id betyder att detta är primärnyckeln i databasen
    // UUID är ett unikt ID som genereras automatiskt för varje ny användare
    @Id
    private final UUID id = UUID.randomUUID();

    // Användarens användarnamn
    private String username;

    // Användarens krypterade lösenord (vi sparar ALDRIG lösenord i klartext!)
    private String passwordHash;

    private Boolean admin;

    // Konstruktor som används när vi skapar en ny användare
    public User(String username, String passwordHash, boolean admin) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.admin = admin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (admin != null && admin) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            return List.of();
        }
    }

    @Override
    public @Nullable String getPassword() {
        return passwordHash;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
