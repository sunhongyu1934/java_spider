window._zhugeSdk = function(e) {
    function t(i) {
        if (n[i])
            return n[i].exports;
        var r = n[i] = {
            i: i,
            l: !1,
            exports: {}
        };
        return e[i].call(r.exports, r, r.exports, t),
            r.l = !0,
            r.exports
    }
    var n = {};
    return t.m = e,
        t.c = n,
        t.i = function(e) {
            return e
        }
        ,
        t.d = function(e, n, i) {
            t.o(e, n) || Object.defineProperty(e, n, {
                configurable: !1,
                enumerable: !0,
                get: i
            })
        }
        ,
        t.n = function(e) {
            var n = e && e.__esModule ? function() {
                    return e['default']
                }
                : function() {
                    return e
                }
            ;
            return t.d(n, "a", n),
                n
        }
        ,
        t.o = function(e, t) {
            return Object.prototype.hasOwnProperty.call(e, t)
        }
        ,
        t.p = "",
        t(t.s = 70)
}({
    0: function(e, t) {
        var n = window.navigator
            , i = window.document
            , r = n.userAgent
            , o = Array.prototype
            , s = Object.prototype
            , a = s.toString
            , c = o.forEach
            , u = Array.isArray
            , f = o.slice
            , d = s.hasOwnProperty
            , l = {}
            , p = {
            each: function(e, t, n) {
                if (null != e)
                    if (c && e.forEach === c)
                        e.forEach(t, n);
                    else if (e.length === +e.length) {
                        for (var i = 0, r = e.length; i < r; i++)
                            if (i in e && t.call(n, e[i], i, e) === l)
                                return
                    } else
                        for (var o in e)
                            if (d.call(e, o) && t.call(n, e[o], o, e) === l)
                                return
            },
            extend: function(e) {
                return p.each(f.call(arguments, 1), function(t) {
                    for (var n in t)
                        void 0 !== t[n] && (e[n] = t[n])
                }),
                    e
            },
            isUndefined: function(e) {
                return void 0 === e
            },
            isString: function(e) {
                return "[object String]" == a.call(e)
            },
            isArray: u || function(e) {
                return "[object Array]" === a.call(e)
            }
            ,
            isFunction: function(e) {
                return "[object Function]" === a.call(e)
            },
            isObject: function(e) {
                return "[object Object]" === a.call(e)
            },
            hasMobileSdk: function() {
                var e = !!(window.zhugeTracker || window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.zhugeTracker);
                return {
                    flag: e,
                    track: function(t, n) {
                        e && (window.zhugeTracker ? window.zhugeTracker.trackProperty(t, p.JSONEncode(n)) : window.webkit.messageHandlers.zhugeTracker.postMessage({
                            type: "track",
                            name: t,
                            prop: n
                        }))
                    },
                    identify: function(t, n) {
                        e && (window.zhugeTracker ? window.zhugeTracker.identifyProperty(t, p.JSONEncode(n)) : window.webkit.messageHandlers.zhugeTracker.postMessage({
                            type: "identify",
                            name: t,
                            prop: n
                        }))
                    }
                }
            },
            includes: function(e, t) {
                return -1 !== e.indexOf(t)
            },
            encode: function(e) {
                var t = {};
                for (var n in e)
                    t["_" + n] = e[n];
                return t
            },
            truncate: function(e, t) {
                var n;
                return "string" == typeof e ? n = e.slice(0, t) : p.isArray(e) ? (n = [],
                    p.each(e, function(e) {
                        n.push(p.truncate(e, t))
                    })) : p.isObject(e) ? (n = {},
                    p.each(e, function(e, i) {
                        n[i] = p.truncate(e, t)
                    })) : n = e,
                    n
            },
            strip_empty_properties: function(e) {
                var t = {};
                return p.each(e, function(e, n) {
                    p.isString(e) && e.length > 0 && (t[n] = e)
                }),
                    t
            },
            trim: function(e) {
                return (e || "").replace(/\s+/g, " ").replace(/^\s+/, "").replace(/\s+$/, "")
            },
            random: function(e, t) {
                return Math.round(Math.random() * (t - e)) + e
            },
            JSONEncode: function() {
                return function(e) {
                    var t = e
                        , n = function(e) {
                        var t = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g
                            , n = {
                            "\b": "\\b",
                            "\t": "\\t",
                            "\n": "\\n",
                            "\f": "\\f",
                            "\r": "\\r",
                            '"': '\\"',
                            "\\": "\\\\"
                        };
                        return t.lastIndex = 0,
                            t.test(e) ? '"' + e.replace(t, function(e) {
                                var t = n[e];
                                return "string" == typeof t ? t : "\\u" + ("0000" + e.charCodeAt(0).toString(16)).slice(-4)
                            }) + '"' : '"' + e + '"'
                    }
                        , i = function(e, t) {
                        var r = ""
                            , o = 0
                            , s = ""
                            , c = ""
                            , u = 0
                            , f = r
                            , l = []
                            , p = t[e];
                        switch (p && "object" == typeof p && "function" == typeof p.toJSON && (p = p.toJSON(e)),
                            typeof p) {
                            case "string":
                                return n(p);
                            case "number":
                                return isFinite(p) ? String(p) : "null";
                            case "boolean":
                            case "null":
                                return String(p);
                            case "object":
                                if (!p)
                                    return "null";
                                if (r += "    ",
                                        l = [],
                                    "[object Array]" === a.apply(p)) {
                                    for (u = p.length,
                                             o = 0; o < u; o += 1)
                                        l[o] = i(o, p) || "null";
                                    return c = 0 === l.length ? "[]" : r ? "[\n" + r + l.join(",\n" + r) + "\n" + f + "]" : "[" + l.join(",") + "]",
                                        r = f,
                                        c
                                }
                                for (s in p)
                                    d.call(p, s) && (c = i(s, p)) && l.push(n(s) + (r ? ": " : ":") + c);
                                return c = 0 === l.length ? "{}" : r ? "{" + l.join(",") + f + "}" : "{" + l.join(",") + "}",
                                    r = f,
                                    c
                        }
                    };
                    return i("", {
                        "": t
                    })
                }
            }(),
            JSONDecode: function() {
                var e, t, n, i, r = {
                    '"': '"',
                    "\\": "\\",
                    "/": "/",
                    b: "\b",
                    f: "\f",
                    n: "\n",
                    r: "\r",
                    t: "\t"
                }, o = function(t) {
                    throw {
                        name: "SyntaxError",
                        message: t,
                        at: e,
                        text: n
                    }
                }, s = function(i) {
                    return i && i !== t && o("Expected '" + i + "' instead of '" + t + "'"),
                        t = n.charAt(e),
                        e += 1,
                        t
                }, a = function() {
                    var e, n = "";
                    for ("-" === t && (n = "-",
                        s("-")); t >= "0" && t <= "9"; )
                        n += t,
                            s();
                    if ("." === t)
                        for (n += "."; s() && t >= "0" && t <= "9"; )
                            n += t;
                    if ("e" === t || "E" === t)
                        for (n += t,
                                 s(),
                             "-" !== t && "+" !== t || (n += t,
                                 s()); t >= "0" && t <= "9"; )
                            n += t,
                                s();
                    if (e = +n,
                            isFinite(e))
                        return e;
                    o("Bad number")
                }, c = function() {
                    var e, n, i, a = "";
                    if ('"' === t)
                        for (; s(); ) {
                            if ('"' === t)
                                return s(),
                                    a;
                            if ("\\" === t)
                                if (s(),
                                    "u" === t) {
                                    for (i = 0,
                                             n = 0; n < 4 && (e = parseInt(s(), 16),
                                        isFinite(e)); n += 1)
                                        i = 16 * i + e;
                                    a += String.fromCharCode(i)
                                } else {
                                    if ("string" != typeof r[t])
                                        break;
                                    a += r[t]
                                }
                            else
                                a += t
                        }
                    o("Bad string")
                }, u = function() {
                    for (; t && t <= " "; )
                        s()
                }, f = function() {
                    switch (t) {
                        case "t":
                            return s("t"),
                                s("r"),
                                s("u"),
                                s("e"),
                                !0;
                        case "f":
                            return s("f"),
                                s("a"),
                                s("l"),
                                s("s"),
                                s("e"),
                                !1;
                        case "n":
                            return s("n"),
                                s("u"),
                                s("l"),
                                s("l"),
                                null
                    }
                    o("Unexpected '" + t + "'")
                }, d = function() {
                    var e = [];
                    if ("[" === t) {
                        if (s("["),
                                u(),
                            "]" === t)
                            return s("]"),
                                e;
                        for (; t; ) {
                            if (e.push(i()),
                                    u(),
                                "]" === t)
                                return s("]"),
                                    e;
                            s(","),
                                u()
                        }
                    }
                    o("Bad array")
                }, l = function() {
                    var e, n = {};
                    if ("{" === t) {
                        if (s("{"),
                                u(),
                            "}" === t)
                            return s("}"),
                                n;
                        for (; t; ) {
                            if (e = c(),
                                    u(),
                                    s(":"),
                                Object.hasOwnProperty.call(n, e) && o('Duplicate key "' + e + '"'),
                                    n[e] = i(),
                                    u(),
                                "}" === t)
                                return s("}"),
                                    n;
                            s(","),
                                u()
                        }
                    }
                    o("Bad object")
                };
                return i = function() {
                    switch (u(),
                        t) {
                        case "{":
                            return l();
                        case "[":
                            return d();
                        case '"':
                            return c();
                        case "-":
                            return a();
                        default:
                            return t >= "0" && t <= "9" ? a() : f()
                    }
                }
                    ,
                    function(r) {
                        var s;
                        return n = r,
                            e = 0,
                            t = " ",
                            s = i(),
                            u(),
                        t && o("Syntax error"),
                            s
                    }
            }(),
            HTTPBuildQuery: function(e, t) {
                var n, i, r = [];
                return void 0 === t && (t = "&"),
                    p.each(e, function(e, t) {
                        n = encodeURIComponent(e.toString()),
                            i = encodeURIComponent(t),
                            r[r.length] = i + "=" + n
                    }),
                    r.join(t)
            },
            getQueryParam: function(e, t) {
                t = t.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
                var n = "[\\?&#]" + t + "=([^&#]*)"
                    , i = new RegExp(n)
                    , r = i.exec(e);
                if (null === r || r && "string" != typeof r[1] && r[1].length)
                    return "";
                var o = r[1];
                try {
                    o = decodeURIComponent(decodeURIComponent(o)).replace(/\+/g, " ")
                } catch (e) {}
                return o
            },
            getDomain: function(e) {
                var t = e.match(/\/\/\S*?\//);
                return t && t.length ? t[0].replace(/\//g, "") : ""
            },
            register_event: function() {
                function e(e, n, i) {
                    return function(r) {
                        if (r = r || t(window.event)) {
                            var o, s, a = !0;
                            return p.isFunction(i) && (o = i(r)),
                                s = n.call(e, r),
                            !1 !== o && !1 !== s || (a = !1),
                                a
                        }
                    }
                }
                function t(e) {
                    return e && (e.preventDefault = t.preventDefault,
                        e.stopPropagation = t.stopPropagation),
                        e
                }
                var n = function(t, n, i, r) {
                    if (!t)
                        return void console.error("No valid element provided to register_event");
                    if (t.addEventListener && !r)
                        t.addEventListener(n, i, !1);
                    else {
                        var o = "on" + n
                            , s = t[o];
                        t[o] = e(t, i, s)
                    }
                };
                return t.preventDefault = function() {
                    this.returnValue = !1
                }
                    ,
                    t.stopPropagation = function() {
                        this.cancelBubble = !0
                    }
                    ,
                    n
            }(),
            cookie: {
                get: function(e) {
                    for (var t = e + "=", n = i.cookie.split(";"), r = 0; r < n.length; r++) {
                        for (var o = n[r]; " " == o.charAt(0); )
                            o = o.substring(1, o.length);
                        if (0 == o.indexOf(t))
                            return decodeURIComponent(o.substring(t.length, o.length))
                    }
                    return null
                },
                parse: function(e) {
                    var t;
                    try {
                        t = p.JSONDecode(p.cookie.get(e)) || {}
                    } catch (e) {}
                    return t
                },
                set: function(e, t, n, r, o) {
                    var s = ""
                        , a = ""
                        , c = "";
                    if (r) {
                        var u = i.location.hostname.match(/[a-z0-9][a-z0-9\-]+\.[a-z\.]{2,6}$/i)
                            , f = u ? u[0] : "";
                        s = f ? "; domain=." + f : ""
                    }
                    if (n) {
                        var d = new Date;
                        d.setTime(d.getTime() + 24 * n * 60 * 60 * 1e3),
                            a = "; expires=" + d.toGMTString()
                    }
                    o && (c = "; secure"),
                        i.cookie = e + "=" + encodeURIComponent(t) + a + "; path=/" + s + c
                },
                remove: function(e) {
                    var t = i.location.hostname.match(/[a-z0-9][a-z0-9\-]+\.[a-z\.]{2,6}$/i)
                        , n = t ? t[0] : "";
                    p.cookie.set(e, "", -1, "." + n)
                }
            },
            info: {
                campaignParams: function() {
                    var e = "utm_source utm_medium utm_campaign utm_content utm_term".split(" ")
                        , t = ""
                        , n = {};
                    return p.each(e, function(e) {
                        t = p.getQueryParam(i.URL, e),
                        t.length && (n["$" + e] = t)
                    }),
                        n
                },
                searchEngine: function(e) {
                    return 0 === e.search("https?://(.*)google.([^/?]*)") ? "google" : 0 === e.search("https?://(.*)baidu.com") ? "baidu" : 0 === e.search("https?://(.*)sogou.com") ? "sogou" : 0 === e.search("https?://(.*)haosou.com") ? "haosou" : null
                },
                searchKeyword: function(e) {
                    var t = p.info.searchEngine(e);
                    return "google" == t ? p.getQueryParam(e, "q") : "baidu" == t ? p.getQueryParam(e, "wd") : "sogou" == t ? p.getQueryParam(e, "query") : "haosou" == t ? p.getQueryParam(e, "q") : null
                },
                referringDomain: function(e) {
                    var t = e.split("/");
                    return t.length >= 3 ? t[2] : ""
                },
                browser: function(e, t, n) {
                    var t = t || "";
                    return n ? p.includes(e, "Mini") ? "Opera Mini" : "Opera" : /(BlackBerry|PlayBook|BB10)/i.test(e) ? "BlackBerry" : p.includes(e, "FBIOS") ? "Facebook Mobile" : p.includes(e, "Chrome") ? "Chrome" : p.includes(e, "CriOS") ? "Chrome iOS" : p.includes(t, "Apple") ? p.includes(e, "Mobile") ? "Mobile Safari" : "Safari" : p.includes(e, "Android") ? "Android Mobile" : p.includes(e, "Konqueror") ? "Konqueror" : p.includes(e, "Firefox") ? "Firefox" : p.includes(e, "MSIE") || p.includes(e, "Trident/") ? "Internet Explorer" : p.includes(e, "Gecko") ? "Mozilla" : ""
                },
                os: function() {
                    var e = r;
                    return /Windows/i.test(e) ? /Phone/.test(e) ? "Windows Mobile" : "Windows" : /(iPhone|iPad|iPod)/.test(e) ? "iOS" : /Android/.test(e) ? "Android" : /(BlackBerry|PlayBook|BB10)/i.test(e) ? "BlackBerry" : /Mac/i.test(e) ? "Mac OS X" : /Linux/.test(e) ? "Linux" : ""
                },
                device: function(e) {
                    return /iPad/.test(e) ? "iPad" : /iPod/.test(e) ? "iPod Touch" : /iPhone/.test(e) ? "iPhone" : /(BlackBerry|PlayBook|BB10)/i.test(e) ? "BlackBerry" : /Windows Phone/i.test(e) ? "Windows Phone" : /Android/.test(e) ? "Android" : ""
                },
                resolution: function() {
                    return screen.width + "*" + screen.height
                }
            },
            UUID: function() {
                var e = function() {
                    for (var e = 1 * new Date, t = 0; e == 1 * new Date; )
                        t++;
                    return e.toString(16) + t.toString(16)
                }
                    , t = function() {
                    return Math.random().toString(16).replace(".", "")
                }
                    , n = function(e) {
                    function t(e, t) {
                        var n, i = 0;
                        for (n = 0; n < t.length; n++)
                            i |= s[n] << 8 * n;
                        return e ^ i
                    }
                    var n, i, o = r, s = [], a = 0;
                    for (n = 0; n < o.length; n++)
                        i = o.charCodeAt(n),
                            s.unshift(255 & i),
                        s.length >= 4 && (a = t(a, s),
                            s = []);
                    return s.length > 0 && (a = t(a, s)),
                        a.toString(16)
                };
                return function() {
                    var i = (screen.height * screen.width).toString(16);
                    return e() + "-" + t() + "-" + n() + "-" + i + "-" + e()
                }
            }()
        };
        e.exports = p
    },
    12: function(e, t, n) {
        var i = e.exports.json = !1;
        e.exports.url = {
            checkLogin: i ? "http://tongji.qichacha.com:8080//json/checkLogin.json" : "http://tongji.qichacha.com:8080/user/checkAuthorization.jsp",
            eventList: i ? "http://tongji.qichacha.com:8080//json/eventList.json" : "http://tongji.qichacha.com:8080/data/datalist.jsp",
            loadEnvData: i ? "http://tongji.qichacha.com:8080//json/envData.json" : "http://tongji.qichacha.com:8080/appusergroup/getEventEnvData.jsp",
            loadUserPropData: i ? "http://tongji.qichacha.com:8080//json/userProps.json" : "http://tongji.qichacha.com:8080/appusergroup/getUserPropMeta.jsp"
        },
            e.exports.analysis = {
                sessionStorageKey: "zhugeAnalysisQueryParam"
            }
    },
    28: function(e, t, n) {
        function i(e, t) {
            this.tracker = e,
                this.config = r.extend({
                    open: !1,
                    isClickAble: null,
                    singlePage: !1
                }, t),
            this.config.open && this._init()
        }
        var r = n(0)
            , o = n(6)
            , s = n(7)
            , a = n(12)
            , c = n(5);
        i.prototype = {
            constructor: i,
            _init: function() {
                this.config.singlePage && this._initHistoryHook(),
                    o.ready(function() {
                        this._initEventBind(),
                            this._onView(),
                            this._checkPermissions()
                    }, this)
            },
            _initHistoryHook: function() {
                var e = window.history
                    , t = e.pushState
                    , n = this;
                e.pushState = function(i) {
                    var r = t.apply(e, arguments);
                    return n._onView(),
                        r
                }
            },
            _initEventBind: function() {
                o.bind(window, "message", this._onMessage, this),
                this.config.singlePage && (o.bind(window, "popstate", this._onView, this),
                    o.bind(window, "hashchange", this._onView, this))
            },
            _checkPermissions: function() {
                window.sessionStorage.getItem("isZhugeAutoTrackAnalysisMode") && c(a.url.checkLogin, {
                    type: a.json ? "get" : "post",
                    context: this,
                    success: function(e) {
                        e.authorized && this._loadAnalysisCode()
                    },
                    error: function(e) {
                        console.error("加载分析脚本失败", e)
                    }
                })
            },
            _loadAnalysisCode: function() {
                window.sessionStorage.setItem("isZhugeAutoTrackAnalysisMode", JSON.stringify({
                    flag: !0
                }));
                var e = "http://tongji.qichacha.com:8080/webapp/lib/sdk/analysis.min.js".replace(/\"/g, "");
                o.loadJs({
                    src: e + "?a=" + Math.random()
                })
            },
            _onMessage: function() {
                if ("http://tongji.qichacha.com:8080" === event.origin) {
                    var e = event.data;
                    switch (e.command) {
                        case "authorized":
                            var t = window.opener || window.parent;
                            if (t === window)
                                return;
                            t.postMessage({
                                command: "hasOpenAutoTrackAnalysis"
                            }, "http://tongji.qichacha.com:8080"),
                                this._loadAnalysisCode();
                            break;
                        default:
                            console.warn("未识别的message信息", e)
                    }
                }
            },
            _onView: function() {
                this.tracker.batchTrack([{
                    dt: "abp",
                    eid: "pv",
                    param: {
                        $page_title: document.title
                    }
                }])
            },
            getEvent: function(e) {
                var t = o.getUniqueSelector(e.target)
                    , n = e.target;
                return {
                    dt: "abp",
                    eid: "click",
                    param: {
                        $page_title: window.document.title,
                        $element_id: o.getAttr(n, "id"),
                        $element_content: o.getTextContent(n),
                        $element_type: o.getTagName(n),
                        $element_style: o.getAttr(n, "class"),
                        $element_selector: s(t),
                        $element_link: e.isValidLink ? o.getAttr(n, "href") : null
                    }
                }
            }
        },
            e.exports = i
    },
    30: function(e, t, n) {
        function i(e, t) {
            this.config = r.extend({
                open: !1,
                stopTrack: null
            }, t || {}),
                this.tracker = e,
                this.eventList = [],
                this.loaded = !1,
            this.config.open && this._init()
        }
        var r = n(0)
            , o = n(6)
            , s = n(5)
            , a = n(8);
        i.prototype = {
            constructor: i,
            _init: function() {
                o.ready(function() {
                    this._initEventBind(),
                        this._loadEventList(),
                    window.sessionStorage.getItem(a.SESSION_KEYS.IS_ANALYSIS_MODE) && this._loadAnalysisCode()
                }, this)
            },
            _initEventBind: function() {
                o.bind(window, "message", this._onMessage, this)
            },
            _loadEventList: function() {
                s("http://tongji.qichacha.com:8080v2/visual", {
                    type: "get",
                    data: {
                        url: location.href,
                        app_key: this.tracker.getKey(),
                        platform: 3
                    },
                    context: this,
                    jsonp: !0,
                    success: function(e) {
                        10001 === e.code && (this.eventList = e.visual_events)
                    }
                })
            },
            _onMessage: function(e) {
                var t = e.origin
                    , n = e.data;
                if ("http://tongji.qichacha.com:8080" === t)
                    switch (n.command) {
                        case "openJsVisualizer":
                            window.opener.postMessage({
                                command: "getQueryParam"
                            }, t);
                            break;
                        case "getQueryParam":
                            window.sessionStorage.setItem(a.SESSION_KEYS.PARAMS, JSON.stringify(n.data)),
                                this._loadAnalysisCode()
                    }
            },
            _loadAnalysisCode: function() {
                if (!this.loaded) {
                    this.loaded = !0,
                        this.config.stopTrack(),
                        window.sessionStorage.setItem(a.SESSION_KEYS.IS_ANALYSIS_MODE, JSON.stringify({
                            flag: !0
                        }));
                    var e = "http://tongji.qichacha.com:8080/webapp/lib/sdk/visualizer.min.js".replace(/\"/g, "");
                    o.loadJs({
                        src: e + "?a=" + Math.random()
                    })
                }
            },
            getEvent: function(e) {
                for (var t = [], n = 0, i = this.eventList.length; n < i; n++)
                    for (var r = this.eventList[n], s = 0; s < r.element.length; s++)
                        if (o.is(e.target, r.element[s]).flag) {
                            for (var a = {
                                element_content: o.getTextContent(e.target),
                                element_link: e.isValidLink ? o.getAttr(e.target, "href") : null
                            }, c = 0; c < r.attr.length; c++) {
                                var u = r.attr[c];
                                a[u.name] = o.getTextContent(o.query(u.selector)[0])
                            }
                            var f = {
                                dt: "evt",
                                eid: r.event_name,
                                param: a
                            };
                            t.push(f)
                        }
                return t
            }
        },
            e.exports = i
    },
    32: function(e, t, n) {
        var i = n(0)
            , r = n(69)
            , o = function(e) {
            this.config = {},
                i.extend(this.config, e),
                this.idle = 0,
                this.last_activity = new Date
        };
        o.prototype._init = function(e, t) {
            if (this._key = e,
                    this._jsc = function() {}
                    ,
                    i.isObject(t)) {
                i.extend(this.config, t);
                for (var n in this.config)
                    i.isObject(this.config[n]) && (this.config[n] = i.JSONEncode(this.config[n]))
            }
            this._initDid(),
                this.cookie = new r("zg_" + this._key,this.config);
            var o = i.cookie.get("_zg");
            o && this.config.inherit_user_data ? (this.cookie.register_once(i.JSONDecode(o), ""),
                i.cookie.remove("_zg")) : this.cookie.register_once({
                sid: 0,
                updated: 0,
                info: 0,
                superProperty: this.config.superProperty,
                platform: this.config.platform
            }, ""),
                this._session(),
                this._info(),
                this._startPing()
        }
            ,
            o.prototype._session = function() {
                var e = this.cookie.props.updated
                    , t = this.cookie.props.sid
                    , n = 1 * new Date
                    , r = new Date;
                if (0 == t || n > e + 60 * this.config.session_interval_mins * 1e3) {
                    if (t > 0 && e > 0) {
                        var o = {
                            dt: "se",
                            pr: {}
                        };
                        o.pr.$ct = r.getTime(),
                            o.pr.$tz = 6e4 * -r.getTimezoneOffset(),
                            o.pr.$dru = e - t,
                            o.pr.$sid = t,
                            o.pr.$cuid = this.cookie.props.cuid,
                            this._batchTrack(o)
                    }
                    t = n;
                    var s = {
                        dt: "ss",
                        pr: {}
                    }
                        , a = i.info.campaignParams();
                    s.pr = i.extend(s.pr, a),
                        this.cookie.register({
                            utm: i.JSONEncode(a)
                        }, ""),
                        s.pr.$ct = r.getTime(),
                        s.pr.$sid = t,
                        s.pr.$cuid = this.cookie.props.cuid,
                        s.pr.$cn = this.config.app_channel,
                        s.pr.$vn = this.config.app_version,
                        s.pr.$tz = 6e4 * -r.getTimezoneOffset(),
                        s.pr.$url = location.href,
                        s.pr.$ref = document.referrer;
                    var c = i.getDomain(document.referrer);
                    s.pr.$referrer_domain = c,
                        this.cookie.register({
                            referrerDomain: c
                        }, ""),
                        this._batchTrack(s),
                        this.cookie.register({
                            sid: t
                        }, "")
                }
                this.cookie.register({
                    updated: n
                }, "")
            }
            ,
            o.prototype._info = function() {
                var e = this.cookie.props.info
                    , t = 1 * new Date;
                if (t > e + 24 * this.config.info_upload_interval_days * 60 * 60 * 1e3) {
                    var n = {
                        dt: "pl",
                        pr: {
                            $rs: i.info.resolution()
                        }
                    }
                        , r = new Date;
                    n.pr.$tz = 6e4 * -r.getTimezoneOffset(),
                        n.pr.$ct = r.getTime(),
                        n.pr.$cuid = this.cookie.props.cuid,
                        n.pr = i.extend(n.pr, i.encode(i.JSONDecode(this.cookie.props.platform))),
                        this._batchTrack(n),
                        this.cookie.register({
                            info: t
                        }, "")
                }
            }
            ,
            o.prototype.debug = function(e) {
                this.config.debug = e
            }
            ,
            o.prototype.identify = function(e, t, n) {
                e += "";
                var r = i.isObject(t) ? t : {}
                    , o = i.isObject(t) ? n : t;
                this.cookie.register({
                    cuid: e
                }, ""),
                    this._session();
                var s = i.hasMobileSdk();
                if (s.flag)
                    s.identify(e, r),
                    i.isFunction(o) && o();
                else {
                    var a = {
                        dt: "usr",
                        pr: {}
                    }
                        , c = new Date;
                    a.pr.$ct = c.getTime(),
                        a.pr.$tz = 6e4 * -c.getTimezoneOffset(),
                        a.pr.$cuid = e,
                        a.pr.$sid = this.cookie.props.sid,
                        a.pr.$url = location.href,
                        a.pr.$ref = document.referrer,
                        a.pr = i.extend(a.pr, i.encode(r)),
                        this._batchTrack(a, o)
                }
            }
            ,
            o.prototype.setUserProperties = function(e, t) {
                this.identify(this.cookie.props.cuid, e, t)
            }
            ,
            o.prototype.page = function(e, t) {
                this._session();
                var n = document.location.href
                    , i = {};
                i.et = "pg",
                    i.pid = n,
                    i.pn = void 0 === e ? n : e,
                    i.tl = document.title,
                    i.ref = document.referrer,
                    i.sid = this.cookie.props.sid,
                    this._batchTrack(i, t)
            }
            ,
            o.prototype.track = function(e, t, n) {
                var r = i.isObject(t) ? t : {}
                    , o = i.isObject(t) ? n : t
                    , s = i.hasMobileSdk();
                s.flag ? (s.track(e, r),
                i.isFunction(o) && o()) : this.batchTrack([{
                    dt: "evt",
                    eid: e,
                    param: r
                }], o)
            }
            ,
            o.prototype.batchTrack = function(e, t) {
                this._session();
                for (var n = [], r = new Date, o = this.cookie.props.utm ? i.JSONDecode(this.cookie.props.utm) : {}, s = 0, a = e.length; s < a; s++) {
                    var c = e[s]
                        , u = {
                        dt: c.dt,
                        pr: {}
                    };
                    u.pr.$ct = r.getTime(),
                        u.pr.$tz = 6e4 * -r.getTimezoneOffset(),
                        u.pr.$cuid = this.cookie.props.cuid,
                        u.pr.$sid = this.cookie.props.sid,
                        u.pr.$url = document.URL,
                        u.pr.$ref = document.referrer,
                        u.pr.$referrer_domain = this.cookie.props.referrerDomain,
                        u.pr.$eid = c.eid;
                    for (var f in o)
                        u.pr[f] = o[f];
                    "evt" === c.dt ? u.pr = i.extend(u.pr, i.encode(c.param)) : u.pr = i.extend(u.pr, c.param),
                        u.pr = i.extend(u.pr, i.encode(i.JSONDecode(this.cookie.props.superProperty))),
                        n.push(u)
                }
                this._batchTrack(n, t)
            }
            ,
            o.prototype._moved = function(e) {
                this.last_activity = new Date,
                    this.idle = 0
            }
            ,
            o.prototype._startPing = function() {
                var e = this;
                i.register_event(window, "mousemove", function() {
                    e._moved.apply(e, arguments)
                }),
                void 0 === this.pingInterval && (this.pingInterval = window.setInterval(function() {
                    e._ping()
                }, this.config.ping_interval))
            }
            ,
            o.prototype._stopPing = function() {
                void 0 !== this.pingInterval && (window.clearInterval(this.pingInterval),
                    delete this.pingInterval)
            }
            ,
            o.prototype._ping = function() {
                if (this.config.ping && this.idle < this.config.idle_timeout) {
                    var e = {};
                    e.type = "ping",
                        e.sdk = "web",
                        e.sdkv = "2.0",
                        e.ak = this._key,
                        e.did = this.did.props.did,
                        e.cuid = this.cookie.props.cuid,
                        this._sendTrackRequest(e)
                } else
                    this._stopPing();
                var t = new Date;
                return t - this.last_activity > this.config.idle_threshold && (this.idle = t - this.last_activity),
                    this
            }
            ,
            o.prototype.getDid = function() {
                return this.did.props.did
            }
            ,
            o.prototype.getSid = function() {
                return this.cookie.props.sid
            }
            ,
            o.prototype.setSuperProperty = function(e) {
                i.isObject(e) && this.cookie.register({
                    superProperty: i.JSONEncode(e)
                })
            }
            ,
            o.prototype.setPlatform = function(e) {
                i.isObject(e) && (this.cookie.register({
                    platform: i.JSONEncode(e),
                    info: 0
                }),
                    this._info())
            }
            ,
            o.prototype._batchTrack = function(e, t) {
                var n = {}
                    , r = new Date;
                n.sln = "itn",
                    n.pl = "js",
                    n.sdk = "zg-js",
                    n.sdkv = "2.0",
                    n.owner = "zg",
                    n.ut = [r.getFullYear(), r.getMonth() + 1, r.getDate()].join("-") + " " + r.toTimeString().match(/\d{2}:\d{2}:\d{2}/)[0],
                    n.tz = 6e4 * -r.getTimezoneOffset(),
                    n.debug = this.config.debug ? 1 : 0,
                    n.ak = this._key,
                    n.usr = {
                        did: this.did.props.did
                    };
                var o = [];
                i.isArray(e) ? o = e : o.push(e),
                    n.data = o,
                    this._sendTrackRequest(n, this._prepareCallback(t, n))
            }
            ,
            o.prototype._prepareCallback = function(e, t) {
                return i.isFunction(e) ? function(n) {
                        e(n, t)
                    }
                    : null
            }
            ,
            o.prototype._initDid = function() {
                var e = i.cookie.get("_zg")
                    , t = i.UUID();
                e && i.JSONDecode(e).uuid && (t = i.JSONDecode(e).uuid),
                i.cookie.get("zg_did") || i.cookie.remove("zg_" + this._key),
                    this.did = new r("zg_did",this.config),
                    this.did.register_once({
                        did: t
                    }, "")
            }
            ,
            o.prototype._sendTrackRequest = function(e, t) {
                var n = i.truncate(e, 255)
                    , r = i.JSONEncode(n)
                    , o = {
                    event: r,
                    _: (new Date).getTime().toString()
                }
                    , s = {
                    bac: this.config.api_host_bac + "&" + i.HTTPBuildQuery(o),
                    normal: this.config.api_host + "&" + i.HTTPBuildQuery(o)
                };
                this._sendRequest(s, t)
            }
            ,
            o.prototype._sendRequest = function(e, t) {
                var n = new Image
                    , i = !1
                    , r = setTimeout(function() {
                    !i && t && (t(),
                        i = !0)
                }, 500)
                    , o = function() {
                    !i && t && (clearTimeout(r),
                        t())
                };
                n.onload = o,
                    n.onerror = function() {
                        var t = new Image;
                        t.onload = o,
                            t.onerror = o,
                            t.src = e.bac
                    }
                    ,
                    n.src = e.normal
            }
            ,
            o.prototype.push = function(e) {
                var t = e.shift();
                this[t] && this[t].apply(this, e)
            }
            ,
            o.prototype.getKey = function() {
                return this._key
            }
            ,
            e.exports = o
    },
    5: function(e, t, n) {
        function i() {
            return window.XMLHttpRequest ? new XMLHttpRequest : new ActiveXObject("Microsoft.XMLHTTP")
        }
        function r(e, t) {
            var n = e.responseText;
            switch (t) {
                case "json":
                    n = JSON.parse(n);
                    break;
                default:
                    console && console.error(arguments, this)
            }
            return n
        }
        function o(e) {
            var t = [];
            for (var n in e) {
                var i = e[n];
                t.push(encodeURIComponent(n) + "=" + encodeURIComponent(c.isString(i) ? i : JSON.stringify(i)))
            }
            return t.join("&")
        }
        function s(e, t) {
            var n = i()
                , s = function(e) {
                c.isFunction(t[e]) && t[e].call(t.context, r(n, t.dataType))
            };
            switch (n.onreadystatechange = function() {
                if (n.readyState === u.DONE)
                    if (n.status >= 200 && n.status <= 299)
                        try {
                            s("success")
                        } catch (e) {
                            s("error")
                        }
                    else
                        s("error")
            }
                ,
                n.onerror = function() {
                    s("error"),
                    console && console.error(arguments, this)
                }
                ,
                n.withCredentials = !0,
                t.type.toLowerCase()) {
                case "get":
                    e += "?" + o(t.data),
                        n.open(t.type, e, t.sync),
                        n.send();
                    break;
                case "post":
                    var a = o(t.data);
                    n.open(t.type, e, t.sync),
                        n.setRequestHeader("Content-Type", "application/x-www-form-urlencoded"),
                        n.send(a);
                    break;
                default:
                    console && console.error(arguments, this)
            }
        }
        function a(e, t) {
            var n = function(e, n) {
                c.isFunction(t[e]) && t[e].call(t.context, n)
            }
                , i = document.createElement("script")
                , r = "callback" + Math.random().toString().split(".")[1]
                , s = e + "?";
            t.data.callback = r,
                window[r] = function(e) {
                    n("success", e)
                }
                ,
                i.src = s + o(t.data),
                i.onerror = function() {
                    n("error")
                }
                ,
                document.body.appendChild(i)
        }
        var c = n(0)
            , u = {
            UNSENT: 0,
            OPENED: 1,
            HEADERS_RECEIVED: 2,
            LOADING: 3,
            DONE: 4
        };
        e.exports = function(e, t) {
            var n = c.extend({
                type: "post",
                data: {},
                dataType: "json",
                success: null,
                error: null,
                sync: !0,
                context: this,
                jsonp: !1
            }, t);
            n.jsonp ? a(e, n) : s(e, n)
        }
    },
    6: function(e, t, n) {
        var i = n(0)
            , r = {
            prefix: {
                expand: "zhuge-auto-track-",
                interface: "zhuge-interface-"
            },
            body: document.getElementsByTagName("body")[0],
            ready: function(e, t) {
                if (this.body)
                    e.call(t);
                else
                    var n = this
                        , i = setInterval(function() {
                        window.document.body && (n.body = window.document.body,
                            clearInterval(i),
                            e.call(t))
                    }, 50)
            },
            addClass: function(e, t) {
                var n = this.getAttr(e, "class") || "";
                new RegExp(t).test(n) || (n += " " + t,
                    r.setAttr(e, "class", i.trim(n)))
            },
            removeClass: function(e, t) {
                var n = this.getAttr(e, "class") || "";
                r.setAttr(e, "class", i.trim(n.replace(t, "")))
            },
            bind: function(e, t, n, r) {
                r = r || e;
                var o = function(t) {
                    if (t.target = t.target || t.srcElement,
                            i.isFunction(n)) {
                        if (!1 === n.call(r, t || window.event, e))
                            return t.preventDefault && t.preventDefault(),
                            t.stopPropagation && t.stopPropagation(),
                            t.returnValue && (t.returnValue = !1),
                            t.cancelBubble && (t.cancelBubble = !0),
                                !1
                    }
                };
                if (e.addEventListener)
                    e.addEventListener(t, o, !1);
                else if (e.attachEvent)
                    e.attachEvent("on" + t, o);
                else {
                    var s = "on" + t
                        , a = e[s];
                    e[s] = function(t) {
                        return i.isFunction(a) && a.call(e, t || window.event),
                            n.call(r, t || window.event, e)
                    }
                }
                return {
                    unbind: function() {
                        e.removeEventListener ? e.removeEventListener(t, o) : e.detachEvent && e.detachEvent("on" + t, o)
                    }
                }
            },
            data: function(e, t) {
                var n = e.dataset || {};
                if (i.isString(t))
                    return n[t];
                for (var r in t)
                    n[r] = t[r]
            },
            getTagName: function(e) {
                return e.tagName.toLowerCase()
            },
            getAttr: function(e, t) {
                return e.getAttribute(t)
            },
            setAttr: function(e, t, n) {
                if (i.isObject(t))
                    for (var r in t)
                        e.setAttribute(r, t[r]);
                else
                    e.setAttribute(t, n)
            },
            isValidLink: function(e) {
                return "a" === this.getTagName(e) && this.getAttr(e, "href") && !/^javascript:/.test(this.getAttr(e, "href"))
            },
            is: function(e, t) {
                for (var n = !1, i = this.query(t), r = e; r && !n && "body" !== this.getTagName(r); ) {
                    for (var o = 0, s = i.length; o < s; o++) {
                        if (i[o] === r) {
                            n = !0;
                            break
                        }
                    }
                    n || (r = this.getParent(r))
                }
                return {
                    flag: n,
                    target: r
                }
            },
            isClickAble: function(e, t, n) {
                if (!e || this.isTextNode(e))
                    return {
                        flag: !1
                    };
                var r = this.getTagName(e)
                    , o = this.getAttr(e, "type")
                    , s = this.isValidLink(e)
                    , a = {
                    flag: !1,
                    target: e,
                    isValidLink: s,
                    form: null
                };
                switch (r) {
                    case "a":
                        if (s) {
                            a.flag = !0;
                            break
                        }
                    case "button":
                        e.disabled || (a.flag = !0);
                        break;
                    case "input":
                        /button|reset|submit/.test(o) && !e.disabled && (a.flag = !0,
                        "submit" === o && (a.form = e.form));
                        break;
                    case "body":
                        a.flag = !1;
                        break;
                    default:
                        if (this.hasBindClick(e) || i.isFunction(t) && t(e))
                            a.flag = !0;
                        else if (!n)
                            return this.isClickAble(this.getParent(e))
                }
                return a
            },
            hasBindClick: function(e) {
                return !!(e.onclick || window.jQuery && (window.jQuery._data || window.jQuery.data)(e, "events"))
            },
            getParent: function(e) {
                return e.parentNode
            },
            getIndexInParent: function(e) {
                for (var t = this.getParent(e), n = 0, i = this.getSelector(e), r = this.query(t, i), o = 0, s = r.length; o < s; o++) {
                    if (r[o] === e)
                        return n;
                    n++
                }
                return n
            },
            getSelector: function(e) {
                for (var t = this.getAttr(e, "id"), n = this.getTagName(e), r = i.trim(this.getAttr(e, "class")).split(/\s/), o = [], s = 0, a = r.length; s < a; s++)
                    new RegExp(this.prefix.interface + "|" + this.prefix.expand).test(r[s]) || o.push(r[s]);
                return o = o.join("."),
                    t ? "#" + t : (o = o ? "." + o : "",
                    n + o)
            },
            getUniqueSelector: function(e, t) {
                var n = this.getSelector(e);
                return /#/.test(n) || "body" === this.getTagName(e) ? (t && (n = n + ">" + t),
                    n) : (n += ":eq(" + this.getIndexInParent(e) + ")",
                t && (n = n + ">" + t),
                    this.getUniqueSelector(this.getParent(e), n))
            },
            getTextContent: function(e) {
                return e.textContent || e.innerText || this.getAttr(e, "type") || e.value || ""
            },
            query: function() {
                var e = ""
                    , t = window.document
                    , n = /:eq\(\d+\)/g;
                if (i.isString(arguments[0]) ? e = arguments[0] : (t = arguments[0] || t,
                        e = arguments[1]),
                        this.isTextNode(t))
                    return [];
                if (n.test(e)) {
                    for (var r = e.split(n), o = e.match(n), s = null, a = 0, c = r.length; a < c; a++) {
                        var u = r[a].replace(/^>/, "");
                        if (!u)
                            break;
                        if (s = this.query(s || t, u),
                                !(o.length > a))
                            return s;
                        var f = o[a].match(/\d+/)[0];
                        if (!(s && f < s.length))
                            return [];
                        s = s[f]
                    }
                    return [s]
                }
                return t.querySelectorAll(e)
            },
            isTextNode: function(e) {
                return "#text" === e.nodeName
            },
            loadJs: function(e) {
                e = i.extend({
                    async: !0,
                    src: "",
                    onLoad: null,
                    onError: null,
                    context: null
                }, e || {});
                var t = document.createElement("script");
                this.setAttr(t, {
                    async: e.async,
                    src: e.src
                }),
                    this.bind(t, "load", e.onLoad, e.context),
                    this.bind(t, "error", e.onError, e.context),
                    this.body.appendChild(t)
            }
        };
        e.exports = r
    },
    69: function(e, t, n) {
        var i = n(0)
            , r = function(e, t) {
            this.name = e,
                this.props = {},
                this.config = i.extend({}, t),
                this.load()
        };
        r.prototype.load = function() {
            var e = i.cookie.parse(this.name);
            e && (this.props = i.extend({}, e))
        }
            ,
            r.prototype.save = function() {
                i.cookie.set(this.name, i.JSONEncode(this.props), this.config.cookie_expire_days, this.config.cookie_cross_subdomain, this.config.cookie_secure)
            }
            ,
            r.prototype.register_once = function(e, t) {
                return !!i.isObject(e) && (void 0 === t && (t = "None"),
                    i.each(e, function(e, n) {
                        this.props[n] && this.props[n] !== t || (this.props[n] = e)
                    }, this),
                    this.save(),
                    !0)
            }
            ,
            r.prototype.register = function(e) {
                return !!i.isObject(e) && (i.extend(this.props, e),
                    this.save(),
                    !0)
            }
            ,
            e.exports = r
    },
    7: function(e, t) {
        function n(e, t) {
            var n = (65535 & e) + (65535 & t);
            return (e >> 16) + (t >> 16) + (n >> 16) << 16 | 65535 & n
        }
        function i(e, t) {
            return e << t | e >>> 32 - t
        }
        function r(e, t, r, o, s, a) {
            return n(i(n(n(t, e), n(o, a)), s), r)
        }
        function o(e, t, n, i, o, s, a) {
            return r(t & n | ~t & i, e, t, o, s, a)
        }
        function s(e, t, n, i, o, s, a) {
            return r(t & i | n & ~i, e, t, o, s, a)
        }
        function a(e, t, n, i, o, s, a) {
            return r(t ^ n ^ i, e, t, o, s, a)
        }
        function c(e, t, n, i, o, s, a) {
            return r(n ^ (t | ~i), e, t, o, s, a)
        }
        function u(e, t) {
            e[t >> 5] |= 128 << t % 32,
                e[14 + (t + 64 >>> 9 << 4)] = t;
            var i, r, u, f, d, l = 1732584193, p = -271733879, h = -1732584194, g = 271733878;
            for (i = 0; i < e.length; i += 16)
                r = l,
                    u = p,
                    f = h,
                    d = g,
                    l = o(l, p, h, g, e[i], 7, -680876936),
                    g = o(g, l, p, h, e[i + 1], 12, -389564586),
                    h = o(h, g, l, p, e[i + 2], 17, 606105819),
                    p = o(p, h, g, l, e[i + 3], 22, -1044525330),
                    l = o(l, p, h, g, e[i + 4], 7, -176418897),
                    g = o(g, l, p, h, e[i + 5], 12, 1200080426),
                    h = o(h, g, l, p, e[i + 6], 17, -1473231341),
                    p = o(p, h, g, l, e[i + 7], 22, -45705983),
                    l = o(l, p, h, g, e[i + 8], 7, 1770035416),
                    g = o(g, l, p, h, e[i + 9], 12, -1958414417),
                    h = o(h, g, l, p, e[i + 10], 17, -42063),
                    p = o(p, h, g, l, e[i + 11], 22, -1990404162),
                    l = o(l, p, h, g, e[i + 12], 7, 1804603682),
                    g = o(g, l, p, h, e[i + 13], 12, -40341101),
                    h = o(h, g, l, p, e[i + 14], 17, -1502002290),
                    p = o(p, h, g, l, e[i + 15], 22, 1236535329),
                    l = s(l, p, h, g, e[i + 1], 5, -165796510),
                    g = s(g, l, p, h, e[i + 6], 9, -1069501632),
                    h = s(h, g, l, p, e[i + 11], 14, 643717713),
                    p = s(p, h, g, l, e[i], 20, -373897302),
                    l = s(l, p, h, g, e[i + 5], 5, -701558691),
                    g = s(g, l, p, h, e[i + 10], 9, 38016083),
                    h = s(h, g, l, p, e[i + 15], 14, -660478335),
                    p = s(p, h, g, l, e[i + 4], 20, -405537848),
                    l = s(l, p, h, g, e[i + 9], 5, 568446438),
                    g = s(g, l, p, h, e[i + 14], 9, -1019803690),
                    h = s(h, g, l, p, e[i + 3], 14, -187363961),
                    p = s(p, h, g, l, e[i + 8], 20, 1163531501),
                    l = s(l, p, h, g, e[i + 13], 5, -1444681467),
                    g = s(g, l, p, h, e[i + 2], 9, -51403784),
                    h = s(h, g, l, p, e[i + 7], 14, 1735328473),
                    p = s(p, h, g, l, e[i + 12], 20, -1926607734),
                    l = a(l, p, h, g, e[i + 5], 4, -378558),
                    g = a(g, l, p, h, e[i + 8], 11, -2022574463),
                    h = a(h, g, l, p, e[i + 11], 16, 1839030562),
                    p = a(p, h, g, l, e[i + 14], 23, -35309556),
                    l = a(l, p, h, g, e[i + 1], 4, -1530992060),
                    g = a(g, l, p, h, e[i + 4], 11, 1272893353),
                    h = a(h, g, l, p, e[i + 7], 16, -155497632),
                    p = a(p, h, g, l, e[i + 10], 23, -1094730640),
                    l = a(l, p, h, g, e[i + 13], 4, 681279174),
                    g = a(g, l, p, h, e[i], 11, -358537222),
                    h = a(h, g, l, p, e[i + 3], 16, -722521979),
                    p = a(p, h, g, l, e[i + 6], 23, 76029189),
                    l = a(l, p, h, g, e[i + 9], 4, -640364487),
                    g = a(g, l, p, h, e[i + 12], 11, -421815835),
                    h = a(h, g, l, p, e[i + 15], 16, 530742520),
                    p = a(p, h, g, l, e[i + 2], 23, -995338651),
                    l = c(l, p, h, g, e[i], 6, -198630844),
                    g = c(g, l, p, h, e[i + 7], 10, 1126891415),
                    h = c(h, g, l, p, e[i + 14], 15, -1416354905),
                    p = c(p, h, g, l, e[i + 5], 21, -57434055),
                    l = c(l, p, h, g, e[i + 12], 6, 1700485571),
                    g = c(g, l, p, h, e[i + 3], 10, -1894986606),
                    h = c(h, g, l, p, e[i + 10], 15, -1051523),
                    p = c(p, h, g, l, e[i + 1], 21, -2054922799),
                    l = c(l, p, h, g, e[i + 8], 6, 1873313359),
                    g = c(g, l, p, h, e[i + 15], 10, -30611744),
                    h = c(h, g, l, p, e[i + 6], 15, -1560198380),
                    p = c(p, h, g, l, e[i + 13], 21, 1309151649),
                    l = c(l, p, h, g, e[i + 4], 6, -145523070),
                    g = c(g, l, p, h, e[i + 11], 10, -1120210379),
                    h = c(h, g, l, p, e[i + 2], 15, 718787259),
                    p = c(p, h, g, l, e[i + 9], 21, -343485551),
                    l = n(l, r),
                    p = n(p, u),
                    h = n(h, f),
                    g = n(g, d);
            return [l, p, h, g]
        }
        function f(e) {
            var t, n = "", i = 32 * e.length;
            for (t = 0; t < i; t += 8)
                n += String.fromCharCode(e[t >> 5] >>> t % 32 & 255);
            return n
        }
        function d(e) {
            var t, n = [];
            for (n[(e.length >> 2) - 1] = void 0,
                     t = 0; t < n.length; t += 1)
                n[t] = 0;
            var i = 8 * e.length;
            for (t = 0; t < i; t += 8)
                n[t >> 5] |= (255 & e.charCodeAt(t / 8)) << t % 32;
            return n
        }
        function l(e) {
            return f(u(d(e), 8 * e.length))
        }
        function p(e, t) {
            var n, i, r = d(e), o = [], s = [];
            for (o[15] = s[15] = void 0,
                 r.length > 16 && (r = u(r, 8 * e.length)),
                     n = 0; n < 16; n += 1)
                o[n] = 909522486 ^ r[n],
                    s[n] = 1549556828 ^ r[n];
            return i = u(o.concat(d(t)), 512 + 8 * t.length),
                f(u(s.concat(i), 640))
        }
        function h(e) {
            var t, n, i = "0123456789abcdef", r = "";
            for (n = 0; n < e.length; n += 1)
                t = e.charCodeAt(n),
                    r += i.charAt(t >>> 4 & 15) + i.charAt(15 & t);
            return r
        }
        function g(e) {
            return unescape(encodeURIComponent(e))
        }
        function v(e) {
            return l(g(e))
        }
        function m(e) {
            return h(v(e))
        }
        function y(e, t) {
            return p(g(e), g(t))
        }
        function _(e, t) {
            return h(y(e, t))
        }
        function k(e, t, n) {
            return t ? n ? y(t, e) : _(t, e) : n ? v(e) : m(e)
        }
        e.exports = k
    },
    70: function(e, t, n) {
        for (var i = n(32), r = n(28), o = n(30), s = n(6), a = n(0), c = {
            api_host: "http://tongji.qichacha.com/web_event/web.gif?method=web_event_srv.upload",
            api_host_bac: "http://tongji.qichacha.com/web_event/web.gif?method=web_event_srv.upload",
            debug: !1,
            inherit_user_data: !0,
            ping: !1,
            ping_interval: 12e3,
            idle_timeout: 3e5,
            idle_threshold: 1e4,
            track_link_timeout: 300,
            cookie_expire_days: 365,
            cookie_cross_subdomain: !0,
            cookie_secure: !1,
            info_upload_interval_days: 7,
            session_interval_mins: 30,
            app_channel: "js",
            app_version: "1.0",
            superProperty: "{}",
            platform: "{}",
            autoTrack: !1,
            isClickAble: null,
            redirectAfterTrack: !1,
            singlePage: !1,
            visualizer: !1,
            deepShare: !1,
            onLoadDeepShare: null
        }, u = window.zhuge || [], f = new i(c); u && u.length > 0; ) {
            var d = u.shift()
                , l = d.shift();
            f[l] && f[l].apply(f, d)
        }
        var p = !0
            , h = new o(f,{
            open: f.config.visualizer,
            stopTrack: function() {
                p = !1
            }
        })
            , g = new r(f,{
            open: f.config.autoTrack,
            isClickAble: f.config.isClickAble,
            singlePage: f.config.singlePage
        })
            , v = function(e) {
            if (p) {
                var t = s.isClickAble(e.target, f.config.isClickAble);
                if (t.flag) {
                    var n = [];
                    if (f.config.autoTrack && !a.hasMobileSdk().flag && n.push(g.getEvent(t)),
                        f.config.visualizer && (n = h.getEvent(t).concat(n)),
                            t.form) {
                        var i = s.getAttr(t.form, "target");
                        if ((!i || "_self" === i) && f.config.redirectAfterTrack)
                            return f.batchTrack(n, function() {
                                t.form.submit()
                            }),
                                !1
                    }
                    if (t.isValidLink) {
                        var r = s.getAttr(t.target, "href")
                            , i = s.getAttr(t.target, "target");
                        if ((!i || "_self" === i) && f.config.redirectAfterTrack)
                            return f.batchTrack(n, function() {
                                location.href = r
                            }),
                                !1
                    }
                    n.length && f.batchTrack(n)
                }
            }
        };
        if (s.ready(function() {
                s.bind(s.body, "click", v)
            }, this),
                f.config.deepShare) {
            var m = new Date
                , y = m.getFullYear().toString() + m.getMonth().toString() + m.getDate().toString();
            s.loadJs({
                src: "http://tongji.qichacha.com:8080/deepshare.min.js?v=" + y,
                onLoad: f.config.onLoadDeepShare
            })
        }
        window.zhuge = f
    },
    8: function(e, t) {
        e.exports = {
            MODE: {
                NORMAL: 0,
                CREATE: 1,
                CREATING_EVENT: 2,
                ADD_SAME: 3,
                ADDING_SAME_AUTO: 4,
                ADDING_SAME_CUSTOM: 5,
                ADD_PROPERTY: 6,
                ADDING_PROPERTY: 7
            },
            SESSION_KEYS: {
                IS_ANALYSIS_MODE: "isZhugeVisualizerMode",
                PARAMS: "jsVisualizerParam"
            }
        }
    }
});
