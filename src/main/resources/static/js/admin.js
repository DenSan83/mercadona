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

// Category delete modal
const categoryIdInput = document.getElementById('categoryIdInput');
const deleteModal = document.getElementById('deleteModal');
deleteModal.addEventListener('show.bs.modal', event => {
    const button = event.relatedTarget;
    categoryIdInput.value = button.getAttribute('data-category-id');
});
deleteModal.addEventListener('hide.bs.modal', () => {
    categoryIdInput.value = '';
});

// Product delete modal
const productIdInput = document.getElementById('productIdInput');
deleteModal.addEventListener('show.bs.modal', event => {
    const button = event.relatedTarget;
    productIdInput.value = button.getAttribute('data-product-id');
});
deleteModal.addEventListener('hide.bs.modal', () => {
    productIdInput.value = '';
});