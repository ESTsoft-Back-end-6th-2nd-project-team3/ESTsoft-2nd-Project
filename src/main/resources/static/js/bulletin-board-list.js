function sortPosts(sortType) {
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

function searchUserPosts() {
    const searchType = document.querySelector('.admin-controls select').value;
    const searchQuery = document.querySelector('.admin-controls input[type="text"]').value;

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

function searchAdminPosts() {
    const searchType = document.querySelector('select[name="searchType"]').value;
    const postType = document.querySelector('select[name="post_type"]').value;
    const targetId = document.querySelector('select[name="target_id"]').value;
    const isActive = document.querySelector('select[name="isActive"]').value;
    const searchQuery = document.querySelector('input[name="searchQuery"]').value;

    const url = new URL(window.location.href);
    url.searchParams.set("searchType", searchType);
    url.searchParams.set("postType", postType);
    url.searchParams.set("targetId", targetId);
    url.searchParams.set("isActive", isActive);
    url.searchParams.set("searchQuery", searchQuery);

    window.location.href = url.toString();
}



