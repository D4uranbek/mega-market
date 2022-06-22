package com.yandex.mega_market.mappers;

import com.yandex.mega_market.DTOs.ShopUnit;
import com.yandex.mega_market.DTOs.ShopUnitImport;
import com.yandex.mega_market.entities.ShopUnitEntity;
import com.yandex.mega_market.entities.enums.ShopUnitType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author D4uranbek
 * @since 22.06.2022
 */
@Component
public class CustomMapper {
    public ShopUnitEntity toShopUnitEntity( ShopUnitImport shopUnitImport ) {
        if ( shopUnitImport == null ) {
            return null;
        } else {
            ShopUnitEntity shopUnitEntity = new ShopUnitEntity();
            shopUnitEntity.setId( shopUnitImport.getId() );
            shopUnitEntity.setName( shopUnitImport.getName() );
            shopUnitEntity.setType( shopUnitImport.getType() );
            shopUnitEntity.setPrice( shopUnitImport.getPrice() );
            return shopUnitEntity;
        }
    }

    public List<ShopUnitEntity> toShopUnitEntityList( List<ShopUnitImport> shopUnitImports ) {
        if ( shopUnitImports == null ) {
            return null;
        } else {
            List<ShopUnitEntity> list = new ArrayList<>( shopUnitImports.size() );

            for ( ShopUnitImport shopUnitImport : shopUnitImports ) {
                list.add( this.toShopUnitEntity( shopUnitImport ) );
            }

            return list;
        }
    }

    public ShopUnit toShopUnit( ShopUnitEntity shopUnitEntity ) {
        if ( shopUnitEntity == null ) {
            return null;
        } else {
            ShopUnit shopUnit = new ShopUnit();
            shopUnit.setParentId( this.getShopUnitEntityParentId( shopUnitEntity ) );
            shopUnit.setDate( shopUnitEntity.getDate() );
            shopUnit.setId( shopUnitEntity.getId() );
            shopUnit.setName( shopUnitEntity.getName() );
            shopUnit.setType( shopUnitEntity.getType() );
            shopUnit.setPrice( shopUnitEntity.getPrice() );
            shopUnit.setChildren( shopUnitEntity.getType() == ShopUnitType.OFFER ? null : this.toShopUnitList( shopUnitEntity.getChildren() ) );
            return shopUnit;
        }
    }

    public List<ShopUnit> toShopUnitList( List<ShopUnitEntity> shopUnitEntities ) {
        if ( shopUnitEntities == null ) {
            return null;
        } else {
            List<ShopUnit> list = new ArrayList<>( shopUnitEntities.size() );

            for ( ShopUnitEntity shopUnitEntity : shopUnitEntities ) {
                list.add( this.toShopUnit( shopUnitEntity ) );
            }

            return list;
        }
    }

    private UUID getShopUnitEntityParentId( ShopUnitEntity shopUnitEntity ) {
        if ( shopUnitEntity == null ) {
            return null;
        } else {
            ShopUnitEntity parent = shopUnitEntity.getParent();
            if ( parent == null ) {
                return null;
            } else {
                return parent.getId();
            }
        }
    }

}
