<!DOCTYPE html>
<html>
<head>
    <title>Редактировать заметку</title>
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

        <h1>Редактировать заметку</h1>

        <form action="/notes/edit/${note.id}" method="post" enctype="multipart/form-data" onsubmit="return validateForm()">
            <div class="form-group">
                <label for="title">Заголовок*</label>
                <input type="text" id="title" name="title" value="${note.title}" required class="form-input">
            </div>

            <div class="form-group">
                <label for="content">Содержание</label>
                <textarea id="content" name="content" rows="5" class="form-textarea">${note.content!''}</textarea>
            </div>

            <div class="form-group">
                <label>
                    <input type="checkbox" name="completed" ${note.completed?then('checked','')}>
                    Завершено
                </label>
            </div>

            <div class="form-group">
                <label for="image">Изображение</label>
                <#if note.imagePath??>
                    <div class="image-preview">
                        <img src="/images/${note.imagePath}" alt="Текущее изображение" width="200">
                        <a href="/images/${note.imagePath}" target="_blank">Просмотреть</a>
                    </div>
                </#if>
                <input type="file" id="image" name="image" accept="image/*">
            </div>

            <div class="form-group">
                <label for="reminder">Напоминание</label>
                <input type="datetime-local" id="reminder" name="reminder"
                       value="${(note.reminder??)?then(note.reminder?string('yyyy-MM-ddTHH:mm'),'')}">
            </div>

            <button type="submit" class="btn">Обновить заметку</button>
        </form>
    </div>
</body>
</html>