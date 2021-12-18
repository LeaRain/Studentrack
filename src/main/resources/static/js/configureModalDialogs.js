function configureModalDialogs () {
    registerEventListener();
}

function registerEventListener() {
    let editModuleButton = document.getElementById("buttonEditModule");
    let closeModuleButton = document.getElementById("buttonCloseEditModuleModal");
    editModuleButton.addEventListener("click", showModalModuleDialog);
    closeModuleButton.addEventListener("click", hideModalModuleDialog);
}

function showModalModuleDialog() {
    let modal = document.getElementById("modalEditModule");
    modal.style.display = "block";

}

function hideModalModuleDialog() {
    let modal = document.getElementById("modalEditModule");
    modal.style.display = "none"
}
