function addRoom(id) {
	loadPage("addRoom.html?id=" + id)
}
var type = $.getUrlVar("type");
function init(param) {
	if (type == "select") {
		$(".f_back").css("display", "inline-block");
		$(".r_add").hide();
	}
	$("#f_main").children().remove();
	ajaxPost(param, "roomList.do", function(data) {
		$("#file_t").tmpl(data).appendTo($("#f_main"));
		if (type == "select") {
			$(".f_item").click(function() {
				$(".f_selectItem").removeClass("f_selectItem");
				$(".f_check").hide();
				$(this).addClass("f_selectItem");
				$(this).children(".f_check").show();
			});
			$(".f_del").hide();
		}
	})
}
init({});
function reload() {
	init({
		name : $(".m_text").text()
	})
}
function select() {
	var s_room = {
		name : $(".f_selectItem").attr("oname"),
		id : $(".f_selectItem").attr("oid"),
	}
	localStorage.s_room = JSON.stringify(s_room);
	loadPage("addMeet.html?type=back");
}