document.addEventListener("DOMContentLoaded", function() {
	//   arrowsRight[index].addEventListener("click", slideRight);
	// 서버에서 전달받은 좋아요 정보
	const likeInfo = /*[[${like}]]*/ null;
	console.log(likeInfo);

});

//제작진-배우 탭전환
const castButton = document.getElementById('castButton');
const crewButton = document.getElementById('crewButton');
const castSection = document.getElementById('castSection');
const crewSection = document.getElementById('crewSection');

castButton.addEventListener('click', function() {
	castButton.classList.add('active');
	crewButton.classList.remove('active');
	castSection.style.display = 'block';
	crewSection.style.display = 'none';
});

crewButton.addEventListener('click', function() {
	crewButton.classList.add('active');
	castButton.classList.remove('active');
	crewSection.style.display = 'block';
	castSection.style.display = 'none';
});

//백드롭-포스터 탭전환
const backdropButton = document.getElementById('backdropButton');
const posterButton = document.getElementById('posterButton');
const backdropSection = document.getElementById('backdropSection');
const posterSection = document.getElementById('posterSection');

backdropButton.addEventListener('click', function() {
	backdropButton.classList.add('active');
	posterButton.classList.remove('active');
	backdropSection.style.display = 'block';
	posterSection.style.display = 'none';
});

posterButton.addEventListener('click', function() {
	posterButton.classList.add('active');
	backdropButton.classList.remove('active');
	posterSection.style.display = 'block';
	backdropSection.style.display = 'none';
});

//리뷰 좋아요/싫어요
const likeButton = document.getElementById('likeButton');
const dislikeButton = document.getElementById('dislikeButton');
const likeString = document.querySelector('.likeString');
const reviewSection = document.getElementById('reviewSection');
const reviewText = document.getElementById('reviewText');
const submitReview = document.getElementById('submitReview');

// URL에서 movieId 추출
const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);
const movieId = urlParams.get("movieId");
let currentRating = null;

// 좋아요/싫어요 버튼 클릭 처리
function handleRatingClick(rating, activeButton, inactiveButton) {
	if (currentRating === rating) {
		// 같은 버튼을 다시 클릭하면 선택 취소
		activeButton.classList.remove('active');
		inactiveButton.classList.remove('active');
		currentRating = null;
		likeString.innerHTML = `
				<p>회원님은 아무것도 선택하지 않았습니다.</p>
				`;
	} else {
		likeString.innerHTML = `
				<p>회원님은 ${rating}을 선택하셨습니다.</p>
				`;
		// 새로운 선택
		activeButton.classList.add('active');
		inactiveButton.classList.remove('active');
		currentRating = rating;

	}
	try {
		const newForm = new FormData();
		newForm.append("movieId", movieId);
		newForm.append("rating", currentRating);

		const response = fetch('/board/movieDetail/likedislike', {
			method: 'POST',
			body: newForm
		});

		if (response.message == "like") {
			alert('좋아요가 등록되었습니다.');
			location.reload(); // 페이지 새로고침
		} else if (response.message == "dislike") {
			alert('싫어요가 등록되었습니다.');
			location.reload(); // 페이지 새로고침
		}
	} catch (error) {
		alert(error.message);
	}

}

likeButton.addEventListener('click', () => {
	handleRatingClick('like', likeButton, dislikeButton);
});

dislikeButton.addEventListener('click', () => {
	handleRatingClick('dislike', dislikeButton, likeButton);
});

// 리뷰 제출
submitReview.addEventListener('click', async () => {

	if (!reviewText.value.trim()) {
		alert('리뷰 내용을 입력해주세요.');
		return;
	}

	try {
		const newForm = new FormData();
		newForm.append("movieId",movieId);
		newForm.append("content",reviewText.value);
		
		const response = await fetch('/board/movieDetail/reviews', {
			method: 'POST',
			body: newForm
			});

		if (response.ok) {
			alert('리뷰가 등록되었습니다.');
			location.reload(); // 페이지 새로고침
		} else {
			throw new Error('리뷰 등록에 실패했습니다.');
		}
	} catch (error) {
		alert(error.message);
	}
});
