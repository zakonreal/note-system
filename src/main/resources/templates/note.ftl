<!DOCTYPE html>
<html>
<head>
    <title>${note.title}</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <div class="container">
        <div class="header">
            <a href="/notes" class="btn">Назад к списку</a>
            <a href="/logout" class="logout-btn">Выйти</a>
        </div>

        <h1>${note.title}</h1>

        <div class="note-meta">
            <span class="meta-item">Создано: ${note.createdDate?string('dd.MM.yyyy')}</span>
            <span class="meta-item status ${note.completed?then('completed', 'pending')}">
                Статус: ${note.completed?then('Завершено', 'В процессе')}
            </span>
            <#if note.reminder??>
                <span class="meta-item">Напоминание: ${note.reminder?string('dd.MM.yyyy HH:mm')}</span>
            </#if>
        </div>

        <div class="note-content">
            <p>${note.content!''}</p>
        </div>

        <#if note.imagePath??>
            <div class="note-image">
                <img src="/images/${note.imagePath}" alt="Изображение заметки" class="img-responsive">
            </div>
        </#if>

        <div class="note-actions">
            <a href="/notes/edit/${note.id}" class="action-btn edit">Редактировать</a>
            <form action="/notes/delete/${note.id}" method="post" class="inline-form">
                <button type="submit" class="action-btn delete">Удалить</button>
            </form>
            <form action="/notes/toggle/${note.id}" method="post" class="inline-form">
                <button type="submit" class="action-btn toggle">
                    ${note.completed?then('Возобновить', 'Завершить')}
                </button>
            </form>
        </div>
    </div>
</body>
</html>