// Se quiser um efeito mais interativo, como abrir com clique em vez de hover, você pode usar esse script:

document.querySelectorAll('.dropdown > a').forEach(item => {
    item.addEventListener('click', event => {
        let dropdownContent = item.nextElementSibling;
        if (dropdownContent.style.display === "block") {
            dropdownContent.style.display = "none";
        } else {
            dropdownContent.style.display = "block";
        }
    });
});
