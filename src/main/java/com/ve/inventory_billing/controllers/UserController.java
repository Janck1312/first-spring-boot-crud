package com.ve.inventory_billing.controllers;

import com.ve.inventory_billing.entities.User;
import com.ve.inventory_billing.exchange.dto.CreateUserDto;
import com.ve.inventory_billing.exchange.dto.PaginationResponseDto;
import com.ve.inventory_billing.exchange.dto.UpdateUserDto;
import com.ve.inventory_billing.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CacheEvict
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired() private UserService userService;

    @GetMapping
    public PaginationResponseDto<User> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = true) Integer page,
            @RequestParam(required = true) Integer showRows
    ) {
        return this.userService.findAll(search, page, showRows);
    }

    @GetMapping("/{id}")
    public Optional<User> findById(@PathVariable Integer id) {
        return this.userService.findById(id);
    }

    @PostMapping()
    public CreateUserDto save (@RequestBody() CreateUserDto createUserDto) throws RuntimeException, BadRequestException {
        return this.userService.save(createUserDto);
    }

    @PutMapping("/{id}")
    public UpdateUserDto update(@RequestBody() UpdateUserDto updateUserDto, @PathVariable Integer id) throws RuntimeException, BadRequestException, NoSuchFieldException, IllegalAccessException {
        return this.userService.update(updateUserDto, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        this.userService.delete(id);
    }
}
