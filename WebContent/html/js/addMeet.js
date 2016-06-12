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
	log(param)
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
		$(".mi_back").css("display", "inline-block");
		setData();
		var s_file = JSON.parse(localStorage.s_file);
		for ( var k in s_file) {
			console.log(JSON.stringify(s_file[k]))
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
		$("#room").text(s_room.name);
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
	if (localStorage.s_room == "{}") {
		alert("请选择会议室");
		return;
	}
	if (localStorage.s_user == "{}") {
		alert("请选择参会人");
		return;
	}
	if (param.startTime > param.endTime) {
		alert("结束时间必须大于开始时间");
		return;
	}
	var s_file = JSON.parse(localStorage.s_file);
	var s_room = JSON.parse(localStorage.s_room);
	var s_user = JSON.parse(localStorage.s_user);
	if (!param.methem) {
		alert("请填写会议主题");
	}
	ajaxPost({
		date : param.date,
		startTime : param.startTime,
		endTime : param.endTime,
		roomID : s_room.id
	}, "checkMeetTime.do", function(data) {// 检查会议时间
		if (data.code == -1) {
			alert("当前时间段已经有会议");
			return;
		}
		param.file = s_file;
		param.s_room = s_room;
		param.s_user = s_user;
		param.status = "draft";
		param.createUser = JSON.parse(localStorage.nowUser);
		var id = $.getUrlVar("id");
		if (id)
			param.id = id;
		ajaxPost(param, "saveMeet.do", function(data) {
			if (data.code == 1) {
				loadPage("myMeet.html");
			}
		});
	});
}
function delFile(id, e) {
	var s_file = JSON.parse(localStorage.s_file);
	console.log(id);
	delete s_file[id];
	$(e).parent().remove();
	localStorage.s_file = JSON.stringify(s_file);
}
