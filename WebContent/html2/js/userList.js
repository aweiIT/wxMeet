function upFile() {
	location.href = "addFile.html";
}
var type = $.getUrlVar("type");
function init(param) {
	if (type == "select") {
		$(".f_back").css("display", "inline-block");
		$(".r_add").hide();
	}

	$("#f_main").children().remove();
	ajaxPost(param, "userList.do", function(data) {
		var depart = {};
		for ( var k in data) {
			var id = data[k].departid;
			var departObj = depart[id];
			if (!departObj) {
				departObj = {};
			}
			departObj.departid = id;
			departObj.departName = data[k].departName;
			var userList = departObj.userList;
			if (!userList) {
				userList = new Array();
			}
			userList.push(data[k]);
			departObj.userNum = userList.length;
			departObj.userList = userList;
			depart[id] = departObj;
		}
		for ( var k in depart) {
			$("#depart_t").tmpl(depart[k]).appendTo($("#f_main"));
		}
		checkInit();
		if (type == "select") {
			$(".d_check").css("display", "inline");
		}
	})
}
init({});
function reload() {
	init({
		role : $("#select").val(),
		name : $(".m_text").text()
	})
}
function synUser() {
	ajaxPost({}, "synUser.do", function(data) {
		if (data.code == 1) {
			alert("同步用户成功");
		}
	})
}
function toggleUser(e, departid) {
	var src = $(e).attr("src");
	if (src == "img/right_ico.png") {
		$(e).attr("src", "img/bottom_ico.png");
	} else {
		$(e).attr("src", "img/right_ico.png")
	}
	$("." + departid).toggle();
}
function checkInit() {
	$("[pType=p_check]").each(function() {
		$(this).click(function() {
			var id = $(this).attr("id");
			if ($(this).prop("checked")) {
				$($("." + id).find("input")).prop("checked", "true");
			} else {
				$($("." + id).find("input")).removeProp("checked");
			}
		});
	})
}
function select() {
	var s_user = localStorage.s_user;
	s_user = JSON.parse(s_user);
	$("[uid]").each(function() {
		if (!$(this).prop("checked"))
			return;
		var id = $(this).attr("uid");
		s_user[id] = {
			id : id,
			name : $(this).attr("uname"),
			depart : $(this).attr("udepart"),
			departid : $(this).attr("udepartid"),
			wx_userid : $(this).attr("wx_userid"),
		}
	});
	localStorage.s_user = JSON.stringify(s_user);
	location.href = "addMeet.html?type=back";
}
function loadUser(id, e) {
	if (type == "select")
		return;
	var param = {
		name : $(e).find(".d_check").attr("uname"),
		depart : $(e).find(".d_check").attr("udepart"),
		departid : $(e).find(".d_check").attr("udepartid"),
		id : id,
		role : $(e).find(".d_check").attr("orole"),
	}
	localStorage.user = JSON.stringify(param);
	location.href = "userInfo.html";

}