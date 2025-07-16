<!DOCTYPE html>
<html>
<head>
    <title>Мои заметки</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <div class="container">
        <div class="header">
            <#if currentUser?? && currentUser.role == 'ADMIN'>
                <a href="/admin" class="admin-btn">Панель администратора</a>
            </#if>
            <a href="/logout" class="logout-btn">Выйти</a>
        </div>

        <h1>Мои заметки</h1>

        <div class="actions">
            <a href="/notes/add" class="btn">Добавить заметку</a>
            <a href="/export/excel" class="btn export-btn">Экспорт в Excel</a>
        </div>

        <form action="/notes" method="get" class="search-form">
            <input type="text" name="query" placeholder="Поиск заметок..." value="${query!''}">
            <button type="submit">Поиск</button>
        </form>

        <table>
            <thead>
                <tr>
                    <th>Заголовок</th>
                    <th>Дата создания</th>
                    <th>Статус</th>
                    <th>Изображение</th>
                    <th>Действия</th>
                </tr>
            </thead>
            <tbody>
                <#list notes as note>
                <tr>
                    <td>${note.title}</td>
                    <!-- Исправлено форматирование даты -->
                    <td>${note.createdDate?date?string('dd.MM.yyyy')}</td>
                    <td class="${note.completed?then('completed', 'pending')}">
                        ${note.completed?then('✓ Завершено', '✗ В процессе')}
                    </td>
                    <td>
                        <#if note.imagePath??>
                            <a href="/images/${note.imagePath}" target="_blank" class="image-link">Просмотр</a>
                        <#else>
                            -
                        </#if>
                    </td>
                    <td class="actions-cell">
                        <a href="/notes/${note.id}" class="action-btn view">Просмотр</a>
                        <a href="/notes/edit/${note.id}" class="action-btn edit">Изменить</a>
                        <form action="/notes/delete/${note.id}" method="post" class="inline-form">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="action-btn delete">Удалить</button>
                        </form>
                        <form action="/notes/toggle/${note.id}" method="post" class="inline-form">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button type="submit" class="action-btn toggle">
                                ${note.completed?then('Возобновить', 'Завершить')}
                            </button>
                        </form>
                    </td>
                </tr>
                <#else>
                <tr>
                    <td colspan="5" class="no-notes">Заметки не найдены</td>
                </tr>
                </#list>
            </tbody>
        </table>

        <#if notes?size gt 0>
            <div class="pagination">
                <#if currentPage gt 0>
                    <a href="/notes?page=${currentPage - 1}&sort=${sort}&direction=${direction}&query=${query!''}"
                       class="page-link">← Назад</a>
                </#if>

                <span class="page-info">Страница ${currentPage + 1} из ${totalPages}</span>

                <#if currentPage lt totalPages - 1>
                    <a href="/notes?page=${currentPage + 1}&sort=${sort}&direction=${direction}&query=${query!''}"
                       class="page-link">Вперед →</a>
                </#if>
            </div>
        </#if>
    </div>
</body>
</html>