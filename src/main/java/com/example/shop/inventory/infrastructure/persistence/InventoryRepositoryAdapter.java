package com.example.shop.inventory.infrastructure.persistence;

import com.example.shop.inventory.application.port.out.InventoryRepository;
import com.example.shop.inventory.domain.InventoryOrderStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class InventoryRepositoryAdapter implements InventoryRepository {
    private final WrappedInventoryRepository wrappedJpaRepo;

    @Override
    @Transactional
    public void cancelItemByOrderId(Long orderI) {
        wrappedJpaRepo.findByInventoryOrderId(orderI)
                .forEach(itemEntity -> cancelOrderedItem(itemEntity, orderI));
    }

    private void cancelOrderedItem(ItemEntity itemEntity, Long orderId) {
        itemEntity.getOrderItems().stream()
                .filter(item -> item.getOrderId().equals(orderId) && item.getStatus().equals(InventoryOrderStatus.CREATED))
                .forEach(item -> {
                    itemEntity.setAvailableQuantity(itemEntity.getAvailableQuantity()+item.getQuantity());
                    item.setStatus(InventoryOrderStatus.CANCELLED);
                });
        wrappedJpaRepo.save(itemEntity);
    }
}

interface WrappedInventoryRepository extends JpaRepository<ItemEntity, String> {

    @Query("select i from ItemEntity i where i.orderItems.orderId = :orderId ")
    List<ItemEntity> findByInventoryOrderId(@Param("orderId") Long orderId);
}
