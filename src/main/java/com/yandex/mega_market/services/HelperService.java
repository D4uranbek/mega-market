package com.yandex.mega_market.services;

import com.yandex.mega_market.entities.ShopUnitEntity;
import com.yandex.mega_market.entities.enums.ShopUnitType;
import com.yandex.mega_market.repositories.ShopUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author D4uranbek
 * @since 22.06.2022
 */
@Service
@RequiredArgsConstructor
public class HelperService {

    private final ShopUnitRepository repository;

    public List<ShopUnitEntity> findAllByType( ShopUnitType type ) {
        return repository.findAllByType( type );
    }

    public ShopUnitEntity findByIdAndType( UUID id, ShopUnitType type ) {
        return repository.findByIdAndType( id, type );
    }

    public Optional<ShopUnitEntity> findById( UUID id ) {
        return repository.findById( id );
    }

    public void save( ShopUnitEntity shopUnitEntity ) {
        repository.save( shopUnitEntity );
    }
}
