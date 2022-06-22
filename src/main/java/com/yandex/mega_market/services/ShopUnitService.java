package com.yandex.mega_market.services;

import com.yandex.mega_market.DTOs.ShopUnit;
import com.yandex.mega_market.DTOs.ShopUnitImportRequest;
import com.yandex.mega_market.entities.ShopUnitEntity;
import com.yandex.mega_market.exceptions.NotFoundException;
import com.yandex.mega_market.mappers.CustomMapper;
import com.yandex.mega_market.repositories.ShopUnitRepository;
import com.yandex.mega_market.validators.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author D4uranbek
 * @since 20.06.2022
 */
@Service
@RequiredArgsConstructor
public class ShopUnitService {

    private final ShopUnitRepository repository;

    private final CustomMapper customMapper;

    private final RequestValidator validator;


    public void importShopUnits( ShopUnitImportRequest shopUnitImportRequest ) {

        validator.validateShopUnitImportRequest( shopUnitImportRequest );
        List<ShopUnitEntity> shopUnits = customMapper.toShopUnitEntityList( shopUnitImportRequest );
        repository.saveAll( shopUnits );

    }

    public void deleteShopUnit( UUID id ) {
        ShopUnitEntity shopUnitEntity = repository.findById( id ).orElseThrow( NotFoundException::new );
        deleteAll( shopUnitEntity );

    }

    private void deleteAll( ShopUnitEntity shopUnit ) {
        List<ShopUnitEntity> shopUnitList = shopUnit.getChildren();
        for ( ShopUnitEntity currentUnit : shopUnitList ) {
            deleteAll( currentUnit );
        }
        repository.delete( shopUnit );
    }

    public ShopUnit getElementById( UUID id ) {
        ShopUnitEntity shopUnitEntity = repository.findById( id ).orElseThrow( NotFoundException::new );
        return customMapper.toShopUnit( shopUnitEntity );
    }

}
