package com.ute.quanlydetai.controller;

import com.ute.quanlydetai.entity.DangKyDeTai;
import com.ute.quanlydetai.entity.DeTai;
import com.ute.quanlydetai.entity.DotDangKy;
import com.ute.quanlydetai.entity.NhomSinhVien;
import com.ute.quanlydetai.entity.SinhVien;
import com.ute.quanlydetai.entity.TaiKhoan;
import com.ute.quanlydetai.entity.ThanhVienNhom;

import com.ute.quanlydetai.repository.DangKyDeTaiRepository;
import com.ute.quanlydetai.repository.DeTaiRepository;
import com.ute.quanlydetai.repository.DotDangKyRepository;
import com.ute.quanlydetai.repository.PhanCongHuongDanRepository;
import com.ute.quanlydetai.repository.ThanhVienNhomRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class DangKyDeTaiController {

    private final DangKyDeTaiRepository dangKyDeTaiRepository;
    private final DeTaiRepository deTaiRepository;
    private final DotDangKyRepository dotDangKyRepository;
    private final ThanhVienNhomRepository thanhVienNhomRepository;
    private final PhanCongHuongDanRepository phanCongHuongDanRepository;

    public DangKyDeTaiController(
            DangKyDeTaiRepository dangKyDeTaiRepository,
            DeTaiRepository deTaiRepository,
            DotDangKyRepository dotDangKyRepository,
            ThanhVienNhomRepository thanhVienNhomRepository,
            PhanCongHuongDanRepository phanCongHuongDanRepository) {

        this.dangKyDeTaiRepository = dangKyDeTaiRepository;
        this.deTaiRepository = deTaiRepository;
        this.dotDangKyRepository = dotDangKyRepository;
        this.thanhVienNhomRepository = thanhVienNhomRepository;
        this.phanCongHuongDanRepository = phanCongHuongDanRepository;
    }

    @GetMapping("/dangky")
    public String danhSachDangKy(Model model) {

        model.addAttribute(
                "dsDangKy",
                dangKyDeTaiRepository.findAll()
        );

        return "dangky";
    }

    @GetMapping("/dangky/them")
    public String hienThiFormThem(
            HttpSession session,
            Model model) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan == null) {
            return "redirect:/dangnhap";
        }

        if (!"SINH_VIEN".equals(taiKhoan.getVaiTro())) {
            return "redirect:/khong-co-quyen";
        }

        SinhVien sinhVien = taiKhoan.getSinhVien();

        if (sinhVien == null) {

            model.addAttribute(
                    "loi",
                    "Tài khoản chưa được liên kết với sinh viên."
            );

            return "them-dangky";
        }

        List<ThanhVienNhom> dsThanhVien =
                thanhVienNhomRepository.findAll();

        ThanhVienNhom thanhVienCuaSinhVien = null;

        for (ThanhVienNhom tv : dsThanhVien) {

            if (tv.getSinhVien() != null
                    && tv.getSinhVien().getId()
                    .equals(sinhVien.getId())) {

                thanhVienCuaSinhVien = tv;
                break;
            }
        }

        if (thanhVienCuaSinhVien == null) {

            model.addAttribute(
                    "loi",
                    "Sinh viên chưa thuộc nhóm nào."
            );

            return "them-dangky";
        }

        if (!"NHOM_TRUONG".equals(
                thanhVienCuaSinhVien.getVaiTro())) {

            model.addAttribute(
                    "loi",
                    "Chỉ nhóm trưởng mới được đăng ký đề tài."
            );

            return "them-dangky";
        }

        model.addAttribute(
                "nhom",
                thanhVienCuaSinhVien.getNhom()
        );

        model.addAttribute(
                "dsDeTai",
                deTaiRepository.findByTrangThai("CON_TRONG")
        );

        model.addAttribute(
                "dsDotDangKy",
                dotDangKyRepository.findAll()
        );

        return "them-dangky";
    }

    @PostMapping("/dangky/them")
    public String themDangKy(
            @RequestParam Long deTaiId,
            @RequestParam Long dotDangKyId,
            @RequestParam(required = false) String ghiChu,
            HttpSession session,
            Model model) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan == null) {
            return "redirect:/dangnhap";
        }

        if (!"SINH_VIEN".equals(taiKhoan.getVaiTro())) {
            return "redirect:/khong-co-quyen";
        }

        SinhVien sinhVien = taiKhoan.getSinhVien();

        if (sinhVien == null) {
            return hienThiLoi(
                    model,
                    null,
                    "Tài khoản chưa được liên kết với sinh viên."
            );
        }

        ThanhVienNhom thanhVienCuaSinhVien = null;

        for (ThanhVienNhom tv :
                thanhVienNhomRepository.findAll()) {

            if (tv.getSinhVien() != null
                    && tv.getSinhVien().getId()
                    .equals(sinhVien.getId())) {

                thanhVienCuaSinhVien = tv;
                break;
            }
        }

        if (thanhVienCuaSinhVien == null) {

            return hienThiLoi(
                    model,
                    null,
                    "Sinh viên chưa thuộc nhóm nào."
            );
        }

        if (!"NHOM_TRUONG".equals(
                thanhVienCuaSinhVien.getVaiTro())) {

            return hienThiLoi(
                    model,
                    thanhVienCuaSinhVien.getNhom(),
                    "Chỉ nhóm trưởng mới được đăng ký đề tài."
            );
        }

        NhomSinhVien nhom =
                thanhVienCuaSinhVien.getNhom();

        DeTai deTai =
                deTaiRepository.findById(deTaiId)
                        .orElse(null);

        if (deTai == null) {

            return hienThiLoi(
                    model,
                    nhom,
                    "Không tìm thấy đề tài."
            );
        }

        if (!"CON_TRONG".equals(
                deTai.getTrangThai())) {

            return hienThiLoi(
                    model,
                    nhom,
                    "Đề tài này không còn trống."
            );
        }

        DotDangKy dotDangKy =
                dotDangKyRepository
                        .findById(dotDangKyId)
                        .orElse(null);

        if (dotDangKy == null) {

            return hienThiLoi(
                    model,
                    nhom,
                    "Không tìm thấy đợt đăng ký."
            );
        }

        LocalDateTime hienTai =
                LocalDateTime.now();

        if (hienTai.isBefore(
                dotDangKy.getSvBatDau())
                || hienTai.isAfter(
                dotDangKy.getSvKetThuc())) {

            return hienThiLoi(
                    model,
                    nhom,
                    "Hiện không nằm trong thời gian sinh viên đăng ký đề tài."
            );
        }

        if (deTai.getDotDangKy() != null
                && !deTai.getDotDangKy().getId()
                .equals(dotDangKyId)) {

            return hienThiLoi(
                    model,
                    nhom,
                    "Đề tài này không thuộc đợt đăng ký đã chọn."
            );
        }

        boolean daDangKy =
                dangKyDeTaiRepository
                        .existsByNhomIdAndDotDangKyId(
                                nhom.getId(),
                                dotDangKyId
                        );

        if (daDangKy) {

            return hienThiLoi(
                    model,
                    nhom,
                    "Nhóm này đã đăng ký đề tài trong đợt đăng ký này."
            );
        }

        DangKyDeTai dangKy =
                new DangKyDeTai();

        dangKy.setSinhVien(sinhVien);
        dangKy.setNhom(nhom);
        dangKy.setDeTai(deTai);
        dangKy.setDotDangKy(dotDangKy);
        dangKy.setTrangThai("CHO_DUYET");
        dangKy.setGhiChu(ghiChu);

        dangKyDeTaiRepository.save(dangKy);

        return "redirect:/dangky";
    }

    @GetMapping("/dangky/duyet/{id}")
    public String duyetDangKy(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan == null) {
            return "redirect:/dangnhap";
        }

        if (!"ADMIN".equals(taiKhoan.getVaiTro())) {
            return "redirect:/khong-co-quyen";
        }

        DangKyDeTai dangKy =
                dangKyDeTaiRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy đăng ký"
                                )
                        );

        DeTai deTai = dangKy.getDeTai();

        if (deTai == null) {

            model.addAttribute(
                    "dsDangKy",
                    dangKyDeTaiRepository.findAll()
            );

            model.addAttribute(
                    "loi",
                    "Đăng ký này không có đề tài."
            );

            return "dangky";
        }

        long soGiangVienHuongDan =
                phanCongHuongDanRepository
                        .countByDeTaiId(deTai.getId());

        if (soGiangVienHuongDan < 1) {

            model.addAttribute(
                    "dsDangKy",
                    dangKyDeTaiRepository.findAll()
            );

            model.addAttribute(
                    "loi",
                    "Không thể duyệt! Đề tài chưa được phân công giảng viên hướng dẫn."
            );

            return "dangky";
        }

        if (soGiangVienHuongDan > 2) {

            model.addAttribute(
                    "dsDangKy",
                    dangKyDeTaiRepository.findAll()
            );

            model.addAttribute(
                    "loi",
                    "Không thể duyệt! Một đề tài chỉ được có tối đa 2 giảng viên hướng dẫn."
            );

            return "dangky";
        }

        if (!"CON_TRONG".equals(
                deTai.getTrangThai())) {

            model.addAttribute(
                    "dsDangKy",
                    dangKyDeTaiRepository.findAll()
            );

            model.addAttribute(
                    "loi",
                    "Không thể duyệt! Đề tài này không còn trống."
            );

            return "dangky";
        }

        dangKy.setTrangThai("DA_DUYET");
        deTai.setTrangThai("DA_DANG_KY");

        deTaiRepository.save(deTai);
        dangKyDeTaiRepository.save(dangKy);

        return "redirect:/dangky";
    }

    @GetMapping("/dangky/tuchoi/{id}")
    public String tuChoiDangKy(
            @PathVariable Long id,
            HttpSession session) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan == null) {
            return "redirect:/dangnhap";
        }

        if (!"ADMIN".equals(taiKhoan.getVaiTro())) {
            return "redirect:/khong-co-quyen";
        }

        DangKyDeTai dangKy =
                dangKyDeTaiRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy đăng ký"
                                )
                        );

        DeTai deTai = dangKy.getDeTai();

        if ("DA_DUYET".equals(
                dangKy.getTrangThai())
                && deTai != null) {

            deTai.setTrangThai("CON_TRONG");
            deTaiRepository.save(deTai);
        }

        dangKy.setTrangThai("TU_CHOI");

        dangKyDeTaiRepository.save(dangKy);

        return "redirect:/dangky";
    }

    @GetMapping("/dangky/xoa/{id}")
    public String xoaDangKy(
            @PathVariable Long id,
            HttpSession session) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan == null) {
            return "redirect:/dangnhap";
        }

        if (!"ADMIN".equals(taiKhoan.getVaiTro())) {
            return "redirect:/khong-co-quyen";
        }

        Optional<DangKyDeTai> optional =
                dangKyDeTaiRepository.findById(id);

        if (optional.isPresent()) {

            DangKyDeTai dangKy =
                    optional.get();

            DeTai deTai =
                    dangKy.getDeTai();

            if ("DA_DUYET".equals(
                    dangKy.getTrangThai())
                    && deTai != null) {

                deTai.setTrangThai("CON_TRONG");
                deTaiRepository.save(deTai);
            }

            dangKyDeTaiRepository
                    .delete(dangKy);
        }

        return "redirect:/dangky";
    }

    private String hienThiLoi(
            Model model,
            NhomSinhVien nhom,
            String loi) {

        model.addAttribute(
                "loi",
                loi
        );

        model.addAttribute(
                "nhom",
                nhom
        );

        model.addAttribute(
                "dsDeTai",
                deTaiRepository
                        .findByTrangThai("CON_TRONG")
        );

        model.addAttribute(
                "dsDotDangKy",
                dotDangKyRepository.findAll()
        );

        return "them-dangky";
    }
}