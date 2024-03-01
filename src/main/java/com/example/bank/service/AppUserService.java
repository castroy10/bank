package com.example.bank.service;

import com.example.bank.dto.appuser.AppUserRequestRegisterDto;
import com.example.bank.dto.appuser.AppUserRequestTokenDto;
import com.example.bank.model.Appuser;
import com.example.bank.model.Email;
import com.example.bank.model.Phone;
import com.example.bank.repository.AppUserRepository;
import com.example.bank.security.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;

    public AppUserService(AppUserRepository appUserRepository, AuthenticationManager authenticationManager, JwtUtil jwtUtil, ModelMapper modelMapper) {
        this.appUserRepository = appUserRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public ResponseEntity<?> register(AppUserRequestRegisterDto appUserRequestRegisterDto) {
        Appuser appuser = modelMapper.map(appUserRequestRegisterDto, Appuser.class);
        appUserSetPhones(appUserRequestRegisterDto, appuser);
        appUserSetEmail(appUserRequestRegisterDto, appuser);
        appUserRepository.save(appuser);
        log.info("Пользователь {} {} {} записан в базу данных, id={}", appuser.getLastName(), appuser.getFirstName(), appuser.getMiddleName(), appuser.getId());
        return ResponseEntity.ok().body(Map.of("Сообщение:", "Пользователь записан в базу данных, id=" + appuser.getId()));
    }

    public ResponseEntity<?> getToken(AppUserRequestTokenDto appUserRequestTokenDto) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(appUserRequestTokenDto.getUsername(), appUserRequestTokenDto.getPassword());
        authenticationManager.authenticate(auth);
        return ResponseEntity.ok().body(Map.of("Token", jwtUtil.generateToken(appUserRequestTokenDto.getUsername())));
    }

    private void appUserSetPhones(AppUserRequestRegisterDto appUserRequestRegisterDto, Appuser appuser) {
        List<String> stringList = appUserRequestRegisterDto.getPhones();
        List<Phone> phoneList = new ArrayList<>();
        for (String string : stringList) {
            Phone phone = new Phone();
            phone.setNumber(string);
            phone.setAppuser(appuser);
            phoneList.add(phone);
        }
        appuser.setPhones(phoneList);
    }

    private void appUserSetEmail(AppUserRequestRegisterDto appUserRequestRegisterDto, Appuser appuser) {
        List<String> stringList = appUserRequestRegisterDto.getEmails();
        List<Email> emailList = new ArrayList<>();
        for (String string : stringList) {
            Email email = new Email();
            email.setEmail(string);
            email.setAppuser(appuser);
            emailList.add(email);
        }
        appuser.setEmails(emailList);
    }

    public Appuser getAppuser(String username) throws UsernameNotFoundException {
        Optional<Appuser> appuser = appUserRepository.findAppuserByUsername(username);
        if (appuser.isPresent()) {
            return appuser.get();
        } else throw new UsernameNotFoundException("Пользователь не найден");
    }
}
