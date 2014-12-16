//window.addEventListener("load", loadOptions);
document.getElementById("saveOptions").addEventListener("click",saveOptions);
document.getElementById("eraseOptions").addEventListener("click",eraseOptions);
function loadOptions() {
	var favColor = localStorage["favColor"];

	// valid colors are red, blue, green and yellow
	if (favColor == undefined || (favColor != "red" && favColor != "blue" && favColor != "green" && favColor != "yellow")) {
		favColor = defaultColor;
	}

	var select = document.getElementById("color");
	for (var i = 0; i < select.children.length; i++) {
		var child = select.children[i];
			if (child.value == favColor) {
			child.selected = "true";
			break;
		}
	}
}

function saveOptions() {
	var account = document.getElementById("account").value;
	var password = document.getElementById("password").value;
	localStorage["account"] = account;
	localStorage["password"] = password;
}

function eraseOptions() {
	localStorage.removeItem("account");
	localStorage.removeItem("password");
	location.reload();
}