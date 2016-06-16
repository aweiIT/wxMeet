init();
var staObj = {
	draft : "草稿",
	noRun : "未开始",
	run : "进行中",
	over : "已结束",
	can : "已取消"
}
var meetObj = {};
function init() {
	ajaxPost({
		userID : $.getUrlVar("userid")
	}, "userList.do", function(data) {
		if (data[0] != null) {
			localStorage.nowUser = JSON.stringify(data[0]);
		}
	})
	$("#m_main").children().remove();
	ajaxPost({
		status : $("#m_select").val(),
		methem : $(".m_text").text(),
		type : "meetList"
	}, "meetList.do", function(data) {
		meetObj = {};
		for ( var k in data) {
			for ( var n in data[k]) {
				var d_obj = data[k];
				var id = d_obj[n].id;
				meetObj[id] = d_obj[n];
			}
		}
		var arr = sortMeet(data);
		for ( var k in arr) {
			var list = sort(arr[k].data, "startTime");
			$("#meet_t").tmpl(
					{
						date : arr[k].date,
						list : list
					},
					{
						getStatus : function(sta) {
							return staObj[sta];
						},
						getDate : function(date) {
							var daArr = date.split("-");
							var span = $("<span/>").addClass("m_day").text(
									daArr[2] + "日    ");
							var dateSpan = $("<span/>");
							dateSpan.text(daArr[0] + "  年" + daArr[1] + "  月");
							dateSpan.prepend(span);
							return dateSpan.html();
						}
					}).appendTo($(".m_main"));
		}
	})
}
function meetInfo(id) {
	localStorage.meet_info = JSON.stringify(meetObj[id]);
	loadPage("meetInfo.html");
}
function sortMeet(data) {
	var arr = new Array();
	for ( var k in data) {
		arr.push({
			date : k,
			data : data[k]
		});
	}
	return sort(arr, "date");
}
