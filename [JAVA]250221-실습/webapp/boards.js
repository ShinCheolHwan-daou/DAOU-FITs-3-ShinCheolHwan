$(document).ready(function () {
    let queryParams = new URLSearchParams(location.search);
    let curSearchType = queryParams.get("searchType") ?? "";
    $("#searchType > option").each(function (idx, item) {
        if ($(item).val() === curSearchType) {
            $(item).attr("selected", "selected");
        }
    })

    let curSearchText = queryParams.get("searchText") ?? "";
    $("#searchText").val(curSearchText);
})

function moveToWriteBoard() {
    location.href = '/boards/create';
}

function logout() {
    $.ajax({
        async: false,
        url: "/logout",
        type: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        dataType: "json",
        success: function (data) {
            if (data.status === "success") {
                location.href = "/";
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(`${jqXHR.status} ${textStatus} ${errorThrown}`);
        }
    })
}

function moveToBoardDetail(boardId) {
    location.href = `/boards/detail?boardId=${boardId}`
}

function search() {
    let searchType = $("#searchType").val()
    let searchText = $("#searchText").val()
    location.href = `/boards?searchType=${searchType}&searchText=${searchText}`
}