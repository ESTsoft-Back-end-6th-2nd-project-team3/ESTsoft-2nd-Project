function sortPosts(sortType) {
    // 정렬 방식에 따라 URL을 변경하여 페이지 다시 로드
    const url = new URL(window.location.href);
    url.searchParams.set('sort', sortType);
    window.location.href = url.toString();
}

function changePage(direction) {
    const currentPage = parseInt(new URLSearchParams(window.location.search).get('page')) || 1;
    const totalPages = [[${totalPages}]];
    let newPage = currentPage;

    if (direction === 'prev' && currentPage > 1) {
        newPage = currentPage - 1;
    } else if (direction === 'next' && currentPage < totalPages) {
        newPage = currentPage + 1;
    }

    const url = new URL(window.location.href);
    url.searchParams.set('page', newPage);
    window.location.href = url.toString();
}


function searchPosts() {
    const searchType = document.getElementById('searchType').value;
    const searchQuery = document.getElementById('searchQuery').value;

    if (searchQuery.trim() === "") {
        alert("검색어를 입력하세요.");
        return;
    }

    const url = new URL(window.location.href);
    url.searchParams.set('searchType', searchType);
    url.searchParams.set('query', searchQuery);
    window.location.href = url.toString();
}

function goToWritePage() {
    // 글쓰기 페이지로 이동
    window.location.href = '/write';
}
