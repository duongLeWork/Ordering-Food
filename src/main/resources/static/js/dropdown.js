document.getElementById('filterDropdown').addEventListener('click', function () {
    var dropdownButton = document.getElementById('filterDropdown');
    var expanded = dropdownButton.getAttribute('aria-expanded') === 'true';
    dropdownButton.setAttribute('aria-expanded', !expanded);  // Toggle the value of aria-expanded
});
