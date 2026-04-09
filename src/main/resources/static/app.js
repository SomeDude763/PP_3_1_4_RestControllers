document.addEventListener('DOMContentLoaded', () => {
    let allUsers = [];
    let allRoles = [];

    const usersTable = document.getElementById("usersTable");
    const modalsContainer = document.getElementById("editModalsContainer");

    fetch('http://localhost:8080/api/user')
        .then(response => response.json())
        .then(user => {
            document.getElementById("navbarUsername").textContent = user.username;
            document.getElementById("tableUsername").textContent = user.username;
            document.getElementById("tableId").textContent = user.id;
            const joinRoles = user.roles.join(", ");
            document.getElementById("navbarRoles").textContent = joinRoles;
            document.getElementById("tableRoles").textContent = joinRoles;
        });

    fetch('http://localhost:8080/api/admin/roles')
        .then(response => response.json())
        .then(data => {
            allRoles = data;

            const rolesContainer = document.getElementById("roles");
            rolesContainer.innerHTML = "";
            allRoles.forEach(role => {
                const label = document.createElement("label");
                label.className = "form-check-label me-2";

                const checkbox = document.createElement("input");
                checkbox.type = "checkbox";
                checkbox.value = role;
                checkbox.name = "roles";
                checkbox.className = "form-check-input me-1";

                label.appendChild(checkbox);
                label.appendChild(document.createTextNode(role));
                rolesContainer.appendChild(label);
            });
        });

    fetch('http://localhost:8080/api/admin')
        .then(response => response.json())
        .then(data => {
            allUsers = data;
            renderUserTable();
        });

    modalsContainer.innerHTML = `
    <div class="modal fade" id="editModal" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <form id="editUserForm">
            <div class="modal-header">
              <h5>Редактировать пользователя</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
              <input type="hidden" id="editUserId">
              <div class="mb-3">
                <label>Username</label>
                <input type="text" id="editUsername" class="form-control">
              </div>
              <div class="mb-3">
                <label>Password</label>
                <input type="text" id="editPassword" class="form-control">
              </div>
              <div class="mb-3">
                <label>Роли</label>
                <div id="editRolesContainer"></div>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрыть</button>
              <button type="submit" class="btn btn-primary">Сохранить</button>
            </div>
          </form>
        </div>
      </div>
    </div>
    `;

    const editModalEl = document.getElementById('editModal');
    const editModal = new bootstrap.Modal(editModalEl);
    const editForm = document.getElementById('editUserForm');
    const editUserId = document.getElementById('editUserId');
    const editUsername = document.getElementById('editUsername');
    const editPassword = document.getElementById('editPassword');
    const editRolesContainer = document.getElementById('editRolesContainer');

    function renderUserTable() {
        usersTable.innerHTML = "";
        allUsers.forEach(user => {
            const roles = user.roles.join(", ");
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>${roles}</td>
                <td>
                    <button class="btn btn-sm btn-info edit-btn">Редактировать</button>
                    <form action="/admin/delete" method="post" style="display:inline;">
                        <input type="hidden" name="id" value="${user.id}">
                        <button class="btn btn-sm btn-danger">Удалить</button>
                    </form>
                   
                </td>
            `;
            const deleteBtn = row.querySelector(".btn-danger");
            deleteBtn.addEventListener('click', (event) => {
                const userId = user.id;
                fetch(`http://localhost:8080/api/admin/${userId}`, {
                    method: 'DELETE'
                })
                    .then(response => {
                        if (response.ok) {
                            row.remove();
                            alert("Пользователь удалён");
                        } else {
                            alert("Ошибка при удалении пользователя");
                        }
                    })
                    .catch(error => console.error(error));
            });

            usersTable.appendChild(row);

            const editBtn = row.querySelector(".edit-btn");
            editBtn.addEventListener('click', () => {
                openEditModal(user);
            });
        });
    }

    function openEditModal(user) {
        editUserId.value = user.id;
        editUsername.value = user.username;
        editPassword.value = "";

        editRolesContainer.innerHTML = "";
        allRoles.forEach(role => {
            const label = document.createElement("label");
            label.className = "form-check-label me-2";

            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkbox.value = role;
            checkbox.className = "form-check-input me-1";

            if (user.roles.includes(role)) checkbox.checked = true;

            label.appendChild(checkbox);
            label.appendChild(document.createTextNode(role));
            editRolesContainer.appendChild(label);
        });

        editModal.show();
    }

    editForm.addEventListener('submit', (e) => {
        e.preventDefault();

        const updatedRoles = [];
        editRolesContainer.querySelectorAll('input[type="checkbox"]:checked')
            .forEach(cb => updatedRoles.push(cb.value));

        const updatedUser = {
            id: editUserId.value,
            username: editUsername.value,
            password: editPassword.value,
            roles: updatedRoles
        };

        fetch('http://localhost:8080/api/admin/edit', {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(updatedUser)
        })
            .then(response => {
                if (response.ok) {
                    alert("Пользователь отредактирован");
                } else {
                    alert("Ошибка при редактировании пользователя");
                }
            })
            .catch(error => console.error(error));
    });

    const createButton = document.getElementById("createButton");
    createButton.addEventListener('click', (event) => {
        event.preventDefault();

        const username = document.getElementById("newUserName").value;
        const password = document.getElementById("newPassword").value;
        const roles = [];
        document.querySelectorAll('#roles input[type="checkbox"]:checked')
            .forEach(cb => roles.push(cb.value));

        const newUser = {username, password, roles};

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
            })
            .catch(error => console.error(error));
    });
});