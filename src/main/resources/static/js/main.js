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

	//Open / Close Cart On Hover
	// function hoverCart() {
	// 	//Show / Hide Cart
	// 	if($('#cart-trigger.hover').hasClass('hover')) {
	// 		$('#cart-trigger').unbind('click');
	//
	// 		$('#cart-trigger').attr('href', 'cart.html');
	//
	// 		if($('#cart-trigger').hasClass('detail')) {
	// 			$('#cart-trigger').attr('href', '../cart.html');
	// 		}
	//
	// 		$('#cart-trigger.hover').on('mouseenter', function() {
	// 			$('.cart-menu').addClass('is-active');
	// 		});
	//
	// 		$('#cart-trigger.hover').on('mouseleave', function() {
	// 			$('.cart-menu').removeClass('is-active');
	// 		});
	//
	// 		$('.cart-menu .cart__display').on('mouseover', function() {
	// 			$('.cart-menu').addClass('is-active');
	// 		});
	//
	// 		$('.cart-menu .cart__display').on('mouseout', function() {
	// 			$('.cart-menu').removeClass('is-active');
	// 		});
	//
	// 	}
	// }
	
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

	//Initial Statement - Hover Cart On Large Display | Click Cart On Smaller One
	// if($(window).width() > 974) {
	// 	$('#cart-trigger').addClass('hover');
	// 	$('#cart-trigger').removeClass('click');
	//
	// 	hoverCart();
	// } else {
	//
	// 	$('#cart-trigger').addClass('click');
	// 	$('#cart-trigger').removeClass('hover');
	//
	// 	clickCart();
	// }
	
	//Get Width On Resize
	// $(window).resize(function() {
	//   $(window).width();
	//
	//   //Hover Cart On Large Display | Click Cart On Smaller One
	//   if($(window).width() > 974) {
	//
	// 	$('#cart-trigger').addClass('hover');
	// 	$('#cart-trigger').removeClass('click');
	//
	// 	hoverCart();
	//   } else {
	//
	// 	$('#cart-trigger').addClass('click');
	// 	$('#cart-trigger').removeClass('hover');
	//
	// 	clickCart();
	//   }
	//
	//   //Display window width on screen resize
	//   console.log($(window).width());
	// });

	//On Load Add class "is-loaded" To Children of ".hero content"
	$(window).on('load', function() {
		$.each($('.hero-content').children(), function(index, value){
			$(value).addClass('is-loaded');
		});
	});
})(jQuery);