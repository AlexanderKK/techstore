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
			alert("Oops");
		},
	});
});