package com.products.repository;

import com.products.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
    @Query(value = "SELECT * FROM category cat WHERE cat.parent_id = :id", nativeQuery = true)
    Collection<CategoryEntity> findChildCategoryById(@Param("id") UUID id);

    @Query(value = "WITH RECURSIVE cats AS (\n" +
                   "SELECT id, parent_id \n" +
                   "FROM category cat \n" +
                   "WHERE cat.parent_id = :id\n" +
                   "UNION\n" +
                   "SELECT newcat.id, newcat.parent_id \n" +
                   "FROM category newcat\t\n" +
                   "JOIN cats ON cats.id = newcat.parent_id\n" +
                   ")\n" +
                   "SELECT * FROM category cat\n" +
                   "JOIN cats ON cats.id = cat.id;"
                   , nativeQuery = true)
    Collection<CategoryEntity> findChildCategoryRecursive(@Param("id") UUID id);

    @Query(value = "WITH RECURSIVE cats AS ( \n" +
                   "SELECT id, parent_id \n" +
                   "FROM category cat \n" +
                   "WHERE cat.id = :parent_id \n" +
                   "UNION \n" +
                   "SELECT newcat.id, newcat.parent_id  \n" +
                   "FROM category newcat \n" +
                   "JOIN cats ON cats.parent_id = newcat.id \n" +
                   ") \n" +
                   "SELECT * FROM category cat \n" +
                   "JOIN cats ON cats.id = cat.id;"
            , nativeQuery = true)
    Collection<CategoryEntity> findParentCategoryRecursive(@Param("parent_id") UUID id);
}
