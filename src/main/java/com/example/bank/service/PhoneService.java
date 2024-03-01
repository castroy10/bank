package com.example.bank.service;

import com.example.bank.dto.phone.RequestAddPhoneDto;
import com.example.bank.dto.phone.RequestDeletePhoneDto;
import com.example.bank.dto.phone.RequestUpdatePhoneDto;
import com.example.bank.exception.LastRecordException;
import com.example.bank.exception.NotFoundRecordException;
import com.example.bank.model.Appuser;
import com.example.bank.model.Phone;
import com.example.bank.repository.PhoneRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class PhoneService {

    private final AppUserService appUserService;
    private final PhoneRepository phoneRepository;

    public PhoneService(AppUserService appUserService, PhoneRepository phoneRepository) {
        this.appUserService = appUserService;
        this.phoneRepository = phoneRepository;
    }

    @Transactional
    public ResponseEntity<?> addPhone(RequestAddPhoneDto requestAddPhoneDto, HttpServletRequest httpServletRequest) {
        String username = httpServletRequest.getRemoteUser();
        Appuser user = appUserService.getAppuser(username);
        Phone phone = new Phone();
        phone.setNumber(requestAddPhoneDto.getPhone());
        phone.setAppuser(user);
        phoneRepository.save(phone);
        log.info("Телефон добавлен, для пользователя с id={}", user.getId());
        return ResponseEntity.ok().body(Map.of("Сообщение:", "Телефон добавлен, для пользователя с id=" + user.getId()));
    }

    @Transactional
    public ResponseEntity<?> deletePhone(RequestDeletePhoneDto requestDeletePhoneDto, HttpServletRequest httpServletRequest) throws LastRecordException, NotFoundRecordException {
        String username = httpServletRequest.getRemoteUser();
        Appuser user = appUserService.getAppuser(username);
        appUserDeletePhone(requestDeletePhoneDto, user);
        log.info("Телефон удален, для пользователя с id={}", user.getId());
        return ResponseEntity.ok().body(Map.of("Сообщение:", "Телефон удален, для пользователя с id=" + user.getId()));
    }

    @Transactional
    public ResponseEntity<?> updatePhone(RequestUpdatePhoneDto requestUpdatePhoneDto, HttpServletRequest httpServletRequest) throws NotFoundRecordException {
        String username = httpServletRequest.getRemoteUser();
        Appuser user = appUserService.getAppuser(username);
        appUserUpdatePhone(requestUpdatePhoneDto, user);
        log.info("Телефон обновлен, для пользователя с id={}", user.getId());
        return ResponseEntity.ok().body(Map.of("Сообщение:", "Телефон обновлен, для пользователя с id=" + user.getId()));
    }

    private void appUserDeletePhone(RequestDeletePhoneDto requestDeletePhoneDto, Appuser appuser) throws LastRecordException, NotFoundRecordException {
        Phone phone = getPhone(requestDeletePhoneDto.getPhone());
        List<Phone> appUserPhonesList = appuser.getPhones();
        if (appUserPhonesList.size() == 1) throw new LastRecordException("Единственный телефон удалить нельзя");
        Iterator<Phone> iterator = appUserPhonesList.iterator();
        while (iterator.hasNext()) {
            Phone phoneIterator = iterator.next();
            if (phoneIterator.equals(phone)) {
                iterator.remove();
            }
        }
        appuser.setPhones(appUserPhonesList);
        phoneRepository.delete(phone);
    }

    private void appUserUpdatePhone(RequestUpdatePhoneDto requestUpdatePhoneDto, Appuser appuser) throws NotFoundRecordException {
        Phone oldPhone = getPhone(requestUpdatePhoneDto.getOldPhone());
        List<Phone> appUserPhonesList = appuser.getPhones();
        Iterator<Phone> iterator = appUserPhonesList.iterator();
        while (iterator.hasNext()) {
            Phone phoneIterator = iterator.next();
            if (phoneIterator.equals(oldPhone)) {
                phoneIterator.setNumber(requestUpdatePhoneDto.getNewPhone());
            }
        }
        appuser.setPhones(appUserPhonesList);
    }

    private Phone getPhone(String number) throws NotFoundRecordException {
        Optional<Phone> optionalPhone = phoneRepository.findPhoneByNumber(number);
        if (optionalPhone.isPresent()) {
            return optionalPhone.get();
        }
        else throw new NotFoundRecordException("Телефон не найден");
    }
}
