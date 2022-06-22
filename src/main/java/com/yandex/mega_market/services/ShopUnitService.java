package com.yandex.mega_market.services;

import com.yandex.mega_market.DTOs.ShopUnit;
import com.yandex.mega_market.DTOs.ShopUnitImportRequest;
import com.yandex.mega_market.DTOs.ShopUnitStatisticResponse;
import com.yandex.mega_market.entities.ShopUnitEntity;
import com.yandex.mega_market.entities.enums.ShopUnitType;
import com.yandex.mega_market.exceptions.NotFoundException;
import com.yandex.mega_market.mappers.CustomMapper;
import com.yandex.mega_market.repositories.ShopUnitRepository;
import com.yandex.mega_market.validators.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
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

    public ShopUnitStatisticResponse getShopUnitChangedPriceLastDay( LocalDateTime date ) {

        List<ShopUnitEntity> shopUnitListOffers = repository.findAllByType( ShopUnitType.OFFER );

        List<ShopUnitEntity> shopUnitEntities = new ArrayList<>();

        for ( ShopUnitEntity current : shopUnitListOffers ) {
            long currentUnitDateTimeInSeconds = shopUnitListOffers.get( 0 )
                    .getLastPriceUpdatedTime().toInstant( ZoneOffset.UTC ).getEpochSecond();

            long result = date.toInstant( ZoneOffset.UTC ).getEpochSecond() - currentUnitDateTimeInSeconds;
            if ( result < 86400 && result > 0 ) {
                shopUnitEntities.add( current );
            }
        }

        List<ShopUnit> shopUnits = new ArrayList<>();
        for ( ShopUnitEntity shopUnitEntity : shopUnitEntities ) {
            ShopUnit toShopUnit = customMapper.toShopUnit( shopUnitEntity );
            shopUnits.add( toShopUnit );
        }
//        List<ShopUnit> list = customMapper.toShopUnitList( shopUnitEntities );

        ShopUnitStatisticResponse statisticResponse = new ShopUnitStatisticResponse();
        statisticResponse.setItems( shopUnits );

        return statisticResponse;
    }
}
