// Environment variables
const API_BASE = window.APP_CONFIG.apiBase;
const AUTH_BASE = window.APP_CONFIG.authBase;
const FRONTEND_BASE = window.APP_CONFIG.frontendBase;
const COURSE_BASE = window.APP_CONFIG.courseBase;
const API_URL = `${API_BASE}${COURSE_BASE}`;

// DOM Elements
const courseForm = document.getElementById('courseForm');
const submitBtn = document.getElementById('submitBtn');
const submitSpinner = document.getElementById('submitSpinner');
const notification = document.getElementById('notification');
const notificationMessage = document.getElementById('notificationMessage');
const alertBox = notification.querySelector('.alert');
const courseId = document.getElementById('courseId').value;

// Show notification
function showNotification(message, type = 'success') {
  notificationMessage.textContent = message;
  alertBox.className = `alert alert-${type} alert-dismissible fade show`;
  notification.classList.add('show');
  setTimeout(hideNotification, 5000);
}

// Hide notification
function hideNotification() {
  notification.classList.remove('show');
}

// Form submission handler
async function submitForm(event) {
  event.preventDefault();

  // Show loading state
  submitBtn.disabled = true;
  submitSpinner.classList.remove('d-none');

  try {
    const formData = new FormData(courseForm);
    
    const response = await fetch(`${API_URL}/${courseId}`, {
      method: 'PUT',
      body: formData,
      credentials: 'include'
    });

    const data = await response.json();

    if (response.ok) {
      showNotification('Course updated successfully!', 'success');
      setTimeout(() => {
        window.location.href = FRONTEND_BASE + COURSE_BASE;
      }, 1500);
    } else {
      throw new Error(data.message || 'Failed to update course');
    }

  } catch (error) {
    console.error('Error:', error);
    showNotification(error.message || 'An error occurred while updating the course', 'danger');
  } finally {
    submitBtn.disabled = false;
    submitSpinner.classList.add('d-none');
  }
}

// Logout functionality
document.getElementById("logoutBtn").addEventListener("click", async () => {
  if (confirm("Are you sure you want to log out?")) {
    try {
      const response = await fetch(`${API_BASE}${AUTH_BASE}/logout`, {
        method: "POST",
        credentials: "include"
      });

      if (response.ok) {
        window.location.href = FRONTEND_BASE + "/login";
      } else {
        throw new Error("Logout failed");
      }
    } catch (error) {
      showNotification("Logout failed. Please try again.", "danger");
    }
  }
});

// Image preview functionality
function previewImage(input) {
  const preview = document.getElementById('imagePreview');
  if (input.files && input.files[0]) {
    const reader = new FileReader();
    reader.onload = function (e) {
      preview.src = e.target.result;
      preview.style.display = 'block';
    }
    reader.readAsDataURL(input.files[0]);
  }
}

// Load course data when the page loads
document.addEventListener('DOMContentLoaded', () => {
  // The course data is already pre-filled by Thymeleaf
  // This event listener ensures the DOM is fully loaded before any JS runs
});
