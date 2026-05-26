package clc65.nguyentandat.DoAnWebAmNhac.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tagvote") // Khớp với bảng tagvote trong phpMyAdmin của bạn
public class TagVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "MaBaiHat")
    private Integer maBaiHat;

    @Column(name = "MaTag")
    private Integer maTag;

    @Column(name = "MaNguoiDung")
    private Integer maNguoiDung;

    @Column(name = "NgayVote")
    private LocalDateTime ngayVote; // Tương thích với kiểu datetime của MySQL

    // ==========================================
    // GETTERS VÀ SETTERS
    // ==========================================
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getMaBaiHat() { return maBaiHat; }
    public void setMaBaiHat(Integer maBaiHat) { this.maBaiHat = maBaiHat; }

    public Integer getMaTag() { return maTag; }
    public void setMaTag(Integer maTag) { this.maTag = maTag; }

    public Integer getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(Integer maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public LocalDateTime getNgayVote() { return ngayVote; }
    public void setNgayVote(LocalDateTime ngayVote) { this.ngayVote = ngayVote; }
}
