package me.timur.servicesearchtelegrambot.repository;

import me.timur.servicesearchtelegrambot.enitity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    List<Service> findAllByCategoryId(Long categoryId);
}