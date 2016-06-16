$.extend({// 获取http请求参数
	getUrlVars : function() {
		var vars = [], hash;
		var hashes = window.location.href.slice(
				window.location.href.indexOf('?') + 1).split('&');
		for (var i = 0; i < hashes.length; i++) {
			hash = hashes[i].split('=');
			vars.push(hash[0]);
			vars[hash[0]] = hash[1];
		}
		return vars;
	},
	getUrlVar : function(name) {
		return $.getUrlVars()[name];
	}
});
/** ajax异步提交数据 */
function ajaxPost(param, action, successMethod) {
	$.ajax({
		type : "POST",
		url : "/wxmeet/" + action,
		dataType : "json",
		contentType : "application/json;charset=UTF-8",
		data : JSON.stringify(param),
		success : successMethod,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
		}
	});
}
function log(data) {
	console.log(JSON.stringify(data))
}
function loadPage(href, e) {
	var src1 = $(".index_select").attr("src");
	if (src1 && e) {
		var arrs1 = src1.split("_");
		$(".index_select").attr("src",
				arrs1[0] + "_" + arrs1[1] + "_" + "1.png");
		$(".index_select").removeClass("index_select");
	}
	if (href.indexOf("?") > -1) {
		href = href + "&_sid=" + Math.floor(Math.random() * 100);
	} else {
		href = href + "?_sid=" + Math.floor(Math.random() * 100);
	}
	// location.href = href;
	$(".i_main").load(href);
	if (e) {
		var src = $(e).addClass("index_select").attr("src");
		var arrs = src.split("_");
		$(e).attr("src", arrs[0] + "_" + arrs[1] + "_" + "0.png");
	}
}
function sort(arr, key) {// 排序JSON
	arr = arr.sort(function(a, b) {
		var x = a[key];
		var y = b[key];
		return ((x < y) ? 1 : ((x > y) ? -1 : 0));
	});
	return arr;
}