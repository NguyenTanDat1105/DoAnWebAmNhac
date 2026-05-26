package clc65.nguyentandat.DoAnWebAmNhac.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "nguoidung") // Khớp hoàn toàn với bảng bên trái trong hình ERD của bạn
public class NguoiDung {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNguoiDung")
    private Integer maNguoiDung;

    @Column(name = "TenDangNhap", length = 50)
    private String tenDangNhap;

    @Column(name = "MatKhau", length = 100)
    private String matKhau;

    @Column(name = "HoTen", length = 100)
    private String hoTen;

    @Column(name = "AnhDaiDien")
    private String anhDaiDien;

    @Column(name = "PhanQuyen") 
    private Integer phanQuyen; 

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao; 

    // ==========================================
    // GETTERS VÀ SETTERS
    // ==========================================
    public Integer getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(Integer maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getAnhDaiDien() { return anhDaiDien; }
    public void setAnhDaiDien(String anhDaiDien) { this.anhDaiDien = anhDaiDien; }

    public Integer getPhanQuyen() { return phanQuyen; }
    public void setPhanQuyen(Integer phanQuyen) { this.phanQuyen = phanQuyen; }

    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }
}