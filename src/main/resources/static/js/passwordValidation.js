document.addEventListener("DOMContentLoaded", function() {
    validatePasswords();
})

function validatePasswords() {
    registerPasswordEventListener();
}

function registerPasswordEventListener() {
    let passwordInput = document.getElementById("inputPassword");
    let passwordConfirmInput = document.getElementById("inputPasswordConfirm");
    passwordInput.addEventListener("keyup", checkPasswordEquality);
    passwordConfirmInput.addEventListener("keyup", checkPasswordEquality);
}


// Inspired by myself, two years ago, also available on GitHub: https://github.com/LeaRain/DeepSpaceBooks/blob/master/views/js/fieldcheck.js
function checkPasswordEquality() {
    // Check the two password fields for equality.
    let passwordInput = document.getElementById("inputPassword");
    let passwordConfirmInput = document.getElementById("inputPasswordConfirm");
    let submitButton = document.getElementById("submitButton");

    if (passwordInput.value !== passwordConfirmInput.value) {
        passwordInput.className = "form-control is-invalid";
        passwordConfirmInput.className = "form-control is-invalid";
        submitButton.disabled = true;
    }

    else {
        passwordInput.className = "form-control is-valid";
        passwordConfirmInput.className = "form-control is-valid";
        submitButton.disabled = false;
    }
}
