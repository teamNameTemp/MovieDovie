document.addEventListener('DOMContentLoaded', () => {
	
	// URL에서 movieId 추출
	const queryString = window.location.search;
	const urlParams = new URLSearchParams(queryString);
	const movieId = urlParams.get("movieId");

	// 모든 액션 버튼에 대한 이벤트 리스너 설정
	const buttons = document.querySelectorAll('.action-btn');

	buttons.forEach(button => {
	    button.addEventListener('click', () => {
	        let action = "add"; // 기본 액션은 추가
	        let endpoint = "";
	        let formData = new FormData();

	        if (button.classList.contains('active')) {
	            action = "remove";
	        }

	        formData.append('movieId', movieId);
	        formData.append('action', action);

	        if (button.classList.contains('like') || button.classList.contains('dislike')) {
	            endpoint = '/board/movieDetail/likes';
	            formData.append('isLike', button.classList.contains('like') ? "Y" : "N");
	        } else if (button.classList.contains('watched')) {
	            endpoint = '/board/movieDetail/watched';
	        } else if (button.classList.contains('bookmark')) {
	            endpoint = '/board/movieDetail/bookmark';
	        } else {
	            return; // 알 수 없는 버튼 유형
	        }

	        fetch(endpoint, {
	            method: 'POST',
	            body: formData
	        })
	        .then(response => response.json())
	        .then(result => {
	            if (result.message === "good") {
	                updateUI(button, result, action);
	                showActionMessage(button, action);
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
	    if (button.classList.contains('like') || button.classList.contains('dislike')) {
	        const likeButton = document.querySelector('.like');
	        const dislikeButton = document.querySelector('.dislike');

				        
			// 좋아요/싫어요 버튼 상태 업데이트
			if (action === "remove") {
			    button.classList.remove('active');
			} else if (result.userLike === 'Y') {
			    likeButton.classList.add('active');
			    dislikeButton.classList.remove('active');
			} else if (result.userLike === 'N') {
			    dislikeButton.classList.add('active');
			    likeButton.classList.remove('active');
			} else {
			    // 중립 상태 (둘 다 선택되지 않음)
			    likeButton.classList.remove('active');
			    dislikeButton.classList.remove('active');
			}

	        // 좋아요/싫어요 카운트 업데이트
	        updateCount(likeButton, result.likeCount);
	        updateCount(dislikeButton, result.dislikeCount);
	    } else {
	        // 봤어요와 북마크 버튼 처리
			if (action === "remove") {
				button.classList.remove('active');
			} else{
				button.classList.toggle('active');
			} 
			
//	        button.classList.toggle('active', action === "add");
	        updateCount(button, result.count);
	    }
	}

	function updateCount(button, count) {
	    const countElement = button.querySelector('.count');
	    if (countElement && count !== undefined) {
	        countElement.textContent = count;
	    }
	}

	function showActionMessage(button, action) {
	    let message = '';
	    if (button.classList.contains('like')) {
	        message = action === 'add' ? '좋아요가 저장되었습니다!' : '좋아요가 취소되었습니다!';
	    } else if (button.classList.contains('dislike')) {
	        message = action === 'add' ? '싫어요가 저장되었습니다!' : '싫어요가 취소되었습니다!';
	    } else if (button.classList.contains('watched')) {
	        message = action === 'add' ? '시청 완료로 표시되었습니다!' : '시청 완료 표시가 취소되었습니다!';
	    } else if (button.classList.contains('bookmark')) {
	        message = action === 'add' ? '북마크에 추가되었습니다!' : '북마크에서 제거되었습니다!';
	    }
	    alert(message);
	}

	function showActionMessage(button, action) {
	    let message = '';
	    if (button.classList.contains('like')) {
	        message = action === 'add' ? '좋아요가 저장되었습니다!' : '좋아요가 취소되었습니다!';
	    } else if (button.classList.contains('dislike')) {
	        message = action === 'add' ? '싫어요가 저장되었습니다!' : '싫어요가 취소되었습니다!';
	    } else if (button.classList.contains('watched')) {
	        message = action === 'add' ? '시청 완료로 표시되었습니다!' : '시청 완료 표시가 취소되었습니다!';
	    } else if (button.classList.contains('bookmark')) {
	        message = action === 'add' ? '북마크에 추가되었습니다!' : '북마크에서 제거되었습니다!';
	    }
	    alert(message);
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

// URL에서 movieId 추출
	const queryString = window.location.search;
	const urlParams = new URLSearchParams(queryString);
	const movieId = urlParams.get("movieId");

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
