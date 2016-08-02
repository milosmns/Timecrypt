$.fn.selectRange = function (start, end) {
    if (!end)
        end = start;
    return this.each(function () {
        if (this.setSelectionRange) {
            this.focus();
            this.setSelectionRange(start, end);
        } else if (this.createTextRange) {
            var range = this.createTextRange();
            range.collapse(true);
            range.moveEnd('character', end);
            range.moveStart('character', start);
            range.select();
        }
    });
};

String.prototype.contains = function (it) {
    return this.indexOf(it) != -1;
};

function isEmail(email) {
    var regex = /^([a-zA-Z0-9_.+-])+@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    return regex.test(email);
}

function getUrlParameter(param) {
    var pageUrl = window.location.search.substring(1);
    var variables = pageUrl.toString().split("&");
    for (var i = 0; i < variables.length; i++) {
        var paramName = variables[i].toString().split("=");
        if (paramName[0] == param) {
            return paramName[1];
        }
    }
    return "";
}

function isValidUrl(str) {
    var pattern = new RegExp('^(https?:\\/\\/)?' + // protocol
    '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|' + // domain name
    '((\\d{1,3}\\.){3}\\d{1,3}))' + // OR ip (v4) address
    '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*' + // port and path
    '(\\?[;&a-z\\d%_.~+=-]*)?' + // query string
    '(\\#[-a-z\\d_]*)?$', 'i'); // fragment locator

    return pattern.test(str);
}