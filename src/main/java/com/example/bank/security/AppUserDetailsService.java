package com.example.bank.security;

import com.example.bank.model.Appuser;
import com.example.bank.repository.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public AppUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Appuser appuser = appUserRepository.findAppuserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new AppUserDetails(appuser);
    }
}
