document.addEventListener("DOMContentLoaded", function(){
    validateLecturerModule();
});

function validateLecturerModule() {
    registerLecturerModuleEventListener();
    checkForCorrectNumericValues();
}

function registerLecturerModuleEventListener() {
    let ectsInput = document.getElementById("inputEcts");
    let creditHoursInput = document.getElementById("inputCreditHours");
    ectsInput.addEventListener("keyup", checkForCorrectNumericValues);
    creditHoursInput.addEventListener("keyup", checkForCorrectNumericValues);
}

function checkForCorrectNumericValues() {
    let ectsInput = document.getElementById("inputEcts");
    let creditHoursInput = document.getElementById("inputCreditHours");
    let submitButton = document.getElementById("submitButton");

    submitButton.disabled = !!(Number(ectsInput.value) < 1 || Number(creditHoursInput.value < 1));
}