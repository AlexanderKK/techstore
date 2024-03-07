const registerForm = document.getElementById('registerForm');

if(registerForm) {
	registerForm.addEventListener('submit', registerUser);
}

function registerUser(evt) {
	evt.preventDefault();

	const csrfToken = document.cookie.replace(/(?:^|.*;\s*)XSRF-TOKEN\s*=\s*([^;]*).*$|^.*$/, '$1');

	const emailInput = document.getElementById('emailRegister');
	const usernameInput = document.getElementById('usernameRegister');
	const passwordInput = document.getElementById('passwordRegister');

	const requestOptions = {
		method: "POST",
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json',
			'X-XSRF-TOKEN': csrfToken
		},
		body: JSON.stringify({
			email: emailInput.value,
			username: usernameInput.value,
			password: passwordInput.value
		})
	}

	fetch(`${window.location.origin}/users/register`, requestOptions)
		.then(response => {
			if(response.ok) {
				emailInput.value = "";
				usernameInput.value = "";
				passwordInput.value = "";

				const popupRegister = document.querySelector(".popup.popup--register");
				popupRegister.classList.remove("is-active");

				location.replace("/users/register/success");

				return;
			}

			return response.json();
		})
		.then(json => {
			const subErrors = json.subErrors;

			if(typeof subErrors === 'undefined' || subErrors === null || subErrors.length === 0) {
				return;
			}

			clearPopupsFieldsErrors();

			for (const subError of subErrors) {
				const field = subError.object;
				const message = subError.message;

				let element = document.querySelector(`.${field}Register-error`);
				element.innerText += `${message}\n`;
			}
		})
		.catch(error => console.log('error', error));
}

function clearPopupsFieldsErrors() {
	const errorMessages = document.querySelectorAll(".popup__error");

	for (const errorMessage of errorMessages) {
		errorMessage.innerText = "";
	}
}
