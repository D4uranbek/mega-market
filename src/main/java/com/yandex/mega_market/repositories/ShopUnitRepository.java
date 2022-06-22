package com.yandex.mega_market.repositories;

import com.yandex.mega_market.entities.ShopUnitEntity;
import com.yandex.mega_market.entities.enums.ShopUnitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author D4uranbek
 * @since 20.06.2022
 */
@Repository
@Transactional
public interface ShopUnitRepository extends JpaRepository<ShopUnitEntity, UUID> {

    List<ShopUnitEntity> findAllByType( ShopUnitType type );

    ShopUnitEntity findByIdAndType( UUID id, ShopUnitType type );

}
