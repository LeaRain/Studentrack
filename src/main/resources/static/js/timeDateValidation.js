document.addEventListener("DOMContentLoaded", function(){
    valiDATE();
});

function valiDATE() {
    registerValidateEventListener();
    checkForCorrectDates();
}

function registerValidateEventListener() {
    let startInput = document.getElementById("inputStart");
    let endInput = document.getElementById("inputEnd");
    startInput.addEventListener("change", checkForCorrectDates)
    endInput.addEventListener("change", checkForCorrectDates)
}

function checkForCorrectDates() {
    let startInput = document.getElementById("inputStart");
    let endInput = document.getElementById("inputEnd");
    let submitButton = document.getElementById("submitButton");

    let startDate = new Date(startInput.value);
    let endDate = new Date(endInput.value);

    submitButton.disabled = startDate > endDate;
}