package com.example.demo.service;


import com.example.demo.SpringRedditException;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.NotificationEmail;
import com.example.demo.model.User;
import com.example.demo.model.VerificationToken;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

// Explain : business logic to create user and saving it to database.
// sending out activation emails etc.
@AllArgsConstructor
@Service
public class AuthService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setUserName(registerRequest.getUserName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEnabled(false);// --> // it hasn't beeen activated.
        user.setCreated(Instant.now());
        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please activate your account",user.getEmail(),"Than you for" +
                " signing up to SPring Reddit" +
                "Please Check :"+"http://localhost:8080/api/auth/accountVerification/"+token));


    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> optionalVerificationToken = verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(optionalVerificationToken.orElseThrow(()->new SpringRedditException("invalid token")));

    }
    @Transactional
    private void fetchUserAndEnable(VerificationToken invalid_token) {
        String username = invalid_token.getUser().getUserName();
        User user = userRepository.findByUserName(username).orElseThrow(()->new SpringRedditException("User not found with name --"+username));
        user.setEnabled(true);
        userRepository.save(user);

    }
}
