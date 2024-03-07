const addReviewBtn = document.querySelector("#addReviewBtn");
const existingReviewError = document.querySelector(".existingReviewError");
const productUuidInput = document.querySelector("#createReviewContainer .productUuid");

if(existingReviewError !== null) {
	existingReviewError.style.display = "none";
}

if(addReviewBtn !== null) {
	addReviewBtn.addEventListener("click", createReview);
}

async function createReview() {
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

	const promise = await fetch(url, requestOptions);

	if(promise.url.endsWith("users/login")) {
		location.replace("/users/login");

		return;
	}

	if(promise.ok) {
		ratingInput.value = "";
		contentTextarea.value = "";
		authorNameInput.value = "";
		authorEmailInput.value = "";

		await loadReviews();
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

async function loadReviews() {
	const productUuid = productUuidInput.value;

	const url = `${window.location.origin}/reviews/product/${productUuid}`
	const requestOptions = {
		headers: restHeaders
	};

	const promise = await fetch(url, requestOptions);
	const reviews = await promise.json();

	const reviewsContainer = document.querySelector(".reviews");
	reviewsContainer.innerHTML = "";

	let sumRating = 0;
	for (const review of reviews) {
		const reviewString = generateReview(review);
		const reviewContainer = createElementFromHTML(reviewString);

		const rating = reviewContainer.children[1].children[1];
		calculateRating(rating);
		sumRating += review.rating;

		reviewsContainer.appendChild(reviewContainer);
	}

	refreshProductRating(sumRating, reviews);
	refreshAverageRating();

	const reviewsCount = reviews.length;
	const reviewBadge = document.querySelector(".reviewBadge");
	reviewBadge.innerText = `Reviews (${reviewsCount})`;

	const reviewCounter = document.querySelector(".reviewCounter");
	reviewCounter.innerText = `${reviewsCount} ${reviewsCount === 1 ? "review" : "reviews"} for ${reviewCounter.getAttribute("product-name")}`;
}

function refreshProductRating(sumRating, reviews) {
	const productRating = document.querySelector(".productRating");

	const dataRating = productRating.children[0];

	for (const star of dataRating.children) {
		star.className = 'far fa-star';
	}

	const reviewsCountBadge = productRating.children[1].children[0];

	const newRating = sumRating / reviews.length;
	dataRating.setAttribute("data-rating-average", newRating.toString());
	reviewsCountBadge.innerText = `(${reviews.length})`;
}

function createElementFromHTML(htmlString) {
	const div = document.createElement("div");
	div.innerHTML = htmlString.trim();

	return div.firstChild;
}

function generateReview(review) {
	return `<div class="media mb-4">
				<object class="img-fluid mr-3 mt-1" style="width: 45px;" data="${review.imageUrl}">
					<img src="https://placehold.co/45x45" alt="User Image">
				</object>
	
				<div class="media-body">
					<h6>
						${review.name}
						<small> - <i>${review.date}</i></small>
					</h6>
	
					<div class="text-primary mb-2 rating" data-rating="${review.rating}">
						<i data-vote="1" class="far fa-star"></i>
						<i data-vote="2" class="far fa-star"></i>
						<i data-vote="3" class="far fa-star"></i>
						<i data-vote="4" class="far fa-star"></i>
						<i data-vote="5" class="far fa-star"></i>
					</div>
	
					<p>${review.content}</p>
				</div>
			</div>`;
}

function clearReviewFieldErrors() {
	existingReviewError.style.display = "none";

	const reviewErrors = document.querySelectorAll(".addReviewError");

	for (const reviewError of reviewErrors) {
		reviewError.style.display = "block";
		reviewError.innerText = "";
	}
}
