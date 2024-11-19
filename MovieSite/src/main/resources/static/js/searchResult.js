function searchMovies(page = 1) {
    const keyword = document.getElementById("searchKeyword").value;
    if (!keyword.trim()) {
        alert("검색어를 입력하세요");
        return;
    }

    const limit = 5; // 한 번에 요청할 영화 수를 늘립니다.

    fetch('/board/search/movies', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `keyword=${encodeURIComponent(keyword)}&page=${page}&limit=${limit}`
    })
    .then(response => response.json())
    .then(movies => {
        const resultsDiv = document.getElementById('searchResult');
        if (page === 1) {
            resultsDiv.innerHTML = ''; // 첫 페이지일 경우에만 초기화
        }
        movies.forEach(movie => {
            const card = `
            <div class="movie-card">
                <img src="${movie.poster_path ? 'https://image.tmdb.org/t/p/w200' + movie.poster_path : '/images/blankPoster.jpg'}" 
                    alt="${movie.title}">
                <h3>${movie.title}</h3>
                <p>${movie.release_date || '개봉일 미정'}</p>
            </div>
            `;
            resultsDiv.innerHTML += card;
        });

        // 더 많은 결과가 있을 경우 "더 보기" 버튼 추가
        if (movies.length === limit) {
            const loadMoreButton = document.createElement('button');
            loadMoreButton.textContent = '더 보기';
            loadMoreButton.onclick = () => searchMovies(page + 1);
            resultsDiv.appendChild(loadMoreButton);
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}