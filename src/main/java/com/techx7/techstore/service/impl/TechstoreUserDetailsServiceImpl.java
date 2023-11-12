package com.techx7.techstore.service.impl;

import com.techx7.techstore.model.entity.Role;
import com.techx7.techstore.model.entity.User;
import com.techx7.techstore.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        return userRepository.findByEmailOrUsername(emailOrUsername)
                .map(TechstoreUserDetailsServiceImpl::map)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(ENTITY_NOT_FOUND, "User")));
    }

    private static UserDetails map(User user) {
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(
                        user.getRoles().stream()
                        .map(TechstoreUserDetailsServiceImpl::map)
                        .toList())
                .build();
    }

    private static GrantedAuthority map(Role role) {
        return new SimpleGrantedAuthority(
                "ROLE_" + role.getName()
        );
    }

}