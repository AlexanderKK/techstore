package com.techx7.techstore.service.impl;

import com.techx7.techstore.exception.EntityNotFoundException;
import com.techx7.techstore.exception.UserAlreadyActivatedException;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.model.entity.UserActivationCode;
import com.techx7.techstore.model.events.UserRegisteredEvent;
import com.techx7.techstore.repository.UserActivationCodeRepository;
import com.techx7.techstore.repository.UserRepository;
import com.techx7.techstore.service.EmailService;
import com.techx7.techstore.service.UserActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.*;

import static com.techx7.techstore.constant.Messages.*;

@Service
public class UserActivationServiceImpl implements UserActivationService {

    private static final String ACTIVATION_CODE_TOKENS =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ACTIVATION_CODE_LENGTH = 20;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final UserActivationCodeRepository userActivationCodeRepository;

    @Autowired
    public UserActivationServiceImpl(EmailService emailService,
                                     UserRepository userRepository,
                                     UserActivationCodeRepository userActivationCodeRepository)   {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.userActivationCodeRepository = userActivationCodeRepository;
    }

    @EventListener(UserRegisteredEvent.class)
    @Override
    public void userRegistered(UserRegisteredEvent event) {
        String activationCode = createActivationCode(event.getUserEmail());

        emailService.sendRegistrationEmail(event.getUserEmail(), event.getUserName(), activationCode);
    }

    @Override
    public void cleanUpObsoleteActivationLinks() {
        List<UserActivationCode> userActivationCodes = userActivationCodeRepository.findAll().stream()
                .filter(userActivationCode -> {
//                    long currentTimeInMillis = System.currentTimeMillis();
//                    long creationTimeInMillis = userActivationCode.getCreated().getTimeInMillis();
//
//                    long lifetimeInMillis = currentTimeInMillis - creationTimeInMillis;
//
//                    long lifetimeMinutes = Math.round(lifetimeInMillis / 60000.0);
//
//                    System.out.println(lifetimeMinutes);

//
//                    ZoneId zoneId = ZoneId.from(userActivationCode.getCreated().atZone(ZoneOffset.UTC));

                    Instant instant = userActivationCode.getCreated().toInstant(ZoneOffset.UTC);

                    OffsetDateTime createdOffset = instant.atOffset(ZoneOffset.UTC);
                    OffsetDateTime currentOffsetDateTime = Instant.now().atOffset(ZoneOffset.UTC);
//
                    LocalDateTime timeOfCreation = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                    LocalDateTime currentTime = LocalDateTime.ofInstant(Instant.now(), ZonedDateTime.now().getZone());

                    System.out.println(userActivationCode.getCreated().toString());
                    System.out.println(currentOffsetDateTime);

                    System.out.println(ChronoUnit.MINUTES.between(timeOfCreation, ZonedDateTime.now()));

                    return true;
                }).toList();

//        userActivationCodeRepository.saveAll(userActivationCodes);
    }

    @Override
    public String createActivationCode(String userEmail) {
        String activationCode = generateActivationCode();

        UserActivationCode userActivationCode = new UserActivationCode();

        userActivationCode.setActivationCode(activationCode);
        userActivationCode.setUser(
                userRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User"))));

        userActivationCodeRepository.save(userActivationCode);

        return activationCode;
    }

    @Override
    public String activateUser(String activationCode) {
        UserActivationCode userActivationCode = userActivationCodeRepository.findByActivationCode(activationCode)
                .orElseThrow(() -> new EntityNotFoundException(VERIFICATION_CODE_NOT_VALID));

        User user = userRepository
                .findByActivationCodesIn(Set.of(userActivationCode))
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        if(user.isActive()) {
            throw new UserAlreadyActivatedException(USER_ALREADY_VERIFIED);
        }

        user.setActive(true);

        userRepository.save(user);

        return user.getUsername();
    }

    private static String generateActivationCode() {
        StringBuilder activationCode = new StringBuilder();

        Random random = new SecureRandom();

        for (int i = 0; i < ACTIVATION_CODE_LENGTH; i++) {
            int rndIndex = random.nextInt(ACTIVATION_CODE_TOKENS.length());

            activationCode.append(ACTIVATION_CODE_TOKENS.charAt(rndIndex));
        }

        return activationCode.toString();
    }

}
