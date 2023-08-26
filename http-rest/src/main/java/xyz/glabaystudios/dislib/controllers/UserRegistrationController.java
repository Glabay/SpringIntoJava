package xyz.glabaystudios.dislib.controllers;

import xyz.glabaystudios.dislib.services.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.glabaystudios.network.dto.RegistrationFormDTO;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/registry")
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    @PostMapping("/registered")
    public ResponseEntity<Boolean> checkIfUserExists(@RequestBody RegistrationFormDTO body) {
        if (Objects.isNull(body)) return new ResponseEntity<>(Boolean.FALSE, HttpStatus.NO_CONTENT);
        if (userRegistrationService.profileExists(body)) return new ResponseEntity<>(Boolean.TRUE, HttpStatus.CREATED);
        else return new ResponseEntity<>(Boolean.FALSE, HttpStatus.CONFLICT);
    }
    @PostMapping("/create")
    public ResponseEntity<String> createNewAccount(@RequestBody RegistrationFormDTO body) {
        if (Objects.isNull(body)) return new ResponseEntity<>("There was nothing provided...", HttpStatus.NO_CONTENT);
        if (userRegistrationService.profileExists(body)) return new ResponseEntity<>("This user seems to already exist...", HttpStatus.ALREADY_REPORTED);
        userRegistrationService.createAndSaveNewAccount(body);
        return new ResponseEntity<>("Successfully create and saved a new record.", HttpStatus.CREATED);
    }
}
