(function ($) {
	"use strict";
	
	// Back To Top
	$(window).scroll(function () {
		var footerTop = $('.footer').offset().top - $('.footer').height() - 100;
		if ($(this).scrollTop() > footerTop) {
			$('.back-to-top').fadeIn('slow');
		} else {
			$('.back-to-top').fadeOut('slow');
		}
	});

	//Animate Every "a" Tag With href{#}
	$('a[href="#"]').click(function () {
		$('html, body').animate({scrollTop: 0}, 500);
		return false;
	});

	// Product Quantity
	$('.quantity button').on('click', function () {
		var button = $(this);
		var oldValue = button.parent().parent().find('input').val();
		if (button.hasClass('btn-plus')) {
			var newVal = parseFloat(oldValue) + 1;
		} else {
			if (oldValue > 0) {
				var newVal = parseFloat(oldValue) - 1;
			} else {
				newVal = 0;
			}
		}
		button.parent().parent().find('input').val(newVal);
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

	//On Load Add class "is-loaded" To Children of ".hero content"
	$(window).on('load', function() {
		$.each($('.hero-content').children(), function(index, value){
			$(value).addClass('is-loaded');
		});
	});


	/**
	 * Image load on file input change
	 */
	$('.custom-file-input').each(function() {
		$(this).on('change', function(e) {
			loadFileImage(e);
		});
	});

	function loadFileImage(evt) {
		const selectedFile = evt.target.files[0];

		const categoryImg = $(evt.target).parent().parent().parent().parent().children().first().children().first();

		const categoryImgError = $(evt.target).parent().parent().children().get(3);
		$(categoryImgError).empty();

		if (!selectedFile || selectedFile.type !== 'image/png') {
			categoryImg.attr('src', 'https://images.pexels.com/photos/1037995/pexels-photo-1037995.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=175');
			categoryImg.title = '';

			$(categoryImgError).append('<small class="text-danger">File type should be .png!</small>');

			return;
		}

		const reader = new FileReader();

		reader.onload = function (evt) {
			categoryImg.attr('src', evt.target.result);
			categoryImg.title = selectedFile.name;
			categoryImg.width(175);
		}

		reader.readAsDataURL(selectedFile);
	}


	const categories = $('.categories-container');

	// btnAddProduct.on('click', function(evt) {
	// 	evt.preventDefault();
	//
	// 	if(categories.children().length === 0) {
	// 		console.log('Invalid categories');
	// 	}
	// })

	const categoriesSelect = $('#product-category');
	const categoryIds = [];

	categoriesSelect.on('change', function(evt) {
		const selectedOption = this.selectedOptions[0];

		if(selectedOption.text.trim() === "Select a category" || selectedOption.value === "") {
			return;
		}

		let isCategoryPresent = false;

		categories.children().each(function() {
			const categorySpan = $(this).children().children().first();

			if(categorySpan.attr("data-id") && categorySpan.data("id") === Number(selectedOption.value)) {
				// console.log(categorySpan.data("id"));

				isCategoryPresent = true;

				return false;
			}
		});

		if(!isCategoryPresent) {
			categories.append(`<div class="col-6 mt-3 category"><a><span data-id="${selectedOption.value}"><i class="fa fa-cogs"></i>${selectedOption.text}</span></a></div>`)
			categoryIds.push(selectedOption.value);
		}

		categories.children().each(function() {
			if($(this).prop('nodeName') === 'INPUT') {
				this.remove();
			}
		});

		if($(categories).length) {
			categories.append(`<input type="hidden" name="categories" value="${categoryIds}"/>`);
		}
	});

})(jQuery);