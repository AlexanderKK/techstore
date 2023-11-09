const registerForm = document.getElementById('registerForm');

registerForm.addEventListener('submit', registerUser);

function registerUser(evt) {
	evt.preventDefault();

	const emailInput = document.getElementById('emailRegister');
	const usernameInput = document.getElementById('usernameRegister');
	const passwordInput = document.getElementById('passwordRegister');

	const requestOptions = {
		method: "POST",
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			email: emailInput.value,
			username: usernameInput.value,
			password: passwordInput.value
		})
	}

	fetch("http://localhost:8080/users/register", requestOptions)
		.then(response => {
			if(response.ok) {
				emailInput.value = "";
				usernameInput.value = "";
				passwordInput.value = "";

				const popupLogin = document.querySelector(".popup.popup--login");
				const popupRegister = document.querySelector(".popup.popup--register");

				popupLogin.classList.add("is-active");
				popupRegister.classList.remove("is-active");

				windowStyles("hidden", "none");

				return;
			}

			return response.json();
		})
		.then(fields => {
			if(typeof fields === 'undefined' || fields.length === 0) {
				return;
			}

			const errorMessages = document.querySelectorAll(".register-error");
			for (const errorMessage of errorMessages) {
				errorMessage.innerText = "";
			}

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