JS
document.addEventListener("DOMContentLoaded", function () {
  const movieList = document.querySelector(".movie-list");  // .movie-list 요소
  const leftArrow = document.querySelector(".fa-chevron-left");  // 왼쪽 화살표
  const rightArrow = document.querySelector(".fa-chevron-right");  // 오른쪽 화살표
  const itemWidth = 270;  // 각 이미지의 너비
  const spaceBetweenItems = 30;  // 각 항목 사이의 간격
  const totalItemWidth = itemWidth + spaceBetweenItems;  // 항목 하나의 전체 너비
  const itemCount = movieList.querySelectorAll(".movie-list-item").length;  // 총 항목 개수

  let currentIndex = 0;  // 현재 보이는 첫 번째 항목 인덱스

  // 슬라이드 이동 함수 (왼쪽으로)
  const slideLeft = () => {
    if (currentIndex > 0) {
      currentIndex--;
      const moveAmount = currentIndex * totalItemWidth;
      console.log(currentIndex + " : " + moveAmount + " left");
      movieList.style.transform = `translateX(-${moveAmount}px)`;
    }
  };

  // 슬라이드 이동 함수 (오른쪽으로)
  const slideRight = () => {
   {
      //const moveAmount = currentIndex * totalItemWidth;
      
      
      //계속이동
      if(currentIndex < itemCount - 12){
    	 	currentIndex++;
    	 	currentIndex++;
    	 	currentIndex++;
    	 	currentIndex++;
    	 	currentIndex++;
    	 	currentIndex++;
		}
		else if(currentIndex == itemCount - 11) {
			currentIndex++;
    	 	currentIndex++;
    	 	currentIndex++;
    	 	currentIndex++;
    	 	currentIndex++;
		}
		
		else if(currentIndex == itemCount - 10) {
			currentIndex++;
    	 	currentIndex++;
    	 	currentIndex++;
    	 	currentIndex++;
		}
		
		else if(currentIndex == itemCount - 9) {
			currentIndex++;
    	 	currentIndex++;
    	 	currentIndex++;
    	 	
		}
		
		else if(currentIndex == itemCount - 8) {
			currentIndex++;
    	 	currentIndex++;
		}
		
		else if(currentIndex == itemCount - 7) {
			currentIndex++;
    	 	
		}
		else{ //처음으로 돌아가기
      		currentIndex = 0;  // 카운터 초기화
		}
		const moveAmount = currentIndex * totalItemWidth;
		movieList.style.transform = `translateX(-${moveAmount}px)`;
    }
  };

  // 왼쪽 화살표 클릭 이벤트
  leftArrow.addEventListener("click", slideLeft);

  // 오른쪽 화살표 클릭 이벤트
  rightArrow.addEventListener("click", slideRight);
});