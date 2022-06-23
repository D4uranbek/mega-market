package com.yandex.mega_market.validators;

import com.yandex.mega_market.DTOs.ShopUnitImport;
import com.yandex.mega_market.DTOs.ShopUnitImportRequest;
import com.yandex.mega_market.entities.enums.ShopUnitType;
import com.yandex.mega_market.exceptions.ValidationException;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author D4uranbek
 * @since 22.06.2022
 */
@Component
public class RequestValidator {
    public void validateShopUnitImportRequest( ShopUnitImportRequest shopUnitImportRequest ) {
        List<ShopUnitImport> items = shopUnitImportRequest.getItems();
        if ( Objects.isNull( items ) || items.isEmpty()
                || shopUnitImportRequest.getUpdateDate() == null ) {
            throw new ValidationException();
        }

        checkForDuplicate( items );

        items.forEach( this::validateShopUnitImport );
    }

    private void checkForDuplicate( List<ShopUnitImport> items ) {
        // в одном запросе не может быть двух элементов с одинаковым id
        Set<UUID> uuids = new HashSet<>();
        for ( ShopUnitImport item : items ) {
            if ( !uuids.add( item.getId() ) )
                throw new ValidationException();
        }
    }

    public void validateShopUnitImport( ShopUnitImport shopUnitImport ) {
        if ( shopUnitImport.getName().isBlank()
                // у категорий поле price должно содержать null
                || ShopUnitType.CATEGORY.equals( shopUnitImport.getType() ) && Objects.nonNull( shopUnitImport.getPrice() )
                // цена товара не может быть null и должна быть больше либо равна нулю
                || ShopUnitType.OFFER.equals( shopUnitImport.getType() )
                && ( Objects.isNull( shopUnitImport.getPrice() ) || shopUnitImport.getPrice() < 0 )
        ) {
            throw new ValidationException();
        }
    }
}
