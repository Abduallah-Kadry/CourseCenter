// Environment variables
const { apiBase: API_BASE, authBase: AUTH_BASE, frontendBase: FRONTEND_BASE,
        courseBase: COURSE_BASE, enrollmentBase: ENROLLMENT_BASE } = window.APP_CONFIG;

// DOM Elements
const DOM = {
  enrolled: document.getElementById('enrolledCourses'),
  noEnrolled: document.getElementById('noEnrolledCourses'),
  recommended: document.getElementById('recommendedCoursesContainer'),
  spinner: document.getElementById('loadingSpinner'),
  logoutBtn: document.getElementById('logoutBtn'),
  notification: document.getElementById('notification'),
  alertBox: document.getElementById('alertBox'),
  msgEl: document.getElementById('notificationMessage'),
};

// Init
document.addEventListener('DOMContentLoaded', () => {
  if (DOM.logoutBtn) {
    DOM.logoutBtn.addEventListener('click', handleLogout);
  }

  loadDashboard();
});

// Unified fetch wrapper
async function apiGet(url) {
  const response = await fetch(url, {
    method: 'GET',
    credentials: 'include',
    headers: { 'Accept': 'application/json' }
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(`HTTP ${response.status}: ${text}`);
  }

  return response.json().catch(() => ({}));
}

// Load dashboard data
async function loadDashboard() {
  try {
    await Promise.all([
      loadEnrolled(),
      loadRecommended()
    ]);
  } catch (err) {
    console.error(err);
    showNotification("Failed to load dashboard data.", "danger");
  }
}

// Load enrolled courses
async function loadEnrolled() {
  showLoading(true);

  try {
    const result = await apiGet(`${API_BASE}${ENROLLMENT_BASE}/enrolledCourses`);
    const courses = Array.isArray(result) ? result : result.data || [];
    renderEnrolledCourses(courses);
  } catch (err) {
    console.error(err);
    showNotification("Could not load enrolled courses.", "danger");
  } finally {
    showLoading(false);
  }
}

// Load recommended courses
async function loadRecommended() {
  showLoading(true);

  try {
    const result = await apiGet(`${API_BASE}${ENROLLMENT_BASE}/non-enrolled-random/4`);
    const courses = Array.isArray(result) ? result : result.data || [];
    renderRecommendedCourses(courses);
  } catch (err) {
    console.error(err);
    showNotification("Failed to load recommended courses.", "danger");
  } finally {
    showLoading(false);
  }
}

// Render enrolled
function renderEnrolledCourses(courses) {
  if (!courses.length) {
    DOM.noEnrolled.style.display = 'block';
    return;
  }

  DOM.noEnrolled.style.display = 'none';
  DOM.enrolled.innerHTML = '';

  courses.forEach(c => DOM.enrolled.appendChild(createCourseCard(c, true)));
}

// Render recommended
function renderRecommendedCourses(courses) {
  DOM.recommended.innerHTML = '';

  if (!courses.length) {
    DOM.recommended.innerHTML = `
      <div class="col-12 text-center py-4">
        <p class="text-muted">No recommended courses available right now.</p>
      </div>`;
    return;
  }

  courses.forEach(c => DOM.recommended.appendChild(createCourseCard(c, false)));
}



// Load average rating via API
async function loadAverageRating(courseId) {
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

function createCourseCard(course, isEnrolled) {
  const col = document.createElement('div');
  col.className = 'col-md-6 col-lg-4 col-xl-3 mb-4';

  const card = document.createElement('div');
  card.className = 'course-card h-100';

  const { id, name, instructorName, imageUrl, cost } = course;

  const ratingContainerId = `ratingStars-${id}`;
  const ratingTextId = `averageRating-${id}`;

  card.innerHTML = `
    <div class="course-img-container">
      <img src="${imageUrl || '/images/course-placeholder.jpg'}"
           class="course-img"
           onerror="this.src='/images/course-placeholder.jpg'"
           alt="${name || 'Course'}">
      ${isEnrolled ? '<span class="course-badge">Enrolled</span>' : ''}
    </div>

    <div class="course-body">
      <h5 class="course-title">${name || 'Untitled Course'}</h5>

      <div class="d-flex align-items-center gap-2 mt-1" id="ratingWrap-${id}">
         <div id="${ratingContainerId}" class="d-flex"></div>
         <small id="${ratingTextId}" class="text-muted"></small>
      </div>

      <div class="mt-auto">
        <div class="course-stats mt-2">
          <span class="course-price">${cost ? `$${parseFloat(cost).toFixed(2)}` : 'Free'}</span>
        </div>

        <a href="${FRONTEND_BASE}${COURSE_BASE}/${id}" class="btn btn-primary w-100 mt-2">
          ${isEnrolled ? 'Continue Learning' : 'View Details'}
        </a>
      </div>
    </div>
  `;

  col.appendChild(card);

  // ⬇️ Load dynamic average rating for each card
  loadAverageRatingForCard(id, ratingContainerId, ratingTextId);

  return col;
}

function updateRatingDisplayForCard(averageRating, starsId, textId) {
  const ratingContainer = document.getElementById(starsId);
  const ratingText = document.getElementById(textId);

  if (!ratingContainer || !ratingText) return;

  const rounded = Math.round(averageRating * 2) / 2;
  const full = Math.floor(rounded);
  const half = rounded % 1 !== 0;
  const empty = 5 - Math.ceil(rounded);

  let starsHtml = "";

  for (let i = 0; i < full; i++) starsHtml += '<i class="fas fa-star text-warning"></i>';
  if (half) starsHtml += '<i class="fas fa-star-half-alt text-warning"></i>';
  for (let i = 0; i < empty; i++) starsHtml += '<i class="far fa-star text-warning"></i>';

  ratingContainer.innerHTML = starsHtml;
  ratingText.textContent = averageRating.toFixed(1);
}


async function loadAverageRatingForCard(courseId, starsId, textId) {
  try {
    const response = await fetch(`${API_BASE}${COURSE_BASE}/${courseId}/average-rate`, {
      credentials: 'include'
    });

    if (!response.ok) return;

    const rating = await response.json();
    updateRatingDisplayForCard(rating, starsId, textId);

  } catch (error) {
    console.error("Error loading rating:", error);
  }
}


// Loading indicator
function showLoading(show) {
  if (!DOM.spinner) return;
  DOM.spinner.style.display = show ? 'block' : 'none';
}

// Notifications
function showNotification(message, type = "success") {
  if (!DOM.notification || !DOM.alertBox || !DOM.msgEl) return;

  DOM.msgEl.textContent = message;
  DOM.alertBox.className = `alert alert-${type} alert-dismissible fade show`;
  DOM.notification.classList.add('show');

  setTimeout(hideNotification, 5000);
}

function hideNotification() {
  if (DOM.notification) DOM.notification.classList.remove('show');
}

// Logout
async function handleLogout(e) {
  e.preventDefault();

  const confirm = await confirmDialog("Are you sure you want to logout?");
  if (!confirm) return;

  try {
    const response = await fetch(`${API_BASE}${AUTH_BASE}/logout`, {
      method: 'POST',
      credentials: 'include'
    });

    if (!response.ok) throw new Error("Logout failed");
    window.location.href = `${FRONTEND_BASE}/login`;

  } catch (err) {
    showNotification("Failed to logout.", "danger");
  }
}

// Confirmation modal promise
function confirmDialog(message) {
  return new Promise(resolve => {
    const modalEl = document.getElementById('confirmationModal');
    const btn = document.getElementById('confirmAction');
    const msgEl = document.getElementById('confirmationMessage');

    if (!modalEl || !btn || !msgEl) {
      resolve(false);
      return;
    }

    msgEl.textContent = message;
    const modal = new bootstrap.Modal(modalEl);

    const confirm = () => {
      cleanup();
      resolve(true);
    };

    const cancel = () => {
      cleanup();
      resolve(false);
    };

    function cleanup() {
      btn.removeEventListener('click', confirm);
      modalEl.removeEventListener('hidden.bs.modal', cancel);
    }

    btn.addEventListener('click', confirm);
    modalEl.addEventListener('hidden.bs.modal', cancel);
    modal.show();
  });
}

// expose
window.hideNotification = hideNotification;
window.showNotification = showNotification;
