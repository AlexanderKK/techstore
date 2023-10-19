
const linksLogin = document.querySelectorAll(".link-login");
const linksRegister = document.querySelectorAll(".link-register");
const popups = document.querySelectorAll(".popup");
const closeBtnLogin = document.querySelector(".popup--login .popup__close");
const closeBtnRegister = document.querySelector(".popup--register .popup__close");
const errorMsgs = document.querySelectorAll(".errorMsg");
const loginMsg = document.querySelector(".loginMsg");

function windowStyles(overflow, pointerEvents) {
	document.querySelector('html').style.overflowY = overflow;
	document.querySelector('body').style.overflowY = overflow;
	
	document.querySelector('html').style.pointerEvents = pointerEvents;
	document.querySelector('body').style.pointerEvents = pointerEvents;
}

//Login Popup
for (linkLogin of linksLogin) {
	linkLogin.addEventListener("mousedown", function() {
		for	(const popup of popups) {
			
			const popupClasses = popup.classList;
			if(popupClasses.contains("popup--login")) {
				popup.classList.add("is-active");
			}
		}

		windowStyles("hidden", "none");
	});
}

//Close Btn Login
closeBtnLogin.addEventListener("mousedown", function() {
	this.parentElement.parentElement.classList.remove("is-active");

	windowStyles("", "all");
});

//Register Popup
for (linkRegister of linksRegister) {
	linkRegister.addEventListener("mousedown", function() {
		for	(const popup of popups) {
			
			const popupClasses = popup.classList;
			if(popupClasses.contains("popup--register")) {
				popup.classList.add("is-active");
			}
			if(popup.classList.contains("popup--login")) {
				popup.classList.remove("is-active");
			}
		}

		windowStyles("hidden", "none");
	});
}

//Close Btn Register
closeBtnRegister.addEventListener("mousedown", function() {
	this.parentElement.parentElement.classList.remove("is-active");

	windowStyles("", "all");
});

//Close Popups On Background Click
// window.addEventListener("mousedown", function(evt) {
// 	for	(const popup of popups) {
// 		if(evt.target == popup) {
// 			popup.classList.remove("is-active");

// 			for	(const errorMsg of errorMsgs) {
// 				errorMsg.classList.add("hidden");
// 			}

// 			document.querySelector('html').style.overflowY = "";
// 			document.querySelector('body').style.overflowY = "";
			
// 			document.querySelector('html').style.pointerEvents = "all";
// 			document.querySelector('body').style.pointerEvents = "all";
// 		}
// 	}
// });

//Close Popups On Escape Key
window.addEventListener("keyup", function(evt) {
	if(evt.code == "Escape") {
		for	(const popup of popups) {
			popup.classList.remove("is-active");
		}

		windowStyles("", "all");
	}
});