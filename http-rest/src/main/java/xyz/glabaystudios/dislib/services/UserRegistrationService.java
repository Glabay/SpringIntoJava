package xyz.glabaystudios.dislib.services;

import xyz.glabaystudios.dislib.data.model.RegisteredUser;
import xyz.glabaystudios.dislib.data.repos.RegisteredUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.glabaystudios.network.dto.RegistrationFormDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service
public class UserRegistrationService {

    private final RegisteredUserRepository registeredUserRepository;

    public boolean profileExists(RegistrationFormDTO dto) {
        return registeredUserRepository.findByUsername(dto.getUsername()).isPresent();
    }

    public boolean userCredentialMatch(RegistrationFormDTO submittedCreds, RegisteredUser objectToCompare) {
        return new BCryptPasswordEncoder().matches(submittedCreds.getPassword(), objectToCompare.getPassword());
    }

    public void createAndSaveNewAccount(RegistrationFormDTO dto) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        RegisteredUser newUser = new RegisteredUser();
        newUser.setCreatedOn(dtf.format(now));
        newUser.setUpdatedOn(dtf.format(now));
        newUser.setUsername(dto.getUsername());
        newUser.setEmail(dto.getEmail());
        newUser.setFirstName(dto.getFirstName());
        newUser.setLastName(dto.getLastName());
        newUser.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        newUser.setDiscordUserId(dto.getDiscordUserId());

        registeredUserRepository.save(newUser);
    }
}
