package com.example.shop.inventory.infrastructure.persistence;

import com.example.shop.inventory.application.port.out.InventoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
public class InventoryConfiguration {

    @Bean
    public InventoryRepository inventoryRepository(WrappedInventoryRepository wrappedInventoryRepository) {
        return new InventoryRepositoryAdapter(wrappedInventoryRepository);
    }
}
