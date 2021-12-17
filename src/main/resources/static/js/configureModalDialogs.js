function configureModalDialogs () {
    registerEventListener();
}

function registerEventListener() {
    let editCourseButton = document.getElementById("buttonEditCourse");
    let closeCourseButton = document.getElementById("buttonCloseEditCourseModal");
    let editModuleButton = document.getElementById("buttonEditModule");
    let closeModuleButton = document.getElementById("buttonCloseEditModuleModal");
    editCourseButton.addEventListener("click", showModalCourseDialog);
    closeCourseButton.addEventListener("click", hideModalCourseDialog);
    editModuleButton.addEventListener("click", showModalModuleDialog);
    closeModuleButton.addEventListener("click", hideModalModuleDialog);
}

function showModalCourseDialog() {
    let modal = document.getElementById("modalEditCourse");
    modal.style.display = "block";

}

function hideModalCourseDialog() {
    let modal = document.getElementById("modalEditCourse");
    modal.style.display = "none"
}

function showModalModuleDialog() {
    let modal = document.getElementById("modalEditModule");
    modal.style.display = "block";

}

function hideModalModuleDialog() {
    let modal = document.getElementById("modalEditModule");
    modal.style.display = "none"
}
