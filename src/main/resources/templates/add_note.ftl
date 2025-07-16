<!DOCTYPE html>
<html>
<head>
    <title>Добавить заметку</title>
    <link rel="stylesheet" href="/css/style.css">
    <script>
        function validateForm() {
            const title = document.getElementById('title').value;
            if (!title.trim()) {
                alert('Заголовок обязателен для заполнения');
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
    <div class="container">
        <div class="header">
            <a href="/notes" class="btn">Назад к списку</a>
            <a href="/logout" class="logout-btn">Выйти</a>
        </div>

        <h1>Добавить новую заметку</h1>

        <form action="/notes/add" method="post" enctype="multipart/form-data" onsubmit="return validateForm()">
            <div class="form-group">
                <label for="title">Заголовок*</label>
                <input type="text" id="title" name="title" required class="form-input">
            </div>

            <div class="form-group">
                <label for="content">Содержание</label>
                <textarea id="content" name="content" rows="5" class="form-textarea"></textarea>
            </div>

            <div class="form-group">
                <label>
                    <input type="checkbox" name="completed">
                    Завершено
                </label>
            </div>

            <div class="form-group">
                <label for="image">Изображение</label>
                <input type="file" id="image" name="image" accept="image/*">
            </div>

            <div class="form-group">
                <label for="reminder">Напоминание</label>
                <input type="datetime-local" id="reminder" name="reminder">
            </div>

            <button type="submit" class="btn">Сохранить заметку</button>
        </form>
    </div>
</body>
</html>