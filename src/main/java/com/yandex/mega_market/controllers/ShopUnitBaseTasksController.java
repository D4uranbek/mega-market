package com.yandex.mega_market.controllers;

import com.yandex.mega_market.DTOs.ShopUnit;
import com.yandex.mega_market.DTOs.ShopUnitImportRequest;
import com.yandex.mega_market.services.ShopUnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

/**
 * @author D4uranbek
 * @since 17.06.2022
 */
@RestController
@RequiredArgsConstructor
@Tag( name = "Базовые задачи" )
public class ShopUnitBaseTasksController {

    private final ShopUnitService service;

    /**
     * POST /imports
     * Импортирует новые товары и/или категории.
     *
     * @param shopUnitImportRequest (optional)
     * @return Вставка или обновление прошли успешно. (status code 200)
     * or Невалидная схема документа или входные данные не верны. (status code 400)
     */
    @Operation(
            description = "Импортирует новые товары и/или категории."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Вставка или обновление прошли успешно."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Невалидная схема документа или входные данные не верны."

            )
    } )
    @PostMapping( value = "/imports",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    @ResponseStatus( HttpStatus.OK )
    public ResponseEntity<Void> importNewShopUnit( @RequestBody( required = false )
                                                   @Valid
                                                   ShopUnitImportRequest shopUnitImportRequest ) {

        service.importShopUnits( shopUnitImportRequest );
        return ResponseEntity.ok().build();
    }


    /**
     * DELETE /delete/{id}
     * Удалить элемент по идентификатору. При удалении категории удаляются все дочерние элементы.
     *
     * @param id Идентификатор (required)
     * @return Удаление прошло успешно. (status code 200)
     * or Невалидная схема документа или входные данные не верны. (status code 400)
     * or Категория/товар не найден. (status code 404)
     */
    @Operation(
            description = "Удалить элемент по идентификатору."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Удаление прошло успешно."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Невалидная схема документа или входные данные не верны."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Категория/товар не найден."
            )
    } )
    @DeleteMapping( value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Void> deleteElement( @Parameter( description = "Идентификатор", required = true )
                                               @PathVariable( "id" )
                                               UUID id ) {

        service.deleteShopUnit( id );
        return ResponseEntity.ok().build();
    }


    /**
     * GET /nodes/{id}
     * Получить информацию об элементе по идентификатору.
     *
     * @param id Идентификатор (required)
     * @return Информация об элементе. (status code 200)
     * or Невалидная схема документа или входные данные не верны. (status code 400)
     * or Категория/товар не найден. (status code 404)
     */
    @Operation(
            description = "Получить информацию об элементе по идентификатору."
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Информация об элементе."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Невалидная схема документа или входные данные не верны."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Категория/товар не найден."
            )
    } )
    @GetMapping( value = "/nodes/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<ShopUnit> getElementInfo( @Parameter( description = "Идентификатор элемента", required = true )
                                                    @PathVariable( "id" )
                                                    UUID id ) {

        ShopUnit shopUnit = service.getElementById( id );
        return new ResponseEntity<>( shopUnit, HttpStatus.OK );

    }

}
