package com.example.bank.service;

import com.example.bank.dto.appuser.AppUserResponseFullDto;
import com.example.bank.dto.search.*;
import com.example.bank.model.Appuser;
import com.example.bank.model.Email;
import com.example.bank.model.Phone;
import com.example.bank.model.SearchEntity;
import com.example.bank.repository.AppUserRepository;
import com.example.bank.repository.EmailRepository;
import com.example.bank.repository.PhoneRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class SearchService {

    private final AppUserRepository appUserRepository;
    private final PhoneRepository phoneRepository;
    private final EmailRepository emailRepository;
    private final ModelMapper modelMapper;

    public SearchService(AppUserRepository appUserRepository, PhoneRepository phoneRepository, EmailRepository emailRepository, ModelMapper modelMapper) {
        this.appUserRepository = appUserRepository;
        this.phoneRepository = phoneRepository;
        this.emailRepository = emailRepository;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<?> searchFull(RequestFullSearchDto requestFullSearchDto) {
        SearchEntity searchEntity = modelMapper.map(requestFullSearchDto, SearchEntity.class);
        Map<Long, Appuser> resultMap = new HashMap<>();
        if (searchEntity.getDate() != null) {
            appUserRepository.findByBirthdayAfter(searchEntity.getDate()).forEach(appuser -> resultMap.put(appuser.getId(), appuser));
        }
        if (searchEntity.getPhone() != null) {
            phoneRepository.findPhoneByNumber(searchEntity.getPhone()).ifPresent(phone -> resultMap.put(phone.getAppuser().getId(), phone.getAppuser()));
        }
        if (searchEntity.getEmail() != null) {
            emailRepository.findEmailByEmail(searchEntity.getEmail()).ifPresent(email -> resultMap.put(email.getAppuser().getId(), email.getAppuser()));
        }
        if (searchEntity.getName() != null) {
            appUserRepository.findByName(searchEntity.getName()).forEach((appuser -> resultMap.put(appuser.getId(), appuser)));
        }
        List<AppUserResponseFullDto> appUserResponseFullDtoList = resultMap.values().stream()
                .map(appuser -> modelMapper.map(appuser, AppUserResponseFullDto.class))
                .sorted(Comparator.comparing(AppUserResponseFullDto::getLastName))
                .toList();
        return ResponseEntity.ok().body(appUserResponseFullDtoList);
    }

    public ResponseEntity<?> searchDate(RequestDateSearchDto requestDateSearchDto) {
        LocalDate localDate = modelMapper.map(requestDateSearchDto.getDate(), LocalDate.class);
        List<Appuser> appuserList = appUserRepository.findByBirthdayAfter(localDate);
        List<AppUserResponseFullDto> appUserResponseFullDtoList = appuserList.stream()
                .map(appuser -> modelMapper.map(appuser, AppUserResponseFullDto.class))
                .sorted(Comparator.comparing(AppUserResponseFullDto::getLastName))
                .toList();
        return ResponseEntity.ok().body(appUserResponseFullDtoList);
    }

    public ResponseEntity<?> searchPhone(RequestPhoneSearchDto requestPhoneSearchDto) throws UsernameNotFoundException {
        Optional<Phone> optionalPhone = phoneRepository.findPhoneByNumber(requestPhoneSearchDto.getPhone());
        if (optionalPhone.isPresent()) {
            return ResponseEntity.ok().body(modelMapper.map(optionalPhone.get().getAppuser(), AppUserResponseFullDto.class));
        } else throw new UsernameNotFoundException("Такой телефон не найден");
    }

    public ResponseEntity<?> searchEmail(RequestEmailSearchDto requestEmailSearchDto) {
        Optional<Email> optionalEmail = emailRepository.findEmailByEmail(requestEmailSearchDto.getEmail());
        if (optionalEmail.isPresent()) {
            return ResponseEntity.ok().body(modelMapper.map(optionalEmail.get().getAppuser(), AppUserResponseFullDto.class));
        } else throw new UsernameNotFoundException("Такой email не найден");
    }

    public ResponseEntity<?> searchName(RequestNameSearchDto requestNameSearchDto) {
        List<Appuser> appuserList = appUserRepository.findByName(requestNameSearchDto.getName().toLowerCase());
        List<AppUserResponseFullDto> appUserResponseFullDtoList = appuserList.stream()
                .map(appuser -> modelMapper.map(appuser, AppUserResponseFullDto.class))
                .sorted(Comparator.comparing(AppUserResponseFullDto::getLastName))
                .toList();
        return ResponseEntity.ok().body(appUserResponseFullDtoList);
    }
}
