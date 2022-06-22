package com.yandex.mega_market.mappers;

import com.yandex.mega_market.DTOs.ShopUnit;
import com.yandex.mega_market.DTOs.ShopUnitImport;
import com.yandex.mega_market.DTOs.ShopUnitImportRequest;
import com.yandex.mega_market.DTOs.TransferInteger;
import com.yandex.mega_market.entities.ShopUnitEntity;
import com.yandex.mega_market.entities.enums.ShopUnitType;
import com.yandex.mega_market.services.HelperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author D4uranbek
 * @since 22.06.2022
 */
@Component
@RequiredArgsConstructor
public class CustomMapper {

    private final HelperService helperService;

    public ShopUnitEntity toShopUnitEntity( ShopUnitImport shopUnitImport, LocalDateTime dateFromShopUnitRequest ) {
        if ( Objects.isNull( shopUnitImport ) ) {
            return null;
        } else {
            ShopUnitEntity shopUnitEntity = new ShopUnitEntity();
            UUID id = shopUnitImport.getId();
            String name = shopUnitImport.getName();
            Long price = shopUnitImport.getPrice();
            ShopUnitType type = shopUnitImport.getType();

            shopUnitEntity.setId( id );
            shopUnitEntity.setName( name );
            shopUnitEntity.setDate( dateFromShopUnitRequest );
            shopUnitEntity.setType( type );
            shopUnitEntity.setPrice( price );

            if ( ShopUnitType.OFFER.equals( type ) ) {
                ShopUnitEntity unitFromDb = helperService.findByIdAndType( id, ShopUnitType.OFFER );
                if ( Objects.nonNull( unitFromDb ) && unitFromDb.getPrice().intValue() != price.intValue() ) {
                    shopUnitEntity.setLastPriceUpdatedTime( dateFromShopUnitRequest );
                } else if ( Objects.isNull( unitFromDb ) ) {
                    shopUnitEntity.setLastPriceUpdatedTime( dateFromShopUnitRequest );
                } else {
                    shopUnitEntity.setLastPriceUpdatedTime( unitFromDb.getLastPriceUpdatedTime() );
                }
            }
            return shopUnitEntity;
        }
    }

    public List<ShopUnitEntity> toShopUnitEntityList( ShopUnitImportRequest shopUnitImportRequest ) {
        List<ShopUnitEntity> shopUnitList = new ArrayList<>();
        List<ShopUnitImport> shopUnitImportList = shopUnitImportRequest.getItems();

        for ( ShopUnitImport shopUnitImport : shopUnitImportList ) {
            ShopUnitEntity shopUnitEntity = toShopUnitEntity( shopUnitImport, shopUnitImportRequest.getUpdateDate() );
            helperService.save( shopUnitEntity );
            shopUnitList.add( shopUnitEntity );
        }

        addParentsToUnits( shopUnitImportList, shopUnitList );
        addAllParentCategoriesToShopUnitList( shopUnitList, new ArrayList<>( shopUnitList ) );
        updateDateForAlLCategories( shopUnitList, shopUnitImportRequest.getUpdateDate() );
        setPriceToAllCategories( shopUnitList );
        setPriceNullToAllCategoriesWithNoChildren();

        return shopUnitList;
    }

    public ShopUnit toShopUnit( ShopUnitEntity shopUnitEntity ) {
        if ( Objects.isNull( shopUnitEntity ) ) {
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
        if ( Objects.isNull( shopUnitEntities ) ) {
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
        if ( Objects.isNull( shopUnitEntity ) ) {
            return null;
        } else {
            ShopUnitEntity parent = shopUnitEntity.getParent();
            if ( Objects.isNull( parent ) ) {
                return null;
            } else {
                return parent.getId();
            }
        }
    }

    private void addParentsToUnits( List<ShopUnitImport> shopUnitImportList, List<ShopUnitEntity> shopUnitList ) {
        for ( int i = 0; i < shopUnitImportList.size(); i++ ) {
            ShopUnitImport currentImportUnit = shopUnitImportList.get( i );

            ShopUnitEntity parent = shopUnitList.stream()
                    .filter( shopUnitEntity -> shopUnitEntity.getId().equals( currentImportUnit.getParentId() ) )
                    .filter( shopUnitEntity -> shopUnitEntity.getType() == ShopUnitType.CATEGORY )
                    .findAny().orElse( null );

            ShopUnitEntity parentFromDb = helperService.findByIdAndType( currentImportUnit.getParentId(),
                    ShopUnitType.CATEGORY );

            ShopUnitEntity currentUnit = shopUnitList.get( i );

            if ( Objects.isNull( parent ) && Objects.nonNull( parentFromDb ) && !shopUnitList.contains( parentFromDb ) ) {
                currentUnit.setParent( parentFromDb );
            } else {
                currentUnit.setParent( parent );
            }
        }
    }

    private void addAllParentCategoriesToShopUnitList( List<ShopUnitEntity> shopUnitList, List<ShopUnitEntity> copyOfOriginalList ) {
        for ( ShopUnitEntity shopUnit : copyOfOriginalList ) {
            while ( true ) {
                if ( Objects.nonNull( shopUnit.getParent() ) && !shopUnitList.contains( shopUnit.getParent() ) ) {
                    ShopUnitEntity parent = helperService.findById( shopUnit.getParent().getId() ).orElse( null );

                    shopUnitList.add( parent );
                    shopUnit = shopUnit.getParent();
                } else {
                    break;
                }
            }
        }
    }

    private void updateDateForAlLCategories( List<ShopUnitEntity> shopUnitList, LocalDateTime updateDate ) {
        shopUnitList.stream()
                .filter( shopUnitEntity -> shopUnitEntity.getType() == ShopUnitType.CATEGORY )
                .filter( shopUnitEntity -> shopUnitEntity.getDate() != updateDate ).
                forEach( shopUnitEntity -> shopUnitEntity.setDate( updateDate ) );
    }

    private void setPriceToAllCategories( List<ShopUnitEntity> shopUnitList ) {
        for ( ShopUnitEntity currentShopUnit : shopUnitList ) {
            if ( currentShopUnit.getType() == ShopUnitType.CATEGORY ) {

                TransferInteger transferInteger = calculatePriceForCategory( currentShopUnit );
                if ( transferInteger.getOfferCount() == 0 ) {
                    currentShopUnit.setPrice( null );
                } else {
                    long avgCategoryPrice = transferInteger.getOfferTotalPrice() / transferInteger.getOfferCount();
                    currentShopUnit.setPrice( avgCategoryPrice );
                }
            }
        }
    }

    private TransferInteger calculatePriceForCategory( ShopUnitEntity currentShopUnit ) {
        int offersSumPrice = 0;
        int offersCount = 0;
        if ( currentShopUnit.getChildren().size() > 0 ) {
            for ( ShopUnitEntity currentUnit : currentShopUnit.getChildren() ) {
                if ( currentUnit.getType() == ShopUnitType.OFFER ) {
                    offersSumPrice += currentUnit.getPrice();
                    offersCount += 1;
                }
                TransferInteger transferInteger = calculatePriceForCategory( currentUnit );
                offersSumPrice += transferInteger.getOfferTotalPrice();
                offersCount += transferInteger.getOfferCount();
            }
        }
        return new TransferInteger( offersSumPrice, offersCount );
    }

    private void setPriceNullToAllCategoriesWithNoChildren() {
        List<ShopUnitEntity> shopUnitList = helperService.findAllByType( ShopUnitType.CATEGORY );
        for ( ShopUnitEntity shopUnit : shopUnitList ) {
            if ( shopUnit.getType() == ShopUnitType.CATEGORY && shopUnit.getChildren().isEmpty() ) {
                shopUnit.setPrice( null );
            }
        }
    }

}
