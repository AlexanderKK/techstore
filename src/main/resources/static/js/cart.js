/**
 * Global declarations
 *
 * @type {jQuery|HTMLElement|*}
 */
const cartMenu = $('.cart-menu');

const cartItems = $(".cart__items");

const restHeaders = {
	'Accept': 'application/json',
	'Content-Type': 'application/json',
}

const buttonsAddToCart = $('.btnAddToCart');

const buttonAddToCartProductDetails = $('#btnAddToCartProductDetails');

const maxQuantity = 100;


/**
 * On page load
 */
checkTableCart();

loadCartItems();

updateCartPageTotal();


/**
 * Close cart on esc key press
 */
$(window).on('keydown',function(evt) {
	if(evt.code === "Escape") {
		cartMenu.removeClass('is-active');
	}
});

/**
 * Close cart on nav trigger click
 */
$('.navbar-toggler').click(function() {
	$('.navbar-collapse.show .cart-menu').removeClass('is-active');
});

/**
 * Open / close cart on click
 */
$('#cart-trigger').on('click',function(evt) {
	evt.preventDefault();

	if(cartMenu.hasClass('is-active')) {
		cartMenu.removeClass('is-active');
	} else {
		cartMenu.addClass('is-active');
	}
});


/**
 * Load cart items
 */
function loadCartItems() {
	const url = `${window.location.origin}/cart/load`;

	const requestOptions = {
		headers: restHeaders
	}

	fetch(url, requestOptions)
		.then(promise => {
			return promise.json();
		})
		.then(response => {
			fillCartContent(response);
		})
		.catch(error => error);
}

/**
 * Fill cart content
 *
 * @param responseJson
 */
function fillCartContent(responseJson) {
	cartItems.empty();

	for(const element of responseJson) {
		const product = element['productDTO'];
		const quantity = element['quantity'];

		const rowId = responseJson.indexOf(element) + 1;

		const cartItem = generateCartItem(rowId, product, quantity);

		cartItems.append(cartItem);
	}

	const cartItemsCount = cartItems.children().length;
	$("#cart-badge").text(cartItemsCount);

	updateCartTotal();
}

/**
 * Generate cart item
 *
 * @param rowId
 * @param product
 * @param quantity
 * @returns {string}
 */
function generateCartItem(rowId, product, quantity) {
	return `<div class="cart__item row${rowId}">
				<div class="cart__content d-flex flex-column flex-sm-row align-items-sm-center">
					<div class="cart__info col-12 col-sm-auto">
						<div class="row align-items-center">
							<div class="cart__img col-5">
								<img pid="${product.uuid}" rowNumber="${rowId}" src="/images/${product.imageUrl}" alt="${product.link}">
							</div>

							<div class="cart__link col-7 py-3 px-2" style="word-wrap: break-word">
								<a href="/products/detail/${product.uuid}">${product.link}</a>
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
								
								<input type="text" class="cart__qty form-control form-control-sm bg-secondary border-0 rounded text-center quantity${product.uuid}" value="${quantity}" maxlength="2" style="font-size: 17px; margin: 0; width: 30px;">
								
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

/**
 * Update cart total
 */
function updateCartTotal() {
	let sumPrices = 0;

	$('.cart__item').each(function() {
		const currentPrice = $(this).children().children().children()[1].children[0].children[2].value;

		const quantityInput = $(this).children().children().children()[1].children[0].children[1];

		const totalVal = (Number(currentPrice) * Number(quantityInput.value)).toFixed(2);

		sumPrices += Number(totalVal);
	});

	//Set value of subtotal span
	const cartSubtotal = $('#cart-subtotal').children()[1];
	cartSubtotal.innerText = "£" + sumPrices.toFixed(2);

	// Set value of shipping span
	const cartShipping = $('#cart-shipping').children()[1];

	const shipping = 15;

	const shippingPrice= Number(cartSubtotal.innerText.replace('£', '')) === 0 ? 0 : shipping;

	cartShipping.innerText = `£${shippingPrice}`;

	const cartShippingPrice = Number(cartShipping.innerText.substring(1, cartShipping.length));

	//Set value of total span
	const cartTotal = $('#cart-total').children()[1];

	cartTotal.innerText = "£" + parseFloat((sumPrices + cartShippingPrice).toFixed(2));
}


buttonsAddToCart.each(function() {
	$(this).on('mouseup', addToCart);
});

buttonAddToCartProductDetails.on('mouseup', function() {
	const productId = $('#product-details-uuid').val();
	const addedQuantity = $('#product-details-quantity').val();


	addProduct(productId, addedQuantity);
});

/**
 * Add to cart
 */
function addToCart() {
	const productId = $(this).parent().children().prev('input').last().val();

	const addedQuantity = $(this).parent().children().last().val();

	addProduct(productId, addedQuantity)
}

function addProduct(productId, addedQuantity) {
	const qtyInput = $('.quantity' + productId);

	const currentQuantity = qtyInput.val();

	const newQty = Number(currentQuantity) + Number(addedQuantity);

	if(newQty > maxQuantity || newQty <= 0) {
		return;
	}

	const url = `${window.location.origin}/cart/add/${productId}/${addedQuantity}`;
	console.log(url)

	const requestOptions = {
		headers: restHeaders,
		method: "POST"
	}

	fetch(url, requestOptions)
		.then(response => {
			if(response.ok) {
				console.log('Item added to cart')

				loadCartItems();

				updateCartPageTotal();
			}

			if(response.status === 400) {
				return response.json().then(data => alert(data.error));
			}
		})
		.catch(error => console.log('error', error))
}

/**
 * Update details quantity input
 */
$('.details-quantity a i').on('click', function () {
	const button = $(this).parent();

	const inputQty= button.parent().parent().parent().find('.input-quantity');

	resetQuantity(inputQty);

	let quantity = parseFloat(inputQty.val());

	if (button.hasClass('btn-plus')) {
		if(quantity < maxQuantity) {
			inputQty.val(quantity + 1);
		}
	} else {
		if (quantity > 1) {
			inputQty.val(quantity - 1);
		}
	}
});

/**
 * Update quantity -> Cart Page
 */
$('.quantity a i').on('click', function () {
	const qtyButton = $(this).parent();

	if (qtyButton.hasClass('btn-plus')) {
		increaseQuantity(qtyButton);
	} else {
		decreaseQuantity(qtyButton);
	}
});

/**
 * Update quantity -> Cart
 */
cartMenu.delegate('.quantity a > i', 'click', function(evt) {
	evt.preventDefault();

	const qtyButton = $(this).parent();

	// const qtyInput = qtyButton.parent().parent().find('input')

	if (qtyButton.hasClass('btn-plus')) {
		increaseQuantity(qtyButton);
	} else {
		decreaseQuantity(qtyButton);
	}
});

/**
 * Increase quantity and update cart stats
 *
 * @param qtyButton
 */
function increaseQuantity(qtyButton) {
	const productId = qtyButton.attr('pid');
	const qtyInput = $('.quantity' + productId);

	resetQuantity(qtyInput);

	let newQty = parseInt(qtyInput.val()) + 1;

	if(newQty <= maxQuantity) {
		qtyInput.val(newQty);

		updateQuantity(productId, newQty);
	}
}

/**
 * Decrease quantity and update cart stats
 *
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

/**
 * Reset quantity
 *
 * @param qtyInput
 * @returns {*|string|jQuery}
 */
function resetQuantity(qtyInput) {
	if(qtyInput.val() < 0) {
		qtyInput.val(1);
	}

	if(qtyInput.val() >= maxQuantity) {
		qtyInput.val(maxQuantity);
	}

	return qtyInput.val();
}

/**
 * Update quantity
 *
 * @param productId
 * @param quantity
 */
function updateQuantity(productId, quantity) {
	const url = `${window.location.origin}/cart/update/${productId}/${quantity}`;

	console.log(url, productId, quantity)

	const requestOptions = {
		headers: restHeaders,
		method: "POST"
	}

	fetch(url, requestOptions)
		.then(response => {
			if(response.ok) {
				console.log('Product quantity updated')

				return response.json().then(newSubtotal => {
					updateSubtotal(newSubtotal, productId);

					updateCartPageTotal();

					updateCartTotal();
				})
			}

			if(response.status === 400) {
				return response.json().then(data => {
					$('.quantity' + productId).val(data['availableQuantity']);

					alert(data.error);
				});
			}
		})

		.catch(error => console.log('error', error))
}

/**
 * Update subtotal
 *
 * @param newProductSubtotal
 * @param productId
 */
function updateSubtotal(newProductSubtotal, productId) {
	$('.subtotal' + productId).text('£' + newProductSubtotal);
}

/**
 * Update total
 */
function updateCartPageTotal() {
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

/**
 * Remove from cart
 *
 * @param removeBtn
 */
function removeFromCart(removeBtn) {
	const productId = removeBtn.attr('pid');

	const url = `${window.location.origin}/cart/remove/${productId}`;

	const requestOptions = {
		headers: restHeaders,
		method: "POST"
	}

	fetch(url, requestOptions)
		.then(response => {
			if(response.ok) {
				const rowNumber = removeBtn.attr('rowNumber');

				removeProduct(rowNumber, productId);

				console.log('Product has been removed')
			}
		})
		.catch(error => console.log('error', error))
}

/**
 * Remove product
 *
 * @param rowNumber
 * @param productId
 */
function removeProduct(rowNumber, productId) {
	const rowId = "row" + rowNumber;

	fadeOutAndRemove($('.' + rowId), productId);
}

/**
 * Image Hover To Remove Item
 */
cartMenu.delegate('img', 'mouseover', function(evt) {
	if(evt.target && evt.target.nodeName === 'IMG') {
		// const cartItem = evt.target.parentElement.parentElement.parentElement.parentElement.parentElement;
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

/**
 * Remove cart item
 */
cartMenu.delegate('.cart__img > img', 'click', function(evt) {
	if(evt.target && evt.target.nodeName === "IMG") {
		removeFromCart($(this));
	}
});

/**
 * Fade out and remove
 *
 * @param product
 * @param productId
 */
function fadeOutAndRemove(product, productId) {
	$(product).fadeOut(350, function () {
		$(product).remove();

		const cartItemsCount = $(".cart__items").children().length;
		$("#cart-badge").text(cartItemsCount);

		const length = $('.cart__items').children().length;
		if (length === 0) {
			cartMenu.removeClass("is-active");
		}

		updateSubtotal(0, productId)

		updateCartPageTotal();

		updateCartTotal();

		checkTableCart();
	});
}

/**
 * Check table cart
 */
function checkTableCart() {
	const tableCart = $('#table-cart');
	const noProductsDiv = $('#no-products');

	tableCart.show();
	noProductsDiv.hide();

	if($('#table-cart tbody').children().length === 0) {
		tableCart.hide();

		noProductsDiv.show();
	}
}
