// this is soo bad, but it's a demo only, so.. :)
var currentThumb;

function isTouchSupported() {
    // yeah, don't worry. this will work
    // noinspection JSUnresolvedVariable
    var msTouchEnabled = window.navigator.msMaxTouchPoints;
    var generalTouchEnabled = "ontouchstart" in document.createElement("div");

    return !!(msTouchEnabled || generalTouchEnabled || Modernizr.touch);
}

// this is used from another file ... right?
// noinspection JSUnusedGlobalSymbols
function setupEvents(num) {
    var thumbId = "#slider" + num + " #thumb";
    var thumb = $(thumbId);

    var hasTouch = isTouchSupported();
    var eventType = hasTouch ? "touchstart" : "mousedown";

    if (hasTouch) {
        $(document).on({
            touchstart: startDrag,
            touchmove: drag,
            touchend: endDrag
        });
    } else {
        thumb.bind(eventType, startDrag);
    }
}

function getEventLocation(e) {
    var out = {x: 0, y: 0};

    if (e.type == 'touchstart' || e.type == 'touchmove' ||
        e.type == 'touchend' || e.type == 'touchcancel') {
        // this works too, chill
        // noinspection JSUnresolvedVariable
        var touch = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];
        out.x = touch.pageX;
        out.y = touch.pageY;
    } else if (e.type == 'mousedown' || e.type == 'mouseup' ||
        e.type == 'mousemove' || e.type == 'mouseover' ||
        e.type == 'mouseout' || e.type == 'mouseenter' || e.type == 'mouseleave') {
        out.x = e.pageX;
        out.y = e.pageY;
    }

    return out;
}

function getThumbLocation(angle, radius, containerSize, thumbVar) {
    if (thumbVar == null || containerSize == null)
        return {top: 0, left: 0};
    else
        return {
            top: -Math.cos(toRadians(angle)) * radius + (containerSize.height / 2 - thumbVar.height() / 2),
            left: Math.sin(toRadians(angle)) * radius + (containerSize.width / 2 - thumbVar.width() / 2)
        }
}

function getRadius(containerSize, thumbVar) {
    var index = parseInt(thumbVar.parent().attr("id").replace("slider", ""));

    var additional = 0;
    if (containerSize.height > 330 && index == 1)
        additional = 7;
    else if (containerSize.height > 290 && index == 2)
        additional = 7;
    else if (containerSize.height > 250 && index == 3)
        additional = 7;

    var offset = 18 + additional;
    if (index == 2)
        offset = 14 + additional;
    else if (index == 3)
        offset = 10 + additional;

    return containerSize.width / 2 - offset;
}

function drag(event) {
    if (currentThumb == null) return;

    event.preventDefault();

    var location = getEventLocation(event);
    var pageX = location.x;
    var pageY = location.y;

    var containerOffset = currentThumb.parent().offset();
    var containerSize = {
        width: currentThumb.parent().width(),
        height: currentThumb.parent().height()
    };

    var distanceFromCenter = {
        left: pageX - containerOffset.left - (containerSize.width / 2),
        top: pageY - containerOffset.top - (containerSize.height / 2)
    };

    var angle = sanitizeAngle(toDegrees(Math.atan2(distanceFromCenter.left, -distanceFromCenter.top)));
    var radius = getRadius(containerSize, currentThumb);

    currentThumb.css(getThumbLocation(angle, radius, containerSize, currentThumb));

    // event-like
    var index = parseInt(currentThumb.parent().attr("id").replace("slider", ""));

    // used from outside, chill
    // noinspection JSUnresolvedFunction
    onCircleSliderDrag(index, Math.round(angle));

    // console.info(event, "Drag!");

    return true;
}

function endDrag(event) {
    // event.preventDefault(); ?

    if (!hasTouch) {
        $(document).unbind("mousemove mouseup");
        $(document).unbind("mouseup");
        currentThumb.unbind("mouseup");
    }

    // console.info(event, "End drag!");

    if (event.target.id == "thumb")
        currentThumb = null;

    return true;
}

function startDrag(event) {
    // event.preventDefault(); ?

    if (event.target.id == "thumb")
        currentThumb = $(event.target);

    if (!hasTouch) {
        $(document).mousemove(drag);
        $(document).mouseup(endDrag);
        currentThumb.mouseup(endDrag);
    }

    // console.info(event, "Start drag!");

    return true;
}

function toRadians(degrees) {
    return degrees * (Math.PI / 180);
}

function toDegrees(radians) {
    return radians * 180 / Math.PI;
}

function sanitizeAngle(degrees) {
    return (degrees < 0) ? 360 + (degrees % -360) : degrees % 360;
}

function positionSlider(num) {
    var id = "#slider" + num;
    var cont = $(id).parent();

    // var minSize = 350 + num * 10;
    var marTop = 15 + num * 30;
    var marLeft = 15 + num * 30;

    var contW = cont.width();
    var contH = cont.height();

    var thisW;
    var thisH;
    // if (!isMobile) {
    if (contW < contH) {
        thisW = contW - 2 * marLeft;
        // var thisH = contH - 2 * marTop;
        thisH = thisW; // new
    } else {
        thisH = contH - 2 * marTop; // new
        thisW = thisH; // new
    }
    // } else {
    // var thisW = minSize;
    // var thisH = minSize;
    // }

    marTop = contH / 2 - thisH / 2;
    marLeft = contW / 2 - thisW / 2;

    // var thumbW = 12 / 100 * thisW;
    // var thumbH = 12 / 100 * thisH;

    var thumbW = 25; // new
    var thumbH = 25; // new

    $(id).css("top", marTop + "px");
    $(id).css("left", marLeft + "px");
    $(id).css("width", thisW + "px");
    $(id).css("height", thisH + "px");
    $(id).css("background-size", thisW + "px " + thisH + "px");
    $(id).css("pointer-events", "auto");

    $(id + " #thumb").css({
        width: thumbW + "px",
        height: thumbH + "px"
    });

    var containerSize = {
        width: thisW,
        height: thisH
    };

    var angle = (num == 1) ? 0 : (0 + Math.pow(-1, num) * 20);
    var radius = getRadius(containerSize, $(id + " #thumb"));

    $(id + " #thumb").css(getThumbLocation(angle, radius, containerSize, $(id + " #thumb")));
}