const ratingStars = document.querySelectorAll(".rating-stars i");
const ratingInput = document.querySelector(".ratingInput");
const ratingStartContainer = document.querySelector(".rating-stars");

const ratings = document.querySelectorAll('.rating');
const ratingAvgDivs = document.querySelectorAll('.rating-average');

if(ratingInput !== null) {
	ratingInput.value = '';
}

ratings.forEach(function(rating) {
	calculateRating(rating);
});

function calculateRating(rating) {
	const ratingValue = rating.getAttribute('data-rating');

	for (let star of rating.children) {
		const starValue = parseFloat(star.getAttribute("data-vote"));
		if (starValue <= ratingValue) {
			star.className = "fas fa-star";
		} else {
			star.className = "far fa-star";
		}
	}
}

if(ratingStartContainer !== null) {
	ratingStartContainer.addEventListener('mouseover', function(evt) {
		this.style.cursor = 'pointer';
	});
}

ratingStars.forEach(function(star) {
	star.addEventListener("mouseover", function() {
		const value = parseFloat(this.getAttribute("data-vote"));
		highlightStars(value);
	});

	star.addEventListener("click", function() {
		const value = parseFloat(this.getAttribute("data-vote"));
		highlightStars(value);
		ratingInput.value = value;
	});
});

refreshAverageRating();

/**
 * Calculating average rating for every product and displaying it with stars
 */
function refreshAverageRating() {
	ratingAvgDivs.forEach((stars) => {
		let averageRating = Number(
			stars.getAttribute('data-rating-average')
		);

		if(isNaN(averageRating)) {
			return;
		}

		averageRating = parseFloat(averageRating.toFixed(1));

		const wholeAverageRating = Math.floor(averageRating);

		for (let i = 0; i < stars.children.length; i++) {
			const currentRating = i + 1;
			const star = stars.children[i];

			if(currentRating > wholeAverageRating) {
				if(averageRating >= currentRating - 0.5) {
					star.className = 'fas fa-star-half-alt';
				}

				break;
			}

			star.className = 'fas fa-star';
		}
	});
}

function highlightStars(value) {
	ratingStars.forEach(function(star) {
		const starValue = parseFloat(star.getAttribute("data-vote"));
		if (starValue <= value) {
			star.className = "fas fa-star";
		} else {
			star.className = "far fa-star";
		}
	});
}