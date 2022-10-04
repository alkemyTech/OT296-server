package com.alkemy.ong.repository;

import com.alkemy.ong.entity.Members;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembersRepository extends JpaRepository<Members, String> {
    Page<Members> findAll(Pageable pageable);
}
