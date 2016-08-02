/**
 * Returns an object representing a TimeCrypt message.
 */
function collectAndPrepareData() {
	var error = null;

	var title = $("#message-title").val();
	if ($.trim(title).length == 0)
		title = "Crypted Message";

	var text = $("#message-text").val();
	if ($.trim(text).length == 0)
		text = "Check out TimeCrypt @ http://www.timecrypt.co/";

	var views = parseInt($("#views").text(), 10);
	if (views < 1)
		views = 1;

	var dur = parseInt($("#duration").text(), 10);
	if (dur < 1)
		dur = 1;

	var desc = $("#description").text().toLowerCase();
	if ($.trim(desc).length == 0)
		desc = "days";

	if (desc.contains("day"))
		desc = "days";
	else if (desc.contains("week"))
		desc = "weeks";
	else if (desc.contains("month"))
		desc = "months";
	else if (desc.contains("year"))
		desc = "years";
	else
		desc = "days";

	/*
	 * From docs on http://momentjs.com/
	 * moment().add('days', 10);
	 */
	var date = moment().add(desc, dur).format("YYYY-MM-DD");
	var friendlyDate = moment().add(desc, dur).fromNow();

	var sendTo = $.trim($("#send-to").val());
	var sendFrom = $.trim($("#send-from").val());
	if ( (sendTo.length != 0 && !isEmail(sendTo)) ||
		 (sendFrom.length != 0 && !isEmail(sendFrom)) )
		error = "You must type in a valid email";

	var password = $.trim($("#password").val());
	if (password.length > 0 && password.length < 5)
		error = "Password must be at least 5 characters long";

	var cryptext = {
		title: title,
		text: text,
		views: views,
		date: date,
		friendlyDate: friendlyDate,
		sendTo: sendTo,
		sendFrom: sendFrom,
		password: password,
		error: error
	};

	return cryptext;
}

function createCryptext(log) {
	var cryptext = collectAndPrepareData();
	if (log || cryptext == null) {
		alert(JSON.stringify(cryptext));
	} else if (cryptext.error == null) {
		$(".container button").attr("disabled", "disabled");

		// no error, really post to server
		var cloned = {
			"text": cryptext.text,
			"views": cryptext.views,
			"password": cryptext.password,
			"title": cryptext.title,
			"lifetime": cryptext.date,
			"language": "en", // change later
			"email_to": cryptext.sendTo,
			"email_from": cryptext.sendFrom
		}

		$.post("service/create_note.php", cloned)
			.done(function(data) {
				if (data != "-1") {
					$(".container button").removeAttr("disabled");
					$("#message-text").val("TIMECRYPT CREATED. YOUR LINK IS:\n\n"
											+ data + "\n\nREMEMBER, YOU HAVE ONLY "
											+ cryptext.views + " ALLOWED VIEW(S).");
					$("#message-title").val("");
					$("nav ul li").first().trigger("click");
				} else {
					alert("Something went wrong. Please check your data and try again.");
				}
			});
	} else {
		// there is an error!
		alert(cryptext.error);
	}
}