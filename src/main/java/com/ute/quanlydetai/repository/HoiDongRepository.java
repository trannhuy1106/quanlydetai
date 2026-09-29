package com.ute.quanlydetai.repository;

import com.ute.quanlydetai.entity.HoiDong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HoiDongRepository extends JpaRepository<HoiDong, Long> {

    Optional<HoiDong> findFirstByDeTaiId(Long deTaiId);
}