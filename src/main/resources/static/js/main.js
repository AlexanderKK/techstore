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

	// $('html, body').animate({scrollTop: 0}, 750);

	//Animate Every "a" Tag With href{#}
	$('a[href="#"]').click(function () {
		$('html, body').animate({scrollTop: 0}, 500);
		return false;
	});

	// Change cart behaviour

	// On page load
	const cart = $('.cart > a');

	if($(window).width() <= 974) {
		cart.removeAttr('id', '');
		cart.attr('href', '/cart');
		$('.cart-menu').removeClass('is-active')
	} else {
		cart.attr('id', 'cart-trigger');
		cart.removeAttr('href');
	}

	// On window resize
	$(window).on('resize',function() {
		$(window).width();

		//Hover Cart On Large Display | Click Cart On Smaller One
		if($(window).width() <= 992) {
			cart.removeAttr('id');
			cart.attr('href', '/cart');
			$('.cart-menu').removeClass('is-active')
		} else {
			cart.attr('id', 'cart-trigger');
			cart.removeAttr('href');
		}

		//Display window width on screen resize
		// console.log($(window).width());
	});

	// Radio checks
	const radios = $('input[type="radio"]');

	radios.each(function() {
		const radio = $(this);

		radio.on('change', function() {
			radio.val(radio.next().text());
		});
	});

	const fileInput = $('.custom-file-input');

	//On Load Add class "is-loaded" To Children of ".hero content"
	$(window).on('load', function() {
		$.each($('.hero-content').children(), function(index, value){
			$(value).addClass('is-loaded');
		});

		// fileInput.each(function() {
		// 	console.log($(this).attr("field"))
		// 	if($(this).val() !== "") {
		// 		const selectedFile = fileInput.val();
		// 		// console.log(selectedFile);
		// 	}
		// });
	});


	/**
	 * Image upload
	 * Image load on file input change
	 */
	fileInput.each(function() {
		$(this).on('change', function(e) {
			// console.dir(e.target)
			loadFileImage(this);
		});
	});

	const inputImageUrl = $('#input-imageUrl');

	function loadFileImage(input) {
		let selectedFile;

		if(typeof input.files !== 'undefined') {
			selectedFile = input.files[0];
			// console.log(selectedFile);
		}

		const categoryImg = $(input).parent().parent().parent().parent().children().first().children().first();

		const categoryImgError = $(input).parent().parent().parent().parent().children().last().children().first();

		$(categoryImgError).empty();

		if (!selectedFile ||
			(selectedFile.type !== 'image/png' && selectedFile.type !== 'image/jpeg') ||
			selectedFile.size > 2097152) {
			categoryImg.attr('src', 'https://images.pexels.com/photos/1037995/pexels-photo-1037995.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=175');

			categoryImg.title = '';
			input.value = '';

			inputImageUrl.val('');

			if(selectedFile.size > 2097152) {
				$(categoryImgError).append('<small class="text-danger">Please do not exceed the image size limit</small>');
			} else {
				$(categoryImgError).append('<small class="text-danger">Please choose an image of valid type</small>');
			}

			return;
		}

		const reader = new FileReader();

		reader.onload = function (e) {
			categoryImg.attr('src', e.target.result);
			categoryImg.title = selectedFile.name;

			// inputImageUrl.val(selectedFile.name);
		}

		reader.readAsDataURL(selectedFile);
	}

	/**
	 * Image load on edit page load
	 */
	const imageEditUrl = $('.input-image.edit');

	$(window).on('load', function() {
		const imageUrl = imageEditUrl.attr('field');
		const imgTag = imageEditUrl.children().first();

		if(imageUrl === undefined) {
			imgTag.attr('src', 'https://images.pexels.com/photos/1037995/pexels-photo-1037995.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=175');
		} else {
			imgTag.attr('src', `${imageUrl}`);

			inputImageUrl.val(imageUrl);
		}
	});


	/**
	 * Categories
	 */
	const categories = $('.categories-container');

	const categoriesSelect = $('#product-category');

	const categoryIds= [];

	// Remove category from container before adding to product
	categories.delegate('.category', 'click', function(evt) {
		if(evt.target) {
			fadeOutAndRemove(this);
			categoriesSelect.prop("selectedIndex", 0);
		}
	});

	function fadeOutAndRemove(category) {
		$(category).fadeOut(350, function () {
			$(category).remove();
			const inputField = categories.children().last();
			const categoryId = $(this).children().first().first().first().children().last().children().first().attr("data-id");

			const categoryIndex = categoryIds.indexOf(categoryId);
			if (categoryIndex > -1) {
				categoryIds.splice(categoryIndex, 1);
			}

			inputField.val(categoryIds);
			// inputField.val(inputField.val().replaceAll(`${categoryId},`,""));
			// if(inputField.val().length === 1 || inputField.val().indexOf(categoryId) === inputField.val().length - 1) {
			// 	inputField.val(inputField.val().replaceAll(`${categoryId}`,""));
			// }
		})
	}

	// Reset inputs -> remove is-invalid class
	$('.form-group').children().each(function() {
		$(this).on('blur', function() {
			if($(this).next().hasClass("categories-container")) {
				return false;
			}

			$(this).removeClass("is-invalid")

			$(this).next().hide();
		});
	});

	$(window).on('mouseover', function(evt) {
		if(categories.children().length === 1 && evt.target !== $(categoriesSelect)) {
			categoriesSelect.addClass("is-invalid");
		}
	});

	// $(categoriesSelect).on('mouseover', function(evt) {
	// 	if(categories.children().length > 1) {
	// 		categoriesSelect.removeClass("is-invalid");
	// 	}
	// });


	// Add is-invalid class to inputs

	// if($('#product-category + div + small').text().length !== 0) {
	// 	categoriesSelect.addClass("is-invalid");
	// }


	// Select categories
	// Add categories to product

	// console.log(categories.attr('field').textContent);
	const attrField = categories.attr('field');

	if(categoryIds.length === 0 && attrField) {
		const ids = attrField.split(",");

		const options = $(`#product-category option`);
		for (const option of options) {
			// console.log(ids, option.value);

			if(ids.includes(option.value)) {
				categories.append(`<div class="col-6 mt-3 category"><a class="row justify-content-between align-items-center"><div class="col-5"><img width="60" src='${$(option).data("imageurl")}' alt="Category Picture"></div><div class="col-7"><span data-id="${option.value}">${option.text}</span></div></a></div>`)

				categoryIds.push(option.value);
			}
		}

		if(categories.hasClass('roles-container')) {
			categories.append(`<input type="hidden" name="roles" value="${categoryIds}"/>`);
		} else {
			categories.append(`<input type="hidden" name="categories" value="${categoryIds}"/>`);
		}
	}

	categoriesSelect.on('change', function(evt) {
		const selectedOption = this.selectedOptions[0];

		if(selectedOption.text.trim() === "Select a category" || selectedOption.value === "") {
			return;
		}

		let isCategoryPresent = false;

		categories.children().each(function() {
			const categorySpan = $(this).children().first().first().first().children().last().children().first();

			if(categorySpan.attr("data-id") && categorySpan.data("id") === Number(selectedOption.value)) {
				// console.log(categorySpan.data("id"));
				isCategoryPresent = true;

				return false;
			}
		});

		if(!isCategoryPresent) {
			categories.append(`<div class="col-6 mt-3 category"><a class="row justify-content-between align-items-center"><div class="col-5"><img width="60" src='${$(selectedOption).data("imageurl")}' alt="Category Picture"></div><div class="col-7"><span data-id="${selectedOption.value}">${selectedOption.text}</span></div></a></div>`)

			if(!categoryIds.includes(selectedOption.value)) {
				categoryIds.push(selectedOption.value);
			}

			categoriesSelect.removeClass("is-invalid");

			$('#product-category + small').hide();
		}

		categories.children().each(function() {
			if($(this).prop('nodeName') === 'INPUT') {
				this.remove();
			}
		});

		categoryIds.sort();

		if(categories.hasClass('roles-container')) {
			categories.append(`<input type="hidden" name="roles" value="${categoryIds}"/>`);
		} else {
			categories.append(`<input type="hidden" name="categories" value="${categoryIds}"/>`);
		}
	});

	// $('textarea, .table-responsive').each(function() {
	// 	$(this).on('scroll', function() {
	// 		$('body').addClass("stop-scrolling");
	// 	});
	//
	// 	$(this).on('mouseout', function() {
	// 		$('body').removeClass("stop-scrolling");
	// 	});
	// });

	/**
	 * Scrolling
	 */
	function disableScroll() {
		// Get the current page scroll position
		const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
		const scrollLeft = window.pageXOffset || document.documentElement.scrollLeft;

		// if any scroll is attempted, set this to the previous value
		window.onscroll = function() {
			window.scrollTo(scrollLeft, scrollTop);
		};
	}

	function enableScroll() {
		window.onscroll = function() {};
	}

	$('body').show();

	$(window).on('load', function() {
		$('.alert').each(function() {
			$(this).delay(7000).fadeOut(500, function () {
				$(this).remove();
			});
		});
	});

})(jQuery);