package clc65.nguyentandat.DoAnWebAmNhac.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "binhluanvote")
public class BinhLuanVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "MaBinhLuan")
    private Integer maBinhLuan;

    @Column(name = "MaNguoiDung")
    private Integer maNguoiDung;

    // Sử dụng Integer để khớp với kiểu tinyint(1) trong CSDL (ví dụ: 1 là Upvote, 0 hoặc -1 là Downvote)
    @Column(name = "IsUpvote")
    private Integer isUpvote;

    @Column(name = "NgayVote")
    private LocalDateTime ngayVote;

    // ==========================================
    // GETTERS VÀ SETTERS
    // ==========================================
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getMaBinhLuan() { return maBinhLuan; }
    public void setMaBinhLuan(Integer maBinhLuan) { this.maBinhLuan = maBinhLuan; }

    public Integer getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(Integer maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public Integer getIsUpvote() { return isUpvote; }
    public void setIsUpvote(Integer isUpvote) { this.isUpvote = isUpvote; }

    public LocalDateTime getNgayVote() { return ngayVote; }
    public void setNgayVote(LocalDateTime ngayVote) { this.ngayVote = ngayVote; }
}
