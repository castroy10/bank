package com.example.bank.config;

import com.example.bank.dto.appuser.AppUserRequestRegisterDto;
import com.example.bank.model.Appuser;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableScheduling
public class ModelMapperConfig {

    private final PasswordEncoder passwordEncoder;

    public ModelMapperConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.addConverter(stringToLocalDate);

        modelMapper.typeMap(AppUserRequestRegisterDto.class, Appuser.class)
                .addMapping(AppUserRequestRegisterDto::getBalance, Appuser::setBalance)
                .addMapping(AppUserRequestRegisterDto::getBalance, Appuser::setFirstBalance)
                .addMappings(mapper -> mapper.skip(Appuser::setPhones))
                .addMappings(mapper -> mapper.skip(Appuser::setEmails))
                .addMappings(mapper -> mapper.using(stringToBcrypt).map(AppUserRequestRegisterDto::getPassword, Appuser::setPassword));

        return modelMapper;
    }

    private final AbstractConverter<String, LocalDate> stringToLocalDate = new AbstractConverter<>() {
        @Override
        protected LocalDate convert(String source) {
            return LocalDate.parse(source, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }
    };

    private final AbstractConverter<String, String> stringToBcrypt = new AbstractConverter<>() {
        @Override
        protected String convert(String string) {
            return passwordEncoder.encode(string);
        }
    };
}
