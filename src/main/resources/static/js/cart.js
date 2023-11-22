/**
 * Cart open/close
 */

// Close cart on Esc key
$(window).on('keydown',function(evt) {
	if(evt.code === "Escape") {
		$('.cart-menu').removeClass('is-active');
	}
});

// Close Cart On Nav Trigger Click
$('.navbar-toggler').click(function() {
	$('.navbar-collapse.show .cart-menu').removeClass('is-active');
});

//Open / Close Cart On Click
$('#cart-trigger').on('click',function(evt) {
	evt.preventDefault();

	if($('.cart-menu').hasClass('is-active')) {
		$('.cart-menu').removeClass('is-active');
	} else {
		$('.cart-menu').addClass('is-active');
	}
});

/**
 * Cart functionality
 */

var data = [];

$('.cart__item').each(function( index, element ) {
	var parent = $(element);
	
	//Laptop Model
	var model = parent.children().children().children().first().children()[0].children[0].getAttribute('alt');

	//Laptop Price
	var price = parent.children().children().children()[2].children[0].innerText;
	price = price.substring(1, price.length);

	//Laptop Quantity
	var quantity = parent.children().children().children()[1].children[0].children[1].value;

	console.log({m: model, p: price, q: quantity})

	var laptop = {model: model, price: price, quantity: quantity};
	data.push(laptop);
});


// Add product to cart
const buttonsAddToCart = $('.btnAddToCart');

buttonsAddToCart.each(function() {
	$(this).on('click', addToCart);
});

const csrfToken = document.cookie.replace(/(?:^|.*;\s*)XSRF-TOKEN\s*=\s*([^;]*).*$|^.*$/, '$1');

function addToCart(evt) {
	evt.preventDefault();

	const productId = $(this).parent().children().prev('input').last().val();
	const quantity = $(this).parent().children().last().val();

	// const quantity = $('#quantity' + productId).val();

	const url = `${window.location.origin}/cart/add/${productId}/${quantity}`;
	console.log(url)

	const requestOptions = {
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json',
			'X-XSRF-TOKEN': csrfToken
		},
		method: "POST",
		body: JSON.stringify({
			productId: productId,
			quantity: quantity
		})
	}

	fetch(url, requestOptions)
		.then(response => {
			if(response.ok) {
				console.log('Item added to cart')
			} else {
				console.log('Log in to add product to cart')
			}
		})
		.catch(error => console.log('error', error))
}


// Product Quantity
$('.quantity button').on('click', function () {
	const qtyButton = $(this);

	// const qtyInput = qtyButton.parent().parent().find('input')

	if (qtyButton.hasClass('btn-plus')) {
		increaseQuantity(qtyButton);
	} else {
		decreaseQuantity(qtyButton);
	}
});

/**
 * Increase quantity and update cart stats
 * @param qtyButton
 */
function increaseQuantity(qtyButton) {
	const productId = qtyButton.attr('pid');
	const qtyInput = $('#quantity' + productId);

	if(qtyInput.val() < 0) {
		qtyInput.val(0);
	}

	if(qtyInput.val() > 15) {
		qtyInput.val(15);
	}

	let newQty = parseInt(qtyInput.val()) + 1;

	if(newQty <= 15) {
		qtyInput.val(newQty);

		updateQuantity(productId, newQty);
	}
}

/**
 * Decrease quantity and update cart stats
 * @param qtyButton
 */
function decreaseQuantity(qtyButton) {
	const productId = qtyButton.attr('pid');
	const qtyInput = $('#quantity' + productId);

	if(qtyInput.val() < 0) {
		qtyInput.val(1);
	}

	if(qtyInput.val() > 15) {
		qtyInput.val(15);
	}

	let newQty = parseInt(qtyInput.val()) - 1;

	if (newQty > 0) {
		qtyInput.val(newQty);

		updateQuantity(productId, newQty);
	}
}

function updateQuantity(productId, quantity) {
	const url = `${window.location.origin}/cart/update/${productId}/${quantity}`;
	console.log(url, productId, quantity)

	const requestOptions = {
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json',
			'X-XSRF-TOKEN': csrfToken
		},
		method: "POST",
		body: JSON.stringify({
			productId: productId,
			quantity: quantity
		})
	}

	fetch(url, requestOptions)
		.then(promise => {
			if(promise.ok) {
				console.log('Product quantity updated')
			} else {
				console.log('Log in to use the cart')
			}

			return promise.json();
		})
		.then(newSubtotal => {
			updateSubtotal(newSubtotal, productId);

			updateTotal();
		})
		.catch(error => console.log('error', error))
}

function updateSubtotal(newProductSubtotal, productId) {
	$('#subtotal' + productId).text(newProductSubtotal);
}

// Update cart price, subtotal, shipping and total
updateTotal();

function updateTotal() {
	// Price
	$(".product-price").each(function() {
		$(this).text(`£${parseFloat($(this).text().replace('£',''))}`);
	})

	// Subtotal
	let subtotalPrice = 0;

	$(".product-subtotal").each(function() {
		subtotalPrice += parseFloat($(this).text().replace('£',''));

		$(this).text(`£${parseFloat($(this).text().replace('£',''))}`);
	})

	let cartSubtotal = $('#cart-subtotal');
	cartSubtotal.text(`£${parseFloat(subtotalPrice.toFixed(2))}`);

	// Shipping
	const shippingPrice= cartSubtotal.text() === '£0' ? 0 : 15;

	$('.cart-shipping').text(`£${shippingPrice}`);

	// Total
	const totalPrice = subtotalPrice + shippingPrice;
	$('#cart-total').text(`£${parseFloat(totalPrice.toFixed(2))}`);
}


// Remove product from cart
$('.btnRemoveFromCart').on('click', function() {
	const removeBtn = $(this);

	removeFromCart(removeBtn);
});

function removeFromCart(removeBtn) {
	const productId = removeBtn.attr('pid');

	const url = `${window.location.origin}/cart/remove/${productId}`;

	const requestOptions = {
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json',
			'X-XSRF-TOKEN': csrfToken
		},
		method: "POST"
	}

	fetch(url, requestOptions)
		.then(promise => {
			if(promise.ok) {
				const rowNumber = removeBtn.attr('rowNumber');

				console.log(rowNumber);

				removeProduct(rowNumber);

				updateTotal()

				console.log('Product has been deleted')
			} else {
				console.log('Log in to use the cart')
			}
		})
		.catch(error => console.log('error', error))
}

function removeProduct(rowNumber) {
	const rowId = "row" + rowNumber;

	$('#' + rowId).remove();
}

// Submit cart
$("#formCart").submit(function(evt) {
	evt.preventDefault();

	// $.post("localhost/laptopia/index.php", { data: data});
	// return false;

	$.ajax({
		method: "POST",
		url: "localhost/laptopia/index.php",
		data: data,
	
		success: function() {
			alert("Success");
		},
	
		error: function() {
			// alert("Oops");
			console.log(data);
		},
	});
});