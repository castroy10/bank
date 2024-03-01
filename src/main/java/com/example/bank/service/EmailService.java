package com.example.bank.service;

import com.example.bank.dto.email.RequestAddEmailDto;
import com.example.bank.dto.email.RequestDeleteEmailDto;
import com.example.bank.dto.email.RequestUpdateEmailDto;
import com.example.bank.exception.LastRecordException;
import com.example.bank.exception.NotFoundRecordException;
import com.example.bank.model.Appuser;
import com.example.bank.model.Email;
import com.example.bank.repository.EmailRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class EmailService {

    private final AppUserService appUserService;
    private final EmailRepository emailRepository;

    public EmailService(AppUserService appUserService, EmailRepository emailRepository) {
        this.appUserService = appUserService;
        this.emailRepository = emailRepository;
    }

    @Transactional
    public ResponseEntity<?> addEmail(RequestAddEmailDto requestAddEmailDto, HttpServletRequest httpServletRequest) {
        String username = httpServletRequest.getRemoteUser();
        Appuser user = appUserService.getAppuser(username);
        Email email = new Email();
        email.setEmail(requestAddEmailDto.getEmail());
        email.setAppuser(user);
        emailRepository.save(email);
        log.info("Email добавлен, для пользователя с id={}", user.getId());
        return ResponseEntity.ok().body(Map.of("Сообщение:", "Email добавлен, для пользователя с id=" + user.getId()));
    }

    @Transactional
    public ResponseEntity<?> deleteEmail(RequestDeleteEmailDto requestDeleteEmailDto, HttpServletRequest httpServletRequest) throws LastRecordException, NotFoundRecordException {
        String username = httpServletRequest.getRemoteUser();
        Appuser user = appUserService.getAppuser(username);
        emailDelete(requestDeleteEmailDto, user);
        log.info("Email удален, для пользователя с id={}", user.getId());
        return ResponseEntity.ok().body(Map.of("Сообщение:", "Email удален, для пользователя с id=" + user.getId()));
    }

    @Transactional
    public ResponseEntity<?> updateEmail(RequestUpdateEmailDto requestUpdateEmailDto, HttpServletRequest httpServletRequest) throws NotFoundRecordException {
        String username = httpServletRequest.getRemoteUser();
        Appuser user = appUserService.getAppuser(username);
        emailUpdate(requestUpdateEmailDto, user);
        log.info("Email обновлен, для пользователя с id={}", user.getId());
        return ResponseEntity.ok().body(Map.of("Сообщение:", "Email обновлен, для пользователя с id=" + user.getId()));
    }

    private void emailUpdate(RequestUpdateEmailDto requestUpdateEmailDto, Appuser appuser) throws NotFoundRecordException {
        Email oldEmail = getEmail(requestUpdateEmailDto.getOldEmail());
        List<Email> emailList = appuser.getEmails();
        Iterator<Email> iterator = emailList.iterator();
        while (iterator.hasNext()) {
            Email emailIterator = iterator.next();
            if (emailIterator.equals(oldEmail)) {
                emailIterator.setEmail(requestUpdateEmailDto.getNewEmail());
            }
        }
        appuser.setEmails(emailList);
    }

    private void emailDelete(RequestDeleteEmailDto requestDeleteEmailDto, Appuser appuser) throws LastRecordException, NotFoundRecordException {
        Email email = getEmail(requestDeleteEmailDto.getEmail());
        List<Email> emailList = appuser.getEmails();
        if (emailList.size() == 1) throw new LastRecordException("Единственную почту удалить нельзя");
        Iterator<Email> iterator = emailList.iterator();
        while (iterator.hasNext()) {
            Email emailIterator = iterator.next();
            if (emailIterator.equals(email)) {
                iterator.remove();
            }
        }
        appuser.setEmails(emailList);
        emailRepository.delete(email);
    }

    private Email getEmail(String string) throws NotFoundRecordException {
        Optional<Email> emailOptional = emailRepository.findEmailByEmail(string);
        if (emailOptional.isPresent()) {
            return emailOptional.get();
        }
        else throw new NotFoundRecordException("Email не найдет");
    }
}