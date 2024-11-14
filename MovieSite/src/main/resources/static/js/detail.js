document.addEventListener("DOMContentLoaded", function () {
    // 공통 변수 설정
    const movieLists = document.querySelectorAll(".movie-list");  // 모든 .movie-list 요소 선택
    const arrowsLeft = document.querySelectorAll(".fa-chevron-left.arrow");  // 왼쪽 화살표
    const arrowsRight = document.querySelectorAll(".fa-chevron-right.arrow");  // 오른쪽 화살표

    // 각 영화 리스트마다 슬라이드 기능 구현
    movieLists.forEach((movieList, index) => {
      const itemWidth = 270;  // 각 아이템의 너비
      const spaceBetweenItems = 30;  // 각 아이템 간의 간격
      const totalItemWidth = itemWidth + spaceBetweenItems;  // 하나의 아이템 전체 너비
      const itemCount = movieList.querySelectorAll(".movie-list-item").length;  // 아이템 개수
      let currentIndex = 0;  // 현재 보여지는 첫 번째 아이템 인덱스

      // 슬라이드 이동 함수 (왼쪽으로)
      const slideLeft = () => {
        if (currentIndex > 0) {
          currentIndex--;
        } 
        const moveAmount = currentIndex * totalItemWidth;
        movieList.style.transform = `translateX(-${moveAmount}px)`;
      };

      // 슬라이드 이동 함수 (오른쪽으로)
      const slideRight = () => {
        if (currentIndex < itemCount - 1) {
            currentIndex++;
        } else {
            currentIndex = 0; // 처음으로 돌아가기
        }
        const moveAmount = currentIndex * totalItemWidth;
        movieList.style.transform = `translateX(-${moveAmount}px)`;
      };

    //   // 왼쪽 화살표 클릭 이벤트
    //   arrowsLeft[index].addEventListener("click", slideLeft);

    //   // 오른쪽 화살표 클릭 이벤트
    //   arrowsRight[index].addEventListener("click", slideRight);
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
    const reviewSection = document.getElementById('reviewSection');
    const reviewText = document.getElementById('reviewText');
    const submitReview = document.getElementById('submitReview');

    let currentRating = null;

    // 좋아요/싫어요 버튼 클릭 처리
    function handleRatingClick(rating, activeButton, inactiveButton) {
        if (currentRating === rating) {
            // 같은 버튼을 다시 클릭하면 선택 취소
            activeButton.classList.remove('active');
            currentRating = null;
            reviewSection.classList.remove('show');
        } else {
            // 새로운 선택
            activeButton.classList.add('active');
            inactiveButton.classList.remove('active');
            currentRating = rating;
            reviewSection.classList.add('show');
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
        if (!currentRating) {
            alert('좋아요 또는 싫어요를 선택해주세요.');
            return;
        }

        if (!reviewText.value.trim()) {
            alert('리뷰 내용을 입력해주세요.');
            return;
        }

        try {
            const response = await fetch('/api/reviews', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    movieId: movieId, // 영화 ID는 페이지에서 가져와야 함
                    rating: currentRating,
                    content: reviewText.value
                })
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
});