const ratingStars = document.querySelectorAll(".rating-stars i");
const ratingInput = document.querySelector(".ratingInput");
const ratingStartContainer = document.querySelector(".rating-stars");

ratingInput.value = '';

const ratings = document.querySelectorAll('.rating');
ratings.forEach(function(rating) {
	const ratingValue = rating.getAttribute('data-rating');

	for (let star of rating.children) {
		const starValue = parseFloat(star.getAttribute("data-vote"));
		if (starValue <= ratingValue) {
			star.className = "fas fa-star";
		} else {
			star.className = "far fa-star";
		}
	}
});

ratingStartContainer.addEventListener('mouseover', function(evt) {
	this.style.cursor = 'pointer';
});

ratingStars.forEach(function(star) {
	star.addEventListener("mouseover", function() {
		const value = parseFloat(this.getAttribute("data-vote"));
		highlightStars(value);
	});

	star.addEventListener("click", function() {
		const value = parseFloat(this.getAttribute("data-vote"));
		highlightStars(value);
		ratingInput.value = value;

		// alert("You rated this " + value + " stars!");
	});
});

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