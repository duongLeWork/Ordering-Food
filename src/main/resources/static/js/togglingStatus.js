// Add event listeners for clicking on the toggle icons
document.querySelectorAll('.fa-toggle-on, .fa-toggle-off').forEach(function(icon) {
    icon.addEventListener('click', function() {
        // Toggle the class between 'fa-toggle-on' and 'fa-toggle-off'
        const productId = icon.getAttribute('data-product-id');
        const isOn = icon.classList.contains('fa-toggle-on');

        // Update the icon's class based on the current state
        if (isOn) {
            icon.classList.remove('fa-toggle-on');
            icon.classList.add('fa-toggle-off');
            icon.classList.remove('text-success');
            icon.classList.add('text-danger');
        } else {
            icon.classList.remove('fa-toggle-off');
            icon.classList.add('fa-toggle-on');
            icon.classList.remove('text-danger');
            icon.classList.add('text-success');
        }

        // Optional: Send the updated status to the backend
        console.log('Product ' + productId + ' status updated to ' + (isOn ? 'off' : 'on'));

        // You can make an AJAX request here to update the product status in the backend if needed
        // Example:
        // fetch('/update-product-status', {
        //     method: 'POST',
        //     body: JSON.stringify({ productId: productId, status: !isOn }),
        //     headers: { 'Content-Type': 'application/json' }
        // });
    });
});