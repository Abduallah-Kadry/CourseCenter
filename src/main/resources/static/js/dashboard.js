// Configuration
const config = window.APP_CONFIG || {};
const API_BASE = config.apiBase;
const USER_BASE = config.userBase;

// DOM Elements
const usersTableBody = document.getElementById('usersTableBody');
const pagination = document.getElementById('pagination');

// State
let currentPage = 0;
const pageSize = 10;
let totalPages = 1;

// Initialize the dashboard when the DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
  loadUsers(currentPage);
  // Event delegation for action buttons
  usersTableBody.addEventListener('click', handleTableActions);
});

// Load users with pagination
async function loadUsers(page) {
  try {
    showLoading();
    const response = await fetch(`${API_BASE}${USER_BASE}?page=${page}&size=${pageSize}`, {
      method: 'GET',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    currentPage = data.data.number;
    totalPages = data.data.totalPages;

    renderUsers(data.data.content);
    renderPagination();
  } catch (error) {
    console.error('Error loading users:', error);
    showError('Failed to load users. Please try again.');
  } finally {
    hideLoading();
  }
}

// Render users in the table
function renderUsers(users) {
  if (!users || !Array.isArray(users)) {
    usersTableBody.innerHTML = `
            <tr>
                <td colspan="5" class="text-center">No users found</td>
            </tr>
        `;
    return;
  }

  usersTableBody.innerHTML = users.map(user => {

    const isAdmin = user.authorities && user.authorities.some(auth =>
      auth.authority === 'ROLE_ADMIN');


    const roleBadge = isAdmin
      ? '<span class="badge bg-success">Admin</span>'
      : '<span class="badge bg-secondary">Student</span>';

    return `
            <tr>
                <td>${user.id || 'N/A'}</td>
                <td>${user.firstName || ''} ${user.lastName || ''}</td>
                <td>${user.email || 'N/A'}</td>
                <td>${roleBadge}</td>
                <td>
                    ${!isAdmin ? `
                        <button class="btn btn-sm btn-outline-danger delete-user-btn" 
                                data-user-id="${user.id}" 
                                title="Delete User">
                            <i class="bi bi-trash"></i>
                        </button>
                    ` : ''}
                </td>
            </tr>
        `;
  }).join('');
}

// Render pagination controls
function renderPagination() {
  if (!pagination) return;

  let paginationHTML = '';

  // Previous button
  paginationHTML += `
        <li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
            <a class="page-link" href="#" ${currentPage === 0 ? 'tabindex="-1"' : ''}>
                &laquo;
            </a>
        </li>
    `;

  // Page numbers
  for (let i = 0; i < totalPages; i++) {
    paginationHTML += `
            <li class="page-item ${i === currentPage ? 'active' : ''}">
                <a class="page-link" href="#">${i + 1}</a>
            </li>
        `;
  }

  // Next button
  paginationHTML += `
        <li class="page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}">
            <a class="page-link" href="#" ${currentPage === totalPages - 1 ? 'tabindex="-1"' : ''}>
                &raquo;
            </a>
        </li>
    `;

  pagination.innerHTML = paginationHTML;

  // Add event listeners to pagination links
  document.querySelectorAll('.page-link').forEach((link, index) => {
    link.addEventListener('click', (e) => {
      e.preventDefault();
      if (index === 0 && currentPage > 0) {
        // Previous page
        loadUsers(currentPage - 1);
      } else if (index === totalPages + 1 && currentPage < totalPages - 1) {
        // Next page
        loadUsers(currentPage + 1);
      } else if (index > 0 && index <= totalPages) {
        // Specific page
        const page = index - 1;
        if (page !== currentPage) {
          loadUsers(page);
        }
      }
    });
  });
}

// Handle table actions (promote, delete)
async function handleTableActions(event) {
  const target = event.target.closest('button');
  if (!target) return;

  const userId = target.getAttribute('data-user-id');
  if (!userId) return;

  if (target.classList.contains('promote-user-btn')) {
    await promoteToAdmin(userId);
  } else if (target.classList.contains('delete-user-btn')) {
    if (confirm('Are you sure you want to delete this user? This action cannot be undone.')) {
      await deleteUser(userId);
    }
  }
}

// Promote user to admin
async function promoteToAdmin(userId) {
  try {
    const response = await fetch(`${API_BASE}/admin/${userId}/role`, {
      method: 'PUT',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    console.log(response)

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response}`);
    }


    // Reload users to reflect changes
    loadUsers(currentPage);
    showNotification('User promoted to admin successfully!','success');
  } catch (error) {

    console.error('Error promoting user:', error);
    showNotification(error,'danger');
  }
}

// Delete user
async function deleteUser(userId) {
  try {
    const response = await fetch(`${API_BASE}${USER_BASE}/${userId}`, {
      method: 'DELETE',
      credentials: 'include'
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    // Reload users to reflect changes
    await loadUsers(currentPage);
    showNotification('User deleted successfully!');
  } catch (error) {
    console.error('Error deleting user:', error);
    showNotification(error,'danger');
  }
}

// Helper function to show loading state
function showLoading() {
  // You can implement a loading spinner here if needed
  document.body.style.cursor = 'wait';
}

// Helper function to hide loading state
function hideLoading() {
  document.body.style.cursor = 'default';
}

// Helper function to show success message

// Notification elements
const notification = document.getElementById('notification');
const alertBox = document.getElementById('alertBox');
const notificationMessage = document.getElementById('notificationMessage');
let notificationTimeout;

function showNotification(message, type) {

  // Clear any existing timeout to prevent premature hiding
  if (notificationTimeout) {
    clearTimeout(notificationTimeout);
  }

  // Update notification content and styling
  notificationMessage.textContent = message;
  alertBox.className = `alert alert-${type} alert-dismissible fade show`;
  notification.classList.add('show');

  // Auto-hide after 5 seconds
  notificationTimeout = setTimeout(hideNotification, 5000);
}

// Hide notification function
function hideNotification() {
  notification.classList.remove('show');
  // Wait for fade out animation to complete before removing content
  setTimeout(() => {
    notificationMessage.textContent = '';
    alertBox.className = 'alert';
  }, 300);


}


// Helper function to show error message
function showError(message) {
  // You can use a toast notification library or show a simple alert
  alert('Error: ' + message);
}

// Make functions available globally if needed
window.dashboard = {
  loadUsers,
  promoteToAdmin,
  deleteUser
};