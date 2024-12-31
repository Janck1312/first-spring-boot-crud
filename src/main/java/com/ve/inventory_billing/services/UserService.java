package com.ve.inventory_billing.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.ve.inventory_billing.entities.User;
import com.ve.inventory_billing.exchange.dto.CreateUserDto;
import com.ve.inventory_billing.exchange.dto.PaginationResponseDto;
import com.ve.inventory_billing.exchange.dto.UpdateUserDto;
import com.ve.inventory_billing.exchange.services.Mapper;
import com.ve.inventory_billing.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired(required = true) private UserRepository userRepository;
    private Logger logger = Logger.getLogger("UserService");

    public CreateUserDto save(CreateUserDto createUserDto) throws RuntimeException, BadRequestException {
        if(this.isEmailNotUnique(createUserDto.getEmail()))
            throw new BadRequestException("email should be unique");
        if(this.isIdentificationNotUnique(createUserDto.getIdentification()))
            throw  new BadRequestException("identification should be unique");

        Mapper<User, CreateUserDto> returnMapper = new Mapper<User, CreateUserDto>();
        Mapper<CreateUserDto, User> mapper = new Mapper<CreateUserDto, User>();

        createUserDto.setPassword(this.encryptPassword(createUserDto.getPassword()));
        createUserDto.setCreatedAt(new Date());
        createUserDto.setUpdatedAt(new Date());
        User user = mapper.getObjectMappedForCreate(createUserDto, User.class);

        user = this.userRepository.save(user);
        return returnMapper.getObjectMappedForResponse(
                user,
                CreateUserDto.class
        );
    }

    public UpdateUserDto update(UpdateUserDto updateUserDto, Integer id) throws RuntimeException, BadRequestException, NoSuchFieldException, IllegalAccessException {
        Optional<User> userExists = this.userRepository.findById(id);
        if(userExists.isEmpty()) throw new BadRequestException("user not found");

        Mapper<UpdateUserDto, User> mapper = new Mapper <UpdateUserDto, User>();
        Mapper<User, UpdateUserDto> returnMapper = new Mapper <User, UpdateUserDto>();

        User user = mapper.getObjectMappedForCreate(updateUserDto, User.class);
        User oldData = userExists.get();

        if(!updateUserDto.getPassword().isEmpty()) {
            user.setPassword(this.encryptPassword(updateUserDto.getPassword()));
        }

        if(!user.getEmail().equals(oldData.getEmail())) {
            if(this.isEmailNotUnique(user.getEmail()))
                    throw new BadRequestException("email is not unique");
        }

        if(!user.getIdentification().equals(oldData.getIdentification())) {
            if(this.isIdentificationNotUnique(user.getIdentification()))
                throw  new BadRequestException("identification should be unique");
        }

        user.setUpdatedAt(new Date());
        user.setId(oldData.getId());
        return returnMapper.getObjectMappedForResponse(
                this.userRepository.save(mapper.getObjectForUpdate(user, oldData)),
                UpdateUserDto.class
        );
    }

    public PaginationResponseDto<User> findAll(String search, Integer page, Integer showRows) throws RuntimeException {
        List<User> data = null;
        if(search.isEmpty())
            data = this.userRepository.pagination(page * showRows, showRows);
        else
            data =  this.userRepository.pagination(search, page * showRows, showRows);

        return new PaginationResponseDto<User>(
                page, showRows, data.toArray().length, page * showRows, data
        );
    }

    public Optional<User> findById(Integer id) {
        return this.userRepository.findById(id);
    }

    public void delete(Integer id) {
        this.userRepository.deleteById(id);
    }

    private String encryptPassword(String plainPassword) {
        return BCrypt.withDefaults().hashToString(
                12,
                plainPassword.toCharArray()
        );
    }

    private Boolean isEmailNotUnique(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        return user.isPresent();
    }

    private Boolean isIdentificationNotUnique(String identification) {
        Optional<User> user = this.userRepository.findByIdentification(identification);
        return user.isPresent();
    }
}
