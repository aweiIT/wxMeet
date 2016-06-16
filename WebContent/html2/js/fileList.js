function upFile() {
	//location.href = "addFile.html?type=select";
	loadPage("addFile.html?type=select");
}
var type = $.getUrlVar("type");
function init(param) {
	if (type == "select") {
		$(".f_back").css("display", "inline-block");
		// $(".f_headUp").hide();
	}
	$("#f_main").children().remove();
	ajaxPost(param, "fileList.do", function(data) {
		$("#file_t").tmpl(data).appendTo($("#f_main"));
		if (type == "select") {
			$(".f_item").click(function() {
				$(this).toggleClass("f_selectItem");
				$(this).children(".f_check").toggle();
			});
			$(".f_del").hide();
		}
	})
}
init({});
function reload() {
	init({
		type : $("#select").val(),
		name : $(".m_text").text()
	})
}
function select() {
	var s_file = localStorage.s_file;
	if (!s_file)
		s_file = "{}";
	s_file = JSON.parse(s_file);
	$(".f_selectItem").each(function() {
		var id = $(this).attr("oId");
		s_file[id] = {
			name : $(this).attr("oName"),
			type : $(this).attr("oType"),
			nowUrl : $(this).attr("oNowUrl"),
			id : $(this).attr("oId")
		}
	});
	localStorage.s_file = JSON.stringify(s_file);
	loadPage("addMeet.html?type=back");
	//location.href = "";
}