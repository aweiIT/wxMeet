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
	if (href.indexOf("?") > -1) {
		href = href + "&_sid=" + Math.floor(Math.random() * 100);
	} else {
		href = href + "?_sid=" + Math.floor(Math.random() * 100);
	}
	location.href = href;
}
function sort(arr, key) {//排序JSON
	arr = arr.sort(function(a, b) {
		var x = a[key];
		var y = b[key];
		return ((x < y) ? 1 : ((x > y) ? -1 : 0));
	});
	return arr;
}