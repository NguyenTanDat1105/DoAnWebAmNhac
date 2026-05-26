package clc65.nguyentandat.DoAnWebAmNhac;

import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

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
        
        // 🛑 ĐÃ SỬA: Nếu chưa đăng nhập HOẶC là Người dùng thường (quyen == 0) -> Đá văng về trang đăng nhập hoặc trang chủ
        if (userLogged == null || userLogged.getPhanQuyen() == 0) {
            return "redirect:/dang-nhap";
        }
        
        m.addAttribute("baiHatMoi", new BaiHat());
        return "thembaihat"; 
    }

    @PostMapping("/them-bai-hat")
    public String saveBaiHat(@ModelAttribute("baiHatMoi") BaiHat baiHat, HttpSession session) {
        NguoiDung userLogged = (NguoiDung) session.getAttribute("userLogged");
        
        // 🛑 ĐÃ SỬA: Chặn luôn ở đầu nhận dữ liệu từ Form để đảm bảo an toàn tuyệt đối
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
        
        // 🛠️ ĐÃ SỬA: Nếu không đăng nhập HOẶC không phải quyền số 2 (Admin) -> Trục xuất về trang chủ
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
        
        // 🛠️ ĐÃ SỬA: Đảm bảo chỉ quyền số 2 (Admin) mới có quyền gửi form lưu dữ liệu cập nhật
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
        
        // 🛠️ ĐÃ SỬA: Chỉ cho phép tài khoản mang quyền số 2 (Admin thực tế trong DB) truy cập
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
        
        // 🛠️ ĐÃ SỬA: Chặn đứng hành vi tự gõ link url xóa bừa bãi, chỉ cho quyền số 2 thực thi
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
        // 1. Kiểm tra tài khoản trùng lặp
        NguoiDung checkTontai = nguoiDungRepository.findByTenDangNhap(user.getTenDangNhap());
        if (checkTontai != null) {
            m.addAttribute("error", "Tên đăng nhập này đã tồn tại trên hệ thống!");
            return "dangky"; 
        }
        
        // 2. Thiết lập các thông số thời gian và ảnh đại diện mặc định
        user.setNgayTao(LocalDateTime.now()); 
        user.setAnhDaiDien("default_avatar.png"); 
        
        // 🛠️ SỬA LẠI TẠI ĐÂY: Xử lý nhận quyền động từ Form giao diện gửi lên
        // Nếu trên giao diện người dùng không chọn hoặc bằng null, hệ thống mới gán mặc định là 0
        if (user.getPhanQuyen() == null) {
            user.setPhanQuyen(0); 
        } else if (user.getPhanQuyen() == 2) {
            // 🔒 BẢO MẬT: Ngăn chặn tuyệt đối việc hacker cố tình sửa code HTML của Form để tự đăng ký quyền Admin (2)
            user.setPhanQuyen(0); 
        }
        
        // 3. Tiến hành lưu xuống Cơ sở dữ liệu
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
    // 7. XỬ LÝ THÊM BÌNH LUẬN MỚI
    // ==========================================
    @PostMapping("/gui-binh-luan")
    public String guiBinhLuan(@RequestParam("maBaiHat") Integer maBaiHat,
                              @RequestParam("noiDung") String noiDung,
                              HttpSession session) {
        if (noiDung == null || noiDung.trim().isEmpty()) {
            return "redirect:/play/" + maBaiHat;
        }

        NguoiDung userLogged = (NguoiDung) session.getAttribute("userLogged");
        if (userLogged == null) {
            return "redirect:/dang-nhap";
        }

        BinhLuan bl = new BinhLuan();
        bl.setMaBaiHat(maBaiHat);
        bl.setMaNguoiDung(userLogged.getMaNguoiDung()); 
        bl.setNoiDung(noiDung.trim());
        bl.setNgayBinhLuan(LocalDateTime.now());

        binhLuanRepository.save(bl);
        return "redirect:/play/" + maBaiHat;
    }

 // ==========================================
    // 8. XỬ LÝ THÍCH (UPVOTE) BÌNH LUẬN - CHỐNG SPAM VOTE NHIỀU LẦN
    // ==========================================
    @PostMapping("/vote-binh-luan")
    public String voteBinhLuan(@RequestParam("maBaiHat") Integer maBaiHat,
                               @RequestParam("maBinhLuan") Integer maBinhLuan,
                               HttpSession session,
                               ModelMap m) {
        
        NguoiDung userLogged = (NguoiDung) session.getAttribute("userLogged");
        if (userLogged == null) {
            return "redirect:/dang-nhap";
        }

        // 🛠️ BƯỚC BỔ SUNG: Kiểm tra xem người dùng này đã từng vote bình luận này chưa
        // Sử dụng hàm có sẵn trong Repository của bạn (hoặc bạn có thể tự viết hàm tìm kiếm tương tự)
        BinhLuanVote voteDaTonTai = binhLuanVoteRepository.findByMaBinhLuanAndMaNguoiDung(maBinhLuan, userLogged.getMaNguoiDung());
        
        if (voteDaTonTai != null) {
            // 🛑 NẾU ĐÃ VOTE RỒI: Không lưu nữa và chuyển hướng về trang nghe nhạc với tín hiệu báo lỗi
            // Cách xử lý đơn giản và trực quan nhất là truyền một tham số báo lỗi lên URL
            return "redirect:/play/" + maBaiHat + "?errorVote=true";
        }

        // NẾU CHƯA VOTE: Tiến hành lưu lượt vote mới như bình thường
        BinhLuanVote blVote = new BinhLuanVote();
        blVote.setMaBinhLuan(maBinhLuan);
        blVote.setMaNguoiDung(userLogged.getMaNguoiDung()); 
        blVote.setIsUpvote(1); 
        blVote.setNgayVote(LocalDateTime.now());

        binhLuanVoteRepository.save(blVote);
        return "redirect:/play/" + maBaiHat;
    }
}