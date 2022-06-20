package com.yandex.mega_market.services;

import com.yandex.mega_market.repositories.ShopUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author D4uranbek
 * @since 20.06.2022
 */
@Service
@RequiredArgsConstructor
public class ShopUnitService {

    private final ShopUnitRepository repository;

}
