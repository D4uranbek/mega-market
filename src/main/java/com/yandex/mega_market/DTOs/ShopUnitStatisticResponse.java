package com.yandex.mega_market.DTOs;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

/**
 * @author D4uranbek
 * @since 17.06.2022
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShopUnitStatisticResponse {

    @Schema( description = "История в произвольном порядке." )
    @Valid
    private List<ShopUnit> items;

}
