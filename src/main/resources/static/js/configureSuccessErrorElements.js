function configureSuccessErrorElements() {
    setSuccessMessageVisibility();
    setErrorMessageVisibility();
}

function setSuccessMessageVisibility() {
    let successMessageContainer = document.getElementById("successMessage");
    let successMessageText = document.getElementById("successMessageText");

    if (successMessageContainer != null && successMessageText.textContent !== "") {
        successMessageContainer.hidden = false;
    }
}

function setErrorMessageVisibility() {
    let errorMessageContainer = document.getElementById("errorMessage");
    let errorMessageText = document.getElementById("errorMessageText");

    if (errorMessageContainer != null && errorMessageText.textContent !== "") {
        errorMessageContainer.hidden = false;
    }
}