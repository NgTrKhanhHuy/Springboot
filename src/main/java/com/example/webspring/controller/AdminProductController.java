package com.example.webspring.controller;

import com.example.webspring.dto.ProductDTO;
import com.example.webspring.entity.Category;
import com.example.webspring.entity.Product;
import com.example.webspring.service.CategoryService;
import com.example.webspring.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // Hiển thị danh sách sản phẩm với phân trang
    @GetMapping("/admin/products")
    public String listProducts(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        Page<Product> productPage = productService.findPaginatedProducts(PageRequest.of(page, size));
        model.addAttribute("productPage", productPage);
        return "admin-products";  // View: admin-products.html
    }
    // Hiển thị form thêm sản phẩm
//    @GetMapping("/admin/products/add")
//    public String showAddProductForm(Model model) {
//        model.addAttribute("productDTO", new ProductDTO());
//        return "admin-add-product";  // View nằm trong templates/admin-add-product.html
//    }

    // Hiển thị form thêm sản phẩm kèm danh sách loại sản phẩm
    @GetMapping("/admin/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin-add-product";
    }

//    @PostMapping("/admin/products/add")
//    public String addProduct(@Valid @ModelAttribute("productDTO") ProductDTO productDTO,
//                             BindingResult bindingResult,
//                             Model model) {
//
//        if (bindingResult.hasErrors()) {
//            return "admin-add-product";
//        }
//
//        // Xử lý upload file ảnh
//        MultipartFile imageFile = productDTO.getImage();
//        String imageUrl = "";
//        if (imageFile != null && !imageFile.isEmpty()) {
//            try {
//                String originalFilename = imageFile.getOriginalFilename();
//                String fileExtension = "";
//                if (originalFilename != null && originalFilename.contains(".")) {
//                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
//                }
//                // Tạo tên file duy nhất
//                String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
//
//                // Sử dụng thư mục 'uploads/images' trong thư mục làm việc hiện tại
//                String uploadDir = System.getProperty("user.dir") + "/uploads/images/";
//                File uploadFolder = new File(uploadDir);
//                if (!uploadFolder.exists()) {
//                    uploadFolder.mkdirs();
//                }
//
//                File destFile = new File(uploadFolder, uniqueFileName);
//                imageFile.transferTo(destFile);
//
//                // Đường dẫn tương đối để truy cập file (bạn cần cấu hình ResourceHandler để phục vụ từ đây)
//                imageUrl = "/uploads/images/" + uniqueFileName;
//                System.out.println("User dir: " + System.getProperty("user.dir"));
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                model.addAttribute("error", "Lỗi khi tải lên ảnh.");
//                return "admin-add-product";
//            }
//        } else {
//            model.addAttribute("error", "Vui lòng chọn ảnh cho sản phẩm.");
//            return "admin-add-product";
//        }
//
//        // Tạo đối tượng Product từ dữ liệu DTO
//        Product product = new Product();
//        product.setName(productDTO.getName());
//        product.setDescription(productDTO.getDescription());
//        product.setPrice(productDTO.getPrice());
//        product.setImageUrl(imageUrl);
//        // Nếu có xử lý Category, thêm xử lý tương ứng ở đây
//
//        // Lưu sản phẩm qua service
//        productService.saveProduct(product);
//
//        // Sau khi thêm thành công, chuyển hướng đến danh sách sản phẩm của admin
//        return "redirect:/admin/products";
//    }
// Xử lý form thêm sản phẩm
@PostMapping("/admin/products/add")
public String addProduct(@Valid @ModelAttribute("productDTO") ProductDTO productDTO,
                         BindingResult bindingResult,
                         Model model) {

    if (bindingResult.hasErrors()) {
        // Nếu có lỗi, reload danh sách categories để hiển thị lại form
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin-add-product";
    }

    // Xử lý upload file ảnh
    MultipartFile imageFile = productDTO.getImage();
    String imageUrl = "";
    if (imageFile != null && !imageFile.isEmpty()) {
        try {
            String originalFilename = imageFile.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            // Ví dụ: lưu file vào thư mục uploads/images (đã cấu hình ResourceHandler)
            String uploadDir = System.getProperty("user.dir") + "/uploads/images/";
            File uploadFolder = new File(uploadDir);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }
            File destFile = new File(uploadFolder, uniqueFileName);
            imageFile.transferTo(destFile);

            imageUrl = "/uploads/images/" + uniqueFileName;
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Lỗi khi tải lên ảnh.");
            model.addAttribute("categories", categoryService.getAllCategories());
            return "admin-add-product";
        }
    } else {
        model.addAttribute("error", "Vui lòng chọn ảnh cho sản phẩm.");
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin-add-product";
    }

    // Tạo đối tượng Product từ dữ liệu trong DTO
    Product product = new Product();
    product.setName(productDTO.getName());
    product.setDescription(productDTO.getDescription());
    product.setPrice(productDTO.getPrice());
    product.setImageUrl(imageUrl);

    // Lấy đối tượng Category theo categoryId được chọn
    Category category = categoryService.getCategoryById(productDTO.getCategoryId());
    product.setCategory(category);

    // Lưu sản phẩm qua service
    productService.saveProduct(product);

    // Sau khi thêm thành công, chuyển hướng đến trang danh sách sản phẩm của admin
    return "redirect:/admin/products";
}

}



