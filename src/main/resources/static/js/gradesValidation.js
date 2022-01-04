document.addEventListener("DOMContentLoaded", function(){
    validateGrades();
});

function validateGrades() {
    registerGradeEventListener();
    checkForCorrectGrades();
}

function registerGradeEventListener() {
    let gradeInputs = document.getElementsByClassName("gradeInput");

    for (let gradeInput of gradeInputs) {
        gradeInput.addEventListener("keyup", checkForCorrectGrades)
    }
}

function checkForCorrectGrades() {
    let correctGrades = [1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0, 3.3, 3.7, 4.0, 5.0];
    let gradeInputs = document.getElementsByClassName("gradeInput");
    let submitButton = document.getElementById("submitButtonGrades");
    let enabledButton = true;

    for (let gradeInput of gradeInputs) {
        let gradeValue = Number(gradeInput.value);

        if (!(correctGrades.includes(gradeValue))) {
            enabledButton = false;
            break;
        }
    }
    submitButton.disabled = !enabledButton;
}