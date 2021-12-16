function configureModalDialog () {
    registerEventListener();
}

function registerEventListener() {
    let editCourseButton = document.getElementById("buttonEditCourse");
    let closeButton = document.getElementById("buttonCloseEditCourseModal");
    editCourseButton.addEventListener("click", showModalDialog)
    closeButton.addEventListener("click", hideModalDialog)
}

function showModalDialog() {
    let modal = document.getElementById("modalEditCourse");
    modal.style.display = "block";

}

function hideModalDialog() {
    let modal = document.getElementById("modalEditCourse");
    modal.style.display = "none"
}