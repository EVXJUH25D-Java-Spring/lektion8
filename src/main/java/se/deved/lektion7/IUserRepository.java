package se.deved.lektion7;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

// Detta är ett Repository - en klass som pratar med databasen
// @Repository säger till Spring att detta är en databasklass
// JpaRepository ger oss automatiskt metoder som save(), findById(), findAll() etc.
@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {

    // Spring skapar automatiskt denna metod åt oss baserat på namnet
    // "findByUsername" betyder: hitta en användare som har ett visst användarnamn
    // Optional betyder att användaren kanske inte finns (då får vi tillbaka null-liknande värde)
    Optional<User> findByUsername(String username);
}