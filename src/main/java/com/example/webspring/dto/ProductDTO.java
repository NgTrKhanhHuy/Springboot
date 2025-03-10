package com.example.webspring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id; // Để xác định sản phẩm cần sửa

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @Positive(message = "Giá sản phẩm phải lớn hơn 0")
    private BigDecimal price;

    // Ảnh sản phẩm – bắt buộc chọn file
//    @NotNull(message = "Vui lòng chọn ảnh cho sản phẩm")
    private MultipartFile image;

    // Field ẩn để chứa đường dẫn ảnh cũ (nếu không upload ảnh mới, dùng giá trị này)
    private String oldImg;
    // Loại sản phẩm (chọn qua select)
    @NotNull(message = "Vui lòng chọn loại sản phẩm")
    private Long categoryId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getOldImg() {
        return oldImg;
    }

    public void setOldImg(String oldImg) {
        this.oldImg = oldImg;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", image=" + image +
                ", oldImg='" + oldImg + '\'' +
                ", categoryId=" + categoryId +
                '}';
    }
}
