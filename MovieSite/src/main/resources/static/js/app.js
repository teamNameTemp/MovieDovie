@ -1,107 +1,94 @@

document.addEventListener("DOMContentLoaded", function() {
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



		window.onload = function() {
			// 갯수 업데이트 함수
			function updateNotificationCount() {
				const dropdownItems = document.querySelectorAll('.dropdown-content .dropdown-item'); // 드롭다운 항목들을 다시 선택
				const notificationCount = document.getElementById('notification-count'); // 갯수를 표시할 요소
				let itemCount = dropdownItems.length; // 항목 수를 가져옴

				// 항목 수가 99 이상일 경우 99+로 표시
				if (itemCount >= 5) {
					notificationCount.textContent = '4+';
				} else {
					notificationCount.textContent = itemCount;
				}
			}

			// 각 드롭다운 항목에 클릭 이벤트 추가
			const dropdownItems = document.querySelectorAll('.dropdown-content .dropdown-item');
			dropdownItems.forEach(item => {
				item.addEventListener('click', function(event) {
					event.preventDefault(); // 기본 동작 방지
					item.remove(); // 클릭한 항목 삭제
					updateNotificationCount(); // 항목 갯수 업데이트
				});
			});

			// 초기 항목 갯수 표시
			updateNotificationCount();
		}
<<<<<<< HEAD
=======
		else if(currentIndex == itemCount - 11) {
			currentIndex+=5;
		}
		
		else if(currentIndex == itemCount - 10) {
			currentIndex+=4;
		}
		
		else if(currentIndex == itemCount - 9) {
			currentIndex+=3;
    	 	
		}
		
		else if(currentIndex == itemCount - 8) {
			currentIndex+=2;
		}
		
		else if(currentIndex == itemCount - 7) {
			currentIndex++;
    	 	
		} else {
          currentIndex = 0;  // 처음으로 돌아가기
        }
        const moveAmount = currentIndex * totalItemWidth;
        movieList.style.transform = `translateX(-${moveAmount}px)`;
      };
>>>>>>> bbkim

		// 왼쪽 화살표 클릭 이벤트
		arrowsLeft[index].addEventListener("click", slideLeft);

		// 오른쪽 화살표 클릭 이벤트
		arrowsRight[index].addEventListener("click", slideRight);
	});
});



