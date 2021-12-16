function validateCourse() {
    registerEventListener();
    checkForCorrectDates();
}

function registerEventListener() {
    let startDateInput = document.getElementById("inputStartDate");
    let endDateInput = document.getElementById("inputEndDate");
    startDateInput.addEventListener("change", checkForCorrectDates)
    endDateInput.addEventListener("change", checkForCorrectDates)
}

function checkForCorrectDates() {
    let startDateInput = document.getElementById("inputStartDate");
    let endDateInput = document.getElementById("inputEndDate");
    let submitButton = document.getElementById("submitButtonCourse");

    let startDate = new Date(startDateInput.value);
    let endDate = new Date(endDateInput.value);

    submitButton.disabled = startDate > endDate;

}