package com.navneet.ecommerce.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.navneet.ecommerce.annotations.TrackResponceTime;
import com.navneet.ecommerce.entities.Products;

@TrackResponceTime
public interface ProductDao extends JpaRepository<Products, Long> {
	// Method to fetch a product entity along with its related entities
	@Query("SELECT p FROM Products p LEFT JOIN FETCH p.category LEFT JOIN FETCH p.target")
	public Page<Products> findAllProducts(Pageable page);

	// Method to fetch productId, colorName, and sizeName corresponding to a product
	@Query("SELECT p.productId, c.colorName, s.sizeName FROM Products p "
			+ "LEFT JOIN ProductVariants v ON v.products = p " + "LEFT JOIN Color c ON v.color = c "
			+ "LEFT JOIN Size s ON v.size = s " + "WHERE p IN :products "
			+ "GROUP BY p.productId, c.colorName, s.sizeName")
	public List<Object[]> findUniqueColorsAndSizesForProducts(List<Products> products);

	// Method to fetch product based on some applied filters
	@Query("SELECT DISTINCT p FROM Products p " +
	        "LEFT JOIN FETCH p.category c " +
	        "LEFT JOIN FETCH p.target t " +
	        "WHERE COALESCE(:categoryId, p.category.categoryId) = p.category.categoryId " +
	        "AND COALESCE(:targetId, p.target.targetId) = p.target.targetId " +
	        "AND (COALESCE(:colorId, -1) = -1 OR EXISTS (SELECT 1 FROM ProductVariants pv " +
	        "JOIN pv.color clr " +
	        "WHERE pv.products = p AND COALESCE(:colorId, clr.colorId) = clr.colorId))")
	Page<Products> findProductsByFilters(Integer categoryId, Integer targetId, Integer colorId, Pageable pageable);

	@Query("SELECT DISTINCT p FROM Products p " + "LEFT JOIN FETCH p.category c " + "LEFT JOIN FETCH p.target t "
			+ "WHERE (LOWER(p.productName) LIKE %:searchedString% )"
			+ "AND (:categoryId IS NULL OR p.category.categoryId = :categoryId) "
			+ "AND (:targetId IS NULL OR p.target.targetId = :targetId) "
			+ "AND p IN (SELECT DISTINCT pv.products FROM ProductVariants pv " + "JOIN pv.color clr "
			+ "WHERE (:colorId IS NULL OR clr.colorId = :colorId) )")
	public Page<Products> findProductsByName(Pageable page, String searchedString, Integer categoryId, Integer targetId,
			Integer colorId);

}
