package com.yandex.mega_market.mappers;

import com.yandex.mega_market.DTOs.ShopUnit;
import com.yandex.mega_market.DTOs.ShopUnitImport;
import com.yandex.mega_market.entities.ShopUnitEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author D4uranbek
 * @since 20.06.2022
 */
@Component
@Mapper( componentModel = "spring" )
public interface ShopUnitMapper {

    ShopUnitEntity toShopUnitEntity( ShopUnitImport shopUnitImport );


    List<ShopUnitEntity> toShopUnitEntityList( List<ShopUnitImport> shopUnitImports );

    @Mapping( target = "parentId", source = "shopUnitEntity.parent.id" )
    ShopUnit toShopUnit( ShopUnitEntity shopUnitEntity );

    List<ShopUnit> toShopUnitList( List<ShopUnitEntity> shopUnitEntities );

}
