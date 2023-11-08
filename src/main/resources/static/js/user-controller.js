const registerForm = document.getElementById('registerForm');

registerForm.addEventListener('submit', registerUser);

const email = document.getElementById('emailRegister').value;
const username = document.getElementById('usernameRegister').value;
const password = document.getElementById('passwordRegister').value;

function registerUser(evt) {
	evt.preventDefault();

	const requestOptions = {
		method: "POST",
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			email: email,
			username: username,
			password: password
		})
	}

	fetch("http://localhost:8080/users/register", requestOptions)
		.then(response => console.log(response))
		.catch(error => console.log('error', error));
}