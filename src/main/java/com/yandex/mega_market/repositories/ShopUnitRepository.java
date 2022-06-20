package com.yandex.mega_market.repositories;

import com.yandex.mega_market.entities.ShopUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author D4uranbek
 * @since 20.06.2022
 */
@Repository
public interface ShopUnitRepository extends JpaRepository<ShopUnitEntity, Long> {
}
