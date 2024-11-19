document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('likeButton').onclick = function() {
        document.getElementById('reviewSection').style.display = 'block';
    };
    document.getElementById('dislikeButton').onclick = function() {
        document.getElementById('reviewSection').style.display = 'block';
    };
    document.getElementById('submitReview').onclick = function() {
        const review = document.getElementById('reviewText').value;
        // 여기에 감상평을 제출하는 로직을 추가하세요.
        alert('감상평이 제출되었습니다: ' + review);
        document.getElementById('reviewText').value = ''; // 입력창 초기화
        document.getElementById('reviewSection').style.display = 'none'; // 섹션 숨기기
    };
}); 