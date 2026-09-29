package com.ute.quanlydetai.repository;

import com.ute.quanlydetai.entity.BoMon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoMonRepository extends JpaRepository<BoMon, Long> {
}