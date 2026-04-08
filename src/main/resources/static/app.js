document.addEventListener('DOMContentLoaded', () => {
    fetch('http://localhost:8080/api/user')
        .then(response => response.json())
        .then(data => {
            document.getElementById("navbarUsername").textContent = data.username;
            let joinRoles = data.roles.map(r => r.role ? r.role : r).join(", ");
            document.getElementById("navbarRoles").textContent = joinRoles;
        });

    fetch('http://localhost:8080/api/admin')
        .then(response => response.json())
        .then(data => {
            let userTable = document.getElementById("usersTable");
            userTable.innerHTML = "";
            data.forEach(user => {
                let roles = user.roles.map(r => r.role ? r.role : r).join(", ");
                let row = `
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${roles}</td>
                        <td>
                            <button class="btn btn-sm btn-info" data-bs-toggle="modal" 
                            data-bs-target="#editModal${user.id}">Редактировать</button>
                            <form action="/admin/delete" method="post" style="display:inline;">
                                <input type="hidden" name="id" value="${user.id}">
                                <button class="btn btn-sm btn-danger">Удалить</button>
                            </form>
                        </td>
                    </tr>
                `;
                userTable.innerHTML += row;
            });

            let rolesContainer = document.getElementById("roles");
            rolesContainer.innerHTML = "";
            if (data.length > 0 && data[0].roles) {
                let roleList = data[0].roles;
                roleList.forEach(role => {
                    let roleName = role.role ? role.role : role;
                    let label = document.createElement("label");
                    label.className = "form-check-label me-2";
                    let checkbox = document.createElement("input");
                    checkbox.type = "checkbox";
                    checkbox.value = roleName;
                    checkbox.name = "roles";
                    checkbox.className = "form-check-input me-1";
                    label.appendChild(checkbox);
                    label.appendChild(document.createTextNode(roleName));
                    rolesContainer.appendChild(label);
                });
            }
        });

    let createButton = document.getElementById("createButton");
    createButton.addEventListener('click', (event) => {
        event.preventDefault();
        let username = document.getElementById("newUserName").value;
        let password = document.getElementById("newPassword").value;
        let roles = [];
        document.querySelectorAll('#roles input[type="checkbox"]:checked').forEach(cb => {
            roles.push(cb.value);
        });

        let newUser = {username, password, roles};
        fetch('http://localhost:8080/api/admin/new', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(newUser)
        })
            .then(response => {
                if (response.ok) {
                    alert("Пользователь создан");
                    document.getElementById("newUserName").value = "";
                    document.getElementById("newPassword").value = "";
                    document.querySelectorAll('#roles input[type="checkbox"]')
                        .forEach(cb => cb.checked = false);
                } else {
                    alert("Ошибка при создании пользователя");
                }
                window.location.href = '/admin';
            })
            .catch(error => console.error(error));
    });
});

document.addEventListener('DOMContentLoaded', () => {
    let allUsers = [];

    fetch('http://localhost:8080/api/admin')
        .then(response => response.json())
        .then(data => {
            allUsers = data;
            let userTable = document.getElementById("usersTable");
            userTable.innerHTML = "";
            data.forEach(user => {
                let roles = user.roles.map(r => r.role ? r.role : r).join(", ");
                let row = `
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.username}</td>
                        <td>${roles}</td>
                        <td>
                            <button class="btn btn-sm btn-info" data-bs-toggle="modal" 
                            data-bs-target="#editModal${user.id}">Редактировать</button>
                            <form action="/admin/delete" method="post" style="display:inline;">
                                <input type="hidden" name="id" value="${user.id}">
                                <button class="btn btn-sm btn-danger">Удалить</button>
                            </form>
                        </td>
                    </tr>
                `;
                userTable.innerHTML += row;
            });

            allUsers.forEach(user => {
                let modal = document.getElementById('editModal' + user.id);
                if (!modal) return;

                let usernameInput = modal.querySelector('input[name="username"]');
                let passwordInput = modal.querySelector('input[name="password"]');
                let roleCheckboxes = modal.querySelectorAll('input[name="roles"]');

                modal.addEventListener('show.bs.modal', () => {
                    usernameInput.value = user.username;
                    passwordInput.value = "";
                    roleCheckboxes.forEach(cb => {
                        cb.checked = user.roles.some(r => (r.role ? r.role : r) === cb.value);
                    });
                });

                modal.querySelector('.btn-primary').addEventListener('click', (event) => {
                    event.preventDefault();
                    let updatedUsername = usernameInput.value;
                    let updatedPassword = passwordInput.value;
                    let updatedRoles = [];
                    modal.querySelectorAll('input[name="roles"]:checked')
                        .forEach(cb => updatedRoles.push(cb.value));

                    let updatedUser = {
                        id: user.id,
                        username: updatedUsername,
                        password: updatedPassword,
                        roles: updatedRoles
                    };

                    fetch('/admin/edit', {
                        method: 'PUT',
                        headers: {'Content-Type': 'application/json'},
                        body: JSON.stringify(updatedUser)
                    })
                        .then(response => {
                            if (response.ok) {
                                alert('Пользователь отредактирован');
                                window.location.href = '/admin';
                            } else {
                                alert('Ошибка при редактировании пользователя');
                            }
                        })
                        .catch(error => console.error(error));
                });
            });
        });
});



