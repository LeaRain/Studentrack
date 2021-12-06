function validateRegistration() {
    registerRadioButtonEventListener();
    // Initialize the form fields for the user type at the beginning and not only in case of a click event for the event listeners.
    updateUserTypeFormFields();
}

function registerRadioButtonEventListener() {
    let studentRadioButton = document.getElementById("radioUserStudent");
    let lecturerRadioButton = document.getElementById("radioUserLecturer");

    studentRadioButton.addEventListener("click", updateUserTypeFormFields)
    lecturerRadioButton.addEventListener("click", updateUserTypeFormFields)
}

function updateUserTypeFormFields() {
    // Since there are only two states (student and lecturer), only one button is required for checks.
    let studentRadioButton = document.getElementById("radioUserStudent");
    let academicTitleFormGroup = document.getElementById("formGroupAcademicTitle");
    let majorFormGroup = document.getElementById("formGroupMajor");

    // studentRadioButton checked means lecturerRadioButtonUnchecked.
    let studentChecked = studentRadioButton.checked;
    // Academic title is for lecturers.
    academicTitleFormGroup.hidden = studentChecked;
    // Major is for students.
    majorFormGroup.hidden = !studentChecked;
}