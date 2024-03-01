package com.example.bank.repository;

import com.example.bank.model.Appuser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<Appuser, Long> {

    Optional<Appuser> findAppuserByUsername(String username);

    List<Appuser> findByBirthdayAfter(LocalDate localDate);

    @Query("from Appuser a where lower(concat(a.lastName,' ',a.firstName,' ',a.middleName)) like (concat(:name,'%'))")
    List<Appuser> findByName(@Param("name") String name);
}
