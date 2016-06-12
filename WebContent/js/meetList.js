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
		$("#m_main").children().remove();
		ajaxPost({
			status : $("#m_select").val(),
			methem : $(".m_text").text()
		}, "meetList.do", function(data) {
			meetObj = {};
			for ( var k in data) {
				for ( var n in data[k]) {
					var d_obj = data[k];
					var id = d_obj[n].id;
					meetObj[id] = d_obj[n];
				}
				$("#meet_t")
						.tmpl(
								{
									date : k,
									list : data[k]
								},
								{
									getStatus : function(sta) {
										return staObj[sta];
									},
									getDate : function(date) {
										var daArr = date.split("-");
										var span = $("<span/>").addClass(
												"m_day").text(daArr[2] + "");
										var dateSpan = $("<span/>");
										dateSpan.text("  日"+daArr[0] + "  年"
												+ daArr[1] + "  月");
										dateSpan.prepend(span);
										return dateSpan.html();
									}
								}).appendTo($(m_main));
			}
		})
	}
	function meetInfo(id) {
		localStorage.meet_info = JSON.stringify(meetObj[id]);
		location.href = "meetInfo.html";
	}