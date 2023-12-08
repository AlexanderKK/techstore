package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.model.session.TechStoreUserDetails;
import com.techx7.techstore.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.techx7.techstore.constant.Messages.ENTITY_NOT_FOUND;

public class TechstoreUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public TechstoreUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        User user = userRepository.findByEmailOrUsername(emailOrUsername)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));

        return new TechStoreUserDetails(user);
    }

}
