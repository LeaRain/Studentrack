document.addEventListener("DOMContentLoaded", function(){
    let type = checkForType();
    configureModalDialogs(type);
});

function checkForType() {
    let moduleButton = document.getElementById("buttonEditModule");
    if (moduleButton === null) {
        return "Withdraw";
    }

    return "Edit";
}

function configureModalDialogs (type) {
    registerModalDialogEventListener(type);
}

function registerModalDialogEventListener(type) {
    let editModuleButton = document.getElementById("button" + type +"Module");
    let closeModuleButton = document.getElementById("buttonClose" + type + "ModuleModal");
    editModuleButton.addEventListener("click", function() {
        showModalModuleDialog(type);
    });
    closeModuleButton.addEventListener("click", function() {
        hideModalModuleDialog(type)
    });
}

function showModalModuleDialog(type) {
    let modal = document.getElementById("modal" + type + "Module");
    modal.style.display = "block";

}

function hideModalModuleDialog(type) {
    let modal = document.getElementById("modal" + type + "Module");
    modal.style.display = "none"
}
