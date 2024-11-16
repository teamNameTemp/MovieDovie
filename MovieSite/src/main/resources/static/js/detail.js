document.addEventListener('DOMContentLoaded', () => {
	
	// URL에서 movieId 추출
	const queryString = window.location.search;
	const urlParams = new URLSearchParams(queryString);
	const movieId = urlParams.get("movieId");

	// 리뷰 좋아요/싫어요
	const buttons = document.querySelectorAll('.action-btn');

	buttons.forEach(button => {
	    button.addEventListener('click', () => {
	        let isLike = "N";
	        let action = "add"; // 기본 액션은 추가

	        if (button.classList.contains('like')) {
	            isLike = "Y";
	        } else if (button.classList.contains('dislike')) {
	            isLike = "N";
	        } else {
	            // 좋아요/싫어요 버튼이 아닌 경우 처리
	            return;
	        }

	        // 버튼이 이미 active 상태인 경우, 액션을 'remove'로 설정
	        if (button.classList.contains('active')) {
	            action = "remove";
	        }

	        const likeForm = new FormData();
	        likeForm.append('isLike', isLike);
	        likeForm.append('movieId', movieId);
	        likeForm.append('action', action);
	        
	        fetch('/board/movieDetail/likes', {
	            method: 'POST',
	            body: likeForm
	        })
	        .then(response => response.json())
	        .then(result => {
	            if (result.message === "good") {
	                if (action === "add") {
	                    alert(isLike === 'Y' ? '좋아요가 저장되었습니다!' : '싫어요가 저장되었습니다!');
	                } else {
	                    alert(isLike === 'Y' ? '좋아요가 취소되었습니다!' : '싫어요가 취소되었습니다!');
	                }
	                updateUI(button, result, action);
	            } else {
	                alert('처리 중 오류가 발생했습니다: ' + (result.error || '알 수 없는 오류'));
	            }
	        })
	        .catch(error => {
	            console.error('Error:', error);
	            alert('요청 처리 중 오류가 발생했습니다.');
	        });
	    });
	});

	function updateUI(button, result, action) {
	    if (button) {
	        if (action === "add") {
	            button.classList.add('active');
	        } else {
	            button.classList.remove('active');
	        }
	    }

	    // 상호 배타적인 버튼 처리 (좋아요/싫어요)
	    if (button.classList.contains('like')) {
	        const dislikeButton = document.querySelector('.dislike');
	        if (dislikeButton) dislikeButton.classList.remove('active');
	    } else if (button.classList.contains('dislike')) {
	        const likeButton = document.querySelector('.like');
	        if (likeButton) likeButton.classList.remove('active');
	    }

	    // 카운트 업데이트 로직 (서버에서 새 카운트를 반환한다고 가정)
	    if (result.likeCount !== undefined) {
	        const likeCountElement = document.querySelector('.like .count');
	        if (likeCountElement) likeCountElement.textContent = result.likeCount;
	    }
	    if (result.dislikeCount !== undefined) {
	        const dislikeCountElement = document.querySelector('.dislike .count');
	        if (dislikeCountElement) dislikeCountElement.textContent = result.dislikeCount;
	    }
	}
	
	//리뷰 관련
	const reviewText = document.getElementById('reviewText');
	 const submitReview = document.getElementById('submitReview');
	 const reviewList = document.getElementById('reviewList');

	 submitReview.addEventListener('click', () => {
	     const review = reviewText.value.trim();
	     
	     if (review === '') {
	         alert('리뷰 내용을 입력해주세요.');
	         return;
	     }

	     // 새 리뷰 요소 생성
	     const reviewElement = document.createElement('div');
	     reviewElement.className = 'review-item';
	     
	     // 현재 시간 포맷팅
	     const now = new Date();
	     const formattedDate = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')} ${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`;

	     // 리뷰 내용 설정
	     reviewElement.innerHTML = `
	         <p class="review-content">${escapeHTML(review)}</p>
	         <p class="review-date">${formattedDate}</p>
	     `;

	     // 리뷰 목록에 새 리뷰 추가
	     reviewList.insertBefore(reviewElement, reviewList.firstChild);

	     // 입력 필드 초기화
	     reviewText.value = '';
	 });

	 // HTML 이스케이프 함수
	 function escapeHTML(str) {
	     return str.replace(/[&<>'"]/g, 
	         tag => ({
	             '&': '&amp;',
	             '<': '&lt;',
	             '>': '&gt;',
	             "'": '&#39;',
	             '"': '&quot;'
	         }[tag] || tag)
	     );
	 }
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



const reviewSection = document.getElementById('reviewSection');
const reviewText = document.getElementById('reviewText');
const submitReview = document.getElementById('submitReview');

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
