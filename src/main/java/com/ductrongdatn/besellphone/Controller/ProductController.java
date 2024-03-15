package com.ductrongdatn.besellphone.Controller;

import com.ductrongdatn.besellphone.DTO.ProductDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @Valid @ModelAttribute ProductDTO productDTO,
            BindingResult result
    ) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            List<MultipartFile> files = productDTO.getFiles();
            files = files == null ? new ArrayList<MultipartFile>() : files;
            for (MultipartFile file : files) {
                if(file.getSize() == 0) {
                    continue;
                }
                // Kiểm tra kích thước file và định dạng
                if(file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum size is 10MB");
                }
                String contentType = file.getContentType();
                if(contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }
                // Lưu file và cập nhật thumbnail trong DTO
                String filename = storeFile(file); // Thay thế hàm này với code của bạn để lưu file
                //lưu vào đối tượng product trong DB => sẽ làm sau
                //lưu vào bảng product_images
            }
            return ResponseEntity.ok("Product created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String storeFile(MultipartFile file) throws IOException{
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        Path uploadDir = Paths.get("uploads");
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);

        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    @GetMapping
    public ResponseEntity<String> getAllProducts(@RequestParam("page") int page, @RequestParam("limit") int limit){

        return ResponseEntity.ok("getProducts here");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductsById(@PathVariable("id") String productId){

        return ResponseEntity.ok("getProductsById here" + productId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id){
        return ResponseEntity.ok(String.format("Product with id = %d deleted successfully", id));
    }

}
