<!DOCTYPE html>
<html>
<head>
    <title>Управление пользователями</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
    <div class="container">
        <div class="header">
            <a href="/notes" class="btn">К заметкам</a>
            <a href="/logout" class="logout-btn">Выйти</a>
        </div>

        <h1>Управление пользователями</h1>

        <form action="/admin" method="get" class="search-form">
            <input type="text" name="query" placeholder="Поиск пользователей..." value="${query!''}">
            <button type="submit">Поиск</button>
        </form>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Имя пользователя</th>
                    <th>Статус</th>
                    <th>Роль</th>
                    <th>Действия</th>
                </tr>
            </thead>
            <tbody>
                <#list users as user>
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td class="${user.active?then('completed', 'pending')}">
                        ${user.active?then('Активен', 'Неактивен')}
                    </td>
                    <td>${user.role}</td>
                    <td class="actions-cell">
                        <form action="/admin/toggle/${user.id}" method="post" class="inline-form">
                            <button type="submit" class="action-btn toggle">
                                ${user.active?then('Деактивировать', 'Активировать')}
                            </button>
                        </form>

                        <form action="/admin/role/${user.id}" method="post" class="inline-form">
                            <select name="role" class="role-select">
                                <option value="USER" ${(user.role == 'USER')?then('selected','')}>USER</option>
                                <option value="ADMIN" ${(user.role == 'ADMIN')?then('selected','')}>ADMIN</option>
                            </select>
                            <button type="submit" class="action-btn edit">Изменить</button>
                        </form>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>

        <div class="pagination">
            <#if currentPage gt 0>
                <a href="/admin?page=${currentPage - 1}&query=${query!''}" class="page-link">← Назад</a>
            </#if>

            <span class="page-info">Страница ${currentPage + 1} из ${totalPages}</span>

            <#if currentPage lt totalPages - 1>
                <a href="/admin?page=${currentPage + 1}&query=${query!''}" class="page-link">Вперед →</a>
            </#if>
        </div>
    </div>
</body>
</html>