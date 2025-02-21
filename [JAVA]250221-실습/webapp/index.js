function login() {
    let sendData = {
        id: $("#id").val(),
        password: $("#password").val()
    }

    $.ajax({
        async: false,
        url: "/login",
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
                alert("로그인에 실패했습니다.");
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(`${jqXHR.status} ${textStatus} ${errorThrown}`);
        }
    })
}