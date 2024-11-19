
document.addEventListener("DOMContentLoaded", function() {
	const genreButtons = document.querySelectorAll('.tab-button');
	const genreContents = document.querySelectorAll('.genre-content');
	//왼오 스크롤
	const favArrowLeft = document.querySelector('.fa-chevron-left');
	const favArrowRight = document.querySelector('.fa-chevron-right');

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
		let currentIndex = 0;  // 현재 보여지는 첫 번째 아이템 인덱스

		// 슬라이드 이동 함수 (왼쪽으로)
		const slideLeft = () => {
			if (currentIndex > 0) {
				currentIndex--;
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

				// 항목 수가 4 이상일 경우 4+로 표시
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

	// 초기 상태 설정
	updateVisibility();
});

  // 초기 항목 갯수 표시
  updateNotificationCount();

  // 드롭다운 메뉴 클릭
  const dropbtnClick = document.querySelector('.dropbtn_click');
  if (dropbtnClick) {
    dropbtnClick.onclick = () => {
      dropdown();
    };
  }

//TOGGLE

const ball = document.querySelector(".toggle-ball");
const items = document.querySelectorAll(
	".container,.movie-list-title,.navbar-container,.sidebar,.left-menu-icon,.toggle"
);

ball.addEventListener("click", () => {
	items.forEach((item) => {
		item.classList.toggle("active");
	});
	ball.classList.toggle("active");
});


//dropdown
window.onload = () => {
	document.querySelector('.dropbtn_click').onclick = () => {
		dropdown();
	}
	document.getElementsByClassName('fastfood').onclick = () => {
		showMenu(value);
	};
	dropdown = () => {
		var v = document.querySelector('.dropdown-content');
		var dropbtn = document.querySelector('.dropbtn')
		v.classList.toggle('show');
		dropbtn.style.borderColor = 'rgb(94, 94, 94)';
	}

	showMenu = (value) => {
		var dropbtn_icon = document.querySelector('.dropbtn_icon');
		var dropbtn_content = document.querySelector('.dropbtn_content');
		var dropbtn_click = document.querySelector('.dropbtn_click');
		var dropbtn = document.querySelector('.dropbtn');

		dropbtn_icon.innerText = '';
		dropbtn_content.innerText = value;
		dropbtn_content.style.color = '#252525';
		dropbtn.style.borderColor = '#3992a8';
	}
}
window.onclick = (e) => {
	if (!e.target.matches('.dropbtn_click')) {
		var dropdowns = document.getElementsByClassName("dropdown-content");

		var dropbtn_icon = document.querySelector('.dropbtn_icon');
		var dropbtn_content = document.querySelector('.dropbtn_content');
		var dropbtn_click = document.querySelector('.dropbtn_click');
		var dropbtn = document.querySelector('.dropbtn');

		var i;
		for (i = 0; i < dropdowns.length; i++) {
			var openDropdown = dropdowns[i];
			if (openDropdown.classList.contains('show')) {
				openDropdown.classList.remove('show');
			}
		}
	}
}
