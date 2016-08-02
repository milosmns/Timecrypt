function isTouchSupported() {
	var msTouchEnabled = window.navigator.msMaxTouchPoints;
	var generalTouchEnabled = "ontouchstart" in document.createElement("div");

	if (msTouchEnabled || generalTouchEnabled || Modernizr.touch)
		return true;

	return false;
}

function setupEvents(num) {
	var containerId = "#slider" + num;
	var container = $(containerId);
	var thumbId = "#slider" + num + " #thumb";
	var thumb = $(thumbId);

	hasTouch = isTouchSupported();
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
	var out = { x: 0, y: 0 };
	
	if (e.type == 'touchstart' || e.type == 'touchmove' ||
		e.type == 'touchend' || e.type == 'touchcancel')
	{
		var touch = e.originalEvent.touches[0] || e.originalEvent.changedTouches[0];
		out.x = touch.pageX;
		out.y = touch.pageY;
	} else if (e.type == 'mousedown' || e.type == 'mouseup' ||
			   e.type == 'mousemove' || e.type == 'mouseover' ||
			   e.type=='mouseout' || e.type=='mouseenter' || e.type=='mouseleave')
	{
		out.x = e.pageX;
		out.y = e.pageY;
	}

	return out;
};

function getThumbLocation(angle, radius, containerSize, thumbVar) {
	if (thumbVar == null || containerSize == null)
		return { top: 0, left: 0 };
	else
		return {
        	top: -Math.cos(toRadians(angle)) * radius + (containerSize.height / 2 - thumbVar.height() / 2),
        	left: Math.sin(toRadians(angle)) * radius + (containerSize.width / 2 - thumbVar.width() / 2)
    	}
}

function getRadius(containerSize, thumbVar) {
	var offset = -5;
	if (isMobile())
		offset = -9;
	return containerSize.width / 2 - thumbVar.width() / 2 - offset;
}

function drag(event) {
	if (CURRENT_THUMB == null) return;

	event.preventDefault(); 

	var location = getEventLocation(event);
	var pageX = location.x;
	var pageY = location.y;

	var containerOffset  = CURRENT_THUMB.parent().offset();
	var containerSize = {
		width: CURRENT_THUMB.parent().width(),
		height: CURRENT_THUMB.parent().height()
	};

	var distanceFromCenter = {
		left: pageX - containerOffset.left - (containerSize.width / 2),
		top: pageY - containerOffset.top - (containerSize.height / 2)
	};

	var angle = sanitizeAngle(toDegrees(Math.atan2(distanceFromCenter.left, -distanceFromCenter.top)));
	var radius = getRadius(containerSize, CURRENT_THUMB);

	CURRENT_THUMB.css(getThumbLocation(angle, radius, containerSize, CURRENT_THUMB));

	// event-like
	var index = parseInt(CURRENT_THUMB.parent().attr("id").replace("slider", ""));
	onCircleSliderDrag(index, Math.round(angle));

	// console.info(event, "Drag!");

	return true;
}

function endDrag(event) {
	// event.preventDefault(); ?

	if (!hasTouch) {
		$(document).unbind("mousemove mouseup");
		$(document).unbind("mouseup");
		CURRENT_THUMB.unbind("mouseup");
	}

	// console.info(event, "End drag!");

	if (event.target.id == "thumb")
		CURRENT_THUMB = null;

	return true;
}

function startDrag(event) {
	// event.preventDefault(); ?

	if (event.target.id == "thumb")
		CURRENT_THUMB = $(event.target);

    if (!hasTouch) {
        $(document).mousemove(drag);
        $(document).mouseup(endDrag);
        CURRENT_THUMB.mouseup(endDrag);
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

	// init
	var margin = 10;	
	var contW = cont.width();
	var contH = cont.height();

	// dimensions
	if (contW < contH) {
		var thisW = contW - 2 * margin;
		var thisH = thisW;
	} else {
		var thisH = contH - 2 * margin;
		var thisW = thisH;
	}

	// maxW/maxH
	if (thisW > 400 || thisH > 400) {
		thisW = 400;
		thisH = 400;
	}

	// margins
	var marTop = contH / 2 - thisH / 2;
	var marLeft = contW / 2 - thisW / 2;

	// handler size
	if (isMobile()) {
		var thumbW = 35;
		var thumbH = 35;
	} else {
		var thumbW = 25;
		var thumbH = 25;
	}

	// set values
	$(id).css({
		top: marTop + "px",
		left: marLeft + "px",
		width: thisW + "px",
		height: thisH + "px",
		backgroundSize: (thisW + "px " + thisH + "px")
	});

	// set handler values
	$(id + " #thumb").css({
		width: thumbW + "px",
		height: thumbH + "px"
	});

	// save container size in one object
	var containerSize = {
		width: thisW,
		height: thisH
	};

	var angle = 10;
	var radius = getRadius(containerSize, $(id + " #thumb"));

	if (num == 1 && LAST_POSITION_1 != null)
		$(id + " #thumb").css(LAST_POSITION_1);
	else if (num == 2 && LAST_POSITION_2 != null)
		$(id + " #thumb").css(LAST_POSITION_2);
	else // normal case
		$(id + " #thumb").css(getThumbLocation(angle, radius, containerSize, $(id + " #thumb")));
}