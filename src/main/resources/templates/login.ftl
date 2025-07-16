<!DOCTYPE html>
<html>
<head>
    <title>Вход в систему</title>
    <link rel="stylesheet" href="/css/style.css">
    <style>
        .error-message {
            color: #d32f2f;
            background-color: #ffebee;
            padding: 10px 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            text-align: center;
            border: 1px solid #ffcdd2;
        }
    </style>
</head>
<body>
    <div class="container center-container">
        <h1>Вход в систему</h1>

        <#if error??>
            <div class="error-message">${error}</div>
        </#if>

        <form action="/login" method="post">
            <div class="form-group">
                <label for="username">Имя пользователя</label>
                <input type="text" id="username" name="username" required class="form-input">
            </div>

            <div class="form-group">
                <label for="password">Пароль</label>
                <input type="password" id="password" name="password" required class="form-input">
            </div>

            <button type="submit" class="btn">Войти</button>
        </form>

        <div class="auth-links">
            <p>Ещё нет аккаунта? <a href="/register">Зарегистрироваться</a></p>
        </div>
    </div>
</body>
</html>