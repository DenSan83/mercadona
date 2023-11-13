// Toast interactions
const toastElList = document.querySelectorAll('.toast')
if (toastElList.length > 0) {
    setTimeout(() => {
        toastElList.forEach(toast => {
            const bootstrapToast = new bootstrap.Toast(toast);
            bootstrapToast.hide();
        });
    }, 5000);
}

// Delete modal
const categoryIdInput = document.getElementById('categoryIdInput');
const deleteModal = document.getElementById('deleteModal');
deleteModal.addEventListener('show.bs.modal', event => {
    const button = event.relatedTarget; console.log(button)
    console.log(button.getAttribute('data-category-id'))
    categoryIdInput.value = button.getAttribute('data-category-id');
    console.log(categoryIdInput)
});
deleteModal.addEventListener('hide.bs.modal', () => {
    categoryIdInput.value = '';
});