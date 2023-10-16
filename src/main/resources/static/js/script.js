document.addEventListener('DOMContentLoaded', function () {
    // Ajax call method
    let getCategoryContent = (e, elm) => {
        e.preventDefault();
        let categoryId = elm.getAttribute('data-category');
        console.log(categoryId);
        let apiUrl = '/ajax-products';
        if (categoryId !== '0') {
            apiUrl += '?category=' + categoryId;
        }

        // Ajax request
        fetch(apiUrl)
            .then(response => {
                if (response.status !== 200) {
                    console.error('Error: ' + response.status);
                    return;
                }

                response.text().then(data => {
                    // Insert data in page
                    let productContainer = document.getElementById('product-container');
                    productContainer.innerHTML = data;
                });
            })
            .catch(error => {
                console.error('Fetch Error: ', error);
            });
    }

    // Ajax call on Categories from aside
    let categoryListAside = document.getElementById('categoriesAside');
    let categoriesAside = categoryListAside.getElementsByTagName('li');
    for (let i = 0; i < categoriesAside.length; i++) {
        categoriesAside[i].addEventListener('click', e => getCategoryContent(e, categoriesAside[i]));
    }

    // Ajax call on categories from responsive menu
    let categoryListResponsive = document.getElementById('categoriesResponsive');
    if (categoryListResponsive) { // Asegúrate de que el elemento exista
        let categoriesResponsive = categoryListResponsive.getElementsByTagName('li');
        for (let i = 0; i < categoriesResponsive.length; i++) {
            categoriesResponsive[i].addEventListener('click', e => getCategoryContent(e, categoriesResponsive[i]));
        }
    }
});
