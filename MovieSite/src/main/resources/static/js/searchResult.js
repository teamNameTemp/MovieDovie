// 검색 기능 구현
document.addEventListener("DOMContentLoaded", function() {
    // 검색 폼 제출 이벤트 리스너
    const searchForm = document.querySelector('.search-box form');
    const searchBtn = document.querySelector('.search-btn');
    const searchInput = document.querySelector('.search-txt');

    // 검색 실행 함수
    const searchKeyword = () => {
        const keyword = document.querySelector('#searchKeyword').value;

        if (!keyword.trim()) {
            alert('검색어를 입력해주세요.');
            return;
        }

        window.location.href = `/searchResult?keyword=${encodeURIComponent(keyword)}`;
    };

    // 폼 제출 이벤트
    if (searchForm) {
        searchForm.addEventListener('submit', function(e) {
            e.preventDefault();
            searchKeyword();
        });
    }

    // 검색 버튼 클릭 이벤트
    if (searchBtn) {
        searchBtn.addEventListener('click', function(e) {
            e.preventDefault();
            searchKeyword();
        });
    }

    // 엔터 키 이벤트
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                searchKeyword();
            }
        });
    }
});