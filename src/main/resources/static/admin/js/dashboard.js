function trackOrder() {
    const orderId = document.getElementById('orderIdInput').value;

    if (orderId) {
      // Send AJAX request to check if the order exists
      fetch(`/admin/check-order/${orderId}`)
        .then(response => {
          if (response.ok) {
            // If the order exists, redirect to order-details page
            window.location.href = `/admin/orders/${orderId}`;
          } else if (response.status === 404) {
            // If the order doesn't exist, show "No order found" message
            document.getElementById('noOrderFound').style.display = 'block';
          }
        })
        .catch(error => {
          console.error('Error:', error);
        });
    } else {
      // If input is empty, prompt the user to enter a valid order ID
      alert("Please enter a valid order ID.");
    }
}

// Get sales data from the backend
const salesData = /*[[${salesData}]]*/ [];
const months = salesData.map(data => data.month);
const salesAmount = salesData.map(data => data.saleAmount);
const revenue = salesData.map(data => data.revenue);

// Create the chart using Chart.js
const ctx = document.getElementById('salesChart').getContext('2d');
const salesChart = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: months,
        datasets: [{
            label: 'Sale',
            data: salesAmount,
            backgroundColor: 'rgba(54, 162, 235, 0.5)', // Blue for Sales
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1
        },
        {
            label: 'Revenue',
            data: revenue,
            backgroundColor: 'rgba(255, 159, 64, 0.5)', // Orange for Revenue
            borderColor: 'rgba(255, 159, 64, 1)',
            borderWidth: 1
        }]
    },
    options: {
        responsive: true,
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});