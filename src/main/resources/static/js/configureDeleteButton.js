function configureDeleteButton() {
    setDeleteButtonEnabling();
    registerCheckboxEventListener();
}

function registerCheckboxEventListener() {
    let deleteCheckbox = document.getElementById("checkboxDelete");
    deleteCheckbox.addEventListener("click", setDeleteButtonEnabling)

}

function setDeleteButtonEnabling() {
    let deleteCheckbox = document.getElementById("checkboxDelete");
    let deleteButton = document.getElementById("submitButtonDelete");

    console.log(deleteCheckbox.checked);

    deleteButton.disabled = deleteCheckbox.checked === false;

}