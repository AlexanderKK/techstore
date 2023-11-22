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


/**
 * Add product to cart
 */
const buttonsAddToCart = $('.btnAddToCart');

buttonsAddToCart.each(function() {
	$(this).on('click', addToCart);
});

const csrfToken = document.cookie.replace(/(?:^|.*;\s*)XSRF-TOKEN\s*=\s*([^;]*).*$|^.*$/, '$1');

function addToCart(evt) {
	const productId = $(this).parent().children().prev('input').last().val();

	const addedQuantity = $(this).parent().children().last().val();

	const currentQuantity = $('.quantity' + productId).val();

	const newQty = Number(addedQuantity) + Number(currentQuantity);

	if(newQty > 15 || newQty <= 0) {
		return;
	}

	const url = `${window.location.origin}/cart/add/${productId}/${addedQuantity}`;
	console.log(url)

	const requestOptions = {
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json',
			'X-XSRF-TOKEN': csrfToken
		},
		method: "POST"
	}

	fetch(url, requestOptions)
		.then(response => {
			if(response.ok) {
				console.log('Item added to cart')

				loadCartItems();

				updateTotal();
			} else {
				console.log('Log in to add product to cart')
			}
		})
		.catch(error => console.log('error', error))
}

// On page load
loadCartItems();

function loadCartItems() {
	const url = `${window.location.origin}/cart/load`;
	console.log(url)

	const requestOptions = {
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json',
			'X-XSRF-TOKEN': csrfToken
		}
	}

	fetch(url, requestOptions)
		.then(promise => {
			if(promise.ok) {
				console.log('Items loaded')
			} else {
				console.log('Log in to use the cart')
			}

			return promise.json();
		})
		.then(response => {
			fillCartContent(response);
		})
		.catch(error => console.log('error', error))
}

const cartItems = $(".cart__items");

function fillCartContent(responseJson) {
	cartItems.empty();

	for(const element of responseJson) {
		const product = element['productDTO'];
		const quantity = element['quantity'];

		const rowId = responseJson.indexOf(element) + 1;

		let cartItem = generateCartItem(rowId, product, quantity);

		cartItems.append(cartItem);
	}

	const cartItemsCount = cartItems.children().length;
	$("#cart-badge").text(cartItemsCount);

	updateTotalCart();
}

function generateCartItem(rowId, product, quantity) {
	return `<div class="cart__item row${rowId}">
				<div class="cart__content d-flex flex-column flex-sm-row align-items-sm-center">
					<div class="cart__info col-12 col-sm-auto">
						<div class="row align-items-center">
							<div class="cart__img col-5">
								<img pid="${product.uuid}" rowNumber="${rowId}" src="/images/${product.imageUrl}" alt="${product.link}">
							</div>

							<div class="cart__link col-7 py-3 px-2" style="word-wrap: break-word">
								<a href="${product.link}">${product.link}</a>
							</div>
						</div>
					</div>

					<div class="row align-items-center justify-content-start">
						<div class="cart__quantity col-auto">
							<div class="input-group quantity mx-auto" style="width: 120px;">
								<div class="input-group-btn">
									<a pid="${product.uuid}" class="btn btn-sm btn-minus" style="font-size: 19px;">
										<i class="fa fa-minus-circle"></i>
									</a>
								</div>
								
								<input type="text" class="cart__qty form-control form-control-sm bg-secondary border-0 rounded text-center quantity${product.uuid}"" value="${quantity}" maxlength="2" style="font-size: 17px; margin: 0; width: 30px;">
								
								<input type="text" class="cart__unit" value="${product.price}" hidden>
								
								<div class="input-group-btn">
									<a pid="${product.uuid}" class="btn btn-sm btn-plus" style="font-size: 19px; color: #000">
										<i class="fa fa-plus-circle"></i>
									</a>
								</div>
							</div>
						</div>

						<div class="cart__price col-auto">
							<span class="product-subtotal subtotal${product.uuid}">£${parseFloat((Number(product.price) * Number(quantity)).toFixed(2))}</span>
						</div>
					</div>
				</div>
			</div>`;
}

function updateTotalCart() {
	//Sum prices
	let sumPrices = 0;

	$('.cart__item').each(function() {

		const currentPrice = $(this).children().children().children()[1].children[0].children[2].value;

		const quantityInput = $(this).children().children().children()[1].children[0].children[1];

		const totalVal = (Number(currentPrice) * Number(quantityInput.value)).toFixed(2);

		// console.log(totalVal)

		sumPrices += Number(totalVal);
	});

	//Set value of subtotal span
	const cartSubtotal = $('#cart-subtotal').children()[1];
	cartSubtotal.innerText = "£" + sumPrices.toFixed(2);

	// Set value of shipping span
	const cartShipping = $('#cart-shipping').children()[1];

	const shippingPrice= Number(cartSubtotal.innerText.replace('£', '')) === 0 ? 0 : 15;

	cartShipping.innerText = `£${shippingPrice}`;

	const cartShippingPrice = Number(cartShipping.innerText.substring(1, cartShipping.length));

	//Set value of total
	const cartTotal = $('#cart-total').children()[1];
	cartTotal.innerText = "£" + (sumPrices + cartShippingPrice).toFixed(2);
}

/**
 * Update product quantity
 */

// Update quantity -> Cart Page
$('.quantity a i').on('click', function () {
	const qtyButton = $(this).parent();

	// const qtyInput = qtyButton.parent().parent().find('input')

	if (qtyButton.hasClass('btn-plus')) {
		increaseQuantity(qtyButton);
	} else {
		decreaseQuantity(qtyButton);
	}
});

// Update quantity -> Cart
$('.cart-menu').delegate('.quantity a > i', 'click', function(evt) {
	evt.preventDefault();

	const qtyButton = $(this).parent();
	console.log(qtyButton);

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
	const qtyInput = $('.quantity' + productId);

	resetQuantity(qtyInput);

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
	const qtyInput = $('.quantity' + productId);

	resetQuantity(qtyInput);

	let newQty = parseInt(qtyInput.val()) - 1;

	if (newQty > 0) {
		qtyInput.val(newQty);

		updateQuantity(productId, newQty);
	}
}

function resetQuantity(qtyInput) {
	if(qtyInput.val() < 0) {
		qtyInput.val(1);
	}

	if(qtyInput.val() > 15) {
		qtyInput.val(15);
	}

	return qtyInput.val();
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
		method: "POST"
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

			updateTotalCart()
		})
		.catch(error => console.log('error', error))
}

function updateSubtotal(newProductSubtotal, productId) {
	$('.subtotal' + productId).text(newProductSubtotal);
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

	$(".product-subtotal-page").each(function() {
		subtotalPrice += parseFloat($(this).text().replace('£',''));

		$(this).text(`£${parseFloat($(this).text().replace('£',''))}`);
	})

	let cartSubtotal = $('#cart-subtotal-page');
	cartSubtotal.text(`£${parseFloat(subtotalPrice.toFixed(2))}`);

	// Shipping
	const shippingPrice= cartSubtotal.text() === '£0' ? 0 : 15;

	$('#cart-shipping-page').text(`£${shippingPrice}`);

	// Total
	const totalPrice = subtotalPrice + shippingPrice;
	$('#cart-total-page').text(`£${parseFloat(totalPrice.toFixed(2))}`);
}


/**
 * Remove product from cart
 */
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

				removeProduct(rowNumber, productId);

				console.log('Product has been deleted')
			} else {
				console.log('Log in to use the cart')
			}
		})
		.catch(error => console.log('error', error))
}

function removeProduct(rowNumber, productId) {
	const rowId = "row" + rowNumber;

	fadeOutAndRemove($('.' + rowId), productId);
}


//Image Hover To Remove Item
$('.cart-menu').delegate('img', 'mouseover', function(evt) {
	if(evt.target && evt.target.nodeName === 'IMG') {
		// var cartItem = evt.target.parentElement.parentElement.parentElement.parentElement.parentElement;
		const img = $(this);

		$(img).addClass('erasable');

		$(evt.target).on('mouseover', function() {
			$(img).addClass('erasable');
		});

		$(evt.target).on('mouseout', function() {
			$(img).removeClass('erasable');
		});
	}
});

//Remove cart item
$('.cart-menu').delegate('.cart__img > img', 'click', function(evt) {
	if(evt.target && evt.target.nodeName === "IMG") {
		removeFromCart($(this));
	}
});

checkTableCart();

function fadeOutAndRemove(product, productId) {
	$(product).fadeOut(350, function () {
		$(product).remove();

		const cartItemsCount = $(".cart__items").children().length;
		$("#cart-badge").text(cartItemsCount);

		const length = $('.cart__items').children().length;
		if (length === 0) {
			$('.cart-menu').removeClass("is-active");
		}

		updateSubtotal(0, productId)

		updateTotal();

		updateTotalCart();

		checkTableCart();
	});
}

function checkTableCart() {
	$('#table-cart').show();
	$('#no-products').hide();

	if($('#table-cart tbody').children().length === 0) {
		$('#table-cart').hide();

		$('#no-products').show();
	}
}