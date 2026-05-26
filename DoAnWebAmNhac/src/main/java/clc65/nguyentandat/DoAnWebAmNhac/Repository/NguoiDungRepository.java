package clc65.nguyentandat.DoAnWebAmNhac.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import clc65.nguyentandat.DoAnWebAmNhac.Entity.NguoiDung;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
    
    NguoiDung findByTenDangNhap(String tenDangNhap);  
}