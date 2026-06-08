# WEB_SHOP - Hệ Thống Quản Lý Sản Phẩm Quần Áo

Ứng dụng được xây dựng bằng **Java Servlet**, **JSP** và **JSTL**, cho phép quản lý thông tin sản phẩm quần áo thông qua quy trình nhập liệu và xác nhận trước khi lưu dữ liệu.

## Mô tả hệ thống

Hệ thống hỗ trợ người dùng nhập thông tin sản phẩm quần áo, kiểm tra tính hợp lệ của dữ liệu, xác nhận thông tin trước khi lưu và hiển thị danh sách sản phẩm đã được thêm vào hệ thống. Dữ liệu được xử lý theo mô hình MVC, giúp tách biệt giao diện, xử lý nghiệp vụ và dữ liệu.

## Chức năng chính

* Thêm sản phẩm quần áo mới.
* Kiểm tra tính hợp lệ của dữ liệu đầu vào.
* Kiểm tra trùng mã sản phẩm.
* Xác nhận thông tin sản phẩm trước khi lưu.
* Cho phép quay lại màn hình nhập liệu để chỉnh sửa.
* Hiển thị danh sách sản phẩm đã lưu.
* Quản lý thông tin sản phẩm gồm:

  * Mã sản phẩm
  * Tên sản phẩm
  * Loại sản phẩm
  * Kích thước (Size)
  * Màu sắc
  * Giá bán
  * Số lượng tồn kho

## Công nghệ sử dụng

* Java Servlet
* JSP
* JSTL
* Apache Tomcat
* Maven

## Yêu cầu môi trường

* JDK 21 hoặc mới hơn
* Maven 3.9+
* Apache Tomcat 9.x

## Build dự án

```bash
mvn clean package
```

Sau khi build thành công, file WAR sẽ được tạo tại:

```bash
target/BTL_WEB.war
```

## Triển khai trên Tomcat

Sao chép file WAR vào thư mục `webapps` của Tomcat:

```bash
cp target/BTL_WEB.war /path/to/tomcat/webapps/
```

Khởi động Tomcat:

```bash
/path/to/tomcat/bin/startup.sh
```

Truy cập ứng dụng tại:

```text
http://localhost:8080/BTL_WEB
```

## Dừng Tomcat

```bash
/path/to/tomcat/bin/shutdown.sh
```

## Cấu trúc xử lý

Quy trình thêm sản phẩm được thực hiện theo hai bước:

### Bước 1: Nhập thông tin sản phẩm

Người dùng nhập đầy đủ thông tin sản phẩm và gửi biểu mẫu.

### Bước 2: Xác nhận thông tin

Hệ thống hiển thị lại toàn bộ thông tin đã nhập để người dùng xác nhận trước khi lưu hoặc quay lại chỉnh sửa.

Sau khi xác nhận thành công, sản phẩm sẽ được lưu vào hệ thống và hiển thị trong danh sách sản phẩm.

## Triển khai

Ứng dụng có thể được triển khai trên các máy chủ hỗ trợ Servlet như:

* Apache Tomcat
* Eclipse Jetty
* WildFly
* GlassFish

Thông qua file WAR được tạo trong thư mục `target/`.
