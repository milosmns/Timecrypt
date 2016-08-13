function collectAndPrepareData() {
    var error = null;

    var title = $("#message-title").val();
    if ($.trim(title).length == 0)
        title = "Encrypted Message";

    var text = $("#message-text").val();
    if ($.trim(text).length == 0)
        text = "Check out TimeCrypt @ " + APP_URL;

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
    // also included elsewhere...
    var date = moment().add(desc, dur).format("YYYY-MM-DD");
    var friendlyDate = moment().add(desc, dur).fromNow();

    var sendTo = $.trim($("#send-to").val());
    var sendFrom = $.trim($("#send-from").val());
    if ((sendTo.length != 0 && !isEmail(sendTo)) ||
        (sendFrom.length != 0 && !isEmail(sendFrom)))
        error = "You must type in a valid email";

    var password = $.trim($("#password").val());
    if (password.length > 0 && password.length < 5)
        error = "Password must be at least 5 characters long";

    return {
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
}

function createTimecrypt(log) {
    var timecrypt = collectAndPrepareData();
    if (log || timecrypt == null) {
        alert(JSON.stringify(timecrypt));
    } else if (timecrypt.error == null) {
        $(".container button").attr("disabled", "disabled");

        // no error, really post to server
        var cloned = {
            "text": timecrypt.text,
            "views": timecrypt.views,
            "password": timecrypt.password,
            "title": timecrypt.title,
            "lifetime": timecrypt.date,
            "email_to": timecrypt.sendTo,
            "email_from": timecrypt.sendFrom
        };

        $.post(API_URL + "v2/create", cloned)
            .done(function (jsonData) {
                if (jsonData != null && jsonData.status_code == 0) {
                    var url = APP_URL + "?c=" + jsonData.id;
                    $(".container button").removeAttr("disabled");
                    $("#message-text").val("TIMECRYPT CREATED. SEND THIS LINK:\n\n"
                    + url + "\n\nREMEMBER, YOU HAVE ONLY "
                    + timecrypt.views + " ALLOWED VIEW(S).");
                    $("#message-title").val("");
                    $("nav ul li").first().trigger("click");
                } else {
                    alert("Something went wrong. Please check your data and try again.");
                    console.log(jsonData);
                }
            });
    } else {
        // there is an error!
        alert(timecrypt.error);
    }
}