// Declarations
// const linksLogin = document.querySelectorAll(".link-login");
// const closeBtnLogin = document.querySelector(".popup--login .popup__close");

const popups = document.querySelectorAll(".popup");
const linksRegister = document.querySelectorAll(".link-register");
const closeBtnRegister = document.querySelector(".popup--register .popup__close");

function windowStyles(overflow, pointerEvents) {
	document.querySelector('html').style.overflowY = overflow;
	document.querySelector('body').style.overflowY = overflow;
	
	document.querySelector('html').style.pointerEvents = pointerEvents;
	document.querySelector('body').style.pointerEvents = pointerEvents;
}

// Login Popup
// if(linksLogin) {
// 	for (const linkLogin of linksLogin) {
// 		if (!linkLogin) {
// 			break;
// 		}
//
// 		linkLogin.addEventListener("mousedown", function () {
// 			for (const popup of popups) {
//
// 				const popupClasses = popup.classList;
// 				if (popupClasses.contains("popup--login")) {
// 					popup.classList.add("is-active");
// 				}
// 			}
//
// 			windowStyles("hidden", "none");
// 		});
// 	}
// }

// Close Btn Login
// if(closeBtnLogin) {
// 	closeBtnLogin.addEventListener("mousedown", function() {
// 		this.parentElement.parentElement.classList.remove("is-active");
//
// 		windowStyles("", "all");
// 	});
// }

// Register Popup
if(linksRegister) {
	for (const linkRegister of linksRegister) {
		if (!linkRegister) {
			break;
		}

		linkRegister.addEventListener("mousedown", function () {
			for (const popup of popups) {

				const popupClasses = popup.classList;
				if (popupClasses.contains("popup--register")) {
					popup.classList.add("is-active");
				}
				if (popup.classList.contains("popup--login")) {
					popup.classList.remove("is-active");
				}
			}

			windowStyles("hidden", "none");
		});
	}
}

// Close Btn Register
if(closeBtnRegister) {
	closeBtnRegister.addEventListener("mousedown", function () {
		this.parentElement.parentElement.classList.remove("is-active");

		windowStyles("", "all");
	});
}

// Close Popups On Background Click
window.addEventListener("mousedown", function(evt) {
	if(popups) {
		for (const popup of popups) {
			if (evt.target === popup) {
				popup.classList.remove("is-active");

				document.querySelector('html').style.overflowY = "";
				document.querySelector('body').style.overflowY = "";

				document.querySelector('html').style.pointerEvents = "all";
				document.querySelector('body').style.pointerEvents = "all";
			}
		}
	}
});

// Close Popups On Escape Key
window.addEventListener("keyup", function(evt) {
	if(evt.code === "Escape" && popups) {
		for	(const popup of popups) {
			popup.classList.remove("is-active");
		}

		windowStyles("", "all");
	}
});
