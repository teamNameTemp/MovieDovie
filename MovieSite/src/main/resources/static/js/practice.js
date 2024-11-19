document.addEventListener("DOMContentLoaded", function() {
    const button = document.getElementById("toggleButton");
    const div = document.getElementById("targetDiv");

    button.addEventListener('click', function() {
        if (div.style.display === 'none') {
            div.style.display = 'block';
        } else {
            div.style.display = 'none';
        }
    });
});
