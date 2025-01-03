package com.ve.inventory_billing.services;

import com.ve.inventory_billing.entities.Product;
import com.ve.inventory_billing.exchange.dto.CreateProductDto;
import com.ve.inventory_billing.exchange.dto.PaginationResponseDto;
import com.ve.inventory_billing.exchange.dto.UpdateProductDto;
import com.ve.inventory_billing.exchange.services.Mapper;
import com.ve.inventory_billing.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ProductService {
    @Autowired private ProductRepository productRepository;

    public CreateProductDto save(CreateProductDto createProductDto) throws BadRequestException {
        if(this.findByCode(createProductDto.getCode()))
            throw new BadRequestException("duplicated key code already exists.");
        Product newProduct = new Mapper<CreateProductDto, Product>()
                                    .getObjectMappedForCreate(createProductDto, Product.class);
        return new Mapper<Product, CreateProductDto>()
                    .getObjectMappedForResponse(this.productRepository.save(newProduct), CreateProductDto.class);
    }

    public UpdateProductDto update(UpdateProductDto updateProductDto, Integer id) throws BadRequestException, NoSuchFieldException, IllegalAccessException {
        Optional<Product> productExists = this.findById(id);
        if(productExists.isEmpty())
            throw new BadRequestException("product not found");

        if(!productExists.get().getCode().equals(updateProductDto.getCode())){
            if(this.findByCode(updateProductDto.getCode()))
                throw new BadRequestException("duplicated key code already exists.");
        }
        updateProductDto.setId(productExists.get().getId());
        Product oldData = productExists.get();
        oldData = new Mapper<UpdateProductDto, Product>().getObjectForUpdate(
                new Mapper<UpdateProductDto, Product>().getObjectMappedForCreate(
                        updateProductDto,
                        Product.class
                ),
                oldData
        );

        return new Mapper<Product, UpdateProductDto>().getObjectMappedForResponse(
                this.productRepository.save(oldData),
                UpdateProductDto.class
        );
    }

    public PaginationResponseDto<Product> findAll(String search, Integer page, Integer showRows) throws RuntimeException {
        List<Product> data = null;
        if(search.isEmpty())
            data = this.productRepository.pagination(page * showRows, showRows);
        else
            data =  this.productRepository.pagination(search, page * showRows, showRows);

        return new PaginationResponseDto<Product>(
                page, showRows, data.toArray().length, page * showRows, data
        );
    }

    public void deleteById(Integer id) {
        this.productRepository.deleteById(id);
    }

    private Boolean findByCode(String code) {
        Optional<Product> product = this.productRepository.findByCode(code);
        return product.isPresent();
    }

    public Optional<Product>findById(Integer id) {
        return this.productRepository.findById(id);
    }
}