package clc65.nguyentandat.DoAnWebAmNhac;

import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import clc65.nguyentandat.DoAnWebAmNhac.Repository.BaiHatRepository;
import clc65.nguyentandat.DoAnWebAmNhac.Repository.NguoiDungRepository;
import clc65.nguyentandat.DoAnWebAmNhac.Repository.TagRepository;
import clc65.nguyentandat.DoAnWebAmNhac.Repository.TagVoteRepository;
import clc65.nguyentandat.DoAnWebAmNhac.Repository.BinhLuanRepository;
import clc65.nguyentandat.DoAnWebAmNhac.Repository.BinhLuanVoteRepository;

import clc65.nguyentandat.DoAnWebAmNhac.Entity.BaiHat;
import clc65.nguyentandat.DoAnWebAmNhac.Entity.NguoiDung;
import clc65.nguyentandat.DoAnWebAmNhac.Entity.BinhLuan;
import clc65.nguyentandat.DoAnWebAmNhac.Entity.BinhLuanVote;
import clc65.nguyentandat.DoAnWebAmNhac.Entity.TagVote;

import jakarta.servlet.http.HttpSession;

@Controller
public class WebController {

    @Autowired
    private BaiHatRepository baiHatRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagVoteRepository tagVoteRepository;

    @Autowired
    private BinhLuanRepository binhLuanRepository;

    @Autowired
    private BinhLuanVoteRepository binhLuanVoteRepository;

    // ==========================================
    // 1. TRANG CHỦ & TÌM KIẾM
    // ==========================================
    @GetMapping("/")
    public String showIndex(ModelMap m, @RequestParam(name = "tuKhoa", required = false) String tuKhoa, HttpSession session) {
        List<BaiHat> ds;
        if (tuKhoa != null && !tuKhoa.trim().isEmpty()) {
            ds = baiHatRepository.findByTenBaiHatContainingAndTrangThaiDuyet(tuKhoa, 1);
            if (ds.isEmpty()) {
                ds = baiHatRepository.findByNguoiTrinhBayContainingAndTrangThaiDuyet(tuKhoa, 1);
            }
            m.addAttribute("tuKhoaDaTim", tuKhoa);
        } else {
            ds = baiHatRepository.findByTrangThaiDuyet(1);
            m.addAttribute("tuKhoaDaTim", null); 
        }
        m.addAttribute("listBaiHat", ds);

        NguoiDung userLogged = (NguoiDung) session.getAttribute("userLogged");
        m.addAttribute("userLogged", userLogged);

        return "index";
    }
    
    // ==========================================
    // 2. PHÁT NHẠC & HIỂN THỊ VOTE & BÌNH LUẬN
    // ==========================================
    @GetMapping("/play/{id}")
    public String xemChiTietVaPhatNhac(@PathVariable("id") Integer id, ModelMap m, HttpSession session) {
        BaiHat bh = baiHatRepository.findById(id).orElse(null);
        if (bh != null) {
            // --- XỬ LÝ LINK EMBED YOUTUBE ---
            String urlGoc = bh.getLinkYoutube();
            String videoId = "";
            if (urlGoc != null) {
                urlGoc = urlGoc.trim();
                if (urlGoc.contains("watch?v=")) {
                    videoId = urlGoc.substring(urlGoc.indexOf("watch?v=") + 8);
                    if (videoId.contains("&")) {
                        videoId = videoId.substring(0, videoId.indexOf("&"));
                    }
                } else if (urlGoc.contains("youtu.be/")) {
                    videoId = urlGoc.substring(urlGoc.indexOf("youtu.be/") + 9);
                    if (videoId.contains("?")) {
                        videoId = videoId.substring(0, videoId.indexOf("?"));
                    }
                } else {
                    videoId = urlGoc;
                }
            }
            String urlNhung = "https://www.youtube.com/embed/" + videoId + "?autoplay=1";
            m.addAttribute("baiHat", bh);
            m.addAttribute("linkEmbed", urlNhung);

            // Lấy thông tin người dùng đang đăng nhập hiện tại
            NguoiDung userLogged = (NguoiDung) session.getAttribute("userLogged");
            m.addAttribute("userLogged", userLogged);

            // --- XỬ LÝ LOGIC ĐẾM VOTE THỂ LOẠI & TRẠNG THÁI ACTIVE ---
            List<clc65.nguyentandat.DoAnWebAmNhac.Entity.Tag> dsTag = tagRepository.findAll();
            Map<Integer, Integer> banDoLuotVote = new HashMap<>();
            Set<Integer> tapHopTagDaVote = new HashSet<>(); // Lưu các mã tag mà user này đã vote cho bài hát này
            
            for (clc65.nguyentandat.DoAnWebAmNhac.Entity.Tag tag : dsTag) {
                // Tính tổng số lượt vote của từng tag cho bài hát hiện tại
                int soVote = tagVoteRepository.countByMaBaiHatAndMaTag(id, tag.getMaTag());
                banDoLuotVote.put(tag.getMaTag(), soVote);
                
                // Nếu người dùng đã đăng nhập, kiểm tra xem họ đã vote tag này chưa để hiển thị màu sáng lập tức
                if (userLogged != null) {
                    TagVote voteTonTai = tagVoteRepository.findByMaBaiHatAndMaTagAndMaNguoiDung(id, tag.getMaTag(), userLogged.getMaNguoiDung());
                    if (voteTonTai != null) {
                        tapHopTagDaVote.add(tag.getMaTag());
                    }
                }
            }
            m.addAttribute("listTag", dsTag);
            m.addAttribute("banDoLuotVote", banDoLuotVote); 
            m.addAttribute("tapHopTagDaVote", tapHopTagDaVote);

            // --- XỬ LÝ HỆ THỐNG BÌNH LUẬN & UPVOTE ---
            List<BinhLuan> dsBinhLuan = binhLuanRepository.findByMaBaiHatOrderByNgayBinhLuanDesc(id);
            Map<Integer, Integer> banDoVoteBinhLuan = new HashMap<>();
            Map<Integer, String> tenNguoiBinhLuan = new HashMap<>();
            
            for (BinhLuan bl : dsBinhLuan) {
                int soUpvote = binhLuanVoteRepository.countByMaBinhLuanAndIsUpvote(bl.getMaBinhLuan(), 1);
                banDoVoteBinhLuan.put(bl.getMaBinhLuan(), soUpvote);
                
                NguoiDung nd = nguoiDungRepository.findById(bl.getMaNguoiDung()).orElse(null);
                if (nd != null) {
                    tenNguoiBinhLuan.put(bl.getMaBinhLuan(), nd.getHoTen());
                } else {
                    tenNguoiBinhLuan.put(bl.getMaBinhLuan(), "Người dùng ẩn danh");
                }
            }
            
            m.addAttribute("listBinhLuan", dsBinhLuan);
            m.addAttribute("banDoVoteBinhLuan", banDoVoteBinhLuan);
            m.addAttribute("tenNguoiBinhLuan", tenNguoiBinhLuan);

        } else {
            return "redirect:/";
        }
        return "play";
    }
    
    // ==========================================
    // 3. QUẢN LÝ BÀI HÁT (FULL CRUD)
    // ==========================================
    @GetMapping("/them-bai-hat")
    public String showFormThem(ModelMap m, HttpSession session) {
        NguoiDung userLogged = (NguoiDung) session.getAttribute("userLogged");
        if (userLogged == null || userLogged.getPhanQuyen() == 0) {
            return "redirect:/dang-nhap";
        }
        m.addAttribute("baiHatMoi", new BaiHat());
        return "thembaihat"; 
    }

    @PostMapping("/them-bai-hat")
    public String saveBaiHat(@ModelAttribute("baiHatMoi") BaiHat baiHat, HttpSession session) {
        NguoiDung userLogged = (NguoiDung) session.getAttribute("userLogged");
        if (userLogged == null || userLogged.getPhanQuyen() == 0) {
            return "redirect:/dang-nhap";
        }
        baiHat.setTrangThaiDuyet(1); 
        baiHatRepository.save(baiHat);
        return "redirect:/"; 
    }

    @GetMapping("/sua-bai-hat/{id}")
    public String showFormSua(@PathVariable("id") Integer id, ModelMap m, HttpSession session) {
        NguoiDung userLogged = (NguoiDung) session.getAttribute("userLogged");
        if (userLogged == null || userLogged.getPhanQuyen() != 2) {
            return "redirect:/";
        }
        BaiHat bh = baiHatRepository.findById(id).orElse(null);
        if (bh == null) {
            return "redirect:/admin";
        }
        m.addAttribute("baiHatCanSua", bh);
        return "suabaihat"; 
    }

    @PostMapping("/sua-bai-hat")
    public String thucHienCapNhat(@ModelAttribute("baiHatCanSua") BaiHat baiHat, HttpSession session) {
        NguoiDung userLogged = (NguoiDung) session.getAttribute("userLogged");
        if (userLogged == null || userLogged.getPhanQuyen() != 2) {
            return "redirect:/";
        }
        baiHat.setTrangThaiDuyet(1);
        baiHatRepository.save(baiHat);
        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String showAdminPage(ModelMap m, HttpSession session) {
        NguoiDung userLogged = (NguoiDung) session.getAttribute("userLogged");
        if (userLogged == null || userLogged.getPhanQuyen() != 2) {
            return "redirect:/";
        }
        List<BaiHat> dsToanBo = baiHatRepository.findAll();
        m.addAttribute("listAdmin", dsToanBo);
        return "admin"; 
    }

    @GetMapping("/delete/{id}")
    public String deleteBaiHat(@PathVariable("id") Integer id, HttpSession session) {
        NguoiDung userLogged = (NguoiDung) session.getAttribute("userLogged");
        if (userLogged == null || userLogged.getPhanQuyen() != 2) {
            return "redirect:/";
        }
        baiHatRepository.deleteById(id);
        return "redirect:/admin";
    }

    // ==========================================
    // 4. CHỨC NĂNG ĐĂNG KÝ TÀI KHOẢN
    // ==========================================
    @GetMapping("/dang-ky")
    public String showFormDangKy(ModelMap m) {
        m.addAttribute("nguoiDungMoi", new NguoiDung());
        return "dangky";
    }

    @PostMapping("/dang-ky")
    public String thucHienDangKy(@ModelAttribute("nguoiDungMoi") NguoiDung user, ModelMap m) {
        NguoiDung checkTontai = nguoiDungRepository.findByTenDangNhap(user.getTenDangNhap());
        if (checkTontai != null) {
            m.addAttribute("error", "Tên đăng nhập này đã tồn tại trên hệ thống!");
            return "dangky"; 
        }
        
        user.setNgayTao(LocalDateTime.now()); 
        user.setAnhDaiDien("default_avatar.png"); 
        
        if (user.getPhanQuyen() == null) {
            user.setPhanQuyen(0); 
        } else if (user.getPhanQuyen() == 2) {
            user.setPhanQuyen(0); 
        }
        
        nguoiDungRepository.save(user);
        return "redirect:/dang-nhap";
    }

    // ==========================================
    // 5. CHỨC NĂNG ĐĂNG NHẬP & ĐĂNG XUẤT
    // ==========================================
    @GetMapping("/dang-nhap")
    public String showFormDangNhap() {
        return "dangnhap"; 
    }

    @PostMapping("/dang-nhap")
    public String thucHienDangNhap(
            @RequestParam("txtUsername") String username,
            @RequestParam("txtPassword") String password,
            HttpSession session, 
            ModelMap m) {
        NguoiDung userExist = nguoiDungRepository.findByTenDangNhap(username);
        if (userExist != null && userExist.getMatKhau().equals(password)) {
            session.setAttribute("userLogged", userExist);
            return "redirect:/";
        } else {
            m.addAttribute("error", "Tài khoản hoặc mật khẩu không chính xác!");
            return "dangnhap";
        }
    }

    @GetMapping("/dang-xuat")
    public String thucHienDangXuat(HttpSession session) {
        session.invalidate(); 
        return "redirect:/";
    }
    
    // ==========================================
    // ✨ 6. XỬ LÝ BÌNH CHỌN (VOTE) THỂ LOẠI QUA AJAX (CẬP NHẬT: TOGGLE VOTE / UNVOTE)
    // ==========================================
    @PostMapping("/vote-the-loai")
    @ResponseBody
    public ResponseEntity<?> xuLyVote(@RequestParam("maBaiHat") Integer maBaiHat, 
                                      @RequestParam("maTag") Integer maTag,
                                      HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        NguoiDung userLogged = (NguoiDung) session.getAttribute("userLogged");
        
        if (userLogged == null) {
            return ResponseEntity.status(401).body("Bạn chưa đăng nhập!");
        }

        // Bước 1: Kiểm tra xem người dùng hiện tại đã bình chọn cho tag này tại bài hát này chưa
        TagVote voteDaTonTai = tagVoteRepository.findByMaBaiHatAndMaTagAndMaNguoiDung(maBaiHat, maTag, userLogged.getMaNguoiDung());
        
        boolean currentVotedStatus;
        
        if (voteDaTonTai != null) {
            // 👉 NẾU ĐÃ VOTE -> Người dùng nhấn lần 2 -> Tiến hành HỦY VOTE (Xóa khỏi CSDL)
            tagVoteRepository.delete(voteDaTonTai);
            currentVotedStatus = false; // Trạng thái hiện tại: Đã bỏ chọn
        } else {
            // 👉 NẾU CHƯA VOTE -> Người dùng nhấn lần 1 -> Tiến hành THÊM MỚI lượt vote vào CSDL
            TagVote voteMoi = new TagVote();
            voteMoi.setMaBaiHat(maBaiHat);
            voteMoi.setMaTag(maTag);
            voteMoi.setMaNguoiDung(userLogged.getMaNguoiDung()); 
            voteMoi.setNgayVote(LocalDateTime.now());
            tagVoteRepository.save(voteMoi); 
            currentVotedStatus = true; // Trạng thái hiện tại: Đã chọn
        }

        // Bước 2: Đếm lại tổng số lượt bình chọn mới nhất của Tag này đối với bài hát
        int tongSoVoteMoi = tagVoteRepository.countByMaBaiHatAndMaTag(maBaiHat, maTag);

        // Bước 3: Gửi dữ liệu JSON phản hồi chính xác về cho JavaScript xử lý giao diện
        response.put("success", true);
        response.put("voted", currentVotedStatus); // Trả về true (Vừa kích hoạt) hoặc false (Vừa hủy)
        response.put("newVoteCount", tongSoVoteMoi); // Con số tổng số lượt vote mới nhất
        
        return ResponseEntity.ok(response);
    }

    // ==========================================
    // 7. XỬ LÝ THÊM BÌNH LUẬN MỚI QUA AJAX
    // ==========================================
    @PostMapping("/gui-binh-luan")
    @ResponseBody 
    public ResponseEntity<?> guiBinhLuan(@RequestParam("maBaiHat") Integer maBaiHat,
                                         @RequestParam("noiDung") String noiDung,
                                         HttpSession session) {
        if (noiDung == null || noiDung.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Nội dung không được để trống");
        }

        NguoiDung userLogged = (NguoiDung) session.getAttribute("userLogged");
        if (userLogged == null) {
            return ResponseEntity.status(401).body("Bạn chưa đăng nhập!");
        }

        BinhLuan bl = new BinhLuan();
        bl.setMaBaiHat(maBaiHat);
        bl.setMaNguoiDung(userLogged.getMaNguoiDung()); 
        bl.setNoiDung(noiDung.trim());
        bl.setNgayBinhLuan(LocalDateTime.now());
        BinhLuan savedBl = binhLuanRepository.save(bl);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("maBinhLuan", savedBl.getMaBinhLuan()); 
        response.put("hoTen", userLogged.getHoTen());
        response.put("noiDung", savedBl.getNoiDung());
        
        return ResponseEntity.ok(response);
    }

    // ==========================================
    // 8. XỬ LÝ THÍCH BÌNH LUẬN QUA AJAX (TOGGLE LIKE / UNLIKE)
    // ==========================================
    @PostMapping("/vote-binh-luan")
    @ResponseBody 
    public ResponseEntity<?> voteBinhLuan(@RequestParam("maBinhLuan") Integer maBinhLuan,
                                          HttpSession session) {
        
        NguoiDung userLogged = (NguoiDung) session.getAttribute("userLogged");
        if (userLogged == null) {
            return ResponseEntity.status(401).body("Bạn chưa đăng nhập!");
        }

        BinhLuanVote voteDaTonTai = binhLuanVoteRepository.findByMaBinhLuanAndMaNguoiDung(maBinhLuan, userLogged.getMaNguoiDung());
        boolean loggedLikedStatus;
        
        if (voteDaTonTai != null) {
            binhLuanVoteRepository.delete(voteDaTonTai);
            loggedLikedStatus = false; 
        } else {
            BinhLuanVote blVote = new BinhLuanVote();
            blVote.setMaBinhLuan(maBinhLuan);
            blVote.setMaNguoiDung(userLogged.getMaNguoiDung()); 
            blVote.setIsUpvote(1); 
            blVote.setNgayVote(LocalDateTime.now());
            binhLuanVoteRepository.save(blVote);
            loggedLikedStatus = true; 
        }

        int soUpvoteMoi = binhLuanVoteRepository.countByMaBinhLuanAndIsUpvote(maBinhLuan, 1);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("liked", loggedLikedStatus); 
        response.put("newLikeCount", soUpvoteMoi); 
        
        return ResponseEntity.ok(response);
    }
    // ==========================================
    // ✨ 9. XỬ LÝ SỬA BÌNH LUẬN QUA AJAX (CHỈ CHÍNH CHỦ)
    // ==========================================
    @PostMapping("/sua-binh-luan")
    @ResponseBody
    public ResponseEntity<?> suaBinhLuan(@RequestParam("maBinhLuan") Integer maBinhLuan,
                                         @RequestParam("noiDung") String noiDung,
                                         HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        NguoiDung userLogged = (NguoiDung) session.getAttribute("userLogged");
        
        if (userLogged == null) {
            return ResponseEntity.status(401).body("Bạn chưa đăng nhập!");
        }
        if (noiDung == null || noiDung.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Nội dung không được để trống!");
        }

        BinhLuan bl = binhLuanRepository.findById(maBinhLuan).orElse(null);
        if (bl == null) {
            return ResponseEntity.status(404).body("Không tìm thấy bình luận cần sửa!");
        }

        // KIỂM TRA QUYỀN: Chỉ có người viết ra bình luận đó mới được quyền sửa
        if (!bl.getMaNguoiDung().equals(userLogged.getMaNguoiDung())) {
            return ResponseEntity.status(403).body("Bạn không có quyền sửa bình luận này!");
        }

        bl.setNoiDung(noiDung.trim());
        binhLuanRepository.save(bl);

        response.put("success", true);
        response.put("noiDungMoi", bl.getNoiDung());
        return ResponseEntity.ok(response);
    }

    // ==========================================
    // ✨ 10. XỬ LÝ XÓA BÌNH LUẬN QUA AJAX (CHÍNH CHỦ HOẶC ADMIN)
    // ==========================================
    @PostMapping("/xoa-binh-luan")
    @ResponseBody
    public ResponseEntity<?> xoaBinhLuan(@RequestParam("maBinhLuan") Integer maBinhLuan,
                                         HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        NguoiDung userLogged = (NguoiDung) session.getAttribute("userLogged");
        
        if (userLogged == null) {
            return ResponseEntity.status(401).body("Bạn chưa đăng nhập!");
        }

        BinhLuan bl = binhLuanRepository.findById(maBinhLuan).orElse(null);
        if (bl == null) {
            return ResponseEntity.status(404).body("Không tìm thấy bình luận!");
        }

        // KIỂM TRA QUYỀN: Phải là Người viết bình luận HOẶC là Admin (phanQuyen == 2)
        if (!bl.getMaNguoiDung().equals(userLogged.getMaNguoiDung()) && userLogged.getPhanQuyen() != 2) {
            return ResponseEntity.status(403).body("Bạn không có quyền xóa bình luận này!");
        }

        try {
            // 1. Xóa sạch các dữ liệu liên quan ở bảng liên kết (BinhLuanVote) tránh lỗi Foreign Key
            binhLuanVoteRepository.deleteByMaBinhLuan(maBinhLuan);
            
            // 2. Tiến hành xóa bình luận chính thức
            binhLuanRepository.delete(bl);
            
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi hệ thống khi xóa bình luận: " + e.getMessage());
        }
    }
}