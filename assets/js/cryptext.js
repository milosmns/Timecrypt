function initializeCryptextUI() {
	// message text reaction
	$("#message-text").on("input", function(e) {
		if ($.trim($(this).val()).length == 0) {
			// empty
			if (ACTIVE) {
				$("nav").slideUp(300);
				$(".logo-container > input").css("display", "none");
				$(".logo-container").removeClass("leftAligned").addClass("centerAligned");
				$(".logo-container > img").removeClass("withLeftMargin");
				$("textarea").removeClass("leftAligned").addClass("centerAligned");
			}
			// button goes even for password typing
			$("button").slideUp(300);
		} else {
			// not empty
			if (ACTIVE) {
				$("nav").slideDown(300);
				$(".logo-container > input").css("display", "inline-block");
				$(".logo-container").removeClass("centerAligned").addClass("leftAligned");
				$(".logo-container > img").addClass("withLeftMargin");
				$("textarea").removeClass("centerAligned").addClass("leftAligned");
			}
			// button goes even for password typing
			$("button").slideDown(300);
		}
	});

	// message title edit reaction
	$("#screen-title").on("input", function(e) {
		var value = $.trim($(this).val());
		var def = "Crypted Message";

		if (value.length == 0) {
			$("#message-title").val(def);
		} else {
			$("#message-title").val(value);
		}
	});

	// assign menu clicks
	$("nav ul li").click(function(data) {
		var ord = $(this).attr("ord");
		for (var i = 1; i <= 4; i++)
			$("#page" + i).clearQueue().finish().slideUp(300);
		// $("#page" + CURRENT_PAGE).clearQueue().finish().slideUp(300);
		$("#page" + ord).clearQueue().finish().slideDown(300, function() {
			// animation finished
			onPageChanged(CURRENT_PAGE, ord);
		});
	});

	// assign logo click
	$(".logo-path")
		.click(function(data) {
			window.location.href = "http://timecrypt.co/hq/help/";
		});

	// create message
	$(".container > button").click(function(data) {
		if (ACTIVE)
			createCryptext(false); // from operations.js [true = debug alert, false = send to server]
		else
			$.post("service/get_note.php", {
				"id" : encodeURIComponent(cryptextId),
				"password" : $("#message-text").val()
			})
				.done(onCryptextDecrypted);
	});
}

function onPageChanged(oldPage, newPage) {
	CURRENT_PAGE = parseInt(newPage, 10);
	switch (CURRENT_PAGE) {
		case 1: {
			var def = "Crypted Message";
			var val = $("#message-title").val();
			if (val == def) val = ""; // or the other way around?
			$("#screen-title").removeAttr("disabled").val(val);
			break;
		}
		case 2: {
			positionSlider(1);
			positionTextInSlider();
			$("#screen-title").attr("disabled", "disabled").val("ALLOWED VIEWS");
			break;
		}
		case 3: {
			positionSlider(2);
			positionTextInSlider();
			$("#screen-title").attr("disabled", "disabled").val("SELF-DESTRUCT DATE");
			break;
		}
		case 4: {
			$("#screen-title").attr("disabled", "disabled").val("DELIVERY");
			break;
		}
	}

	$("nav > ul > li:nth-child(" + oldPage + ")").removeClass("selected");
	$("nav > ul > li:nth-child(" + newPage + ")").addClass("selected");
}