<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String authError = (String) request.getAttribute("authError");
    if (authError == null && session.getAttribute("authError") != null) {
        authError = (String) session.getAttribute("authError");
        session.removeAttribute("authError");
    }
    String enteredUsername = (String) request.getAttribute("enteredUsername");
    if (enteredUsername == null) enteredUsername = "";
    String authSuccess = (String) session.getAttribute("authSuccess");
    session.removeAttribute("authSuccess");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700&family=Archivo+Black&display=swap" rel="stylesheet">
    <style>
        :root {
            --bg: #f6f1e7;
            --card: rgba(255, 253, 247, 0.92);
            --text: #20302f;
            --muted: #667675;
            --line: #d7e1db;
            --accent: #0d6c63;
            --accent-dark: #0a5a52;
            --danger: #b42318;
            --shadow: 0 18px 40px rgba(32, 48, 47, 0.12);
        }

        * { box-sizing: border-box; }

        body {
            margin: 0;
            min-height: 100vh;
            display: grid;
            place-items: center;
            padding: 24px 16px;
            background:
                radial-gradient(circle at top left, rgba(13, 108, 99, 0.08), transparent 34%),
                radial-gradient(circle at bottom right, rgba(179, 83, 24, 0.08), transparent 28%),
                linear-gradient(160deg, #faf7f0 0%, #e8f2ee 100%);
            font-family: "Plus Jakarta Sans", "Segoe UI", sans-serif;
            color: var(--text);
        }

        .box {
            width: 100%;
            max-width: 460px;
            border: 1px solid var(--line);
            border-radius: 24px;
            background: var(--card);
            box-shadow: var(--shadow);
            padding: 28px;
            backdrop-filter: blur(10px);
        }

        h1 {
            margin: 0 0 8px;
            font-family: "Archivo Black", "Segoe UI", sans-serif;
            font-size: 1.65rem;
            line-height: 1.1;
            letter-spacing: -0.02em;
        }

        .subtitle {
            margin: 0 0 18px;
            color: var(--muted);
            line-height: 1.55;
            font-size: 0.97rem;
        }

        .credential-note {
            margin: 0 0 12px;
            padding: 12px 14px;
            border-radius: 14px;
            background: #f7fbfa;
            border: 1px solid #d9e7e3;
            color: #385452;
            font-size: 0.92rem;
            line-height: 1.55;
        }

        .field { margin-bottom: 14px; }

        label {
            display: block;
            margin-bottom: 6px;
            font-size: 0.86rem;
            font-weight: 700;
            letter-spacing: 0.02em;
            color: #334847;
        }

        input {
            width: 100%;
            border: 1px solid var(--line);
            border-radius: 14px;
            padding: 14px 15px;
            font: inherit;
            color: var(--text);
            background: #fff;
            outline: none;
            transition: border-color 0.16s ease, box-shadow 0.16s ease, transform 0.16s ease;
        }

        input:focus {
            border-color: rgba(13, 108, 99, 0.55);
            box-shadow: 0 0 0 4px rgba(13, 108, 99, 0.12);
        }

        button {
            width: 100%;
            border: 0;
            border-radius: 14px;
            padding: 14px 16px;
            font: inherit;
            font-weight: 700;
            background: linear-gradient(180deg, var(--accent) 0%, var(--accent-dark) 100%);
            color: #fff;
            cursor: pointer;
            box-shadow: 0 12px 24px rgba(13, 108, 99, 0.22);
            transition: transform 0.15s ease, box-shadow 0.15s ease, filter 0.15s ease;
        }

        button:hover {
            transform: translateY(-1px);
            filter: brightness(1.02);
            box-shadow: 0 16px 28px rgba(13, 108, 99, 0.25);
        }

        .msg {
            margin-bottom: 12px;
            padding: 11px 12px;
            border-radius: 12px;
            font-size: 0.92rem;
            line-height: 1.45;
        }

        .err { background: #fff3f2; color: var(--danger); border: 1px solid #f0c6c1; }
        .ok { background: #ecfaf5; color: #0f6159; border: 1px solid #b7e0d8; }

        .links {
            margin-top: 16px;
            font-size: 0.93rem;
            display: flex;
            justify-content: space-between;
            gap: 12px;
            flex-wrap: wrap;
        }

        a {
            color: var(--accent);
            text-decoration: none;
            font-weight: 700;
        }

        a:hover { text-decoration: underline; }

        @media (max-width: 480px) {
            .box { padding: 22px 18px; border-radius: 20px; }
            h1 { font-size: 1.45rem; }
            .links { flex-direction: column; }
        }
    </style>
</head>
<body>
<div class="box">
    <h1>Đăng nhập mua hàng</h1>
    <p class="subtitle">Truy cập tài khoản để xem giỏ hàng, đặt đơn và quản lý thông tin cá nhân.</p>
    <div class="credential-note">Tài khoản demo: <strong>demo</strong> | Mật khẩu: <strong>123456</strong><br>Tài khoản admin: <strong>admin</strong> | Mật khẩu: <strong>admin123</strong></div>

    <% if (authError != null) { %><div class="msg err"><%= authError %></div><% } %>
    <% if (authSuccess != null) { %><div class="msg ok"><%= authSuccess %></div><% } %>

    <form action="<%= request.getContextPath() %>/auth/login" method="post">
        <div class="field">
            <label for="username">Tên đăng nhập</label>
            <input id="username" name="username" value="<%= enteredUsername %>" autocomplete="username">
        </div>
        <div class="field">
            <label for="password">Mật khẩu</label>
            <input id="password" type="password" name="password" autocomplete="current-password">
        </div>
        <button type="submit">Đăng nhập</button>
    </form>

    <div class="links">
        <a href="<%= request.getContextPath() %>/auth/register">Tạo tài khoản mới</a>
        <a href="<%= request.getContextPath() %>/shop">Quay về shop</a>
    </div>
</div>
</body>
</html>
