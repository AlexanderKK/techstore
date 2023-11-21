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


// Product Quantity
$('.quantity button').on('click', function () {
	const button = $(this);

	const qtyInput = button.parent().parent().find('input')

	// const productId = $(this).attr('pid');
	// const qtyInput = $('quantity' + productId)

	if (button.hasClass('btn-plus')) {
		let newQty = parseInt(qtyInput.val()) + 1;

		if(newQty <= 15) {
			qtyInput.val(newQty);
		}
	} else {
		let newQty = parseInt(qtyInput.val()) - 1;

		if (newQty > 0) {
			qtyInput.val(newQty);
		}
	}
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

// function clickCart() {
// 	if($('#cart-trigger').hasClass('click')) {
// 		$('#cart-trigger').unbind('mouseenter');
// 		$('#cart-trigger').unbind('mouseleave');
// 		$('.cart-menu .cart__display').unbind('mouseover');
// 		$('.cart-menu .cart__display').unbind('mouseout');
//
// 		$('#cart-trigger').attr('href', '#');
//
// 		$('#cart-trigger').on('click',function(evt) {
// 			evt.preventDefault();
//
// 			if($('.cart-menu').hasClass('is-active')) {
// 				$('.cart-menu').removeClass('is-active');
// 			} else {
// 				$('.cart-menu').addClass('is-active');
// 			}
// 		});
//
// 	}
// }


// Close Cart On Nav Trigger Click
$('.navbar-toggler').click(function() {
	$('.navbar-collapse.show .cart-menu').removeClass('is-active');
});


// Add product to cart
const btnAddToCart = $('#btnAddToCart');
btnAddToCart.on('click', function(evt) {
	alert("Add to cart");
});

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