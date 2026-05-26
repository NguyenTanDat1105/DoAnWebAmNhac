package clc65.nguyentandat.DoAnWebAmNhac.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tag") // Khớp với bảng tag trong phpMyAdmin của bạn
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTag")
    private Integer maTag;

    @Column(name = "TenTag", length = 50)
    private String tenTag;

    @Column(name = "LaGoiY")
    private Integer laGoiY; 

    // ==========================================
    // GETTERS VÀ SETTERS
    // ==========================================
    public Integer getMaTag() { return maTag; }
    public void setMaTag(Integer maTag) { this.maTag = maTag; }

    public String getTenTag() { return tenTag; }
    public void setTenTag(String tenTag) { this.tenTag = tenTag; }

    public Integer getLaGoiY() { return laGoiY; }
    public void setLaGoiY(Integer laGoiY) { this.laGoiY = laGoiY; }
}