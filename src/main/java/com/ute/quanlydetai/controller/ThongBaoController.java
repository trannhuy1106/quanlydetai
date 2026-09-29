package com.ute.quanlydetai.controller;

import com.ute.quanlydetai.entity.ThongBao;
import com.ute.quanlydetai.entity.TaiKhoan;
import com.ute.quanlydetai.repository.ThongBaoRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class ThongBaoController {

    private final ThongBaoRepository thongBaoRepository;

    public ThongBaoController(
            ThongBaoRepository thongBaoRepository) {

        this.thongBaoRepository = thongBaoRepository;
    }

    @GetMapping("/thongbao")
    public String danhSachThongBao(Model model) {

        model.addAttribute(
                "dsThongBao",
                thongBaoRepository.findAll()
        );

        return "thongbao";
    }

    @GetMapping("/thongbao/them")
    public String hienThiFormThem(
            HttpSession session,
            Model model) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan == null) {
            return "redirect:/dangnhap";
        }

        if ("SINH_VIEN".equals(taiKhoan.getVaiTro())) {
            return "redirect:/khong-co-quyen";
        }

        model.addAttribute(
                "taiKhoan",
                taiKhoan
        );

        return "them-thongbao";
    }

    @PostMapping("/thongbao/them")
    public String themThongBao(
            @RequestParam String tieuDe,
            @RequestParam String noiDung,
            HttpSession session) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan == null) {
            return "redirect:/dangnhap";
        }

        if ("SINH_VIEN".equals(taiKhoan.getVaiTro())) {
            return "redirect:/khong-co-quyen";
        }

        String nguoiDang;

        if ("GIANG_VIEN".equals(taiKhoan.getVaiTro())
                && taiKhoan.getGiangVien() != null) {

            nguoiDang =
                    taiKhoan.getGiangVien().getHoTen();

        } else if ("SINH_VIEN".equals(taiKhoan.getVaiTro())
                && taiKhoan.getSinhVien() != null) {

            nguoiDang =
                    taiKhoan.getSinhVien().getHoTen();

        } else {

            nguoiDang =
                    taiKhoan.getTenDangNhap();
        }

        ThongBao thongBao =
                new ThongBao();

        thongBao.setTieuDe(tieuDe);
        thongBao.setNoiDung(noiDung);
        thongBao.setNguoiDang(nguoiDang);
        thongBao.setNgayDang(LocalDateTime.now());
        thongBao.setTrangThai("DA_DANG");

        thongBaoRepository.save(thongBao);

        return "redirect:/thongbao";
    }

    @GetMapping("/thongbao/xoa/{id}")
    public String xoaThongBao(
            @PathVariable Long id,
            HttpSession session) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan == null) {
            return "redirect:/dangnhap";
        }

        if ("SINH_VIEN".equals(taiKhoan.getVaiTro())) {
            return "redirect:/khong-co-quyen";
        }

        thongBaoRepository.deleteById(id);

        return "redirect:/thongbao";
    }
}