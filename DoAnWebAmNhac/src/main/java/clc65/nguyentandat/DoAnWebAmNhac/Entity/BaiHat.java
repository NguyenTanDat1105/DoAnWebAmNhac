package clc65.nguyentandat.DoAnWebAmNhac.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "baihat")
public class BaiHat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaBaiHat") // Ép cấu hình khớp với cột MaBaiHat trong DB
    private Integer maBaiHat;
    
    @Column(name = "tenBaiHat") // Ép cấu hình khớp với cột tên CamelCase gốc
    private String tenBaiHat;
    
    @Column(name = "nguoiTrinhBay") // Ép cấu hình khớp với cột tên CamelCase gốc
    private String nguoiTrinhBay;
    
    @Column(name = "linkYoutube") // Ép cấu hình khớp với cột tên CamelCase gốc
    private String linkYoutube;
    
    @Column(name = "TrangThaiDuyet")
    private Integer trangThaiDuyet;
    
    // Tạo các hàm Getter và Setter (Bắt buộc để Spring Boot đọc được dữ liệu)
    public Integer getMaBaiHat() {
        return maBaiHat;
    }
    public void setMaBaiHat(Integer maBaiHat) {
        this.maBaiHat = maBaiHat;
    }
    public String getTenBaiHat() {
        return tenBaiHat;
    }
    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }
    public String getNguoiTrinhBay() {
        return nguoiTrinhBay;
    }
    public void setNguoiTrinhBay(String nguoiTrinhBay) {
        this.nguoiTrinhBay = nguoiTrinhBay;
    }
    public String getLinkYoutube() {
        return linkYoutube;
    }
    public void setLinkYoutube(String linkYoutube) {
        this.linkYoutube = linkYoutube;
    }
    public Integer getTrangThaiDuyet() {
        return trangThaiDuyet;
    }
    public void setTrangThaiDuyet(Integer trangThaiDuyet) {
        this.trangThaiDuyet = trangThaiDuyet;
    }
    
}