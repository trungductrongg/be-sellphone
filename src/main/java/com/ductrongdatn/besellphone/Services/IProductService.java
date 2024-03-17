package com.ductrongdatn.besellphone.Services;

import com.ductrongdatn.besellphone.DTO.ProductDTO;
import com.ductrongdatn.besellphone.DTO.ProductImageDTO;
import com.ductrongdatn.besellphone.Models.Product;
import com.ductrongdatn.besellphone.Models.ProductImage;
import com.ductrongdatn.besellphone.Responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;
    Product getProductById(long id) throws Exception;
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    Product updateProduct(long id, ProductDTO productDTO) throws Exception;
    void deleteProduct(long id);
    boolean existsByName(String name);
    ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws Exception;
}

