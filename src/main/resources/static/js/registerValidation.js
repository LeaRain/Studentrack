function validateRegistration() {
    registerEventListener();
    // Initialize the form fields for the user type at the beginning and not only in case of a click event for the event listeners.
    updateUserTypeFormFields();
}

function registerEventListener() {
    let studentRadioButton = document.getElementById("radioUserStudent");
    let lecturerRadioButton = document.getElementById("radioUserLecturer");
    let passwordInput = document.getElementById("inputPassword");
    let passwordConfirmInput = document.getElementById("inputPasswordConfirm");

    studentRadioButton.addEventListener("click", updateUserTypeFormFields);
    lecturerRadioButton.addEventListener("click", updateUserTypeFormFields);
    passwordInput.addEventListener("keyup", checkPasswordEquality);
    passwordConfirmInput.addEventListener("keyup", checkPasswordEquality);
}

function updateUserTypeFormFields() {
    // Since there are only two states (student and lecturer), only one button is required for checks.
    let studentRadioButton = document.getElementById("radioUserStudent");
    let academicTitleFormGroup = document.getElementById("formGroupAcademicTitle");
    let majorFormGroup = document.getElementById("formGroupMajor");
    let facultyFormGroup = document.getElementById("formGroupFaculty")

    // studentRadioButton checked means lecturerRadioButtonUnchecked.
    let studentChecked = studentRadioButton.checked;
    // Academic title is for lecturers.
    academicTitleFormGroup.hidden = studentChecked;
    // Major is for students.
    majorFormGroup.hidden = !studentChecked;
    // Faculty is for lecturers.
    facultyFormGroup.hidden = studentChecked;
}

function checkPasswordEquality() {
    // Check the two password fields for equality.
    let passwordInput = document.getElementById("inputPassword");
    let passwordConfirmInput = document.getElementById("inputPasswordConfirm");

    if (passwordInput.value !== passwordConfirmInput.value) {
        passwordInput.className = "form-control is-invalid";
        passwordConfirmInput.className = "form-control is-invalid";
    }

    else {
        passwordInput.className = "form-control is-valid";
        passwordConfirmInput.className = "form-control is-valid";
    }
}