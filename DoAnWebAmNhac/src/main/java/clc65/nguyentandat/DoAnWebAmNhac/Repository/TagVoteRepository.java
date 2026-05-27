package clc65.nguyentandat.DoAnWebAmNhac.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import clc65.nguyentandat.DoAnWebAmNhac.Entity.TagVote;

@Repository
public interface TagVoteRepository extends JpaRepository<TagVote, Integer>{
	// Hàm quan trọng: Đếm xem một bài hát cụ thể có bao nhiêu lượt vote cho một Tag cụ thể
    int countByMaBaiHatAndMaTag(Integer maBaiHat, Integer maTag);
    
    // Hàm kiểm tra nâng cao: Xem người dùng này đã từng vote cho bài hát này chưa (tránh spam vote)
    TagVote findByMaBaiHatAndMaNguoiDung(Integer maBaiHat, Integer maNguoiDung);
    
    TagVote findByMaBaiHatAndMaTagAndMaNguoiDung(Integer maBaiHat, Integer maTag, Integer maNguoiDung);
}
