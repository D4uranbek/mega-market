package com.yandex.mega_market.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author D4uranbek
 * @since 17.06.2022
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Error {

    @NotNull
    @Schema( required = true )
    private Integer code;

    @NotNull
    @Schema( required = true )
    private String message;

}

