package com.ute.quanlydetai.controller;

import com.ute.quanlydetai.entity.DeTai;
import com.ute.quanlydetai.entity.DiemDanhGia;
import com.ute.quanlydetai.entity.GiangVien;
import com.ute.quanlydetai.entity.HoiDong;
import com.ute.quanlydetai.entity.KetQuaDeTai;
import com.ute.quanlydetai.entity.TaiKhoan;
import com.ute.quanlydetai.entity.ThanhVienHoiDong;

import com.ute.quanlydetai.repository.DeTaiRepository;
import com.ute.quanlydetai.repository.DiemDanhGiaRepository;
import com.ute.quanlydetai.repository.HoiDongRepository;
import com.ute.quanlydetai.repository.KetQuaDeTaiRepository;
import com.ute.quanlydetai.repository.PhanCongHuongDanRepository;
import com.ute.quanlydetai.repository.PhanCongPhanBienRepository;
import com.ute.quanlydetai.repository.ThanhVienHoiDongRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class DiemDanhGiaController {

    private final DiemDanhGiaRepository diemDanhGiaRepository;
    private final DeTaiRepository deTaiRepository;
    private final PhanCongHuongDanRepository phanCongHuongDanRepository;
    private final PhanCongPhanBienRepository phanCongPhanBienRepository;
    private final HoiDongRepository hoiDongRepository;
    private final ThanhVienHoiDongRepository thanhVienHoiDongRepository;
    private final KetQuaDeTaiRepository ketQuaDeTaiRepository;

    public DiemDanhGiaController(
            DiemDanhGiaRepository diemDanhGiaRepository,
            DeTaiRepository deTaiRepository,
            PhanCongHuongDanRepository phanCongHuongDanRepository,
            PhanCongPhanBienRepository phanCongPhanBienRepository,
            HoiDongRepository hoiDongRepository,
            ThanhVienHoiDongRepository thanhVienHoiDongRepository,
            KetQuaDeTaiRepository ketQuaDeTaiRepository) {

        this.diemDanhGiaRepository = diemDanhGiaRepository;
        this.deTaiRepository = deTaiRepository;
        this.phanCongHuongDanRepository = phanCongHuongDanRepository;
        this.phanCongPhanBienRepository = phanCongPhanBienRepository;
        this.hoiDongRepository = hoiDongRepository;
        this.thanhVienHoiDongRepository = thanhVienHoiDongRepository;
        this.ketQuaDeTaiRepository = ketQuaDeTaiRepository;
    }

    @GetMapping("/diem")
    public String danhSachDiem(Model model) {

        model.addAttribute(
                "dsDiem",
                diemDanhGiaRepository.findAll()
        );

        return "diem";
    }

    @GetMapping("/diem/them")
    public String hienThiFormThem(
            HttpSession session,
            Model model) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan == null) {
            return "redirect:/dangnhap";
        }

        if (!"GIANG_VIEN".equals(taiKhoan.getVaiTro())) {

            return hienThiLoi(
                    "Chỉ giảng viên mới được chấm điểm.",
                    model
            );
        }

        if (taiKhoan.getGiangVien() == null) {

            return hienThiLoi(
                    "Tài khoản chưa được liên kết với giảng viên.",
                    model
            );
        }

        model.addAttribute(
                "giangVien",
                taiKhoan.getGiangVien()
        );

        model.addAttribute(
                "dsDeTai",
                deTaiRepository.findAll()
        );

        return "them-diem";
    }

    @PostMapping("/diem/them")
    public String themDiem(
            @RequestParam Long deTaiId,
            @RequestParam Double diem,
            @RequestParam(required = false) String nhanXet,
            HttpSession session,
            Model model) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan == null) {
            return "redirect:/dangnhap";
        }

        if (!"GIANG_VIEN".equals(taiKhoan.getVaiTro())) {

            return hienThiLoi(
                    "Chỉ giảng viên mới được chấm điểm.",
                    model
            );
        }

        if (taiKhoan.getGiangVien() == null) {

            return hienThiLoi(
                    "Tài khoản chưa được liên kết với giảng viên.",
                    model
            );
        }

        GiangVien giangVien =
                taiKhoan.getGiangVien();

        Long giangVienId =
                giangVien.getId();

        model.addAttribute(
                "giangVien",
                giangVien
        );

        DeTai deTai =
                deTaiRepository.findById(deTaiId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy đề tài"
                                )
                        );

        if (diem < 0 || diem > 10) {

            return hienThiLoi(
                    "Điểm phải nằm trong khoảng từ 0 đến 10.",
                    model
            );
        }

        if (phanCongHuongDanRepository
                .existsByDeTaiIdAndGiangVienId(
                        deTaiId,
                        giangVienId
                )) {

            return hienThiLoi(
                    "Không thể chấm điểm! Bạn đang hướng dẫn đề tài này.",
                    model
            );
        }

        if (!phanCongPhanBienRepository
                .existsByDeTaiIdAndGiangVienId(
                        deTaiId,
                        giangVienId
                )) {

            return hienThiLoi(
                    "Bạn không được phân công phản biện đề tài này.",
                    model
            );
        }

        if (deTai.getDotDangKy() != null
                && deTai.getDotDangKy().getHanGvpbNopDiem() != null) {

            LocalDateTime hanNopDiem =
                    deTai.getDotDangKy().getHanGvpbNopDiem();

            if (LocalDateTime.now().isAfter(hanNopDiem)) {

                return hienThiLoi(
                        "Đã quá hạn nộp điểm phản biện.",
                        model
                );
            }
        }

        if (diemDanhGiaRepository
                .existsByDeTaiIdAndGiangVienId(
                        deTaiId,
                        giangVienId
                )) {

            return hienThiLoi(
                    "Bạn đã chấm điểm đề tài này rồi.",
                    model
            );
        }

        DiemDanhGia danhGia =
                new DiemDanhGia();

        danhGia.setDeTai(deTai);
        danhGia.setGiangVien(giangVien);
        danhGia.setDiem(diem);
        danhGia.setNhanXet(nhanXet);
        danhGia.setNgayCham(LocalDateTime.now());

        diemDanhGiaRepository.save(danhGia);

        return "redirect:/diem";
    }

    @GetMapping("/diem/xoa/{id}")
    public String xoaDiem(
            @PathVariable Long id) {

        diemDanhGiaRepository.deleteById(id);

        return "redirect:/diem";
    }

    @GetMapping("/diem/ketqua/{deTaiId}")
    public String xemKetQua(
            @PathVariable Long deTaiId,
            HttpSession session,
            Model model) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan == null) {
            return "redirect:/dangnhap";
        }

        DeTai deTai =
                deTaiRepository.findById(deTaiId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy đề tài"
                                )
                        );

        Optional<KetQuaDeTai> ketQuaOptional =
                ketQuaDeTaiRepository.findByDeTaiId(
                        deTaiId
                );

        if ("SINH_VIEN".equals(taiKhoan.getVaiTro())) {

            if (ketQuaOptional.isEmpty()
                    || !"DA_CONG_BO".equals(
                            ketQuaOptional.get().getTrangThai())) {

                model.addAttribute(
                        "deTai",
                        deTai
                );

                model.addAttribute(
                        "loi",
                        "Kết quả đề tài chưa được công bố."
                );

                model.addAttribute(
                        "dsDiem",
                        List.of()
                );

                model.addAttribute(
                        "ketQuaCuoi",
                        null
                );

                return "ketqua-diem";
            }
        }

        List<DiemDanhGia> dsDiem =
                diemDanhGiaRepository.findByDeTaiId(
                        deTaiId
                );

        model.addAttribute(
                "deTai",
                deTai
        );

        model.addAttribute(
                "dsDiem",
                dsDiem
        );

        model.addAttribute(
                "ketQuaCuoi",
                ketQuaOptional.orElse(null)
        );

        return "ketqua-diem";
    }

    @GetMapping("/diem/congbo/{deTaiId}")
    public String congBoKetQua(
            @PathVariable Long deTaiId,
            HttpSession session) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan == null) {
            return "redirect:/dangnhap";
        }

        if (!"ADMIN".equals(taiKhoan.getVaiTro())) {
            return "redirect:/khong-co-quyen";
        }

        KetQuaDeTai ketQua =
                ketQuaDeTaiRepository
                        .findByDeTaiId(deTaiId)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Đề tài chưa được tổng hợp kết quả."
                                )
                        );

        if ("DA_TONG_HOP".equals(
                ketQua.getTrangThai())) {

            ketQua.setTrangThai(
                    "DA_CONG_BO"
            );

            ketQuaDeTaiRepository.save(
                    ketQua
            );
        }

        return "redirect:/diem/ketqua/"
                + deTaiId;
    }

    @PostMapping("/diem/tonghop/{deTaiId}")
    public String tongHopKetQua(
            @PathVariable Long deTaiId,
            HttpSession session,
            Model model) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan == null) {
            return "redirect:/dangnhap";
        }

        if (!"GIANG_VIEN".equals(taiKhoan.getVaiTro())) {

            return hienThiLoiTongHop(
                    deTaiId,
                    "Chỉ giảng viên mới được tổng hợp kết quả.",
                    model
            );
        }

        if (taiKhoan.getGiangVien() == null) {

            return hienThiLoiTongHop(
                    deTaiId,
                    "Tài khoản chưa được liên kết với giảng viên.",
                    model
            );
        }

        Optional<HoiDong> hoiDongOptional =
                hoiDongRepository.findFirstByDeTaiId(
                        deTaiId
                );

        if (hoiDongOptional.isEmpty()) {

            return hienThiLoiTongHop(
                    deTaiId,
                    "Đề tài chưa được phân công hội đồng phản biện.",
                    model
            );
        }

        HoiDong hoiDong =
                hoiDongOptional.get();

        List<ThanhVienHoiDong> dsThanhVienHoiDong =
                thanhVienHoiDongRepository.findByHoiDongId(
                        hoiDong.getId()
                );

        if (dsThanhVienHoiDong.size() < 3) {

            return hienThiLoiTongHop(
                    deTaiId,
                    "Hội đồng phải có ít nhất 3 thành viên.",
                    model
            );
        }

        if (dsThanhVienHoiDong.size() > 5) {

            return hienThiLoiTongHop(
                    deTaiId,
                    "Hội đồng không được vượt quá 5 thành viên.",
                    model
            );
        }

        Optional<ThanhVienHoiDong> thuKyOptional =
                thanhVienHoiDongRepository
                        .findFirstByHoiDongIdAndVaiTro(
                                hoiDong.getId(),
                                "THU_KY"
                        );

        if (thuKyOptional.isEmpty()) {

            return hienThiLoiTongHop(
                    deTaiId,
                    "Hội đồng chưa có Thư ký.",
                    model
            );
        }

        Optional<ThanhVienHoiDong> chuTichOptional =
                thanhVienHoiDongRepository
                        .findFirstByHoiDongIdAndVaiTro(
                                hoiDong.getId(),
                                "CHU_TICH"
                        );

        if (chuTichOptional.isEmpty()) {

            return hienThiLoiTongHop(
                    deTaiId,
                    "Hội đồng chưa có Chủ tịch.",
                    model
            );
        }

        ThanhVienHoiDong chuTich =
                chuTichOptional.get();

        if (chuTich.getGiangVien() == null) {

            return hienThiLoiTongHop(
                    deTaiId,
                    "Chủ tịch hội đồng chưa được liên kết với giảng viên.",
                    model
            );
        }

        if (!chuTich.getGiangVien()
                .getId()
                .equals(
                        taiKhoan.getGiangVien().getId()
                )) {

            return hienThiLoiTongHop(
                    deTaiId,
                    "Chỉ Chủ tịch hội đồng mới được tổng hợp kết quả.",
                    model
            );
        }

        List<DiemDanhGia> dsDiem =
                diemDanhGiaRepository.findByDeTaiId(
                        deTaiId
                );

        if (dsDiem.isEmpty()) {

            return hienThiLoiTongHop(
                    deTaiId,
                    "Đề tài chưa có điểm đánh giá.",
                    model
            );
        }

        double tongDiem = 0;
        int soDiem = 0;

        for (DiemDanhGia dg : dsDiem) {

            if (dg.getDiem() != null) {

                tongDiem += dg.getDiem();

                soDiem++;
            }
        }

        if (soDiem == 0) {

            return hienThiLoiTongHop(
                    deTaiId,
                    "Đề tài chưa có điểm hợp lệ.",
                    model
            );
        }

        double diemCuoi =
                tongDiem / soDiem;

        DeTai deTai =
                deTaiRepository.findById(deTaiId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy đề tài"
                                )
                        );

        KetQuaDeTai ketQua =
                ketQuaDeTaiRepository
                        .findByDeTaiId(deTaiId)
                        .orElse(new KetQuaDeTai());

        ketQua.setDeTai(deTai);

        ketQua.setChuTich(
                taiKhoan.getGiangVien()
        );

        ketQua.setDiemCuoi(
                diemCuoi
        );

        ketQua.setNgayTongHop(
                LocalDateTime.now()
        );

        ketQua.setTrangThai(
                "DA_TONG_HOP"
        );

        ketQuaDeTaiRepository.save(
                ketQua
        );

        return "redirect:/diem/ketqua/"
                + deTaiId;
    }

    private String hienThiLoi(
            String loi,
            Model model) {

        model.addAttribute(
                "loi",
                loi
        );

        model.addAttribute(
                "dsDeTai",
                deTaiRepository.findAll()
        );

        return "them-diem";
    }

    private String hienThiLoiTongHop(
            Long deTaiId,
            String loi,
            Model model) {

        DeTai deTai =
                deTaiRepository.findById(deTaiId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy đề tài"
                                )
                        );

        List<DiemDanhGia> dsDiem =
                diemDanhGiaRepository.findByDeTaiId(
                        deTaiId
                );

        Optional<KetQuaDeTai> ketQua =
                ketQuaDeTaiRepository.findByDeTaiId(
                        deTaiId
                );

        model.addAttribute(
                "loi",
                loi
        );

        model.addAttribute(
                "deTai",
                deTai
        );

        model.addAttribute(
                "dsDiem",
                dsDiem
        );

        model.addAttribute(
                "ketQuaCuoi",
                ketQua.orElse(null)
        );

        return "ketqua-diem";
    }
}