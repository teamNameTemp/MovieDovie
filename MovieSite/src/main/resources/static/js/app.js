
document.addEventListener("DOMContentLoaded", function() {
	const genreButtons = document.querySelectorAll('.tab-button');
	const genreContents = document.querySelectorAll('.genre-content');

	// 탭 전환 기능
	genreButtons.forEach((button, index) => {
		button.addEventListener('click', () => {
			// 모든 버튼에서 active 클래스 제거
			genreButtons.forEach(btn => btn.classList.remove('active'));
			// 클릭된 버튼에 active 클래스 추가
			button.classList.add('active');

			// 모든 콘텐츠 숨기기
			genreContents.forEach(content => content.classList.remove('active'));
			// 선택된 장르의 콘텐츠 표시
			genreContents[index].classList.add('active');
		});
	});

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
		console.log(itemCount);
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
			if (currentIndex < itemCount - 12) {
				currentIndex += 6;
			}
			else if (currentIndex == itemCount - 11) {
				currentIndex += 5;
			}

			else if (currentIndex == itemCount - 10) {
				currentIndex += 4;
			}

			else if (currentIndex == itemCount - 9) {
				currentIndex += 3;

			}

			else if (currentIndex == itemCount - 8) {
				currentIndex += 2;
			}

			else if (currentIndex == itemCount - 7) {
				currentIndex++;

			} else {
				currentIndex = 0;  // 처음으로 돌아가기
			}
			const moveAmount = currentIndex * totalItemWidth;
			movieList.style.transform = `translateX(-${moveAmount}px)`;
		};

		// 왼쪽 화살표 클릭 이벤트
		arrowsLeft[index].addEventListener("click", slideLeft);

		// 오른쪽 화살표 클릭 이벤트
		arrowsRight[index].addEventListener("click", slideRight);


	});

});



