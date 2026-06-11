# IT211 Project - Banking Management System

## Giới thiệu

Banking Management System là hệ thống ngân hàng số được xây dựng bằng Spring Boot, cung cấp các chức năng quản lý người dùng, tài khoản ngân hàng, giao dịch chuyển tiền, xác thực JWT, eKYC và quản lý phiên đăng nhập bằng Refresh Token.

## Công nghệ sử dụng

### Backend

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate
* JWT Authentication
* MySQL
* Cloudinary
* Lombok
* Maven

### Database

* MySQL

## Chức năng chính

### 1. Xác thực và phân quyền

#### Đăng ký tài khoản

* Tạo tài khoản người dùng mới.
* Tự động tạo tài khoản ngân hàng sau khi đăng ký.

#### Đăng nhập

* Xác thực username/password.
* Cấp phát Access Token (JWT).
* Cấp phát Refresh Token.

#### Refresh Token

* Tạo Access Token mới khi Access Token hết hạn.
* Hỗ trợ xoay vòng Refresh Token (Refresh Token Rotation).

#### Đăng xuất

* Thu hồi (Revoke) Refresh Token.
* Ngăn Refresh Token tiếp tục được sử dụng.

#### Phân quyền

* ADMIN
* CUSTOMER

---

### 2. Quản lý người dùng

ADMIN có quyền:

* Xem danh sách người dùng.
* Xem chi tiết người dùng.
* Cập nhật thông tin người dùng.
* Xóa người dùng.

---

### 3. Quản lý tài khoản ngân hàng

* Xem danh sách tài khoản.
* Xem thông tin tài khoản.
* Cập nhật tài khoản.
* Xóa tài khoản.
* Vấn tin số dư.

---

### 4. Quản lý mã PIN giao dịch

* Đổi mã PIN giao dịch.
* Kiểm tra PIN cũ trước khi cập nhật.

---

### 5. Chuyển tiền

#### Chuyển tiền nội bộ

* Chuyển tiền giữa các tài khoản trong hệ thống.
* Kiểm tra:

    * Tài khoản nguồn tồn tại.
    * Tài khoản đích tồn tại.
    * Tài khoản đang hoạt động.
    * PIN giao dịch hợp lệ.
    * Số dư đủ để giao dịch.

#### Lưu lịch sử giao dịch

* Sinh mã giao dịch tự động.
* Lưu thông tin:

    * Tài khoản gửi.
    * Tài khoản nhận.
    * Số tiền.
    * Nội dung giao dịch.
    * Thời gian giao dịch.
    * Trạng thái giao dịch.

---

### 6. Sao kê giao dịch

* Xem lịch sử giao dịch theo tài khoản.
* Hỗ trợ phân trang.
* Sắp xếp theo thời gian mới nhất.

---

### 7. eKYC

#### Gửi hồ sơ định danh

Người dùng gửi:

* CCCD mặt trước.
* CCCD mặt sau.
* Họ tên.
* Số CCCD.
* Ngày sinh.
* Giới tính.
* Địa chỉ.

Ảnh được lưu trên Cloudinary.

#### Duyệt hồ sơ eKYC

ADMIN có thể:

* Approve hồ sơ.
* Cập nhật trạng thái CONFIRM.
* Kích hoạt trạng thái KYC của người dùng.

#### Từ chối hồ sơ eKYC

ADMIN có thể:

* Reject hồ sơ.
* Cập nhật trạng thái REJECT.

---

## Bảo mật

### JWT Authentication

Mỗi request được xác thực thông qua:

Authorization: Bearer <access_token>

JWT Filter sẽ:

* Đọc token từ header.
* Xác thực chữ ký.
* Kiểm tra thời gian hết hạn.
* Thiết lập SecurityContext.

### Refresh Token

Lưu trữ trong database:

* token
* expiryDate
* revoked

Hỗ trợ:

* Refresh Token Rotation
* Token Revocation

---

## Cấu trúc phân quyền

### ADMIN

Có quyền:

* Quản lý User.
* Quản lý Account.
* Duyệt eKYC.
* Xem giao dịch.

### CUSTOMER

Có quyền:

* Đăng nhập.
* Đổi PIN.
* Chuyển tiền.
* Xem số dư.
* Xem lịch sử giao dịch.
* Gửi hồ sơ eKYC.

---

## API Chính

### Auth

* POST /api/v1/auth/register
* POST /api/v1/auth/login
* POST /api/v1/auth/refresh-token
* POST /api/v1/auth/logout

### User

* GET /api/v1/users
* GET /api/v1/users/{id}
* PUT /api/v1/users/{id}
* DELETE /api/v1/users/{id}

### Account

* GET /api/v1/accounts
* GET /api/v1/accounts/{id}
* GET /api/v1/accounts/{id}/balance
* PUT /api/v1/accounts/{id}
* DELETE /api/v1/accounts/{id}
* PUT /api/v1/accounts/{id}/change-pin

### Transaction

* POST /api/transactions/transfer
* GET /api/transactions/history/{accountId}

### eKYC

* POST /api/v1/kyc/upload
* PUT /api/v1/kyc/{id}/approve
* PUT /api/v1/kyc/{id}/reject

---

## Thành viên thực hiện

* Họ và tên: Vũ Đức Huy Hoàng

