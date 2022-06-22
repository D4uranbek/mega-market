package com.yandex.mega_market.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author D4uranbek
 * @since 17.06.2022
 */
@Getter
@Setter
@NoArgsConstructor
public class ShopUnitImportRequest {

    @Schema( description = "Импортируемые элементы" )
    @Valid
    private List<ShopUnitImport> items;

    @Schema(
            example = "2022-05-28T21:12:01Z",
            description = "Время обновления добавляемых товаров/категорий."
    )
    @Valid
    @DateTimeFormat( iso = ISO.DATE_TIME )
    private LocalDateTime updateDate;

}
