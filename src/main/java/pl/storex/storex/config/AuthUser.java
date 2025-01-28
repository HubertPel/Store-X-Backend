package pl.storex.storex.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.storex.storex.user.model.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthUser {

    private final static Logger log = LoggerFactory.getLogger(AuthUser.class);

    public Optional<User> currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        Object connectionDetails = authentication.getDetails();
        log.info("Principal: {}", principal.toString());
        log.info("ConnDetails: {}", connectionDetails.toString());
        return Optional.of(principal);
    }

}
