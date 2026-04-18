package com.btl_web.controller;

import com.btl_web.dao.AddressDAO;
import com.btl_web.dao.UserDAO;
import com.btl_web.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/profile", "/profile/update", "/profile/address/add", "/profile/address/default",
    "/profile/address/update"})

public class ProfileServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = requireLogin(request, response);
        if (currentUser == null) {
            return;
        }

        try {
            User latest = userDAO.selectByUserName(currentUser.getUsername());
            if (latest != null) {
                request.setAttribute("profileUser", latest);
                request.getSession().setAttribute("currentUser", latest);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            throw new IllegalStateException("Không thể tải thông tin người dùng.", ex);
        }

        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        User currentUser = requireLogin(request, response);
        if (currentUser == null) {
            return;
        }

        String path = request.getServletPath();
        if ("/profile/update".equals(path)) {
            try {
                updateProfile(request, response, currentUser);
            } catch (ClassNotFoundException | SQLException ex) {
                request.getSession().setAttribute("profileError", "Không thể cập nhật thông tin cá nhân.");
                throw new IllegalStateException("Không thể cập nhật thông tin người dùng.", ex);
            }
            return;
        }

        if ("/profile/address/add".equals(path)) {
            try {
                addAddress(request, response, currentUser);
            } catch (ClassNotFoundException ex) {
                System.getLogger(ProfileServlet.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (SQLException ex) {
                System.getLogger(ProfileServlet.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            return;
        }

        if ("/profile/address/default".equals(path)) {
            setDefaultAddress(request, response, currentUser);
            return;
        }

        if ("/profile/address/update".equals(path)) {
            updateAddress(request, response, currentUser);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/profile");
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws IOException, ClassNotFoundException, SQLException {
        User latest = userDAO.selectByUserName(currentUser.getUsername());
        if (isProfileLocked(latest)) {
            request.getSession().setAttribute("profileError",
                    "Thông tin cá nhân đã được xác lập, không thể thay đổi thêm.");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        String fullName = normalize(request.getParameter("fullName"));
        String ageText = normalize(request.getParameter("age"));
        String gender = normalize(request.getParameter("gender"));
        String email = normalize(request.getParameter("email"));
        String phone = normalize(request.getParameter("phone"));
        String baseAddress = normalize(request.getParameter("baseAddress"));

        if (fullName.isEmpty() || ageText.isEmpty() || gender.isEmpty() || email.isEmpty() || phone.isEmpty()
                || baseAddress.isEmpty()) {
            request.getSession().setAttribute("profileError", "Vui lòng nhập đầy đủ thông tin cá nhân cố định.");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException exception) {
            request.getSession().setAttribute("profileError", "Tuổi phải là số nguyên hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        if (age < 1 || age > 120) {
            request.getSession().setAttribute("profileError", "Tuổi phải trong khoảng từ 1 đến 120.");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        if (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
            request.getSession().setAttribute("profileError", "Email không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        if (!phone.matches("[0-9]{9,11}")) {
            request.getSession().setAttribute("profileError", "Số điện thoại phải gồm 9-11 chữ số.");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        if (!("Nam".equals(gender) || "Nữ".equals(gender) || "Khác".equals(gender))) {
            request.getSession().setAttribute("profileError", "Giới tính không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        User result = userDAO.updateProfile(
                currentUser.getUsername(),
                fullName,
                age,
                gender,
                email,
                phone,
                baseAddress);

        if (result != null) {
            request.getSession().setAttribute("currentUser", result);
            request.getSession().setAttribute("profileSuccess", "Cập nhật thông tin cá nhân thành công.");
        } else {
            request.getSession().setAttribute("profileError", "Không thể cập nhật thông tin cá nhân.");
        }
        response.sendRedirect(request.getContextPath() + "/profile");
    }

    private boolean isProfileLocked(User profileUser) {
        return profileUser != null
                && profileUser.getAge() > 0
                && profileUser.getGender() != null && !profileUser.getGender().trim().isEmpty()
                && profileUser.getEmail() != null && !profileUser.getEmail().trim().isEmpty()
                && profileUser.getPhone() != null && !profileUser.getPhone().trim().isEmpty()
                && profileUser.getBaseAddress() != null && !profileUser.getBaseAddress().trim().isEmpty();
    }

    private void addAddress(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws IOException, ClassNotFoundException, SQLException {
        String recipientName = normalize(request.getParameter("recipientName"));
        String recipientPhone = normalize(request.getParameter("recipientPhone"));
        String shippingAddress = normalize(request.getParameter("shippingAddress"));
        boolean setDefault = "on".equalsIgnoreCase(request.getParameter("setDefault"));
        AddressDAO addressDAO = new AddressDAO();

        if (recipientName.isEmpty() || recipientPhone.isEmpty() || shippingAddress.isEmpty()) {
            request.getSession().setAttribute("profileError", "Vui lòng nhập đủ thông tin địa chỉ giao hàng.");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        if (!recipientPhone.matches("[0-9]{9,11}")) {
            request.getSession().setAttribute("profileError", "Số điện thoại người nhận phải gồm 9-11 chữ số.");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        int addressId = addressDAO.addAddress(
                currentUser.getUsername(),
                recipientName,
                recipientPhone,
                shippingAddress);

        if (addressId > 0) {
            request.getSession().setAttribute("profileSuccess", "Đã thêm địa chỉ giao hàng.");
            
            // Nếu checkbox "Đặt làm mặc định" được chọn, đặt địa chỉ này làm mặc định
            if (setDefault) {
                new com.btl_web.dao.UserDAO().setDefaultAddress(currentUser.getUsername(), addressId);
            }
        } else {
            request.getSession().setAttribute("profileError", "Không thể thêm địa chỉ giao hàng.");
        }
        response.sendRedirect(request.getContextPath() + "/profile");
    }

    private void setDefaultAddress(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws IOException {
        String addressIdStr = normalize(request.getParameter("addressId"));
        if (addressIdStr.isEmpty()) {
            request.getSession().setAttribute("profileError", "ID địa chỉ không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        try {
            int addressId = Integer.parseInt(addressIdStr);
            boolean success = new com.btl_web.dao.UserDAO().setDefaultAddress(currentUser.getUsername(), addressId);
            if (success) {
                request.getSession().setAttribute("profileSuccess", "Đã cập nhật địa chỉ mặc định.");
            } else {
                request.getSession().setAttribute("profileError", "Không thể cập nhật địa chỉ mặc định.");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("profileError", "ID địa chỉ không hợp lệ.");
        } catch (SQLException | ClassNotFoundException e) {
            request.getSession().setAttribute("profileError", "Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/profile");
    }

    private void updateAddress(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws IOException {
        AddressDAO addressDAO = new AddressDAO();
        String addressIdStr = normalize(request.getParameter("addressId"));
        String recipientName = normalize(request.getParameter("recipientName"));
        String recipientPhone = normalize(request.getParameter("recipientPhone"));
        String shippingAddress = normalize(request.getParameter("shippingAddress"));
        boolean setDefault = "on".equalsIgnoreCase(request.getParameter("setDefault"));

        if (addressIdStr.isEmpty() || recipientName.isEmpty() || recipientPhone.isEmpty() || shippingAddress.isEmpty()) {
            request.getSession().setAttribute("profileError", "Vui lòng nhập đầy đủ thông tin địa chỉ cần sửa.");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        if (!recipientPhone.matches("[0-9]{9,11}")) {
            request.getSession().setAttribute("profileError", "Số điện thoại người nhận phải gồm 9-11 chữ số.");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        try {
            int addressId = Integer.parseInt(addressIdStr);
            int rowsUpdated = addressDAO.updateAddress(
                    addressId,
                    currentUser.getUsername(),
                    recipientName,
                    recipientPhone,
                    shippingAddress);

            if (rowsUpdated > 0) {
                request.getSession().setAttribute("profileSuccess", "Đã cập nhật địa chỉ giao hàng.");
                
                UserDAO userDAO = new com.btl_web.dao.UserDAO();
                // Nếu checkbox "Đặt làm mặc định" được chọn, cập nhật địa chỉ mặc định
                if (setDefault) {
                    userDAO.setDefaultAddress(currentUser.getUsername(), addressId);
                } else {
                    // Nếu checkbox không được chọn nhưng địa chỉ này là mặc định hiện tại, xóa mặc định
                    User latestUser = userDAO.selectByUserName(currentUser.getUsername());
                    if (latestUser != null && latestUser.getDefaultAddress() != null 
                            && latestUser.getDefaultAddress().getId() == addressId) {
                        userDAO.clearDefaultAddress(currentUser.getUsername());
                    }
                }
            } else {
                request.getSession().setAttribute("profileError", "Không thể cập nhật địa chỉ giao hàng.");
            }
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("profileError", "ID địa chỉ không hợp lệ.");
        } catch (SQLException | ClassNotFoundException e) {
            request.getSession().setAttribute("profileError", "Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/profile");
    }

    private User requireLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return null;
        }
        return currentUser;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
