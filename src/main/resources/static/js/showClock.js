document.addEventListener("DOMContentLoaded", function(){
    showClock();
});

function showClock() {
    setInterval(createClock, 1000);
}

function createClock() {
    let clockParagraph = document.getElementById("paragraphClock");
    clockParagraph.textContent = getCurrentTime();
}

function getCurrentTime() {
    let currentDate = new Date();
    return currentDate.toLocaleTimeString();
}