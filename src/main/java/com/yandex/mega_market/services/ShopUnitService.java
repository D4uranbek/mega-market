package com.yandex.mega_market.services;

import com.yandex.mega_market.DTOs.ShopUnit;
import com.yandex.mega_market.DTOs.ShopUnitImport;
import com.yandex.mega_market.DTOs.ShopUnitImportRequest;
import com.yandex.mega_market.DTOs.ShopUnitType;
import com.yandex.mega_market.entities.ShopUnitEntity;
import com.yandex.mega_market.exceptions.NotFoundException;
import com.yandex.mega_market.exceptions.ValidationException;
import com.yandex.mega_market.mappers.ShopUnitMapper;
import com.yandex.mega_market.repositories.ShopUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * @author D4uranbek
 * @since 20.06.2022
 */
@Service
@RequiredArgsConstructor
public class ShopUnitService {

    private final ShopUnitRepository repository;
    private final ShopUnitMapper mapper;

    public void importShopUnits( ShopUnitImportRequest shopUnitImportRequest ) {

        List<ShopUnitImport> items = shopUnitImportRequest.getItems();
        // в одном запросе не может быть двух элементов с одинаковым id
        Set<UUID> uuids = new HashSet<>();
        for ( ShopUnitImport item : items ) {
            if ( !uuids.add( item.getId() ) )
                throw new ValidationException();
        }

        Date updateDate = shopUnitImportRequest.getUpdateDate();

        List<ShopUnitEntity> shopUnits = mapper.toShopUnitEntityList( items );

        for ( ShopUnitEntity shopUnit : shopUnits ) {
            // родителем товара или категории может быть только категория
            repository.findById( shopUnit.getParentId() ).ifPresent( shopUnitEntity -> {
                if ( !ShopUnitType.CATEGORY.equals( shopUnitEntity.getType() ) ) {
                    throw new ValidationException();
                }
            } );

            // у категорий поле price должно содержать null
            if ( ShopUnitType.CATEGORY.equals( shopUnit.getType() ) && Objects.nonNull( shopUnit.getPrice() ) ) {
                throw new ValidationException();
            }
            // цена товара не может быть null и должна быть больше либо равна нулю
            if ( ShopUnitType.OFFER.equals( shopUnit.getType() ) ) {
                if ( Objects.isNull( shopUnit.getPrice() ) || shopUnit.getPrice() < 0 ) {
                    throw new ValidationException();
                }
            }

            // при обновлении параметров элемента обязательно обновляется поле date в соответствии с временем обновления
            shopUnit.setDate( updateDate );
        }

        // при обновлении товара/категории обновленными считаются все их параметры
        repository.saveAll( shopUnits );

    }

    public void deleteShopUnit( UUID id ) {
        // Категория/товар не найден.
        ShopUnitEntity shopUnitEntity = repository.findById( id ).orElseThrow( NotFoundException::new );

        deleteRecursively( shopUnitEntity );

    }

    private void deleteRecursively( ShopUnitEntity shopUnit ) {

        repository.deleteById( shopUnit.getId() );

        if ( ShopUnitType.CATEGORY.equals( shopUnit.getType() ) ) {
            List<ShopUnitEntity> children = repository.findByParentId( shopUnit.getId() );

            for ( ShopUnitEntity child : children ) {
                deleteRecursively( child );
            }
        }

    }

    // цена категории - это средняя цена всех её товаров, включая товары дочерних категорий.
    // Если категория не содержит товаров цена равна null.
    // При обновлении цены товара, средняя цена категории, которая содержит этот товар, тоже обновляется.
    public ShopUnit getElementById( UUID id ) {

        ShopUnitEntity shopUnitEntity = repository.findById( id ).orElseThrow( NotFoundException::new );

        return getRecursively( shopUnitEntity );

    }

    private ShopUnit getRecursively( ShopUnitEntity shopUnitEntity ) {

        ShopUnit shopUnit = mapper.toShopUnit( shopUnitEntity );

        // для пустой категории поле children равно пустому массиву, а для товара равно null
        if ( ShopUnitType.OFFER.equals( shopUnit.getType() ) ) {
            shopUnit.setChildren( null );
            return shopUnit;
        } else /*( ShopUnitType.CATEGORY.equals( shopUnit.getType() ) )*/ {
            List<ShopUnitEntity> children = repository.findByParentId( shopUnitEntity.getId() );
            if ( children.isEmpty() ) {
                shopUnit.setChildren( List.of() );
                shopUnit.setPrice( null );
            } else {
                List<ShopUnit> shopUnitChildren = new ArrayList<>();
                for ( ShopUnitEntity child : children ) {
                    ShopUnit recursively = getRecursively( child );
                    shopUnitChildren.add( recursively );
                }
                shopUnit.setChildren( shopUnitChildren );
            }
        }

        return shopUnit;
    }
}
