document.addEventListener("DOMContentLoaded", function(){
    validateStudentModule();
});

function validateStudentModule() {
    registerEventListener();
    showSelectedModule();
}

function registerEventListener() {
    let moduleSelect = document.getElementById("selectModule");
    moduleSelect.addEventListener("click", showSelectedModule);
}

function showSelectedModule() {
    let moduleLegends = document.getElementsByClassName("module-legend");
    let moduleTable = document.getElementsByClassName("module-table");
    let moduleSelection = document.getElementById("selectModule");
    let selectedModule = moduleSelection.value;

    for (let i = 0; i < moduleLegends.length; i++) {
        let legendElement = moduleLegends[i];
        let tableElement = moduleTable[i];
        let legendValue = legendElement.getAttribute("value");

        if (legendValue !== selectedModule) {
            legendElement.hidden = true;
            tableElement.hidden = true;
        }

        else {
            legendElement.hidden = false;
            tableElement.hidden = false;
        }
    }
}