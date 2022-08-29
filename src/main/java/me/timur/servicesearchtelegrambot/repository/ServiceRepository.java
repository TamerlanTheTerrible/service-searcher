package me.timur.servicesearchtelegrambot.repository;

import me.timur.servicesearchtelegrambot.enitity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Optional<Service> findByIdAndActiveTrue(Long id);

    List<Service> findAllByCategoryName(String categoryName);

    List<Service> findAllByActiveTrue();

    Optional<Service> findByName(String nameUz);

    Optional<Service> findByNameRu(String nameRU);
}