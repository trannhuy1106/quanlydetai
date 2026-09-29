package com.ute.quanlydetai.controller;

import com.ute.quanlydetai.entity.GiangVien;
import com.ute.quanlydetai.entity.HoiDong;
import com.ute.quanlydetai.entity.ThanhVienHoiDong;

import com.ute.quanlydetai.repository.GiangVienRepository;
import com.ute.quanlydetai.repository.HoiDongRepository;
import com.ute.quanlydetai.repository.ThanhVienHoiDongRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ThanhVienHoiDongController {

    private final ThanhVienHoiDongRepository thanhVienHoiDongRepository;
    private final HoiDongRepository hoiDongRepository;
    private final GiangVienRepository giangVienRepository;

    public ThanhVienHoiDongController(
            ThanhVienHoiDongRepository thanhVienHoiDongRepository,
            HoiDongRepository hoiDongRepository,
            GiangVienRepository giangVienRepository) {

        this.thanhVienHoiDongRepository = thanhVienHoiDongRepository;
        this.hoiDongRepository = hoiDongRepository;
        this.giangVienRepository = giangVienRepository;
    }

    @GetMapping("/hoidong/{id}/thanhvien")
    public String danhSachThanhVien(
            @PathVariable Long id,
            Model model) {

        HoiDong hoiDong = hoiDongRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Không tìm thấy hội đồng"));

        model.addAttribute("hoiDong", hoiDong);

        model.addAttribute(
                "dsThanhVien",
                thanhVienHoiDongRepository.findByHoiDongId(id)
        );

        return "thanhvien-hoidong";
    }

    @GetMapping("/hoidong/{id}/thanhvien/them")
    public String hienThiFormThem(
            @PathVariable Long id,
            Model model) {

        HoiDong hoiDong = hoiDongRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Không tìm thấy hội đồng"));

        model.addAttribute("hoiDong", hoiDong);
        model.addAttribute("dsGiangVien", giangVienRepository.findAll());

        return "them-thanhvien-hoidong";
    }

    @PostMapping("/hoidong/{id}/thanhvien/them")
    public String themThanhVien(
            @PathVariable Long id,
            @RequestParam Long giangVienId,
            @RequestParam String vaiTro,
            Model model) {

        HoiDong hoiDong = hoiDongRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Không tìm thấy hội đồng"));

        GiangVien giangVien = giangVienRepository.findById(giangVienId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Không tìm thấy giảng viên"));

        List<ThanhVienHoiDong> dsThanhVien =
                thanhVienHoiDongRepository.findByHoiDongId(id);

        if (dsThanhVien.size() >= 5) {
            return hienThiLoi(
                    hoiDong,
                    "Hội đồng chỉ được có tối đa 5 giảng viên.",
                    model
            );
        }

        for (ThanhVienHoiDong tv : dsThanhVien) {

            if (tv.getGiangVien().getId().equals(giangVienId)) {
                return hienThiLoi(
                        hoiDong,
                        "Giảng viên này đã có trong hội đồng.",
                        model
                );
            }

            if ("CHU_TICH".equals(vaiTro)
                    && "CHU_TICH".equals(tv.getVaiTro())) {

                return hienThiLoi(
                        hoiDong,
                        "Hội đồng đã có Chủ tịch.",
                        model
                );
            }

            if ("THU_KY".equals(vaiTro)
                    && "THU_KY".equals(tv.getVaiTro())) {

                return hienThiLoi(
                        hoiDong,
                        "Hội đồng đã có Thư ký.",
                        model
                );
            }
        }

        ThanhVienHoiDong thanhVien = new ThanhVienHoiDong();

        thanhVien.setHoiDong(hoiDong);
        thanhVien.setGiangVien(giangVien);
        thanhVien.setVaiTro(vaiTro);

        thanhVienHoiDongRepository.save(thanhVien);

        return "redirect:/hoidong/" + id + "/thanhvien";
    }

    @GetMapping("/hoidong/{hoiDongId}/thanhvien/xoa/{id}")
    public String xoaThanhVien(
            @PathVariable Long hoiDongId,
            @PathVariable Long id) {

        thanhVienHoiDongRepository.deleteById(id);

        return "redirect:/hoidong/" + hoiDongId + "/thanhvien";
    }

    private String hienThiLoi(
            HoiDong hoiDong,
            String loi,
            Model model) {

        model.addAttribute("hoiDong", hoiDong);
        model.addAttribute("dsGiangVien", giangVienRepository.findAll());
        model.addAttribute("loi", loi);

        return "them-thanhvien-hoidong";
    }
}