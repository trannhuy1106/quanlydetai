package com.ute.quanlydetai.repository;

import com.ute.quanlydetai.entity.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThongBaoRepository
        extends JpaRepository<ThongBao, Long> {
}