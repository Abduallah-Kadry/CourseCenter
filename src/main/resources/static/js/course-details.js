// Environment variables from Thymeleaf
const API_BASE = window.APP_CONFIG.apiBase;
const AUTH_BASE = window.APP_CONFIG.authBase;
const FRONTEND_BASE = window.APP_CONFIG.frontendBase;
const COURSE_BASE = window.APP_CONFIG.courseBase;
const USER_BASE = window.APP_CONFIG.userBase;

// Get course ID from URL
const courseId = window.location.pathname.split('/').pop();

// DOM Elements
const enrollBtn = document.querySelector('.enroll-btn');

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    setupEventListeners();
    loadAverageRating();
});





// Load average rating via API
async function loadAverageRating() {
    try {
        const response = await fetch(`${API_BASE}${COURSE_BASE}/${courseId}/average-rate`, {
            credentials: 'include'
        });
        
        if (response.ok) {
            const rating = await response.json();
            updateRatingDisplay(rating);
        }
    } catch (error) {
        console.error('Error loading average rating:', error);
    }
}

// Update the rating display with new average rating
function updateRatingDisplay(averageRating) {
    const ratingContainer = document.getElementById('ratingStars');
    const ratingText = document.getElementById('averageRating');
    
    if (!ratingContainer || !ratingText) return;
    
    // Round to nearest 0.5 for display
    const roundedRating = Math.round(averageRating * 2) / 2;
    const fullStars = Math.floor(roundedRating);
    const hasHalfStar = roundedRating % 1 !== 0;
    
    // Generate stars HTML
    let starsHtml = '';
    
    // Full stars
    for (let i = 0; i < fullStars; i++) {
        starsHtml += '<i class="fas fa-star text-warning"></i>';
    }
    
    // Half star if needed
    if (hasHalfStar) {
        starsHtml += '<i class="fas fa-star-half-alt text-warning"></i>';
    }
    
    // Empty stars
    const emptyStars = 5 - Math.ceil(roundedRating);
    for (let i = 0; i < emptyStars; i++) {
        starsHtml += '<i class="far fa-star text-warning"></i>';
    }
    
    // Update the DOM
    ratingContainer.innerHTML = starsHtml;
    ratingText.textContent = averageRating.toFixed(1);
    
    // Show the rating container if it was hidden
    ratingContainer.closest('.d-flex').style.display = 'flex';
}

// Handle enroll button click
async function handleEnroll() {
    if (!confirm('Are you sure you want to enroll in this course?')) {
        return;
    }

    const enrollBtn = document.querySelector('.enroll-btn');
    const originalText = enrollBtn.innerHTML;
    
    try {
        // Disable button and show loading state
        enrollBtn.disabled = true;
        enrollBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Enrolling...';

        const response = await fetch(`${API_BASE}${USER_BASE}${COURSE_BASE}/reserve/${courseId}`, {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            showNotification('Successfully enrolled in the course!', 'success');
            // Optionally update the UI to show enrollment status
            enrollBtn.classList.remove('btn-primary');
            enrollBtn.classList.add('btn-success');
            enrollBtn.innerHTML = '<i class="fas fa-check me-2"></i>Enrolled';
        } else {
            const error = await response.text();
            throw new Error(error || 'Failed to enroll in the course');
        }
    } catch (error) {
        console.error('Enrollment error:', error);
        showNotification(error.message || 'An error occurred while enrolling in the course', 'danger');
        // Reset button state
        enrollBtn.disabled = false;
        enrollBtn.innerHTML = originalText;
    }
}

// Setup event listeners
function setupEventListeners() {
    // Enroll button
    if (enrollBtn) {
        enrollBtn.addEventListener('click', handleEnroll);
    }
    
    // Logout button
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', handleLogout);
    }
}

// Handle logout
async function handleLogout(event) {
    event.preventDefault();
    
    if (confirm('Are you sure you want to log out?')) {
        try {
            const response = await fetch(`${API_BASE}${AUTH_BASE}/logout`, {
                method: 'POST',
                credentials: 'include'
            });
            
            if (response.ok) {
                window.location.href = FRONTEND_BASE + '/login';
            } else {
                throw new Error('Logout failed');
            }
        } catch (error) {
            console.error('Logout error:', error);
            showNotification('Failed to log out. Please try again.', 'danger');
        }
    }
}

// Notification elements
const notification = document.getElementById('notification');
const alertBox = document.getElementById('alertBox');
const notificationMessage = document.getElementById('notificationMessage');
let notificationTimeout;

// Show notification function
function showNotification(message, type = 'success') {
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
