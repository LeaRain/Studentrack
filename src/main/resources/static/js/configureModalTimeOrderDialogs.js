document.addEventListener("DOMContentLoaded", function(){
    let type = checkForType();
    configureModalDialogs(type);
});

function checkForType() {
    let moduleButton = document.getElementById("buttonEditTimeOrder");
    if (moduleButton === null) {
        return "Withdraw";
    }

    return "Edit";
}

function configureModalDialogs (type) {
    registerTimeOrderEventListener(type);
}

function registerTimeOrderEventListener(type) {
    let editTimeOrderButton = document.getElementById("button" + type +"TimeOrder");
    let closeTimeOrderButton = document.getElementById("buttonClose" + type + "TimeOrderModal");
    editTimeOrderButton.addEventListener("click", function() {
        showModalTimeOrderDialog(type);
    });
    closeTimeOrderButton.addEventListener("click", function() {
        hideModalTimeOrderDialog(type)
    });
}

function showModalTimeOrderDialog(type) {
    let modal = document.getElementById("modal" + type + "TimeOrder");
    modal.style.display = "block";

}

function hideModalTimeOrderDialog(type) {
    let modal = document.getElementById("modal" + type + "TimeOrder");
    modal.style.display = "none"
}
