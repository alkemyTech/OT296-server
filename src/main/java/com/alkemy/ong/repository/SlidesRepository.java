package com.alkemy.ong.repository;

import com.alkemy.ong.entity.Slides;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlidesRepository extends JpaRepository<Slides, String> {
    boolean existsById(String id);

    @Query(nativeQuery = true, value = "SELECT * FROM slides WHERE organization_id = ?1 ORDER BY orders DESC")
    List<Slides> findSlidesByIdOrg(String idOng);
    @Query(nativeQuery = true, value = "SELECT max(orders) FROM slides")
    Integer getMaxOrder();
}
