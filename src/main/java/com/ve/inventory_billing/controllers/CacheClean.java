package com.ve.inventory_billing.controllers;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("cache-clean")
@CacheEvict
public class CacheClean {
    @GetMapping("/cache-clean")
    public void cleanCache() {

    }
}
