

function searchMovies() {
    const keyword = document.getElementById("searchKeyword").value;
    if (!keyword.trim()) {
        alert("검색어를 입력하세요");
        return;
    }

    fetch('/board/search/movies', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `keyword=${encodeURIComponent(keyword)}`
    })
    .then(response => response.json())
    .then(movies => {
        const resultsDiv = document.getElementById('searchResult');
        resultsDiv.innerHTML = '';
        //console.log(movies);
        movies.forEach(movie => {
            const card = `
            <div class ="movie-card">
                <img src="${movie.poster_path ? 'https://image.tmdb.org/t/p/w200' + movie.poster_path : '/images/blankPoster.jpg'}" 
                    alt="${movie.title}">
                <h3>${movie.title}</h3>
                <p>${movie.release_date || '개봉일 미정'}</p>
                </div>
            `;
            resultsDiv.innerHTML += card;// +=를 사용해서 검색 결과를 하나씩 추가
        });
    })
    .catch(error => {
        console.error('Error:', error); // 오류 발생 시 콘솔에 출력
    });
}
