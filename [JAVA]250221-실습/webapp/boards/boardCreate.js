function writeBoard() {
    let sendData = {
        title: $("input[name=title]").val(),
        content: $("textarea[name=content]").val()
    }

    $.ajax({
        async: false,
        url: "/boards",
        type: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        data: JSON.stringify(sendData),
        dataType: "json",
        success: function (data) {
            if (data.status === "success") {
                location.href = "/boards";
            } else {
                alert("글 작성에 실패했습니다.");
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(`${jqXHR.status} ${textStatus} ${errorThrown}`);
        }
    })
}