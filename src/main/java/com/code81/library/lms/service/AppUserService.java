package com.code81.library.lms.service;

import com.code81.library.lms.entity.AppUser;

import com.code81.library.lms.repository.AppUserRepository;
import com.code81.library.lms.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class AppUserService {
    private static final Logger logger = LoggerFactory.getLogger(AppUserService.class);

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AppUser> findAllAppUsers() {
        return appUserRepository.findAll();
    }

    public Optional<AppUser> findAppUserById(Long id) {
        return appUserRepository.findById(id);
    }

    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Transactional
    public AppUser saveAppUser(AppUser appUser) {
        if (appUser.getRole() != null && appUser.getRole().getId() != null) {
            roleRepository.findById(appUser.getRole().getId())
                    .ifPresent(appUser::setRole);
        } else {
            throw new IllegalArgumentException("Role ID is required for AppUser");
        }

        if (appUser.getCreatedAt() == null) {
            appUser.setCreatedAt(LocalDateTime.now());
        }

        appUser.setPasswordHash(passwordEncoder.encode(appUser.getPasswordHash()));

        return appUserRepository.save(appUser);
    }

    @Transactional
    public AppUser updateAppUser(AppUser appUserDetails) {
        return appUserRepository.findById(appUserDetails.getId())
                .map(existingUser -> {
                    existingUser.setUsername(appUserDetails.getUsername());
                    existingUser.setEmail(appUserDetails.getEmail());
                    if (appUserDetails.getRole() != null && appUserDetails.getRole().getId() != null) {
                        roleRepository.findById(appUserDetails.getRole().getId())
                                .ifPresent(existingUser::setRole);
                    } else {
                        throw new IllegalArgumentException("Role ID is required for AppUser update");
                    }
                    existingUser.setLastLoginAt(appUserDetails.getLastLoginAt());

                    return appUserRepository.save(existingUser);
                })
                .orElse(null);
    }

    public void deleteAppUser(Long id) {
        appUserRepository.deleteById(id);
    }
}