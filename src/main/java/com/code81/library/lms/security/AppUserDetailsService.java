package com.code81.library.lms.security;

import com.code81.library.lms.entity.AppUser;
import com.code81.library.lms.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AppUserDetailsService.class);

    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Attempting to load user with username: {}", username);
        Optional<AppUser> userOptional = appUserRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            logger.warn("User not found with username: {}", username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        AppUser appUser = userOptional.get();
        logger.info("User found: {}, Role: {}", appUser.getUsername(), appUser.getRole().getName());
        return appUser;
    }
}