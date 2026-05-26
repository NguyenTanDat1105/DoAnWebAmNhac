package clc65.nguyentandat.DoAnWebAmNhac.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import clc65.nguyentandat.DoAnWebAmNhac.Entity.BaiHat;

@Repository
public interface BaiHatRepository extends JpaRepository<BaiHat, Integer> {
	
	List<BaiHat> findByTrangThaiDuyet(Integer trangThaiDuyet);
	List<BaiHat> findByTenBaiHatContainingAndTrangThaiDuyet(String tenBaiHat, Integer trangThaiDuyet);
	List<BaiHat> findByNguoiTrinhBayContainingAndTrangThaiDuyet(String nguoiTrinhBay, Integer trangThaiDuyet);
}