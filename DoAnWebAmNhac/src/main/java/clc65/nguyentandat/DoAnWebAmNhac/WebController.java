package clc65.nguyentandat.DoAnWebAmNhac;

import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

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

            // --- XỬ LÝ LOGIC ĐẾM VOTE THỂ LOẠI ---
            List<clc65.nguyentandat.DoAnWebAmNhac.Entity.Tag> dsTag = tagRepository.findAll();
            Map<Integer, Integer> banDoLuotVote = new HashMap<>();
            for (clc65.nguyentandat.DoAnWebAmNhac.Entity.Tag tag : dsTag) {
                int soVote = tagVoteRepository.countByMaBaiHatAndMaTag(id, tag.getMaTag());
                banDoLuotVote.put(tag.getMaTag(), soVote);
            }
            m.addAttribute("listTag", dsTag);
            m.addAttribute("banDoLuotVote", banDoLuotVote); 

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

            NguoiDung userLogged = (NguoiDung) session.getAttribute("userLogged");
            m.addAttribute("userLogged", userLogged);

        } else {
            return "redirect:/";
        }
        return "play";
    }
    
    // ==========================================
    // 3. QUẢN LÝ BÀI HÁT (FULL CRUD - PHÂN QUYỀN AN TOÀN)
    // ==========================================
    
    // --- CHỨC NĂNG: THÊM BÀI HÁT (BẢO MẬT: CHỈ NGHỆ SĨ CHỈ SỐ 1 VÀ ADMIN CHỈ SỐ 2) ---
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

    // --- CHỨC NĂNG: SỬA BÀI HÁT (BẢO MẬT: CHỈ ADMIN QUYỀN SỐ 2) ---
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

    // --- CHỨC NĂNG: XEM TRANG QUẢN TRỊ ADMIN (BẢO MẬT: CHỈ ADMIN QUYỀN SỐ 2) ---
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

    // --- CHỨC NĂNG: XÓA BÀI HÁT (BẢO MẬT: CHỈ ADMIN QUYỀN SỐ 2) ---
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
    // 6. XỬ LÝ BÌNH CHỌN (VOTE) THỂ LOẠI
    // ==========================================
    @PostMapping("/vote-the-loai")
    public String xuLyVote(@RequestParam("maBaiHat") Integer maBaiHat, 
                           @RequestParam("maTag") Integer maTag,
                           HttpSession session) {
        
        NguoiDung userLogged = (NguoiDung) session.getAttribute("userLogged");
        if (userLogged == null) {
            return "redirect:/dang-nhap";
        }

        clc65.nguyentandat.DoAnWebAmNhac.Entity.TagVote voteMoi = new clc65.nguyentandat.DoAnWebAmNhac.Entity.TagVote();
        voteMoi.setMaBaiHat(maBaiHat);
        voteMoi.setMaTag(maTag);
        voteMoi.setMaNguoiDung(userLogged.getMaNguoiDung()); 
        voteMoi.setNgayVote(LocalDateTime.now());

        tagVoteRepository.save(voteMoi); 
        return "redirect:/play/" + maBaiHat;
    }

    // ==========================================
    // ✨ 7. XỬ LÝ THÊM BÌNH LUẬN MỚI QUA AJAX (ĐÃ NÂNG CẤP)
    // ==========================================
    @PostMapping("/gui-binh-luan")
    @ResponseBody // 👈 Trả dữ liệu thuần JSON ngầm, ngăn chặn reload trang
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

        // Tạo mới và lưu thực thể Bình Luận
        BinhLuan bl = new BinhLuan();
        bl.setMaBaiHat(maBaiHat);
        bl.setMaNguoiDung(userLogged.getMaNguoiDung()); 
        bl.setNoiDung(noiDung.trim());
        bl.setNgayBinhLuan(LocalDateTime.now());
        BinhLuan savedBl = binhLuanRepository.save(bl);

        // Đóng gói dữ liệu trả về cho JavaScript phía Giao diện tự render
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("maBinhLuan", savedBl.getMaBinhLuan()); // Gửi mã bình luận mới để có thể Like ngay
        response.put("hoTen", userLogged.getHoTen());
        response.put("noiDung", savedBl.getNoiDung());
        
        return ResponseEntity.ok(response);
    }

    // ==========================================
    // ✨ 8. XỬ LÝ THÍCH BÌNH LUẬN QUA AJAX (ĐÃ NÂNG CẤP)
    // ==========================================
    @PostMapping("/vote-binh-luan")
    @ResponseBody // 👈 Trả dữ liệu thuần JSON ngầm, ngăn chặn reload trang
    public ResponseEntity<?> voteBinhLuan(@RequestParam("maBinhLuan") Integer maBinhLuan,
                                          HttpSession session) {
        
        NguoiDung userLogged = (NguoiDung) session.getAttribute("userLogged");
        if (userLogged == null) {
            return ResponseEntity.status(401).body("Bạn chưa đăng nhập!");
        }

        // Kiểm tra xem user này đã thích bình luận này trong DB chưa
        BinhLuanVote voteDaTonTai = binhLuanVoteRepository.findByMaBinhLuanAndMaNguoiDung(maBinhLuan, userLogged.getMaNguoiDung());
        
        if (voteDaTonTai != null) {
            // Trả lỗi 400 nếu trùng lặp để JavaScript bắt và hiển thị Alert cảnh báo
            return ResponseEntity.badRequest().body("Bạn đã thích bình luận này rồi!");
        }

        // Nếu hợp lệ, lưu vào database
        BinhLuanVote blVote = new BinhLuanVote();
        blVote.setMaBinhLuan(maBinhLuan);
        blVote.setMaNguoiDung(userLogged.getMaNguoiDung()); 
        blVote.setIsUpvote(1); 
        blVote.setNgayVote(LocalDateTime.now());
        binhLuanVoteRepository.save(blVote);

        // Tính toán lại tổng lượt vote mới nhất sau khi cộng thành công
        int soUpvoteMoi = binhLuanVoteRepository.countByMaBinhLuanAndIsUpvote(maBinhLuan, 1);

        // Trả kết quả số lượng mới về cho Client
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("newLikeCount", soUpvoteMoi);
        
        return ResponseEntity.ok(response);
    }
}