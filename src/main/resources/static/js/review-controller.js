const addReviewBtn = document.querySelector("#addReviewBtn");
const existingReviewError = document.querySelector(".existingReviewError");
existingReviewError.style.display = "none";

addReviewBtn.addEventListener("click", createReview);

async function createReview() {
	const productUuidInput = document.querySelector("#createReviewContainer .productUuid");
	const ratingInput = document.querySelector("#createReviewContainer .ratingInput");
	const contentTextarea = document.querySelector("#createReviewContainer .reviewContent");
	const authorNameInput = document.querySelector("#createReviewContainer .authorName");
	const authorEmailInput = document.querySelector("#createReviewContainer .authorEmail");

	const csrfToken = document.cookie.replace(/(?:^|.*;\s*)XSRF-TOKEN\s*=\s*([^;]*).*$|^.*$/, '$1');

	const url = `${window.location.origin}/reviews/add`;

	const requestOptions = {
		method: "POST",
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json',
			'X-XSRF-TOKEN': csrfToken
		},
		body: JSON.stringify({
			product: productUuidInput.value,
			rating: ratingInput.value,
			content: contentTextarea.value,
			name: authorNameInput.value,
			email: authorEmailInput.value
		})
	};

	const promise = (await fetch(url, requestOptions));

	if(promise.ok) {
		productUuidInput.value = "";
		ratingInput.value = "";
		contentTextarea.value = "";
		authorNameInput.value = "";
		authorEmailInput.value = "";
	} else {
		clearReviewFieldErrors();

		const response = await promise.json();

		if(response.status === "FORBIDDEN") {
			existingReviewError.style.display = "block";

			return;
		}

		const subErrors = response.subErrors;

		if(subErrors === null || subErrors.length === 0) {
			return;
		}

		for (const subError of subErrors) {
			const field = subError.object;
			const message = subError.message;

			let element = document.querySelector(`.${field}Error`);

			element.innerText = `${message}`;
		}
	}
}

function clearReviewFieldErrors() {
	existingReviewError.style.display = "none";

	const reviewErrors = document.querySelectorAll(".addReviewError");

	for (const reviewError of reviewErrors) {
		reviewError.style.display = "block";
		reviewError.innerText = "";
	}
}
