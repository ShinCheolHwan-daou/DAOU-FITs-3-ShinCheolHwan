$(document).ready(function () {
    $("#searchDate").on("change", function () {
        let searchDate = $(this).val();
        console.log(searchDate);
        let dailyBoxOffices = getDailyBoxOfficeList(searchDate);

        // 테이블 초기화
        $("tbody > tr").remove();
        if (dailyBoxOffices.length === 0) {
            alert("검색 결과가 없습니다.");
            return;
        }
        let tbody = $("tbody");

        for (const dailyBoxOffice of dailyBoxOffices) {
            // 줄 생성
            let tr = $("<tr></tr>");

            // 영화 순위 column 처리
            let rankCol = $("<td></td>").text(dailyBoxOffice.rank);
            tr.append(rankCol);

            // 영화 포스터 column 처리
            let posterCol = $("<td></td>");
            let poster = $("<img />").attr("src", getPoster(dailyBoxOffice.movieNm)).attr("width", "100");
            posterCol.append(poster);
            tr.append(posterCol);

            // 영화제목 column 처리
            let movieNmCol = $("<td></td>").text(dailyBoxOffice.movieNm);
            tr.append(movieNmCol);

            // 누적관객수 column 처리
            let audiAccCol = $("<td></td>").text(parseInt(dailyBoxOffice.audiAcc).toLocaleString());
            tr.append(audiAccCol);

            // 영화 개봉일 column 처리
            let openDtCol = $("<td></td>").text(dailyBoxOffice.openDt);
            tr.append(openDtCol);

            // 삭제 버튼 column 처리
            let deleteBtnCol = $("<td></td>");
            let deleteBtn = $("<input />").attr("type", "button").attr("value", "삭제");
            deleteBtn.on("click", function () {
                $(this).parent().parent().remove();
            });
            deleteBtnCol.append(deleteBtn);
            tr.append(deleteBtnCol);

            tbody.append(tr);
        }
    })
})

function getDailyBoxOfficeList(searchDate) {
    let dailyBoxOfficeList = [];
    $.ajax({
        async: false,
        url: "http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json",
        type: "GET",
        timeout: 5000,
        data: {
            key: "da9b8568a54d90fd33fc9a9045f058f9",
            targetDt: searchDate.replaceAll("-", "")
        },
        dataType: "json",
        success: function (data) {
            dailyBoxOfficeList = data.boxOfficeResult.dailyBoxOfficeList;
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.error(jqXHR.status); // 404
            console.error(textStatus); // "error"
            console.error(errorThrown); // "Not Found"
        }
    })
    return dailyBoxOfficeList;
}

function getPoster(movieName) {
    let url = "";
    $.ajax({
        async: false,
        url: "https://dapi.kakao.com/v2/search/image",
        type: "GET",
        headers: {
            Authorization: "KakaoAK 142dae9fc5a7369a513aa225dcef301a"
        },
        data: {
            query: `${movieName} 포스터`,
        },
        dataType: "json",
        success: function (data) {
            url = data.documents[0].thumbnail_url;
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.error(jqXHR.status); // 404
            console.error(textStatus); // "error"
            console.error(errorThrown); // "Not Found"
        }
    })
    return url;
}