function editBoard(boardId) {
    let sendData = {
        boardId: boardId,
        title: $("input[name=title]").val(),
        content: $("textarea[name=content]").val()
    }

    $.ajax({
        async: false,
        url: `/boards/detail?boardId=${boardId}`,
        type: "put",
        headers: {
            "Content-Type": "application/json"
        },
        data: JSON.stringify(sendData),
        dataType: "json",
        success: function (data) {
            if (data.status === "success") {
                location.href = `/boards/detail?boardId=${boardId}`;
            } else {
                alert("게시글 수정에 실패했습니다.");
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(`${jqXHR.status} ${textStatus} ${errorThrown}`);
        }
    })
}