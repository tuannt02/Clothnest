<h1 align="center">Welcome to Clothnest 👋</h1>
<h5 align="center"> Đồ án nhập môn ứng dụng di động - SE114 </h5>



## 🤷‍♂️  Clothnest là gì?
Clothnest là một ứng dụng di động quản lý shop quần áo, cho phép khách hàng có thể mua hàng trực tuyến, thanh toán chỉ cần vài thao tác đơn giản. Ngoài ra ứng dụng có phân quyền dành cho nhân viên và admin; cho mục đích có thể cập trạng thái mới nhất của sản phẩm, tạo voucher, xem chi tiết trạng thái đơn hàng, thống kê, ... Theo dõi mục chức năng để biết thêm nhé 😉
<details open>
<summary><strong>Preview</strong></summary> <br>
    <dl><dd><details open>
        <summary><strong>Client</strong></summary> <br>
<table style="width:100%">
<tr>
    <th> <p align="center">
       <img src="https://github.com/tuannt02/resource/blob/main/clothnest/client/01_homepage.gif"><br>
       <a href="https://github.com/tuannt02/Clothnest">View 1: Homepage</a>
    </p> </th>
    <th> <p align="center">
       <img src="https://github.com/tuannt02/resource/blob/main/clothnest/client/02_chat_client.gif"><br>
       <a href="https://github.com/tuannt02/Clothnest">View 2: Chat client</a>
    </p> </th>
    <th> <p align="center">
       <img src="https://github.com/tuannt02/resource/blob/main/clothnest/client/03_detail.gif"><br>
       <a href="https://github.com/tuannt02/Clothnest">View 3: Detail</a>
    </p> </th>
</tr>
<tr>
    <th> <p align="center">
       <img src="https://github.com/tuannt02/resource/blob/main/clothnest/client/04_cart.gif"><br>
       <a href="https://github.com/tuannt02/Clothnest">View 4: Cart</a>
    </p> </th>    
    <th> <p align="center">
       <img src="https://github.com/tuannt02/resource/blob/main/clothnest/client/05_search.gif"><br>
       <a href="https://github.com/tuannt02/Clothnest">View 5: Search</a>
    </p> </th>
    <th> <p align="center">
       <img src="https://github.com/tuannt02/resource/blob/main/clothnest/client/06_wishlist.gif"><br>
       <a href="https://github.com/tuannt02/Clothnest">View 6: Wishlist</a>
    </p> </th>
</tr>
    <tr>
    <th> <p align="center">
       <img src="https://github.com/tuannt02/resource/blob/main/clothnest/client/07_orders.gif"><br>
       <a href="https://github.com/tuannt02/Clothnest">View 7: Orders</a>
    </p> </th>    
    <th> <p align="center">
       <img src="https://github.com/tuannt02/resource/blob/main/clothnest/client/08_address.gif"><br>
       <a href="https://github.com/tuannt02/Clothnest">View 8: Address</a>
    </p> </th>
</tr>
</table>
    </details></dd></dl>
    <dl><dd><details>
        <summary><strong>Admin</strong></summary> <br>
<table style="width:100%">
<tr>
    <th> <p align="center">
       <img src="https://github.com/tuannt02/resource/blob/main/clothnest/admin/01_manage_product.gif"><br>
       <a href="https://github.com/tuannt02/Clothnest">View 1: Manage product</a>
    </p> </th>
    <th> <p align="center">
       <img src="https://github.com/tuannt02/resource/blob/main/clothnest/admin/02_chat_admin.gif"><br>
       <a href="https://github.com/tuannt02/Clothnest">View 2: Chat admin</a>
    </p> </th>
    <th> <p align="center">
       <img src="https://github.com/tuannt02/resource/blob/main/clothnest/admin/03_transactions.gif"><br>
       <a href="https://github.com/tuannt02/Clothnest">View 3: Transactions</a>
    </p> </th>
</tr>
<tr>
    <th> <p align="center">
       <img src="https://github.com/tuannt02/resource/blob/main/clothnest/admin/04_grant_permission.gif"><br>
       <a href="https://github.com/tuannt02/Clothnest">View 4: Grant permission</a>
    </p> </th>    
    <th> <p align="center">
       <img src="https://github.com/tuannt02/resource/blob/main/clothnest/admin/05_statistics.gif"><br>
       <a href="https://github.com/tuannt02/Clothnest">View 5: Statistics</a>
    </p> </th>
    <th> <p align="center">
       <img src="https://github.com/tuannt02/resource/blob/main/clothnest/admin/06_manage.gif"><br>
       <a href="https://github.com/tuannt02/Clothnest">View 6: Manage</a>
    </p> </th>
</table>
    </details></dd></dl>
</details>

## 🔥 Danh sách các chức năng của ứng dụng
>Ứng dụng có phân quyền cho 3 vai trò là Customer, Staff, Admin. Vì thế nhóm mình sẽ liệt kê *theo phân quyền* và *chức năng chung cho cả hệ thống*
1. Customer
	- Xem danh sách các sản phẩm theo danh mục, chủ đề.
	- Xem chi tiết sản phẩm một sản phẩm.
	- Đánh giá sản phẩm.
	- Tìm kiếm sản phẩm theo danh mục, màu, size và khoảng giá.
	- Chat với Staff hoặc Admin
	- Giỏ hàng
	- Danh sách các sản phẩm ưa thích
	- Quản lý địa chỉ giao hàng
	- Thanh toán (bao gồm luôn nhập **Voucher giảm giá**)
	- Xem trạng thái của hóa đơn
2. Staff
	- Quản lý sản phẩm (CRUD)
	- Quản lý banner (CRUD)
	- Quản lý danh mục, chủ đề
	- Chat với khách hàng
	- Quản lí giao dịch (R)
	- Quản lý voucher (CRUD)
3. Admin
	- Quản lý sản phẩm (CRUD)
	- Quản lý banner (CRUD)
	- Quản lý danh mục, chủ đề
	- Chat với khách hàng
	- Quản lí giao dịch (R)
	- Quản lý voucher (CRUD)
	- Xem thống kê doanh thu
	- Phân quyền
4. Chức năng chung cho hệ thống
	- Đăng nhập
	- Đăng ký
	- Quên mật khẩu
	- Thay đổi mật khẩu
	- Cập nhật thông tin cá nhân

## ✨ Thông tin về thành viên của nhóm
- Phan Thanh Tú - 20522101
- Hoàng Đình Anh Tuấn - 20522110
- Nguyễn Thái Tuấn - 20522122
- Võ Đình Vân - 20522147

## 🚀 Cách sử dụng
#### 1. Chuẩn bị môi trường
>Để chạy được ứng dụng chúng ta cần IDE là `Android Studio`.

<p align="center">
  <img width="400" src="https://github.com/tuannt02/resource/blob/main/clothnest/utils/01.png" alt="cli output"/>
  
</p>

[Link Download ở đây](https://developer.android.com/studio)

#### 2. Download source code
Download source code qua dòng lệnh sau:
```sh
git clone https://github.com/tuannt02/Clothnest.git
```

#### 3. Chạy chương trình
Đầu tiên, ta cần mở project sau khi tải về
<p align="center">
  <img width="250" src="https://github.com/tuannt02/resource/blob/main/clothnest/utils/02.png"/>
  
</p>
Tiếp theo, run VM lên mà chạy thôi!

<p align="center">
  <img width="250" src="https://github.com/tuannt02/resource/blob/main/clothnest/utils/03.png"/>
</p>

## 💪 Công nghệ, công cụ sử dụng liên quan
1. Notion
2. Figma
3. Draw.io
4. Firebase
5. Android Studio

## 👊 Ủng hộ Clothnest
- Bằng cách ⭐️ repo này nhé! ❤️

## 📝 Giấy phép
- Copyright © 2022 [Nhóm 7](https://github.com/tuannt02/Clothnest).<br />
- This project is [MIT](https://github.com/tuannt02/Clothnest) licensed.
---
_This README was generated with  ❤️  by _[enthusiasm, creativity, sense of responsibility](https://github.com/tuannt02/Clothnest)_
