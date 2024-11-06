function checkNickname() {
    const nickname = document.getElementById('nickname').value;

    fetch(`/api/nickname-check?nickname=${encodeURIComponent(nickname)}`)
        .then(response => response.json())
        .then(data => {
            if (data.isAvailable) {
                alert("사용 가능한 닉네임입니다.");
            } else {
                alert("이미 사용 중인 닉네임입니다.");
            }
        })
        .catch(error => console.error('Error:', error));
}

function saveProfile() {
    const updatedData = {
        nickname: document.getElementById('nickname').value,
        selfIntro: document.getElementById('selfIntro').value,
        snsLink: document.getElementById('snsLink').value
    };

    fetch(`/mypage/${userId}/userinfo`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('프로필이 성공적으로 업데이트되었습니다!');
                location.reload(); // 페이지 새로고침
            } else {
                alert('업데이트에 실패했습니다.');
            }
        })
        .catch(error => console.error('Error:', error));
}

function cancelEdit() {
    location.reload(); // 페이지를 새로고침하여 변경사항을 취소
}
