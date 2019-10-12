var createEvent = function(eventName, ofsx, ofsy) {var ev = document.createEvent('MouseEvents');
    ev.initMouseEvent(eventName, true, false, null, 0, 0, 0, ofsx, ofsy, false, false, false, false, 0, null); return ev;};
var _auto_down = createEvent("mousedown",86,876);