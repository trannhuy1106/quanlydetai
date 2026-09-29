package com.ute.quanlydetai.controller;

import com.ute.quanlydetai.entity.HoiDong;
import com.ute.quanlydetai.entity.DeTai;

import com.ute.quanlydetai.repository.HoiDongRepository;
import com.ute.quanlydetai.repository.DeTaiRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HoiDongController {

    private final HoiDongRepository hoiDongRepository;
    private final DeTaiRepository deTaiRepository;

    public HoiDongController(
            HoiDongRepository hoiDongRepository,
            DeTaiRepository deTaiRepository) {

        this.hoiDongRepository = hoiDongRepository;
        this.deTaiRepository = deTaiRepository;
    }

    @GetMapping("/hoidong")
    public String danhSachHoiDong(Model model) {

        model.addAttribute(
                "dsHoiDong",
                hoiDongRepository.findAll()
        );

        return "hoidong";
    }

    @GetMapping("/hoidong/them")
    public String hienThiFormThem(Model model) {

        model.addAttribute(
                "dsDeTai",
                deTaiRepository.findAll()
        );

        return "them-hoidong";
    }

    @PostMapping("/hoidong/them")
    public String themHoiDong(
            @RequestParam String maHoiDong,
            @RequestParam String tenHoiDong,
            @RequestParam String ngayBaoCao,
            @RequestParam String diaDiem,
            @RequestParam Long deTaiId) {

        DeTai deTai = deTaiRepository
                .findById(deTaiId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy đề tài"
                        ));

        HoiDong hoiDong = new HoiDong();

        hoiDong.setMaHoiDong(maHoiDong);
        hoiDong.setTenHoiDong(tenHoiDong);
        hoiDong.setNgayBaoCao(
                java.time.LocalDate.parse(ngayBaoCao)
        );
        hoiDong.setDiaDiem(diaDiem);
        hoiDong.setDeTai(deTai);
        hoiDong.setTrangThai("DANG_HOAT_DONG");

        hoiDongRepository.save(hoiDong);

        return "redirect:/hoidong";
    }

    @GetMapping("/hoidong/xoa/{id}")
    public String xoaHoiDong(@PathVariable Long id) {

        hoiDongRepository.deleteById(id);

        return "redirect:/hoidong";
    }
}