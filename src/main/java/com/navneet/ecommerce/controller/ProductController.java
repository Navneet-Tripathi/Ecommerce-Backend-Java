package com.navneet.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.navneet.ecommerce.dto.ProductDto;
import com.navneet.ecommerce.services.ProductServices;

@RestController
public class ProductController {
	@Autowired
	private ProductServices productServices;
	
	@GetMapping("/products")
	public List<ProductDto> getProducts(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize){
		return this.productServices.getAllProducts(pageNumber, pageSize);
	}
	
	@PostMapping("/products")
	public ProductDto addAProduct(@RequestBody ProductDto dto) {
		return this.productServices.addAProduct(dto);
	}
}
