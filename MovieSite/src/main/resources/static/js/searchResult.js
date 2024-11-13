let viewType = 'list'; // 기본 보기 형태

function sortResults(criteria) {
    // 모든 탭 버튼에서 active 클래스를 제거
    const buttons = document.querySelectorAll('.tab-button');
    buttons.forEach(button => {
        button.classList.remove('active');
    });

    // 클릭한 버튼에 active 클래스 추가
    const activeButton = document.querySelector(`button[onclick="sortResults('${criteria}')"]`);
    activeButton.classList.add('active');

    // 정렬 로직 구현
    // 예: AJAX 요청으로 서버에 정렬 기준을 전달하고 결과를 업데이트
}

function toggleView(type) {
    viewType = type;
    const results = document.getElementById('results');
    if (type === 'album') {
        results.classList.add('album-view');
        results.classList.remove('list-view');
    } else {
        results.classList.add('list-view');
        results.classList.remove('album-view');
    }
}
