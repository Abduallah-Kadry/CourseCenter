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


// Show notification9
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


console.log(courseForm);

// Form submission handler
async function submitForm(event) {
  event.preventDefault();

  // Show loading state
  submitBtn.disabled = true;
  submitSpinner.classList.remove('d-none');

  try {
    const formData = new FormData(courseForm);
    console.log(formData)
    const response = await fetch(API_URL, {
      method: 'POST',
      body: formData,
      credentials: 'include'
    });

    const data = await response.json();
    console.log(data)

    if (response.ok) {
      showNotification('Course created successfully!', 'success');
      setTimeout(() => {
        window.location.href = FRONTEND_BASE + `${COURSE_BASE}`;
      }, 1500);
    } else {
      throw new Error(data.message || 'Failed to create course');
    }

  } catch (error) {

    console.error('Error:', error);
    showNotification(error.message || 'An error occurred while creating the course', 'danger');
  } finally {
    submitBtn.disabled = false;
    submitSpinner.classList.add('d-none');
  }
}

// Logout functionality
document.getElementById("logoutBtn").addEventListener("click", async () => {
  const isConfirmed = await showConfirmation('Are you sure you want to logout?');
  if (!isConfirmed) return;

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