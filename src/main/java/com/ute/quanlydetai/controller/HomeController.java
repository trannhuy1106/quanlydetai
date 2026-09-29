package com.ute.quanlydetai.controller;

import com.ute.quanlydetai.entity.DangKyDeTai;
import com.ute.quanlydetai.entity.TaiKhoan;
import com.ute.quanlydetai.entity.ThanhVienNhom;
import com.ute.quanlydetai.repository.DangKyDeTaiRepository;
import com.ute.quanlydetai.repository.ThanhVienNhomRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class HomeController {

    private final DangKyDeTaiRepository dangKyDeTaiRepository;
    private final ThanhVienNhomRepository thanhVienNhomRepository;

    public HomeController(
            DangKyDeTaiRepository dangKyDeTaiRepository,
            ThanhVienNhomRepository thanhVienNhomRepository) {

        this.dangKyDeTaiRepository = dangKyDeTaiRepository;
        this.thanhVienNhomRepository = thanhVienNhomRepository;
    }

    @GetMapping("/")
    public String home(
            HttpSession session,
            Model model) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan != null
                && "SINH_VIEN".equals(taiKhoan.getVaiTro())
                && taiKhoan.getSinhVien() != null) {

            ThanhVienNhom thanhVien =
                    thanhVienNhomRepository.findBySinhVienId(
                            taiKhoan.getSinhVien().getId()
                    );

            if (thanhVien != null
                    && thanhVien.getNhom() != null) {

                Optional<DangKyDeTai> dangKy =
                        dangKyDeTaiRepository
                                .findFirstByNhomIdAndTrangThaiOrderByIdDesc(
                                        thanhVien.getNhom().getId(),
                                        "DA_DUYET"
                                );

                if (dangKy.isPresent()) {
                    model.addAttribute(
                            "deTaiKetQua",
                            dangKy.get().getDeTai()
                    );
                }
            }
        }

        return "index";
    }
}