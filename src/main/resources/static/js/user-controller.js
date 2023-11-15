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
		.then(fields => {
			if(typeof fields === 'undefined' || fields.length === 0) {
				return;
			}

			clearPopupsFieldsErrors();

			for (let field in fields) {
				const errorMessage = fields[field];
				// console.log(field, errorMessage);

				document.querySelector(`.${field}Register-error`).innerText += `${errorMessage}\n`;
			}
		})
		.catch(error => console.log('error', error))
		.finally(() => {
			// console.clear();
		});
}

function clearPopupsFieldsErrors() {
	const errorMessages = document.querySelectorAll(".popup__error");

	for (const errorMessage of errorMessages) {
		errorMessage.innerText = "";
	}
}
