const API_BASE_URL = "/api" ;

document.addEventListener('DOMContentLoaded', () => {
    
    //* LOGIN PROCESSES

    const loginForm = document.getElementById('loginForm') ;
    const errorMessage = document.getElementById('errorMessage') ;

    if(loginForm) {

        // If there is a tokane direct to dashboard

        if(localStorage.getItem('token')) {

            window.location.href = 'dashboard.html' ;
            
        }

        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault(); // Prevent page reloading

            const email = document.getElementById('email').value ;
            const password = document.getElementById('password').value ;
            const loginBtn = document.getElementById('loginBtn') ;

            // Lock Button
            loginBtn.disabled = true ;
            loginBtn.innerText = "Logging in..." ;
            errorMessage.innerText = "" ;

            try {

                const response = await fetch(`${API_BASE_URL}/auth/login`, {

                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ email, password })

                }) ;

                if (response.ok) {
                    const data = await response.json();

                    localStorage.setItem('token', data.token);
                    
                    localStorage.setItem('userRole', data.role); 

                    window.location.href = 'dashboard.html';
                } else {

                    errorMessage.innerText = "Login Failed. E-mail or password is incorrect" ;

                }

            } catch (error) {

                console.error('Error:', error) ;
                errorMessage.innerText = "Couldn't connect to the server" ;

            } finally {

                loginBtn.disabled = false ;
                loginBtn.innerText = "Login" ;

            }

        }) ;

    }

    //* DASHBOARD PROCESSES

    if (window.location.pathname.includes('dashboard.html')) {
        
        checkAuth(); // Make security control
        
        // Print username
        const userEmailSpan = document.getElementById('userInfo') ;
        if(userEmailSpan) {

            userEmailSpan.innerText = localStorage.getItem('userEmail') ;

        }

        // Load books
        loadBooks(); 

        // Search if pressed enter
        const searchInput = document.getElementById('searchInput') ;
        if(searchInput) {

            searchInput.addEventListener('keypress', function (e) {
                if (e.key === 'Enter') {
                    searchBooks() ;
                }
            }) ;

        }

    }

}) ;

//* HELPER FUNCTIONS

// Security Control: If there's no token direct to login page
function checkAuth() {

    if(!localStorage.getItem('token')) {

        window.location.href = 'login.html' ;

    }
}

// Logout

function logout() {

    if(confirm("Are you sure you want to log out?")) {

        localStorage.removeItem('token') ;
        localStorage.removeItem('userEmail') ;
        window.location.href = 'login.html' ;

    }
}

// Search function
function searchBooks() {

    loadBooks() ;

}

// Global variable
let allBooksData = []; 
let currentCategory = 'all';
let currentSubCategory = 'all';

document.addEventListener('DOMContentLoaded', () => {
    if (document.getElementById('bookList')) {
        loadBooks();
    }
});

// Loads books from database
async function loadBooks() {
    const token = localStorage.getItem('token');

    if (!token) {
        window.location.href = 'login.html';
        return;
    }

    try {
        const response = await fetch('/api/books', {
            headers: { 
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            allBooksData = await response.json();
            renderBooks(allBooksData);
        } else {
            console.error("Authorization or Server Error:", response.status);
        }
    } catch (error) {
        console.error("Connection Error:", error);
    }
}

function renderBooks(booksToRender) {
    const bookList = document.getElementById('bookList');
    bookList.innerHTML = '';

    if (booksToRender.length === 0) {
        bookList.innerHTML = '<p style="text-align:center; color:#888;">Filtered book couldnt found</p>';
        return;
    }

    booksToRender.forEach(book => {

        const categoryBadge = book.category ? `<span class="badge">${book.category}</span>` : '';

        const bookCard = `
            <div class="book-card">
                <div class="card-header">
                    <h3>${book.title}</h3>
                    ${categoryBadge}
                </div>
                <p><strong>Author:</strong> ${book.author}</p>
                <p><strong>Stock:</strong> ${book.stock}</p>
                
                ${book.stock > 0 
                    ? `<button onclick="borrowBook(${book.id})" class="nav-btn" style="background:#28a745; width:100%; margin-top:10px;">Borrow</button>` 
                    : `<button disabled style="background:#ccc; width:100%; margin-top:10px; border:none; color:white; padding:10px;">Out of Stock</button>`
                }
            </div>
        `;
        bookList.innerHTML += bookCard;
    });
}

// Category Map
const subCategoriesMap = {
    'Fantasy': ['Epic', 'Dark', 'Mythology', 'Magic'],
    'Fiction': ['Tension', 'Classic', 'Psychology', 'Adventure'],
    'Sci-Fi': ['Space', 'Dystopia', 'Artifical Intelligence'],
    'History': ['Ottoman', 'European', 'War'],
    'Children': ['Fairytale', 'Educational'],
    'Philosophy': ['Ancient', 'Modern']
};

// Main Category Selection
function setCategory(category, btnElement) {
    currentCategory = category;
    currentSubCategory = 'all';

    // Set button colors
    document.querySelectorAll('.cat-btn').forEach(btn => btn.classList.remove('active'));
    btnElement.classList.add('active');

    // Administrate sub menu
    updateSubMenu(category);

    // Filter
    filterBooks();
}

// Sub Category Creation
function updateSubMenu(mainCategory) {
    const subNav = document.getElementById('subCategoryNav');
    subNav.innerHTML = '';

    // If select all hide sub categories
    if (mainCategory === 'all' || !subCategoriesMap[mainCategory]) {
        subNav.style.display = 'none';
        return;
    }

    // Show sub category
    subNav.style.display = 'flex';

    // Add all button to see all sub categories
    subNav.innerHTML += `<button class="sub-btn active" onclick="setSubCategory('all', this)">All</button>`;

    // Find related sub categories
    subCategoriesMap[mainCategory].forEach(sub => {
        subNav.innerHTML += `<button class="sub-btn" onclick="setSubCategory('${sub}', this)">${sub}</button>`;
    });
}

// Sub Category Selection
function setSubCategory(subCat, btnElement) {
    currentSubCategory = subCat;

    // Set sub buttons color
    document.querySelectorAll('.sub-btn').forEach(btn => btn.classList.remove('active'));
    btnElement.classList.add('active');

    filterBooks();
}

// Filter
function filterBooks() {
    const searchText = document.getElementById('searchInput').value.toLowerCase();
    
    const filteredList = allBooksData.filter(book => {
        const title = book.title ? book.title.toLowerCase() : "";
        
        // Null safety
        const category = book.category ? book.category : "";
        const subCategory = book.subCategory ? book.subCategory : "";

        // Search control
        const matchesSearch = title.includes(searchText);

        // Main category control
        const matchesMainCat = (currentCategory === 'all') || (category === currentCategory);

        // Sub category control
        const matchesSubCat = (currentSubCategory === 'all') || (subCategory === currentSubCategory);

        return matchesSearch && matchesMainCat && matchesSubCat;
    });

    renderBooks(filteredList);
}

// Borrowing process
async function borrowBook(bookId) {

    //! FOR DEBUG
    console.log("ÖDÜNÇ ALINMAK İSTENEN KİTAP ID:", bookId);

    if(!confirm("Are you sure you want to borrow this book?")) return ;

    const token = localStorage.getItem('token') ;

    try {
        
        const response = await fetch(`${API_BASE_URL}/borrow/book`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ bookId: bookId }) 
        });

        if (response.ok) {

            alert("Book successfully borrowed") ;
            loadBooks() ; // Reload list

        } else {

            const errorText = await response.text();
            // Try to catch error message from backend
            try {
                const errorJson = JSON.parse(errorText) ;
                alert("Hata: " + (errorJson.message || errorJson.error)) ;
            } catch (e) {
                alert("Hata: " + errorText) ;
            }

        }

    } catch (error) {

        console.error("Borrowing Error:", error) ;
        alert("An error occured in process") ;

    }

}

window.returnBook = async function(bookId) {

    if(!confirm("Are you sure you want to return this book?")) return ;

    const token = localStorage.getItem('token') ;

    try {

        console.log("İade İsteği Gönderiliyor. Kitap ID:", bookId) ; //! FOR DEBUG

        const response = await fetch(`${API_BASE_URL}/borrow/return`, {

            method: 'PUT',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ bookId: bookId })

        }) ;

        if(response.ok) {

            alert("Return process successfull");
            // Reload the page
            if(typeof loadHistory === 'function') {
                loadHistory() ; 
            } else {
                window.location.reload() ;
            }

        } else {

            const errorText = await response.text() ;
            console.error("Error Detail:", errorText) ;
            alert("Error Occured: " + errorText) ;

        }

    } catch (error) {

        console.error("Connection Error:", error) ;
        alert("Could not connect to server") ;

    }

}

document.addEventListener('DOMContentLoaded', () => {
    checkAdminAccess();
});

function checkAdminAccess() {

    let role = localStorage.getItem('userRole');
    
    console.log("Ham Rol Verisi:", role); //! DEBUG

    if (!role) return;

    role = role.toUpperCase(); 

    const menuItems = document.querySelector('.menu-items');
    
    if (!menuItems) {
        console.error("ERROR: there's no div having '.menu-items' class check dashboard.html");
        return;
    }

    if (role === 'ADMIN') {
        console.log("Adding Button...");

        if (document.querySelector('.admin-badge')) return;

        const adminLink = document.createElement('a');
        adminLink.href = 'admin.html';
        adminLink.className = 'nav-btn admin-badge';
        adminLink.innerHTML = '<i class="fas fa-user-shield"></i> Admin Panel';
        
        adminLink.style.backgroundColor = '#dc3545';
        adminLink.style.color = 'white';
        adminLink.style.border = '1px solid #dc3545';
        adminLink.style.marginRight = '10px';

        menuItems.insertBefore(adminLink, menuItems.firstChild);
    } else {
        console.log("User is not an admin");
    }
}