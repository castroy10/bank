package com.example.bank.service;

import com.example.bank.controller.TransferController;
import com.example.bank.dto.transfer.RequestTransferDto;
import com.example.bank.exception.NotEnoughMoneyException;
import com.example.bank.model.Appuser;
import com.example.bank.repository.AppUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
public class TransferServiceTest {

    @MockBean
    private final AppUserRepository appUserRepository;
    private final TransferController transferController;
    private final WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    private final Appuser userSender = new Appuser();
    private final Appuser userReceiver = new Appuser();

    @Autowired
    public TransferServiceTest(AppUserRepository appUserRepository, TransferController transferController, WebApplicationContext webApplicationContext) {
        this.appUserRepository = appUserRepository;
        this.transferController = transferController;
        this.webApplicationContext = webApplicationContext;
    }

    @BeforeEach
    public void init() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        userSender.setId(998L);
        userReceiver.setId(999L);
        userSender.setBalance(new BigDecimal("1000.0"));
        userReceiver.setBalance(new BigDecimal("1000.0"));
    }

    @Test
    public void testTransferSuccess() throws NotEnoughMoneyException {
        RequestTransferDto requestTransferDto = new RequestTransferDto();
        requestTransferDto.setUsername("admin3");
        requestTransferDto.setSum(new BigDecimal("100.0"));
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(appUserRepository.findAppuserByUsername("user")).thenReturn(Optional.of(userSender));
        Mockito.when(appUserRepository.findAppuserByUsername("admin3")).thenReturn(Optional.of(userReceiver));
        Mockito.when(request.getRemoteUser()).thenReturn("user");

        ResponseEntity<?> response = transferController.transfer(requestTransferDto, request);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody().toString().contains("Деньги переведены для пользователя с id=999"));
    }

    @Test
    public void testTransferException() throws Exception {
        Mockito.when(appUserRepository.findAppuserByUsername(Mockito.anyString())).thenThrow(new UsernameNotFoundException("Test success"));

        mockMvc.perform(post("/api/v1/transfer")
                        .with(request -> {
                            request.setRemoteUser("user");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"AAA\",\"sum\": 10}"))
                .andExpect(status().is(400))
                .andExpect(result -> {
                    String errorMessage = result.getResponse().getContentAsString();
                    Assertions.assertTrue(errorMessage.contains("Test success"));
                });
    }

    @Test
    public void testTransferNotEnoughMoney() throws Exception {
        Mockito.when(appUserRepository.findAppuserByUsername("user")).thenReturn(Optional.of(userSender));
        Mockito.when(appUserRepository.findAppuserByUsername("admin3")).thenReturn(Optional.of(userReceiver));

        mockMvc.perform(post("/api/v1/transfer")
                        .with(request -> {
                            request.setRemoteUser("user");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"admin3\",\"sum\": 10000}"))
                .andExpect(status().is(400))
                .andExpect(result -> {
                    String errorMessage = result.getResponse().getContentAsString();
                    System.out.println(errorMessage);
                    Assertions.assertTrue(errorMessage.contains("Not enough money"));
                });
    }
}
