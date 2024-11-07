function enableEdit() {
    // 수정 가능한 입력 필드 생성
    document.querySelector('.profile-info').innerHTML = `
        <label>닉네임: <input type="text" id="nickname" value="${nickname}"></label><br>
        <label>등급: <input type="text" id="level" value="${level}"></label><br>
        <label>한줄 소개: <input type="text" id="selfIntro" value="${selfIntro}"></label><br>
        <label>링크: <input type="text" id="snsLink" value="${snsLink}"></label><br>
        <button onclick="submitEdit()">저장</button>
    `;
}

function submitEdit() {
    // 입력된 값 가져오기
    const updatedData = {
        nickname: document.getElementById('nickname').value,
        level: document.getElementById('level').value,
        selfIntro: document.getElementById('selfIntro').value,
        snsLink: document.getElementById('snsLink').value
    };

    // 수정된 데이터 서버로 전송 (예: PUT 요청)
    fetch(`/mypage/${userId}/userinfo`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedData)
    }).then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('프로필이 성공적으로 업데이트되었습니다.');
                location.reload(); // 페이지 새로고침
            } else {
                alert('업데이트에 실패했습니다.');
            }
        });
}
