function initializeTimecryptUI() {
    // ignore it for now..
    // noinspection JSUnusedLocalSymbols
    $("#message-text").on("input", function (e) {
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

    // ignore it for now
    // noinspection JSUnusedLocalSymbols
    $("#screen-title").on("input", function (e) {
        var value = $.trim($(this).val());
        var def = "Encrypted Message";

        if (value.length == 0) {
            $("#message-title").val(def);
        } else {
            $("#message-title").val(value);
        }
    });

    // ignore it for now, just assign all click listeners
    // noinspection JSUnusedLocalSymbols
    $("nav ul li").click(function (data) {
        var ord = $(this).attr("ord");
        for (var i = 1; i <= 4; i++)
            $("#page" + i).clearQueue().finish().slideUp(300);
        // $("#page" + CURRENT_PAGE).clearQueue().finish().slideUp(300);
        $("#page" + ord).clearQueue().finish().slideDown(300, function () {
            // animation finished
            onPageChanged(CURRENT_PAGE, ord);
        });
    });

    // also ignore for now, just assign logo click
    // noinspection JSUnusedLocalSymbols
    $(".logo-path")
        .click(function (data) {
            window.location.href = "http://timecrypt.co/hq/help/";
        });

    // ignore warning, just create message
    // noinspection JSUnusedLocalSymbols
    $(".container > button").click(function (data) {
        if (ACTIVE)
            createTimecrypt(false); // from operations.js [true = debug alert, false = send to server]
        else {
            // this is imported elsewhere
            // noinspection JSUnresolvedVariable
            $.post("service/get_note.php", {
                "id": encodeURIComponent(timecryptId),
                "password": $("#message-text").val()
            })
                .done(onTimecryptDecrypted);
        }
    });
}

function onPageChanged(oldPage, newPage) {
    CURRENT_PAGE = parseInt(newPage, 10);
    switch (CURRENT_PAGE) {
        case 1:
        {
            var def = "Encrypted Message";
            var val = $("#message-title").val();
            if (val == def) val = ""; // or the other way around?
            $("#screen-title").removeAttr("disabled").val(val);
            break;
        }
        case 2:
        {
            positionSlider(1);
            positionTextInSlider();
            $("#screen-title").attr("disabled", "disabled").val("ALLOWED VIEWS");
            break;
        }
        case 3:
        {
            positionSlider(2);
            positionTextInSlider();
            $("#screen-title").attr("disabled", "disabled").val("SELF-DESTRUCT DATE");
            break;
        }
        case 4:
        {
            $("#screen-title").attr("disabled", "disabled").val("DELIVERY");
            break;
        }
    }

    $("nav > ul > li:nth-child(" + oldPage + ")").removeClass("selected");
    $("nav > ul > li:nth-child(" + newPage + ")").addClass("selected");
}