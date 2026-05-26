package clc65.nguyentandat.DoAnWebAmNhac.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import clc65.nguyentandat.DoAnWebAmNhac.Entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    // Nơi đây dùng để lấy toàn bộ danh sách các thể loại nhạc lên giao diện
}
