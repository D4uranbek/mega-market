package com.yandex.mega_market.validators;

import com.yandex.mega_market.DTOs.ShopUnitImport;
import com.yandex.mega_market.DTOs.ShopUnitImportRequest;
import com.yandex.mega_market.entities.enums.ShopUnitType;
import com.yandex.mega_market.exceptions.ValidationException;
import org.springframework.stereotype.Component;

import java.util.Objects;

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
        if ( Objects.isNull( shopUnitImport.getId() )
                || Objects.isNull( shopUnitImport.getName() ) || shopUnitImport.getName().trim().isBlank()
                || shopUnitImport.getType() == ShopUnitType.OFFER && Objects.isNull( shopUnitImport.getPrice() )
                || Objects.nonNull( shopUnitImport.getPrice() ) && shopUnitImport.getPrice() < 0
                || Objects.isNull( shopUnitImport.getType() ) ) {
            throw new ValidationException();
        }
    }
}
