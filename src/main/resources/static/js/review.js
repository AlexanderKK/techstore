const ratingStars = document.querySelectorAll(".rating-stars i");
const ratingInput = document.querySelector(".ratingInput");
const ratingStartContainer = document.querySelector(".rating-stars");

if(ratingInput !== null) {
	ratingInput.value = '';
}

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

// Average rating
const ratingAvgDivs = document.querySelectorAll('.rating-average');

ratingAvgDivs.forEach((stars) => {
	let averageRating = stars.getAttribute('data-rating-average');

	if(isNaN(Number(averageRating))) {
		return;
	}

	console.log(averageRating);

	averageRating = Math.round(Number(averageRating));

	for (let i = 0; i < stars.children.length; i++) {
		if(i === averageRating) {
			break;
		}

		const star = stars.children[i];
		star.className = 'fas fa-star';
	}
});
