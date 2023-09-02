package com.foocmend.repositories;


import com.foocmend.entities.Local;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalRepository extends JpaRepository<Local, String> {
}
