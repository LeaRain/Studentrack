function validateModule() {
    registerEventListener();
    checkForCorrectNumericValues();
}

function registerEventListener() {
    let ectsInput = document.getElementById("inputEcts");
    let creditHoursInput = document.getElementById("inputCreditHours");
    ectsInput.addEventListener("keyup", checkForCorrectNumericValues);
    creditHoursInput.addEventListener("keyup", checkForCorrectNumericValues);
}

function checkForCorrectNumericValues() {
    let ectsInput = document.getElementById("inputEcts");
    let creditHoursInput = document.getElementById("inputCreditHours");
    let submitButton = document.getElementById("submitButtonModule");

    submitButton.disabled = !!(Number(ectsInput.value) < 1 || Number(creditHoursInput.value < 1));
}