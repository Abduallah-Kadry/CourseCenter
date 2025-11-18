console.log('courses.js is loaded!');


// Environment variables
const API_BASE = window.APP_CONFIG.apiBase;
const AUTH_BASE = window.APP_CONFIG.authBase;
const FRONTEND_BASE = window.APP_CONFIG.frontendBase;
const COURSE_BASE = window.APP_CONFIG.courseBase;


const API_URL = `${API_BASE}${COURSE_BASE}`;


console.log(API_BASE)
console.log(AUTH_BASE)
console.log(FRONTEND_BASE)
console.log(COURSE_BASE)


// State
let currentPage = 0;
const pageSize = 8; // Number of items per page
let totalPages = 0;

// DOM Elements
const coursesGrid = document.getElementById('coursesGrid');
const pagination = document.getElementById('pagination');
const loadingSpinner = document.getElementById('loadingSpinner');

// Initialize the page
document.addEventListener('DOMContentLoaded', () => {
  loadCourses(currentPage, pageSize);
  setupEventListeners();
});

// Setup event listeners
function setupEventListeners() {
  // Logout button
  document.getElementById('logoutBtn').addEventListener('click', handleLogout);
}

// Load courses with pagination
async function loadCourses(page, size) {
  try {
    showLoading(true);
    const response = await fetch(`${API_URL}?page=${page}&size=${size}`, {
      credentials: 'include'
    });

    if (!response.ok) {
      throw new Error('Failed to fetch courses');
    }
    const data = await response.json();

    totalPages = data.data.totalPages || 1;
    const courses = data.data.content;

    renderCourses(courses);
    renderPagination();
  } catch (error) {
    console.error('Error loading courses:', error);
    showNotification('Failed to load courses. Please try again.', 'danger');
  } finally {
    showLoading(false);
  }
}


async function getAverageRating(courseId) {
  try {
    const response = await fetch(`${API_BASE}${COURSE_BASE}/${courseId}/average-rate`);
    if (!response.ok) {
      console.error('Failed to fetch average rating for course', courseId);
      return 0;
    }
    return await response.json();
  } catch (error) {
    console.error('Error fetching average rating:', error);
    return 0;
  }
}

// Render courses grid

async function renderCourses(courses) {
  if (!courses || courses.length === 0) {
    coursesGrid.innerHTML = `
            <div class="col-12 text-center py-5">
                <i class="bi bi-inbox" style="font-size: 3rem; opacity: 0.5;"></i>
                <h4 class="mt-3">No courses found</h4>
                <p class="text-muted">Add a new course to get started</p>
            </div>`;
    return;
  }

  // Show loading state
  coursesGrid.innerHTML = '<div class="col-12 text-center py-5">Loading courses...</div>';

  try {
    // Fetch ratings for all courses in parallel
    const coursesWithRatings = await Promise.all(courses.map(async course => {
      const averageRating = await getAverageRating(course.id);
      return {...course, averageRating};
    }));


    // Render courses with their ratings
    coursesGrid.innerHTML = coursesWithRatings.map(course => `
             <div class="col-md-4 col-lg-3 mb-4">
                <div class="card course-card h-100">
                    <img src="${course.imageUrl}"
                         class="card-img-top course-image"
                         alt="${course.name}"
                       >
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">${course.name || 'Untitled Course'}</h5>
                        <div class="mb-2">
                            ${Array(5).fill().map((_, i) =>
      `<span class="${i < Math.round(course.averageRating || 0) ? 'fas fa-star star' : 'far fa-star'}"></span>`
    ).join('')}
                            <small class="text-muted ms-2">(${course.averageRating ? course.averageRating.toFixed(1) : 'N/A'})</small>
                        </div>

                        <div class="mt-auto">
                            <a href="${FRONTEND_BASE}${COURSE_BASE}/${course.id}"
                               class="btn btn-outline-primary w-100">
                                View Details
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        `).join('');

  } catch (error) {
    console.error('Error rendering courses:', error);
    coursesGrid.innerHTML = `
            <div class="col-12 text-center py-5">
                <i class="bi bi-exclamation-triangle text-danger" style="font-size: 3rem;"></i>
                <h4 class="mt-3">Failed to load course ratings</h4>
                <p class="text-muted">Please try refreshing the page</p>
            </div>`;
  }
}

// Render pagination controls
function renderPagination() {
  if (totalPages <= 1) {
    pagination.style.display = 'none';
    return;
  }

  let paginationHTML = `
            <li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${currentPage - 1}">Previous</a>
            </li>`;

  for (let i = 0; i < totalPages; i++) {
    paginationHTML += `
                <li class="page-item ${i === currentPage ? 'active' : ''}">
                    <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
                </li>`;
  }

  paginationHTML += `
            <li class="page-item ${currentPage >= totalPages - 1 ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${currentPage + 1}">Next</a>
            </li>`;

  pagination.innerHTML = paginationHTML;
  pagination.style.display = 'flex';

  // Add click event listeners to pagination links
  document.querySelectorAll('.page-link[data-page]').forEach(link => {
    link.addEventListener('click', (e) => {
      e.preventDefault();
      const page = parseInt(link.dataset.page, 10);
      if (!isNaN(page) && page >= 0 && page < totalPages && page !== currentPage) {
        currentPage = page;
        loadCourses(currentPage, pageSize);
        window.scrollTo(0, 0);
      }
    });
  });
}

// Show/hide loading spinner
function showLoading(show) {
  if (loadingSpinner) loadingSpinner.style.display = show ? 'block' : 'none';
  if (coursesGrid) coursesGrid.style.visibility = show ? 'hidden' : 'visible';
}

// Show notification
function showNotification(message, type = 'success') {
  console.log(`[${type}] ${message}`);
  // Simple alert for now, can be replaced with a toast notification
  alert(`${type.toUpperCase()}: ${message}`);
}

// Handle logout
async function handleLogout(e) {
  e.preventDefault();
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
}