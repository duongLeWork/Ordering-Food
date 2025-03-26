function updateRowsPerPage() {
    const rowsPerPage = document.getElementById('rowsPerPage').value;
    const currentPage = 1; // Reset to the first page
    window.location.href = `${paginationPath}?page=${currentPage}&rowsPerPage=${rowsPerPage}`; // Redirect to the first page with the new rowsPerPage value
}

// Navigate to the specific page entered by the user
function goToPage() {
    const page = document.getElementById('pageInput').value;
    const rowsPerPage = document.getElementById('rowsPerPage').value;

    if (page >= 1 && page <= totalPages) {
        window.location.href = `${paginationPath}?page=${page}&rowsPerPage=${rowsPerPage}`;
    } else {
              alert(`Please enter a page number between 1 and ${totalPages}`);
    }
}


// Function to go to the previous page
function goLeftPage() {
    const currentPage = parseInt(document.getElementById('pageInput').value);
    const rowsPerPage = document.getElementById('rowsPerPage').value;

    // Ensure that the previous page is not less than 1
    if (currentPage > 1) {
        const newPage = currentPage - 1;
        window.location.href = `${paginationPath}?page=${newPage}&rowsPerPage=${rowsPerPage}`;
    }
}

// Function to go to the next page
function goRightPage() {
    const currentPage = parseInt(document.getElementById('pageInput').value);
    const rowsPerPage = document.getElementById('rowsPerPage').value;

    // Ensure that the next page does not exceed total pages
    if (currentPage < totalPages) {
        const newPage = currentPage + 1;
        window.location.href = `${paginationPath}?page=${newPage}&rowsPerPage=${rowsPerPage}`;
    }
}

function updatePaginationButtons() {
    const currentPage = parseInt(document.getElementById('pageInput').value);

    // Hide "Previous" button if on the first page
    if (currentPage === 1) {
        document.getElementById('prevPageBtn').style.display = 'none';
    } else {
        document.getElementById('prevPageBtn').style.display = 'inline';
    }

    // Hide "Next" button if on the last page
    if (currentPage === totalPages) {
        document.getElementById('nextPageBtn').style.display = 'none';
    } else {
        document.getElementById('nextPageBtn').style.display = 'inline';
    }
}
window.onload = updatePaginationButtons;