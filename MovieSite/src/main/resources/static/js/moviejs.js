const inquiryDate = getYesterdayDate();
document.getElementById('targetDate').textContent = inquiryDate;
let searchDate;
//==========================================================================
document.addEventListener('DOMContentLoaded', function() {
    let targetDt = document.getElementById('targetDt');
    function getFormatDate() {
        const date = new Date(targetDt.value);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        
        const formatDate = `${year}${month}${day}`;
        document.getElementById('targetDate').textContent = formatDate;
		searchDate = formatDate;
        return formatDate;
    }
    
    targetDt.addEventListener('change', getFormatDate);
});

//==========================================================================
const form = document.querySelector('form');
form.addEventListener('submit', async function (event) {
    event.preventDefault();
    const KOBIS_API_KEY = document.getElementById('kobisApiKey').value;
    const TMDB_API_KEY = document.getElementById('tmdbApiKey').value;

    if (KOBIS_API_KEY && TMDB_API_KEY) {
        try {
            form.querySelector('button[type="submit"]').disabled = true;
            resetResult();
            await display(KOBIS_API_KEY, TMDB_API_KEY);
        } catch (error) {
            console.error('Display Error: ', error);
            alert('영화 정보를 표시 중 오류가 발생하였습니다.')
            form.querySelector('button[type="submit"]').disabled = false;
        }
    } else {
        // If any API key is missing, display an alert
        form.querySelector('button[type="submit"]').disabled = false;
        alert("영화진흥위원회 API Key와 TMDB API Key를 확인해 주십시오.");
    }
});

async function display(KOBIS_API_KEY, TMDB_API_KEY) {
    const boxOfficeArray = await getBoxOffice(KOBIS_API_KEY);
    const movieNameEngArray = await getMovieInfo(boxOfficeArray, KOBIS_API_KEY);
    const posterLinkArray = await getPosterLink(movieNameEngArray, TMDB_API_KEY);
    

    const posterImg = document.querySelector('#posterImg');
    posterLinkArray.forEach(movie => {
        const li = document.createElement('li');
        const img = document.createElement('img');
        img.src = movie.posterLink;
        li.appendChild(img);
        posterImg.appendChild(li);
    });
}

async function getBoxOffice(KOBIS_API_KEY) {
    try {
        const response = await fetch(`http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=${KOBIS_API_KEY}&targetDt=${searchDate}`);

        if (!response.ok) {
            throw new Error(`KOBIS Box Office HTTP Status: ${response.status}`);
        }

        const responseData = await response.json();
        const boxOffice = responseData.boxOfficeResult.dailyBoxOfficeList;
        const boxOfficeArray = [];

        boxOffice.forEach(movie => {
            boxOfficeArray.push({
                rank: movie.rank,
                title: movie.movieNm,
                code: movie.movieCd
            });
        });

        const tbody = document.querySelector('#boxOffice tbody');

        boxOfficeArray.forEach(movie => {
            const tr = document.createElement('tr');

            const rankTd = document.createElement('td');
            rankTd.textContent = movie.rank;
            tr.appendChild(rankTd);

            const titleTd = document.createElement('td');
            titleTd.textContent = movie.title;
            tr.appendChild(titleTd);

            const codeTd = document.createElement('td');
            codeTd.textContent = movie.code;
            tr.appendChild(codeTd);

            tbody.appendChild(tr);
        });

        return boxOfficeArray;

    } catch (error) {
        form.querySelector('button[type="submit"]').disabled = false;
        console.error('KOBIS Box Office Fetching Error: ', error);
        alert('KOBIS Box Office API 요청 중 오류가 발생하였습니다.');
    }
}


async function getMovieInfo(boxOfficeArray, KOBIS_API_KEY) {
    const promise = boxOfficeArray.map(async movie => {
        const code = movie.code;

        try {
            const response = await fetch(`http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=${KOBIS_API_KEY}&movieCd=${code}`);

            if (!response.ok) {
                throw new Error(`KOBIS Movie Info HTTP Status: ${response.status}`);
            }

            const responseData = await response.json();
            const movieInfo = responseData.movieInfoResult.movieInfo;

            const tbody = document.querySelector('#movieInfo tbody');
            const tr = document.createElement('tr');

            const codeTd = document.createElement('td');
            codeTd.textContent = movie.code;
            tr.appendChild(codeTd);

            const nameEngTd = document.createElement('td');
            nameEngTd.textContent = movieInfo.movieNmEn;
            tr.appendChild(nameEngTd);

            tbody.appendChild(tr);

            return {
                code: movie.code,
                nameEng: movieInfo.movieNmEn
            };

        } catch (error) {
            console.error('KOBIS Movie Info Fetching Error: ', error);
            alert('KOBIS Movie Info 요청 중 오류가 발생하였습니다.');
        }
    });

    try {
        return await Promise.all(promise);
    } catch (error) {
        form.querySelector('button[type="submit"]').disabled = false;
        console.error('KOBIS Movie Info Fetching Error: ', error);
        alert('KOBIS Movie Info 요청 중 오류가 발생하였습니다.');
    }
}

async function getPosterLink(movieNameEngArray, TMDB_API_KEY) {
    const promise = movieNameEngArray.map(async movie => {
        const nameEng = movie.nameEng;

        try {
            const options = {
                method: 'GET',
                headers: {
                    accept: 'application/json',
                    Authorization: `Bearer ${TMDB_API_KEY}`
                }
            };

            const response = await fetch(`https://api.themoviedb.org/3/search/movie?query=${nameEng}&language=ko`, options);

            if (!response.ok) {
                throw new Error(`TMDB Poster HTTP Status: ${response.status}`);
            }

            const responseData = await response.json();
            const posterPath = responseData.results[0].poster_path;
            const posterLink = posterPath ? `https://image.tmdb.org/t/p/original${posterPath}` : 'Failed to find the poster link';

            const tbody = document.querySelector('#posterLink tbody');
            const tr = document.createElement('tr');

            const nameEngTd = document.createElement('td');
            nameEngTd.textContent = nameEng;
            tr.appendChild(nameEngTd);

            const posterLinkTd = document.createElement('td');
            posterLinkTd.textContent = posterLink;
            tr.appendChild(posterLinkTd);

            tbody.appendChild(tr);

            return {
                nameEng: nameEng,
                posterLink: posterLink
            };


        } catch (error) {
            console.error('TMDB Poster Fetching Error: ', error);
            alert('TMDB Poster 요청 중 오류가 발생하였습니다.');
        }
    });

    try {
        form.querySelector('button[type="submit"]').disabled = false;
        return await Promise.all(promise);
    } catch (error) {
        form.querySelector('button[type="submit"]').disabled = false;
        console.error("TMDB Poster Fetching Error: ", error);
        alert('TMDB 포스터 링크 요청중 오류가 발생하였습니다.')
    }
}

function getYesterdayDate() {
    let date = new Date();
    date.setDate(date.getDate() - 1);
    let day = String(date.getDate()).padStart(2, '0');
    let month = String(date.getMonth() + 1).padStart(2, '0'); //January is 0!
    let year = date.getFullYear();

    return year + month + day;
}



function resetResult() {
    const tbodyArray = document.querySelectorAll('tbody');
    const posterImg = document.querySelector('#posterImg');
    tbodyArray.forEach(tbody => {
        tbody.innerHTML = '';
    });
    posterImg.innerHTML = '';
}

