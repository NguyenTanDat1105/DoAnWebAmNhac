package clc65.nguyentandat.DoAnWebAmNhac.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import clc65.nguyentandat.DoAnWebAmNhac.Entity.BinhLuanVote;

@Repository
public interface BinhLuanVoteRepository extends JpaRepository<BinhLuanVote, Integer> {
    
    // Đếm xem bình luận này có bao nhiêu lượt bấm Upvote (với IsUpvote = 1)
    int countByMaBinhLuanAndIsUpvote(Integer maBinhLuan, Integer isUpvote);
    
    BinhLuanVote findByMaBinhLuanAndMaNguoiDung(Integer maBinhLuan, Integer maNguoiDung);
}
