function deleteBoard(boardId) {
    $.ajax({
        async: false,
        url: `/boards/detail?boardId=${boardId}`,
        type: "DELETE",
        dataType: "json",
        success: function (data) {
            if (data.status === "success") {
                location.href = "/boards";
            } else {
                alert("글 삭제에 실패했습니다.");
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(`${jqXHR.status} ${textStatus} ${errorThrown}`);
        }
    })
}

function editBoard(boardId) {
    location.href = `/boards/detail/edit?boardId=${boardId}`
}

function likeBoard(boardId) {
    $.ajax({
        async: false, url: `/boards/like?boardId=${boardId}`, type: "POST", headers: {
            "Content-Type": "application/json"
        }, dataType: "json", success: function (data) {
            if (data.status === "success") {
                $("button[name=like-btn]").css("display", "none");
                $("button[name=cancel-like-btn]").css("display", "inline-block");
                let likeCount = $("#likeCount");
                likeCount.text(parseInt(likeCount.text()) + 1);
            } else {
                alert("좋아요에 실패했습니다.");
            }
        }, error: function (jqXHR, textStatus, errorThrown) {
            alert(`${jqXHR.status} ${textStatus} ${errorThrown}`);
        }
    })
}

function cancelLikeBoard(boardId) {
    $.ajax({
        async: false,
        url: `/boards/like?boardId=${boardId}`,
        type: "DELETE",
        dataType: "json",
        success: function (data) {
            if (data.status === "success") {
                $("button[name=like-btn]").css("display", "inline-block");
                $("button[name=cancel-like-btn]").css("display", "none");
                let likeCount = $("#likeCount");
                likeCount.text(parseInt(likeCount.text()) - 1);
            } else {
                alert("좋아요 취소에 실패했습니다.");
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(`${jqXHR.status} ${textStatus} ${errorThrown}`);
        }
    })
}

function writeComment(boardId) {
    let sendData = {
        boardId: boardId, content: $("textarea[name=new-comment]").val()
    }
    $.ajax({
        async: false, url: "/boards/detail/comments", type: "POST", headers: {
            "Content-Type": "application/json"
        }, data: JSON.stringify(sendData), dataType: "json", success: function (data) {
            if (data.status === "success") {
                $("textarea[name=new-comment]").val("");
                let commentCount = $("#commentCount");
                commentCount.text(parseInt(commentCount.text()) + 1);

                let commentClass = $("<div></div>").attr("class", "comment").attr("id", `comment-${data.comment.id}`);
                let commentDiv = $("<div></div>");
                let commentContent = $("<div></div>").text(data.comment.content);
                let commentMeta = $("<div></div>").attr("class", "comment-meta").text(`${dateFormating(data.comment.createdAt)} | ${data.comment.writer.name}`);
                commentDiv.append(commentContent);
                commentDiv.append(commentMeta);
                commentClass.append(commentDiv);

                let deleteButton = $("<button>삭제</button>");
                deleteButton.attr("name", "delete-btn");
                deleteButton.on('click', function () {
                    deleteComment(data.comment.id);
                })
                commentClass.append(deleteButton);
                $(".comment-section").append(commentClass);
                $(".comment-section").scrollTop($(".comment-section").height());
            } else {
                alert("댓글 작성에 실패했습니다.")
            }
        }, error: function (jqXHR, textStatus, errorThrown) {
            alert(`${jqXHR.status} ${textStatus} ${errorThrown}`);
        }
    })
}

function deleteComment(commentId) {
    $.ajax({
        async: false,
        url: `/boards/detail/comments/detail?commentId=${commentId}`,
        type: "DELETE",
        dataType: "json",
        success: function (data) {
            if (data.status === "success") {
                let commentCount = $("#commentCount");
                commentCount.text(parseInt(commentCount.text()) - 1);
                $(`#comment-${commentId}`).remove();
            } else {
                alert("댓글 삭제에 실패했습니다.");
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(`${jqXHR.status} ${textStatus} ${errorThrown}`);
        }
    })
}

function dateFormating(timestamp) {
    let now = new Date(timestamp);
    const yyyy = now.getFullYear();
    const MM = String(now.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작
    const dd = String(now.getDate()).padStart(2, '0');
    const HH = String(now.getHours()).padStart(2, '0');
    const mm = String(now.getMinutes()).padStart(2, '0');

    return `${yyyy}-${MM}-${dd} ${HH}:${mm}`;
}