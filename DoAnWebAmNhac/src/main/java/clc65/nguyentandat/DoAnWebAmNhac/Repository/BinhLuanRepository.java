package clc65.nguyentandat.DoAnWebAmNhac.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import clc65.nguyentandat.DoAnWebAmNhac.Entity.BinhLuan;

@Repository
public interface BinhLuanRepository extends JpaRepository<BinhLuan, Integer> {
    
    // Tìm các bình luận thuộc về một bài hát và sắp xếp cái mới nhất lên đầu
    List<BinhLuan> findByMaBaiHatOrderByNgayBinhLuanDesc(Integer maBaiHat);
}
