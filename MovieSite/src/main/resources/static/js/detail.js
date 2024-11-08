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

	//cast crew 버튼
	const castButton = document.getElementById('castButton');
    const crewButton = document.getElementById('crewButton');
    const castSection = document.getElementById('castSection');
    const crewSection = document.getElementById('crewSection');

    castButton.addEventListener('click', function() {
        castSection.style.display = 'block'; // 출연 섹션 표시
        crewSection.style.display = 'none'; // 제작 섹션 숨김
    });

    crewButton.addEventListener('click', function() {
        castSection.style.display = 'none'; // 출연 섹션 숨김
        crewSection.style.display = 'block'; // 제작 섹션 표시
    });
});