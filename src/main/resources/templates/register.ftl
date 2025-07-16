<!DOCTYPE html>
<html>
<head>
    <title>Регистрация</title>
    <link rel="stylesheet" href="/css/style.css">
    <script>
        function validateForm() {
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;

            if (!username.trim()) {
                alert('Имя пользователя обязательно');
                return false;
            }

            if (password.length < 4) {
                alert('Пароль должен содержать не менее 4 символов');
                return false;
            }

            if (password !== confirmPassword) {
                alert('Пароли не совпадают');
                return false;
            }

            return true;
        }
    </script>
</head>
<body>
    <div class="container center-container">
        <h1>Регистрация</h1>

        <#if error??>
            <div class="alert error">${error}</div>
        </#if>

        <form action="/register" method="post" onsubmit="return validateForm()">
            <div class="form-group">
                <label for="username">Имя пользователя*</label>
                <input type="text" id="username" name="username" required class="form-input">
            </div>

            <div class="form-group">
                <label for="password">Пароль*</label>
                <input type="password" id="password" name="password" required class="form-input">
            </div>

            <div class="form-group">
                <label for="confirmPassword">Подтвердите пароль*</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required class="form-input">
            </div>

            <button type="submit" class="btn">Зарегистрироваться</button>
        </form>

        <div class="auth-links">
            <p>Уже есть аккаунт? <a href="/login">Войти</a></p>
        </div>
    </div>
</body>
</html>