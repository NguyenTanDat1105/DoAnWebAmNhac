-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th6 07, 2026 lúc 02:42 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `qlwebamnhac`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `baihat`
--

CREATE TABLE `baihat` (
  `MaBaiHat` int(11) NOT NULL,
  `tenBaiHat` varchar(255) DEFAULT NULL,
  `nguoiTrinhBay` varchar(255) DEFAULT NULL,
  `linkYoutube` varchar(255) DEFAULT NULL,
  `NguoiUpload` int(11) DEFAULT NULL,
  `NgayDang` datetime DEFAULT current_timestamp(),
  `TrangThaiDuyet` int(11) DEFAULT 0,
  `GhiChuAdmin` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `baihat`
--

INSERT INTO `baihat` (`MaBaiHat`, `tenBaiHat`, `nguoiTrinhBay`, `linkYoutube`, `NguoiUpload`, `NgayDang`, `TrangThaiDuyet`, `GhiChuAdmin`) VALUES
(1, 'Lạc Trôi', 'Sơn Tùng M-TP', 'Llw9Q6akRo4', 2, '2026-05-25 11:55:11', 1, NULL),
(2, 'Em Của Ngày Hôm Qua', 'Sơn Tùng M-TP', 'Vt4kAu-ziRY', 2, '2026-05-25 11:55:11', 1, NULL),
(3, 'Nơi Này Có Anh', 'Sơn Tùng M-TP', 'FN7ALfpGxiI', 2, '2026-05-25 11:55:11', 1, NULL),
(4, 'Bài Hát Chờ Duyệt', 'Ca Sĩ Ẩn Danh', 'abc123xyz', 3, '2026-05-25 11:55:11', 0, NULL),
(5, 'Airplane Mode', 'Hurrykng - Wean Lê', 'yrNSXeQxnWU', NULL, '2026-05-26 10:11:53', 1, NULL),
(7, 'Nước mắt cá sấu', 'HIEUTHUHAI', 'zaYS8tiD0Og', NULL, '2026-05-26 10:28:54', 1, NULL),
(8, 'Tràn Bộ Nhớ', 'Dương Domic', 'a-jdOviGQ00', NULL, '2026-05-26 12:14:26', 1, NULL),
(9, 'Phép Màu', 'Mounter x MAYDAYs, Minh Tốc', 'jPjQJYKhhk4', NULL, '2026-05-26 12:18:47', 1, NULL),
(10, 'Make Up', 'Dillan Hoàng Phan, buitruonglinh, Lohan, Ryn Lee, Đỗ Nam Sơn, Bảo Thy', 'iCF7Z3irza4', NULL, '2026-05-26 12:19:40', 1, NULL),
(11, 'Sớm Như Vậy', 'buitruonglinh', '6hNenqz0ISA', NULL, '2026-05-26 12:20:43', 1, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `binhluan`
--

CREATE TABLE `binhluan` (
  `MaBinhLuan` int(11) NOT NULL,
  `MaBaiHat` int(11) NOT NULL,
  `MaNguoiDung` int(11) NOT NULL,
  `NoiDung` text NOT NULL,
  `NgayBinhLuan` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `binhluan`
--

INSERT INTO `binhluan` (`MaBinhLuan`, `MaBaiHat`, `MaNguoiDung`, `NoiDung`, `NgayBinhLuan`) VALUES
(1, 1, 3, 'Bài hát này hay quá!', '2026-05-25 11:55:11'),
(2, 1, 2, 'Cảm ơn bạn đã ủng hộ.', '2026-05-25 11:55:11'),
(3, 1, 1, 'Bài hát này cuốn quá, nghe đi nghe lại không chán!', '2026-05-26 01:50:27'),
(4, 1, 5, 'sếp Tùng của tôi ơi !!!', '2026-05-26 03:09:06'),
(5, 1, 1, 'bài này hay, làm melody đỉnh', '2026-05-26 05:49:28'),
(6, 1, 3, 'nghe cuốn phết', '2026-05-26 06:02:01');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `binhluanvote`
--

CREATE TABLE `binhluanvote` (
  `ID` int(11) NOT NULL,
  `MaBinhLuan` int(11) NOT NULL,
  `MaNguoiDung` int(11) NOT NULL,
  `IsUpvote` int(11) DEFAULT NULL,
  `NgayVote` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `binhluanvote`
--

INSERT INTO `binhluanvote` (`ID`, `MaBinhLuan`, `MaNguoiDung`, `IsUpvote`, `NgayVote`) VALUES
(1, 3, 1, 1, '2026-05-26 01:51:00'),
(2, 4, 5, 1, '2026-05-26 03:09:09'),
(5, 1, 5, 1, '2026-05-26 03:30:31'),
(8, 5, 1, 1, '2026-05-26 05:49:39'),
(11, 3, 5, 1, '2026-05-27 03:58:56');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nguoidung`
--

CREATE TABLE `nguoidung` (
  `MaNguoiDung` int(11) NOT NULL,
  `TenDangNhap` varchar(50) NOT NULL,
  `MatKhau` varchar(100) NOT NULL,
  `HoTen` varchar(100) NOT NULL,
  `AnhDaiDien` varchar(255) DEFAULT NULL,
  `PhanQuyen` int(11) DEFAULT 0,
  `NgayTao` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `nguoidung`
--

INSERT INTO `nguoidung` (`MaNguoiDung`, `TenDangNhap`, `MatKhau`, `HoTen`, `AnhDaiDien`, `PhanQuyen`, `NgayTao`) VALUES
(1, 'admin', '123456', 'Quản Trị Viên', NULL, 2, '2026-05-25 11:55:10'),
(2, 'artist', '123456', 'Sơn Tùng M-TP', NULL, 1, '2026-05-25 11:55:10'),
(3, 'user_a', '123456', 'Nguyễn Văn A', NULL, 0, '2026-05-25 11:55:10'),
(5, 'nguyentandat', '11052005', 'Nguyễn Tấn Đạt', 'default_avatar.png', 0, '2026-05-26 02:50:47'),
(7, 'issac', '123123', 'Anh Trai Issac', 'default_avatar.png', 1, '2026-05-26 02:56:38');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tag`
--

CREATE TABLE `tag` (
  `MaTag` int(11) NOT NULL,
  `TenTag` varchar(50) NOT NULL,
  `LaGoiY` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `tag`
--

INSERT INTO `tag` (`MaTag`, `TenTag`, `LaGoiY`) VALUES
(1, 'Pop', 1),
(2, 'Rock', 1),
(3, 'Ballad', 1),
(4, 'Rap', 1),
(5, 'Indie', 1),
(6, 'EDM', 1),
(8, 'Cổ điển', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tagvote`
--

CREATE TABLE `tagvote` (
  `ID` int(11) NOT NULL,
  `MaBaiHat` int(11) NOT NULL,
  `MaTag` int(11) NOT NULL,
  `MaNguoiDung` int(11) NOT NULL,
  `NgayVote` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `tagvote`
--

INSERT INTO `tagvote` (`ID`, `MaBaiHat`, `MaTag`, `MaNguoiDung`, `NgayVote`) VALUES
(1, 1, 1, 1, '2026-05-25 11:55:11'),
(2, 1, 3, 2, '2026-05-25 11:55:11'),
(3, 2, 1, 1, '2026-05-25 11:55:11'),
(4, 1, 5, 1, '2026-05-25 08:39:47'),
(6, 2, 3, 1, '2026-05-25 08:41:01'),
(7, 1, 3, 5, '2026-05-26 03:08:11'),
(8, 1, 2, 5, '2026-05-26 08:46:45'),
(16, 1, 4, 1, '2026-05-27 05:55:40');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `baihat`
--
ALTER TABLE `baihat`
  ADD PRIMARY KEY (`MaBaiHat`),
  ADD KEY `FK_BaiHat_NguoiDung` (`NguoiUpload`);

--
-- Chỉ mục cho bảng `binhluan`
--
ALTER TABLE `binhluan`
  ADD PRIMARY KEY (`MaBinhLuan`),
  ADD KEY `FK_BinhLuan_BaiHat` (`MaBaiHat`),
  ADD KEY `FK_BinhLuan_NguoiDung` (`MaNguoiDung`);

--
-- Chỉ mục cho bảng `binhluanvote`
--
ALTER TABLE `binhluanvote`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `UNIQUE_Comment_User` (`MaBinhLuan`,`MaNguoiDung`),
  ADD KEY `FK_BLVote_NguoiDung` (`MaNguoiDung`);

--
-- Chỉ mục cho bảng `nguoidung`
--
ALTER TABLE `nguoidung`
  ADD PRIMARY KEY (`MaNguoiDung`),
  ADD UNIQUE KEY `TenDangNhap` (`TenDangNhap`);

--
-- Chỉ mục cho bảng `tag`
--
ALTER TABLE `tag`
  ADD PRIMARY KEY (`MaTag`),
  ADD UNIQUE KEY `TenTag` (`TenTag`);

--
-- Chỉ mục cho bảng `tagvote`
--
ALTER TABLE `tagvote`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `UNIQUE_Song_Tag_User` (`MaBaiHat`,`MaTag`,`MaNguoiDung`),
  ADD KEY `FK_TagVote_Tag` (`MaTag`),
  ADD KEY `FK_TagVote_NguoiDung` (`MaNguoiDung`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `baihat`
--
ALTER TABLE `baihat`
  MODIFY `MaBaiHat` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT cho bảng `binhluan`
--
ALTER TABLE `binhluan`
  MODIFY `MaBinhLuan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT cho bảng `binhluanvote`
--
ALTER TABLE `binhluanvote`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT cho bảng `nguoidung`
--
ALTER TABLE `nguoidung`
  MODIFY `MaNguoiDung` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT cho bảng `tag`
--
ALTER TABLE `tag`
  MODIFY `MaTag` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT cho bảng `tagvote`
--
ALTER TABLE `tagvote`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `baihat`
--
ALTER TABLE `baihat`
  ADD CONSTRAINT `FK_BaiHat_NguoiDung` FOREIGN KEY (`NguoiUpload`) REFERENCES `nguoidung` (`MaNguoiDung`) ON DELETE SET NULL;

--
-- Các ràng buộc cho bảng `binhluan`
--
ALTER TABLE `binhluan`
  ADD CONSTRAINT `FK_BinhLuan_BaiHat` FOREIGN KEY (`MaBaiHat`) REFERENCES `baihat` (`MaBaiHat`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_BinhLuan_NguoiDung` FOREIGN KEY (`MaNguoiDung`) REFERENCES `nguoidung` (`MaNguoiDung`);

--
-- Các ràng buộc cho bảng `binhluanvote`
--
ALTER TABLE `binhluanvote`
  ADD CONSTRAINT `FK_BLVote_BinhLuan` FOREIGN KEY (`MaBinhLuan`) REFERENCES `binhluan` (`MaBinhLuan`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_BLVote_NguoiDung` FOREIGN KEY (`MaNguoiDung`) REFERENCES `nguoidung` (`MaNguoiDung`);

--
-- Các ràng buộc cho bảng `tagvote`
--
ALTER TABLE `tagvote`
  ADD CONSTRAINT `FK_TagVote_BaiHat` FOREIGN KEY (`MaBaiHat`) REFERENCES `baihat` (`MaBaiHat`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_TagVote_NguoiDung` FOREIGN KEY (`MaNguoiDung`) REFERENCES `nguoidung` (`MaNguoiDung`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_TagVote_Tag` FOREIGN KEY (`MaTag`) REFERENCES `tag` (`MaTag`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
