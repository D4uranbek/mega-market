package com.yandex.mega_market.services;

import com.yandex.mega_market.DTOs.*;
import com.yandex.mega_market.entities.ShopUnitEntity;
import com.yandex.mega_market.entities.enums.ShopUnitType;
import com.yandex.mega_market.exceptions.NotFoundException;
import com.yandex.mega_market.mappers.CustomMapper;
import com.yandex.mega_market.repositories.ShopUnitRepository;
import com.yandex.mega_market.validators.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

//        List<ShopUnitEntity> shopUnits = mapper.toShopUnitEntityList( shopUnitImportRequest.getItems() );

        List<ShopUnitEntity> shopUnitList = new ArrayList<>();
        List<ShopUnitImport> shopUnitImportList = shopUnitImportRequest.getItems();
        for ( ShopUnitImport shopUnitImport : shopUnitImportList ) {
            ShopUnitEntity toSaveShopUnit = new ShopUnitEntity();

            assignParametersFromImportObjectToShopUnit( toSaveShopUnit, shopUnitImport, shopUnitImportRequest.getUpdateDate() );

            repository.save( toSaveShopUnit );
            shopUnitList.add( toSaveShopUnit );
        }

        addParentsToUnits( shopUnitImportList, shopUnitList );

        addAllParentCategoriesToShopUnitList( shopUnitList, new ArrayList<>( shopUnitList ) );
        updateDateForAlLCategories( shopUnitList, shopUnitImportRequest.getUpdateDate() );

        setPriceToAllCategories( shopUnitList );

        setPriceNullToAllCategoriesWithNoChildren();

        repository.saveAll( shopUnitList );

        /*
        Date updateDate = shopUnitImportRequest.getUpdateDate();
        List<ShopUnitImport> items = shopUnitImportRequest.getItems();
        // в одном запросе не может быть двух элементов с одинаковым id
        Set<UUID> uuids = new HashSet<>();
        for ( ShopUnitImport item : items ) {
            if ( !uuids.add( item.getId() ) ) {
                throw new ValidationException();
            }
        }

        List<ShopUnitEntity> shopUnits = mapper.toShopUnitEntityList( items );

        for ( ShopUnitEntity shopUnit : shopUnits ) {
            // родителем товара или категории может быть только категория
            if ( Objects.nonNull( shopUnit.getParentId() ) ) {
                repository.findById( shopUnit.getParentId() ).ifPresent( shopUnitEntity -> {
                    if ( !ShopUnitType.CATEGORY.equals( shopUnitEntity.getType() ) ) {
                        throw new ValidationException();
                    }
                } );
            }

            // у категорий поле price должно содержать null
            if ( ShopUnitType.CATEGORY.equals( shopUnit.getType() ) && Objects.nonNull( shopUnit.getPrice() ) ) {
                throw new ValidationException();
            }


            // цена товара не может быть null и должна быть больше либо равна нулю
            if ( ShopUnitType.OFFER.equals( shopUnit.getType() ) ) {
                if ( Objects.isNull( shopUnit.getPrice() ) || shopUnit.getPrice() < 0 ) {
                    throw new ValidationException();
                }

                if ( Objects.nonNull( shopUnit.getParentId() ) ) {
                    repository.findById( shopUnit.getId() ).ifPresent( shopUnitEntity -> {
                        if ( !Objects.equals( shopUnitEntity.getPrice(), shopUnit.getPrice() ) ) {
                            // update price of parents

                        }
                    } );
                }
            }

            // при обновлении параметров элемента обязательно обновляется поле date в соответствии с временем обновления
            shopUnit.setDate( updateDate );
        }*/

        // при обновлении товара/категории обновленными считаются все их параметры

    }

    public void deleteShopUnit( UUID id ) {
        // Категория/товар не найден.
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


    private void setPriceNullToAllCategoriesWithNoChildren() {
        List<ShopUnitEntity> shopUnitList = repository.findAllByType( ShopUnitType.CATEGORY );
        for ( ShopUnitEntity shopUnit : shopUnitList ) {
            if ( shopUnit.getType() == ShopUnitType.CATEGORY && shopUnit.getChildren().size() == 0 ) {
                shopUnit.setPrice( null );
            }
        }
    }

    private void updateDateForAlLCategories( List<ShopUnitEntity> shopUnitList, LocalDateTime updateDate ) {
        shopUnitList.stream()
                .filter( ( x ) -> x.getType() == ShopUnitType.CATEGORY )
                .filter( ( x ) -> x.getDate() != updateDate ).
                forEach( ( x ) -> x.setDate( updateDate ) );
    }

    private void addParentsToUnits( List<ShopUnitImport> shopUnitImportList, List<ShopUnitEntity> shopUnitList ) {
        for ( int i = 0; i < shopUnitImportList.size(); i++ ) {
            ShopUnitImport currentImportUnit = shopUnitImportList.get( i );

            ShopUnitEntity parent = shopUnitList.stream()
                    .filter( ( x ) -> x.getId().equals( currentImportUnit.getParentId() ) )
                    .filter( ( x ) -> x.getType() == ShopUnitType.CATEGORY )
                    .findFirst().orElse( null );

            ShopUnitEntity parentFromDb = repository.findByIdAndType(
                    currentImportUnit.getParentId(),
                    ShopUnitType.CATEGORY );

            ShopUnitEntity currentUnit = shopUnitList.get( i );

            if ( parent == null && parentFromDb != null && !shopUnitList.contains( parentFromDb ) ) {
                currentUnit.setParent( parentFromDb );
            } else {
                currentUnit.setParent( parent );
            }
        }
    }

    private void assignParametersFromImportObjectToShopUnit( ShopUnitEntity shopUnit, ShopUnitImport shopUnitImport,
                                                             LocalDateTime dateFromShopUnitRequest ) {
        // к моменту выполнения этого метода все поля будет провалидированы
        UUID id = shopUnitImport.getId();
        String name = shopUnitImport.getName();
        Long price = shopUnitImport.getPrice();
        ShopUnitType type = shopUnitImport.getType();


        shopUnit.setId( id );
        shopUnit.setName( name );
        shopUnit.setDate( dateFromShopUnitRequest );
        shopUnit.setType( type );
        shopUnit.setPrice( price );

        if ( type == ShopUnitType.OFFER ) {
            ShopUnitEntity unitFromDb = repository.findByIdAndType( id, ShopUnitType.OFFER );
            if ( unitFromDb != null && unitFromDb.getPrice().intValue() != price.intValue() ) {
                shopUnit.setLastPriceUpdatedTime( dateFromShopUnitRequest );
            } else if ( unitFromDb == null ) {
                shopUnit.setLastPriceUpdatedTime( dateFromShopUnitRequest );
            } else {
                shopUnit.setLastPriceUpdatedTime( unitFromDb.getLastPriceUpdatedTime() );
            }
        }
        // поле parent инициализируется в отдельном методе
    }

    private ShopUnitStatisticUnit transformShopUnitToStatisticUnit( ShopUnitEntity shopUnit ) {
        ShopUnitStatisticUnit statisticUnit = new ShopUnitStatisticUnit();
        statisticUnit.setId( shopUnit.getId() );
        statisticUnit.setName( shopUnit.getName() );
        if ( shopUnit.getParent() != null ) {
            statisticUnit.setParentId( shopUnit.getParent().getId() );
        } else {
            statisticUnit.setParentId( null );
        }
        statisticUnit.setType( shopUnit.getType() );
        statisticUnit.setDate( shopUnit.getDate() );
        if ( shopUnit.getPrice() == null ) {
            statisticUnit.setPrice( null );
        } else {
            statisticUnit.setPrice( shopUnit.getPrice() );
        }
        if ( shopUnit.getType() == ShopUnitType.OFFER ) {
            statisticUnit.setChildren( null );
        }

        return statisticUnit;
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

    private void addAllParentCategoriesToShopUnitList( List<ShopUnitEntity> shopUnitList, List<ShopUnitEntity> copyOfOriginalList ) {
        for ( ShopUnitEntity shopUnit : copyOfOriginalList ) {
            while ( true ) {
                if ( shopUnit.getParent() != null && !shopUnitList.contains( shopUnit.getParent() ) ) {
                    ShopUnitEntity parent = repository.findById( shopUnit.getParent().getId() ).orElse( null );

                    shopUnitList.add( parent );
                    shopUnit = shopUnit.getParent();
                } else {
                    break;
                }
            }
        }
    }
}
