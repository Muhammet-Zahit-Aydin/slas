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
                    // Take token and save it
                    const token = await response.text() ;
                    localStorage.setItem('token', token) ;
                    localStorage.setItem('userEmail', email) ;

                    // Direct to panel
                    window.location.href = 'dashboard.html' ;
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

// Get books from backend and list
async function loadBooks() {

    const searchInput = document.getElementById('searchInput') ;
    const query = searchInput ? searchInput.value : "" ;
    const token = localStorage.getItem('token') ;
    const bookListDiv = document.getElementById('bookList') ;

    // If there's no token don't process
    if(!token) return ;

    try {

        bookListDiv.innerHTML = '<p style="text-align:center;">Loading...</p>' ;

        const response = await fetch(`${API_BASE_URL}/books/search?query=${query}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}` // JWT Token'ı ekle
            }
        }) ;

        // If session time ran out (403 Forbidden)
        if (response.status === 403) {
            alert("Your session time ran out please login again") ;
            logout() ;
            return ;
        }

        const books = await response.json() ;
        bookListDiv.innerHTML = ""; // Clear list

        if (books.length === 0) {
            bookListDiv.innerHTML = "<p style='text-align:center; width:100%;'>Searched book couldn't found</p>" ;
            return ;
        }

        // Put books in loop and make html
        books.forEach(book => {

            // Check if book is available
            const isAvailable = book.status === 'AVAILABLE' && book.stock > 0 ;
            
            const statusClass = isAvailable ? 'in-stock' : 'out-of-stock' ;
            const statusText = isAvailable ? `Current (Stock: ${book.stock})` : 'Out of stock' ;
            const btnDisabled = !isAvailable ? 'disabled' : '' ;
            const btnText = isAvailable ? 'Borrow' : 'Out of stock' ;

            const cardHTML = `
                <div class="book-card">
                    <div>
                        <div class="book-title">${book.title}</div>
                        <div class="book-author">${book.author}</div>
                        <div class="stock-badge ${statusClass}">${statusText}</div>
                        <small style="color:#999;">ISBN: ${book.isbn}</small>
                    </div>
                    <button class="borrow-btn" ${btnDisabled} onclick="borrowBook(${book.id})">
                        ${btnText}
                    </button>
                </div>
            `;
            bookListDiv.innerHTML += cardHTML ;

        }) ;

    } catch (error) {

        console.error("Hata:", error) ;
        bookListDiv.innerHTML = "<p style='color:red; text-align:center;'>An error occured while loading the books</p>" ;

    }
}

// Borrowing process
async function borrowBook(bookId) {

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
        }) ;

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