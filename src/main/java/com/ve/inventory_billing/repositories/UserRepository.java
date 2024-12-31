package com.ve.inventory_billing.repositories;

import com.ve.inventory_billing.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository()
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> existsByEmail(String email);
    Optional<User> findByIdentification(String identification);

    @Query(value = "SELECT id, name, last_name, password, identification, email, created_at, updated_at, deleted_at FROM users WHERE name LIKE '%:search%' OR last_name LIKE '%:search%' OR identification LIKE '%:search%' OR email LIKE '%:search%' AND deleted_at IS NULL LIMIT :offset, :showRows", nativeQuery = true)
    List<User> pagination(@Param("search") String search, @Param("offset") Integer offset, @Param("showRows") Integer showRows);

    @Query(value = "SELECT id, name, last_name, password, identification, email, created_at, updated_at, deleted_at FROM users WHERE deleted_at IS NULL LIMIT :offset, :showRows", nativeQuery = true)
    List<User> pagination(@Param("offset") Integer offset, @Param("showRows") Integer showRows);
}