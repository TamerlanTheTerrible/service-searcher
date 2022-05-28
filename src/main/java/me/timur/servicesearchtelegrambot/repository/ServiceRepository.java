package me.timur.servicesearchtelegrambot.repository;

import me.timur.servicesearchtelegrambot.enitity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Optional<Service> findByIdAndActiveTrue(Long id);

    List<Service> findAllByCategoryId(Long categoryId);

    List<Service> findAllByActiveTrue();
}