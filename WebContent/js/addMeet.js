function back() {
	$(".i_main").load("meetList.html?id=5");
}
$(".mi_input").focus(function() {
	var val = $(this).text();
	if (val && val == $(this).attr("str")) {
		$(this).text("");
	}
});
$(".mi_input").blur(function() {
	var val = $(this).text();
	if (!val) {
		$(this).text($(this).attr("str"));
	}
});
$(".mi_input").trigger("blur");
function selectRoom() {
	dataBuffer();
	location.href = "roomList.html?type=select";
}
function addFile() {
	dataBuffer();
	location.href = "fileList.html?type=select";
}
function addUser() {
	dataBuffer();
	location.href = "userList.html?type=select";
}
function dataBuffer() {
	var param = {
		room : $("#room").text(),
		methem : $("#methem").text(),
		date : $("#date").val(),
		startTime : $("#startTime").val(),
		endTime : $("#endTime").val(),
		join : $("#join").text(),
		desc : $("#desc").text()
	};
	localStorage.meet = JSON.stringify(param);
}
function setData() {
	var param = JSON.parse(localStorage.meet);
	$("#room").text(param.room);
	$("#methem").text(param.methem);
	$("#date").val(param.date);
	$("#startTime").val(param.startTime);
	$("#endTime").val(param.endTime);
	$("#join").text(param.join);
	$("#desc").text(param.desc)
}
init();
function init() {
	for (var i = 0; i < 24; i++) {
		var n = i;
		if (n < 10)
			n = "0" + n + ":00";
		else {
			n = n + ":00";
		}
		$("<option/>").attr("value", n).text(n).appendTo($("#startTime"));
		$("<option/>").attr("value", n).text(n).appendTo($("#endTime"));
	}

	var type = $.getUrlVar("type");
	if (type == "back") {
		setData();
		var s_file = JSON.parse(localStorage.s_file);
		for ( var k in s_file) {
			$("#file_t").tmpl(s_file[k], {
				getName : function(name) {
					if (name.length > 5) {
						return name.slice(0, 5) + "...";
					}
					return name;
				}
			}).insertBefore($("#mi_fileClear"));
		}
		var s_room = JSON.parse(localStorage.s_room);
		var roomStr = "";
		for ( var k in s_room) {
			roomStr += s_room[k].name + ",";
		}
		$("#room").text(roomStr);
		var s_user = JSON.parse(localStorage.s_user);
		var userStr = "";
		for ( var k in s_user) {
			userStr += s_user[k].depart + "  " + s_user[k].name + ",";
		}
		$("#join").text(userStr);
	} else {
		localStorage.s_user = "{}";
		localStorage.s_file = "{}";
		localStorage.s_room = "{}";
		localStorage.meet = "{}";
	}
}
function saveMeet() {
	var param = {
		methem : $("#methem").text(),
		date : $("#date").val(),
		startTime : $("#startTime").val(),
		endTime : $("#endTime").val(),
		join : $("#join").text(),
		desc : $("#desc").text()
	};
	var s_file = JSON.parse(localStorage.s_file);
	var s_room = JSON.parse(localStorage.s_room);
	var s_user = JSON.parse(localStorage.s_user);
	param.file = s_file;
	param.s_room = s_room;
	param.s_user = s_user;
	param.status = "draft";
	ajaxPost(param, "saveMeet.do", function(data) {
		if (data.code == 1) {
			alert("保存成功");
		}
	});
}