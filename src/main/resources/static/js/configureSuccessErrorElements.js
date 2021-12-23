document.addEventListener("DOMContentLoaded", function(){
    configureSuccessErrorElements();
});

function configureSuccessErrorElements() {
    setMessageVisibility();
}

function setMessageVisibility() {
    let messageContainer = document.getElementById("statusMessage");
    let errorMessageText = document.getElementById("errorMessageText");
    let successMessageText = document.getElementById("successMessageText");

    // Message Container must exist
    if (messageContainer != null && (
        // Error message must exist and is not allowed to be empty OR
        (errorMessageText != null && errorMessageText.textContent !== "")  ||
        // Success message must exist and is not allowed to be empty
        (successMessageText != null && successMessageText.textContent !== "")
    )
    ) {
        messageContainer.hidden = false;

        // Hide success container for empty success message
        if (successMessageText.textContent === "") {
            successMessageText.hidden = true;
        }

        // Hide error container for empty error message
        if (errorMessageText.textContent === "") {
            errorMessageText.hidden = true;
        }

    }
}