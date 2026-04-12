const API_BASE = "http://localhost:8080";
let allPlansData = [];

async function fetchAllPlans() {
    try {
        const res = await fetch(`${API_BASE}/allPlans`);
        if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);

        allPlansData = await res.json();
        const grid = document.getElementById('cards-grid');
        grid.innerHTML = '';

        allPlansData.forEach((plan, planIdx) => {
            const col = document.createElement('div');
            col.className = 'col-12 col-md-6 col-lg-4';

            let sliderHtml = '';
            let dotsHtml = '';
            if (plan.photos && plan.photos.length > 0) {
                plan.photos.forEach((photo, photoIdx) => {
                    const active = photoIdx === 0 ? 'active' : '';
                    sliderHtml += `<img src="${API_BASE}/api/photos/${photo.id}" class="carousel-img ${active}" loading="lazy" alt="Travel Plan">`;
                    dotsHtml += `<span class="dot ${active}"></span>`;
                });
            }

            // Updated mobile-responsive layout for the card
            col.innerHTML = `
                <div class="travel-card h-100 d-flex flex-column" onclick="showFullDetails(${planIdx})">
                    <div class="card-img-container" onclick="event.stopPropagation(); nextImage(${planIdx})">
                        <div class="image-slider" id="slider-${planIdx}">
                            ${sliderHtml || '<img src="https://via.placeholder.com/400x300?text=No+Image" class="carousel-img active">'}
                        </div>
                        <div class="slider-nav">${dotsHtml}</div>
                        <div class="price-tag">₹${plan.planDetails.price}</div>
                    </div>
                    
                    <div class="p-4 d-flex flex-column" style="flex: 1;">
                        <div class="d-flex align-items-center mb-2">
                            <i class="fas fa-star text-warning small me-1"></i>
                            <i class="fas fa-star text-warning small me-1"></i>
                            <i class="fas fa-star text-warning small me-1"></i>
                            <i class="fas fa-star text-warning small me-1"></i>
                            <i class="fas fa-star text-warning small me-1"></i>
                            <span class="ms-2 small text-muted fw-bold">4.9</span>
                        </div>
                        
                        <h4 class="fw-bold mb-2 text-dark">${plan.planDetails.name}</h4>
                        <p class="text-muted small mb-3 mobile-truncate">${plan.planDetails.description}</p>
                        
                        <div class="mt-auto">
                            <div class="badge bg-warning bg-opacity-10 text-warning px-3 py-2 rounded-pill small fw-bold mb-3 d-inline-block">
                                ✨ ${plan.planDetails.message || 'Limited Availability'}
                            </div>
                            
                            <div class="row g-2">
                                <div class="col-6">
                                    <a href="tel:9760444241" class="btn btn-outline-dark w-100 rounded-pill py-2 fw-bold" onclick="event.stopPropagation()">
                                        <i class="fas fa-phone-alt"></i> Call
                                    </a>
                                </div>
                                <div class="col-6">
                                  <a href="https://wa.me/919760444241?text=Hi, I am interested in the ${plan.planDetails.name} package." 
                                    class="btn btn-sm w-100 rounded-pill shadow-sm" 
                                    style="background-color: #25d366; color: #ffffff; font-weight: bold; border: none;" 
                                      onclick="event.stopPropagation()">
                                       <i class="fab fa-whatsapp" style="color: #ffffff;"></i> WhatsApp
                                     </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            `;
            grid.appendChild(col);
        });
    } catch (e) {
        console.error("API Fetch Error:", e);
    }
}

function nextImage(planIdx) {
    const slider = document.getElementById(`slider-${planIdx}`);
    const imgs = slider.querySelectorAll('.carousel-img');
    const dots = slider.parentElement.querySelectorAll('.dot');

    if (imgs.length <= 1) return;

    let cur = Array.from(imgs).findIndex(img => img.classList.contains('active'));
    imgs[cur].classList.remove('active');
    dots[cur].classList.remove('active');

    let next = (cur + 1) % imgs.length;
    imgs[next].classList.add('active');
    dots[next].classList.add('active');
}

function toggleAdminPopup() {
    const p = document.getElementById('admin-popup');
    // If the popup is currently hidden or not set to flex, show it
    if (p.style.display === 'flex') {
        p.style.display = 'none';
    } else {
        p.style.display = 'flex';
    }
}

function showFullDetails(idx) {
    const plan = allPlansData[idx];
    const content = document.getElementById('detail-content');
    content.innerHTML = `
        <h3 class="fw-bold">${plan.planDetails.name}</h3>
        <p class="badge bg-warning text-dark fs-6">Price: ₹${plan.planDetails.price}</p>
        <hr>
        <h6 class="fw-bold">Full Itinerary & Details:</h6>
        <p class="full-desc">${plan.planDetails.description}</p>
        <div class="mt-3 p-3 bg-light rounded">
            <small class="text-secondary">Note: ${plan.planDetails.message}</small>
        </div>
    `;
    toggleDetailPopup();
}

function toggleDetailPopup() {
    const p = document.getElementById('detail-popup');
    p.style.display = (p.style.display === 'flex') ? 'none' : 'flex';
}

function closeDetailOnOut(e) {
    if (e.target.classList.contains('detail-overlay')) toggleDetailPopup();
}

async function loginAdmin() {
    const phoneInput = document.getElementById('adminNumber').value;

    if (!phoneInput) {
        alert("Please enter the admin phone number.");
        return;
    }

    try {
        const url = `${API_BASE}/checking?PhoneNumber=${encodeURIComponent(phoneInput)}`;

        const response = await fetch(url);
        if (!response.ok) throw new Error("Server communication error");

        const isAdmin = await response.json();

        if (isAdmin === true) {
            window.location.href = "admin.html";
        } else {
            alert("Unauthorized! This phone number does not have admin access.");
        }
    } catch (error) {
        console.error("Admin verification failed:", error);
        alert("Could not connect to the server. Please ensure the backend is running.");
    }
}

// Trigger the API call on load
window.onload = fetchAllPlans;