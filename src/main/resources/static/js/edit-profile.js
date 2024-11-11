function checkNickname() {
    const nickname = document.getElementById('nickname').value;

    fetch(`/api/nickname-check?nickname=${encodeURIComponent(nickname)}`)
        .then(response => response.json())
        .then(data => {
            console.log(data); // 응답을 콘솔에 출력하여 확인
            if (data.isAvailable) {
                alert("사용 가능한 닉네임입니다.");
            } else {
                alert("이미 사용 중인 닉네임입니다.");
            }
        })
        .catch(error => console.error('Error:', error));
}

function previewImage(event) {
    const reader = new FileReader();
    reader.onload = function () {
        const imagePreview = document.getElementById('imagePreview');
        imagePreview.style.backgroundImage = `url(${reader.result})`;
        imagePreview.style.backgroundSize = 'cover';
    };
    reader.readAsDataURL(event.target.files[0]);
}

function saveProfile() {
    const formData = new FormData();
    formData.append('nickname', document.getElementById('nickname').value);
    formData.append('selfIntro', document.getElementById('selfIntro').value);
    formData.append('snsLink', document.getElementById('snsLink').value);

    // 이미지 파일이 선택되었는지 확인
    const profileImage = document.getElementById('imageInput').files[0];
    if (profileImage) {
        formData.append('profileImage', profileImage);
    }

    fetch(`/mypage/${userId}/userinfo`, {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('프로필이 성공적으로 업데이트되었습니다!');
                window.location.href = `/mypage/${userId}`; // 마이페이지로 이동
            } else {
                alert('업데이트에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('요청 중 오류가 발생했습니다. 다시 시도해 주세요.');
        });
}

function cancelEdit() {
    if (confirm("변경사항이 취소됩니다. 계속하시겠습니까?")) {
        location.reload(); // 페이지 새로고침하여 변경사항을 취소
    }
}
