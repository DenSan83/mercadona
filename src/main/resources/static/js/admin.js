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
if (deleteModal !== null) {
    deleteModal.addEventListener('show.bs.modal', event => {
        const button = event.relatedTarget;
        categoryIdInput.value = button.getAttribute('data-category-id');
    });
    deleteModal.addEventListener('hide.bs.modal', () => {
        categoryIdInput.value = '';
    });
}

// Product delete modal
const productIdInput = document.getElementById('productIdInput');
if (deleteModal !== null) {
    deleteModal.addEventListener('show.bs.modal', event => {
        const button = event.relatedTarget;
        productIdInput.value = button.getAttribute('data-product-id');
    });
    deleteModal.addEventListener('hide.bs.modal', () => {
        productIdInput.value = '';
    });
}

// Promotion date select
document.addEventListener('DOMContentLoaded', () => {
    const startDateInput = document.getElementById('start-date');
    const endDateInput = document.getElementById('end-date');

    if (startDateInput && endDateInput) {
        startDateInput.min = new Date().toISOString().split('T')[0];

        // startDate must be at least today
        startDateInput.addEventListener('input', () => {
            const startDate = new Date(startDateInput.value);
            const nextDay = new Date(startDate.getTime() + 24 * 60 * 60 * 1000);
            endDateInput.min = nextDay.toISOString().split('T')[0];

            if (endDateInput.value < startDateInput.value) {
                const nextDay = new Date(startDate.getTime() + 24 * 60 * 60 * 1000);
                endDateInput.value = nextDay.toISOString().split('T')[0];
            }
        });

        // endDate must be at least day after startDay
        endDateInput.addEventListener('input', () => {
            if (endDateInput.value < startDateInput.value) {
                startDateInput.value = endDateInput.value;
            }
        });
    }
});
