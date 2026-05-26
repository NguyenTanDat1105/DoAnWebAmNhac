package clc65.nguyentandat.DoAnWebAmNhac.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "binhluan") 
public class BinhLuan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaBinhLuan")
    private Integer maBinhLuan;

    @Column(name = "MaBaiHat")
    private Integer maBaiHat;

    @Column(name = "MaNguoiDung")
    private Integer maNguoiDung;

    @Column(name = "NoiDung", columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "NgayBinhLuan")
    private LocalDateTime ngayBinhLuan;

    // ==========================================
    // GETTERS VÀ SETTERS
    // ==========================================
    public Integer getMaBinhLuan() { return maBinhLuan; }
    public void setMaBinhLuan(Integer maBinhLuan) { this.maBinhLuan = maBinhLuan; }

    public Integer getMaBaiHat() { return maBaiHat; }
    public void setMaBaiHat(Integer maBaiHat) { this.maBaiHat = maBaiHat; }

    public Integer getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(Integer maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public LocalDateTime getNgayBinhLuan() { return ngayBinhLuan; }
    public void setNgayBinhLuan(LocalDateTime ngayBinhLuan) { this.ngayBinhLuan = ngayBinhLuan; }
}
