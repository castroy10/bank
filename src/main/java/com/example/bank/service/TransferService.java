package com.example.bank.service;

import com.example.bank.dto.transfer.RequestTransferDto;
import com.example.bank.exception.NotEnoughMoneyException;
import com.example.bank.model.Appuser;
import com.example.bank.repository.AppUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class TransferService {
    private final AppUserService appUserService;
    private final AppUserRepository appUserRepository;
    private final ReentrantLock lock = new ReentrantLock();

    public TransferService(AppUserService appUserService, AppUserRepository appUserRepository) {
        this.appUserService = appUserService;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public ResponseEntity<?> transfer(RequestTransferDto requestTransferDto, HttpServletRequest httpServletRequest) throws NotEnoughMoneyException {
        lock.lock();
        try {
            Appuser userSender = appUserService.getAppuser(httpServletRequest.getRemoteUser());
            Appuser userRecipient = appUserService.getAppuser(requestTransferDto.getUsername());
            BigDecimal sum = requestTransferDto.getSum();
            if (sum.compareTo(userSender.getBalance()) < 0) {
                userSender.setBalance(userSender.getBalance().subtract(sum));
                userRecipient.setBalance(userRecipient.getBalance().add(sum));
            } else throw new NotEnoughMoneyException("Недостаточно денег. Not enough money");
            appUserRepository.save(userSender);
            appUserRepository.save(userRecipient);
            log.info("Деньги переведены,от пользователя с id={} к пользователю с id={}", userSender.getId(), userRecipient.getId());
            return ResponseEntity.ok().body(Map.of("Сообщение:", "Деньги переведены для пользователя с id=" + userRecipient.getId()));
        } finally {
            lock.unlock();
        }
    }

    @Scheduled(fixedRate = 60000)
    protected void incrementBalance() {
        List<Appuser> appuserList = appUserRepository.findAll();
        List<Appuser> resultList = new ArrayList<>();
        for (Appuser appuser : appuserList) {
            if (appuser.getBalance().compareTo(appuser.getFirstBalance().multiply(new BigDecimal("2.07"))) < 0) {
                appuser.setBalance(appuser.getBalance().add(appuser.getBalance().multiply(new BigDecimal("0.05"))));
                resultList.add(appuser);
            }
        }
        if (!resultList.isEmpty()) appUserRepository.saveAll(resultList);
    }
}
