package com.yandex.mega_market.validators;

import com.yandex.mega_market.DTOs.ShopUnitImport;
import com.yandex.mega_market.DTOs.ShopUnitImportRequest;
import com.yandex.mega_market.entities.enums.ShopUnitType;
import com.yandex.mega_market.exceptions.*;
import org.springframework.stereotype.Component;

/**
 * @author D4uranbek
 * @since 22.06.2022
 */
@Component
public class RequestValidator {
    public void validateShopUnitImportRequest( ShopUnitImportRequest shopUnitImportRequest ) {
        if ( shopUnitImportRequest.getItems().size() < 1 || shopUnitImportRequest.getUpdateDate() == null ) {
            throw new ValidationException();
        }
        shopUnitImportRequest.getItems().forEach( this::validateShopUnitImport );
    }

    public void validateShopUnitImport( ShopUnitImport shopUnitImport ) {
        if ( shopUnitImport.getId() == null ) {
            throw new ValidationException();
        }
        if ( shopUnitImport.getName() == null
                || shopUnitImport.getName().equals( "" )
                || shopUnitImport.getName().trim().length() == 0 ) {
            throw new ValidationException();
        }
        if ( shopUnitImport.getType() == ShopUnitType.OFFER && shopUnitImport.getPrice() == null ) {
            throw new ValidationException();
        }
        if ( shopUnitImport.getPrice() != null && shopUnitImport.getPrice() < 0 ) {
            throw new ValidationException();
        }
        if ( shopUnitImport.getType() == null ) {
            throw new ValidationException();
        }
    }
}
