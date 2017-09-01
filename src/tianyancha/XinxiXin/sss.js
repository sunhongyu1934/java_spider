!
    function(e, t) {
        "object" == typeof module && "object" == typeof module.exports ? module.exports = e.document ? t(e, !0) : function(e) {
            if (!e.document) throw new Error("jQuery requires a window with a document");
            return t(e)
        }: t(e)
    } ("undefined" != typeof window ? window: this,
        function(e, t) {
            function n(e) {
                var t = !!e && "length" in e && e.length,
                    n = pe.type(e);
                return "function" !== n && !pe.isWindow(e) && ("array" === n || 0 === t || "number" == typeof t && t > 0 && t - 1 in e)
            }
            function r(e, t, n) {
                if (pe.isFunction(t)) return pe.grep(e,
                    function(e, r) {
                        return !! t.call(e, r, e) !== n
                    });
                if (t.nodeType) return pe.grep(e,
                    function(e) {
                        return e === t !== n
                    });
                if ("string" == typeof t) {
                    if (Ce.test(t)) return pe.filter(t, e, n);
                    t = pe.filter(t, e)
                }
                return pe.grep(e,
                    function(e) {
                        return pe.inArray(e, t) > -1 !== n
                    })
            }
            function i(e, t) {
                do {
                    e = e[t]
                } while ( e && 1 !== e . nodeType );
                return e
            }
            function o(e) {
                var t = {};
                return pe.each(e.match(Ae) || [],
                    function(e, n) {
                        t[n] = !0
                    }),
                    t
            }
            function a() {
                ie.addEventListener ? (ie.removeEventListener("DOMContentLoaded", s), e.removeEventListener("load", s)) : (ie.detachEvent("onreadystatechange", s), e.detachEvent("onload", s))
            }
            function s() { (ie.addEventListener || "load" === e.event.type || "complete" === ie.readyState) && (a(), pe.ready())
            }
            function u(e, t, n) {
                if (void 0 === n && 1 === e.nodeType) {
                    var r = "data-" + t.replace(qe, "-$1").toLowerCase();
                    if ("string" == typeof(n = e.getAttribute(r))) {
                        try {
                            n = "true" === n || "false" !== n && ("null" === n ? null: +n + "" === n ? +n: He.test(n) ? pe.parseJSON(n) : n)
                        } catch(e) {}
                        pe.data(e, t, n)
                    } else n = void 0
                }
                return n
            }
            function l(e) {
                var t;
                for (t in e) if (("data" !== t || !pe.isEmptyObject(e[t])) && "toJSON" !== t) return ! 1;
                return ! 0
            }
            function c(e, t, n, r) {
                if (Le(e)) {
                    var i, o, a = pe.expando,
                        s = e.nodeType,
                        u = s ? pe.cache: e,
                        l = s ? e[a] : e[a] && a;
                    if (l && u[l] && (r || u[l].data) || void 0 !== n || "string" != typeof t) return l || (l = s ? e[a] = re.pop() || pe.guid++:a),
                    u[l] || (u[l] = s ? {}: {
                        toJSON: pe.noop
                    }),
                    "object" != typeof t && "function" != typeof t || (r ? u[l] = pe.extend(u[l], t) : u[l].data = pe.extend(u[l].data, t)),
                        o = u[l],
                    r || (o.data || (o.data = {}), o = o.data),
                    void 0 !== n && (o[pe.camelCase(t)] = n),
                        "string" == typeof t ? null == (i = o[t]) && (i = o[pe.camelCase(t)]) : i = o,
                        i
                }
            }
            function d(e, t, n) {
                if (Le(e)) {
                    var r, i, o = e.nodeType,
                        a = o ? pe.cache: e,
                        s = o ? e[pe.expando] : pe.expando;
                    if (a[s]) {
                        if (t && (r = n ? a[s] : a[s].data)) {
                            i = (t = pe.isArray(t) ? t.concat(pe.map(t, pe.camelCase)) : t in r ? [t] : (t = pe.camelCase(t)) in r ? [t] : t.split(" ")).length;
                            for (; i--;) delete r[t[i]];
                            if (n ? !l(r) : !pe.isEmptyObject(r)) return
                        } (n || (delete a[s].data, l(a[s]))) && (o ? pe.cleanData([e], !0) : fe.deleteExpando || a != a.window ? delete a[s] : a[s] = void 0)
                    }
                }
            }
            function f(e, t, n, r) {
                var i, o = 1,
                    a = 20,
                    s = r ?
                        function() {
                            return r.cur()
                        }: function() {
                        return pe.css(e, t, "")
                    },
                    u = s(),
                    l = n && n[3] || (pe.cssNumber[t] ? "": "px"),
                    c = (pe.cssNumber[t] || "px" !== l && +u) && Me.exec(pe.css(e, t));
                if (c && c[3] !== l) {
                    l = l || c[3],
                        n = n || [],
                        c = +u || 1;
                    do {
                        c /= o = o || ".5", pe.style(e, t, c + l)
                    } while ( o !== ( o = s () / u) && 1 !== o && --a)
                }
                return n && (c = +c || +u || 0, i = n[1] ? c + (n[1] + 1) * n[2] : +n[2], r && (r.unit = l, r.start = c, r.end = i)),
                    i
            }
            function p(e) {
                var t = $e.split("|"),
                    n = e.createDocumentFragment();
                if (n.createElement) for (; t.length;) n.createElement(t.pop());
                return n
            }
            function h(e, t) {
                var n, r, i = 0,
                    o = void 0 !== e.getElementsByTagName ? e.getElementsByTagName(t || "*") : void 0 !== e.querySelectorAll ? e.querySelectorAll(t || "*") : void 0;
                if (!o) for (o = [], n = e.childNodes || e; null != (r = n[i]); i++) ! t || pe.nodeName(r, t) ? o.push(r) : pe.merge(o, h(r, t));
                return void 0 === t || t && pe.nodeName(e, t) ? pe.merge([e], o) : o
            }
            function g(e, t) {
                for (var n, r = 0; null != (n = e[r]); r++) pe._data(n, "globalEval", !t || pe._data(t[r], "globalEval"))
            }
            function m(e) {
                Pe.test(e.type) && (e.defaultChecked = e.checked)
            }
            function v(e, t, n, r, i) {
                for (var o, a, s, u, l, c, d, f = e.length,
                         v = p(t), y = [], x = 0; x < f; x++) if ((a = e[x]) || 0 === a) if ("object" === pe.type(a)) pe.merge(y, a.nodeType ? [a] : a);
                else if (Xe.test(a)) {
                    for (u = u || v.appendChild(t.createElement("div")), l = (We.exec(a) || ["", ""])[1].toLowerCase(), d = ze[l] || ze._default, u.innerHTML = d[1] + pe.htmlPrefilter(a) + d[2], o = d[0]; o--;) u = u.lastChild;
                    if (!fe.leadingWhitespace && Ie.test(a) && y.push(t.createTextNode(Ie.exec(a)[0])), !fe.tbody) for (o = (a = "table" !== l || Ue.test(a) ? "<table>" !== d[1] || Ue.test(a) ? 0 : u: u.firstChild) && a.childNodes.length; o--;) pe.nodeName(c = a.childNodes[o], "tbody") && !c.childNodes.length && a.removeChild(c);
                    for (pe.merge(y, u.childNodes), u.textContent = ""; u.firstChild;) u.removeChild(u.firstChild);
                    u = v.lastChild
                } else y.push(t.createTextNode(a));
                for (u && v.removeChild(u), fe.appendChecked || pe.grep(h(y, "input"), m), x = 0; a = y[x++];) if (r && pe.inArray(a, r) > -1) i && i.push(a);
                else if (s = pe.contains(a.ownerDocument, a), u = h(v.appendChild(a), "script"), s && g(u), n) for (o = 0; a = u[o++];) Be.test(a.type || "") && n.push(a);
                return u = null,
                    v
            }
            function y() {
                return ! 0
            }
            function x() {
                return ! 1
            }
            function b() {
                try {
                    return ie.activeElement
                } catch(e) {}
            }
            function w(e, t, n, r, i, o) {
                var a, s;
                if ("object" == typeof t) {
                    "string" != typeof n && (r = r || n, n = void 0);
                    for (s in t) w(e, s, n, r, t[s], o);
                    return e
                }
                if (null == r && null == i ? (i = n, r = n = void 0) : null == i && ("string" == typeof n ? (i = r, r = void 0) : (i = r, r = n, n = void 0)), !1 === i) i = x;
                else if (!i) return e;
                return 1 === o && (a = i, (i = function(e) {
                    return pe().off(e),
                        a.apply(this, arguments)
                }).guid = a.guid || (a.guid = pe.guid++)),
                    e.each(function() {
                        pe.event.add(this, t, i, r, n)
                    })
            }
            function T(e, t) {
                return pe.nodeName(e, "table") && pe.nodeName(11 !== t.nodeType ? t: t.firstChild, "tr") ? e.getElementsByTagName("tbody")[0] || e.appendChild(e.ownerDocument.createElement("tbody")) : e
            }
            function C(e) {
                return e.type = (null !== pe.find.attr(e, "type")) + "/" + e.type,
                    e
            }
            function E(e) {
                var t = rt.exec(e.type);
                return t ? e.type = t[1] : e.removeAttribute("type"),
                    e
            }
            function N(e, t) {
                if (1 === t.nodeType && pe.hasData(e)) {
                    var n, r, i, o = pe._data(e),
                        a = pe._data(t, o),
                        s = o.events;
                    if (s) {
                        delete a.handle,
                            a.events = {};
                        for (n in s) for (r = 0, i = s[n].length; r < i; r++) pe.event.add(t, n, s[n][r])
                    }
                    a.data && (a.data = pe.extend({},
                        a.data))
                }
            }
            function k(e, t) {
                var n, r, i;
                if (1 === t.nodeType) {
                    if (n = t.nodeName.toLowerCase(), !fe.noCloneEvent && t[pe.expando]) {
                        i = pe._data(t);
                        for (r in i.events) pe.removeEvent(t, r, i.handle);
                        t.removeAttribute(pe.expando)
                    }
                    "script" === n && t.text !== e.text ? (C(t).text = e.text, E(t)) : "object" === n ? (t.parentNode && (t.outerHTML = e.outerHTML), fe.html5Clone && e.innerHTML && !pe.trim(t.innerHTML) && (t.innerHTML = e.innerHTML)) : "input" === n && Pe.test(e.type) ? (t.defaultChecked = t.checked = e.checked, t.value !== e.value && (t.value = e.value)) : "option" === n ? t.defaultSelected = t.selected = e.defaultSelected: "input" !== n && "textarea" !== n || (t.defaultValue = e.defaultValue)
                }
            }
            function S(e, t, n, r) {
                t = ae.apply([], t);
                var i, o, a, s, u, l, c = 0,
                    d = e.length,
                    f = d - 1,
                    p = t[0],
                    g = pe.isFunction(p);
                if (g || d > 1 && "string" == typeof p && !fe.checkClone && nt.test(p)) return e.each(function(i) {
                    var o = e.eq(i);
                    g && (t[0] = p.call(this, i, o.html())),
                        S(o, t, n, r)
                });
                if (d && (l = v(t, e[0].ownerDocument, !1, e, r), i = l.firstChild, 1 === l.childNodes.length && (l = i), i || r)) {
                    for (a = (s = pe.map(h(l, "script"), C)).length; c < d; c++) o = l,
                    c !== f && (o = pe.clone(o, !0, !0), a && pe.merge(s, h(o, "script"))),
                        n.call(e[c], o, c);
                    if (a) for (u = s[s.length - 1].ownerDocument, pe.map(s, E), c = 0; c < a; c++) o = s[c],
                    Be.test(o.type || "") && !pe._data(o, "globalEval") && pe.contains(u, o) && (o.src ? pe._evalUrl && pe._evalUrl(o.src) : pe.globalEval((o.text || o.textContent || o.innerHTML || "").replace(it, "")));
                    l = i = null
                }
                return e
            }
            function A(e, t, n) {
                for (var r, i = t ? pe.filter(t, e) : e, o = 0; null != (r = i[o]); o++) n || 1 !== r.nodeType || pe.cleanData(h(r)),
                r.parentNode && (n && pe.contains(r.ownerDocument, r) && g(h(r, "script")), r.parentNode.removeChild(r));
                return e
            }
            function j(e, t) {
                var n = pe(t.createElement(e)).appendTo(t.body),
                    r = pe.css(n[0], "display");
                return n.detach(),
                    r
            }
            function D(e) {
                var t = ie,
                    n = st[e];
                return n || ("none" !== (n = j(e, t)) && n || ((t = ((at = (at || pe("<iframe frameborder='0' width='0' height='0'/>")).appendTo(t.documentElement))[0].contentWindow || at[0].contentDocument).document).write(), t.close(), n = j(e, t), at.detach()), st[e] = n),
                    n
            }
            function L(e, t) {
                return {
                    get: function() {
                        if (!e()) return (this.get = t).apply(this, arguments);
                        delete this.get
                    }
                }
            }
            function H(e) {
                if (e in Tt) return e;
                for (var t = e.charAt(0).toUpperCase() + e.slice(1), n = wt.length; n--;) if ((e = wt[n] + t) in Tt) return e
            }
            function q(e, t) {
                for (var n, r, i, o = [], a = 0, s = e.length; a < s; a++)(r = e[a]).style && (o[a] = pe._data(r, "olddisplay"), n = r.style.display, t ? (o[a] || "none" !== n || (r.style.display = ""), "" === r.style.display && Re(r) && (o[a] = pe._data(r, "olddisplay", D(r.nodeName)))) : (i = Re(r), (n && "none" !== n || !i) && pe._data(r, "olddisplay", i ? n: pe.css(r, "display"))));
                for (a = 0; a < s; a++)(r = e[a]).style && (t && "none" !== r.style.display && "" !== r.style.display || (r.style.display = t ? o[a] || "": "none"));
                return e
            }
            function _(e, t, n) {
                var r = yt.exec(t);
                return r ? Math.max(0, r[1] - (n || 0)) + (r[2] || "px") : t
            }
            function M(e, t, n, r, i) {
                for (var o = n === (r ? "border": "content") ? 4 : "width" === t ? 1 : 0, a = 0; o < 4; o += 2)"margin" === n && (a += pe.css(e, n + Fe[o], !0, i)),
                    r ? ("content" === n && (a -= pe.css(e, "padding" + Fe[o], !0, i)), "margin" !== n && (a -= pe.css(e, "border" + Fe[o] + "Width", !0, i))) : (a += pe.css(e, "padding" + Fe[o], !0, i), "padding" !== n && (a += pe.css(e, "border" + Fe[o] + "Width", !0, i)));
                return a
            }
            function R(e, t, n) {
                var r = !0,
                    i = "width" === t ? e.offsetWidth: e.offsetHeight,
                    o = ft(e),
                    a = fe.boxSizing && "border-box" === pe.css(e, "boxSizing", !1, o);
                if (i <= 0 || null == i) {
                    if (((i = pt(e, t, o)) < 0 || null == i) && (i = e.style[t]), lt.test(i)) return i;
                    r = a && (fe.boxSizingReliable() || i === e.style[t]),
                        i = parseFloat(i) || 0
                }
                return i + M(e, t, n || (a ? "border": "content"), r, o) + "px"
            }
            function O(e, t, n, r, i) {
                return new O.prototype.init(e, t, n, r, i)
            }
            function P() {
                return e.setTimeout(function() {
                    Ct = void 0
                }),
                    Ct = pe.now()
            }
            function W(e, t) {
                var n, r = {
                        height: e
                    },
                    i = 0;
                for (t = t ? 1 : 0; i < 4; i += 2 - t) r["margin" + (n = Fe[i])] = r["padding" + n] = e;
                return t && (r.opacity = r.width = e),
                    r
            }
            function B(e, t, n) {
                for (var r, i = ($.tweeners[t] || []).concat($.tweeners["*"]), o = 0, a = i.length; o < a; o++) if (r = i[o].call(n, t, e)) return r
            }
            function I(e, t) {
                var n, r, i, o, a;
                for (n in e) if (r = pe.camelCase(n), i = t[r], o = e[n], pe.isArray(o) && (i = o[1], o = e[n] = o[0]), n !== r && (e[r] = o, delete e[n]), (a = pe.cssHooks[r]) && "expand" in a) {
                    o = a.expand(o),
                        delete e[r];
                    for (n in o) n in e || (e[n] = o[n], t[n] = i)
                } else t[r] = i
            }
            function $(e, t, n) {
                var r, i, o = 0,
                    a = $.prefilters.length,
                    s = pe.Deferred().always(function() {
                        delete u.elem
                    }),
                    u = function() {
                        if (i) return ! 1;
                        for (var t = Ct || P(), n = Math.max(0, l.startTime + l.duration - t), r = 1 - (n / l.duration || 0), o = 0, a = l.tweens.length; o < a; o++) l.tweens[o].run(r);
                        return s.notifyWith(e, [l, r, n]),
                            r < 1 && a ? n: (s.resolveWith(e, [l]), !1)
                    },
                    l = s.promise({
                        elem: e,
                        props: pe.extend({},
                            t),
                        opts: pe.extend(!0, {
                                specialEasing: {},
                                easing: pe.easing._default
                            },
                            n),
                        originalProperties: t,
                        originalOptions: n,
                        startTime: Ct || P(),
                        duration: n.duration,
                        tweens: [],
                        createTween: function(t, n) {
                            var r = pe.Tween(e, l.opts, t, n, l.opts.specialEasing[t] || l.opts.easing);
                            return l.tweens.push(r),
                                r
                        },
                        stop: function(t) {
                            var n = 0,
                                r = t ? l.tweens.length: 0;
                            if (i) return this;
                            for (i = !0; n < r; n++) l.tweens[n].run(1);
                            return t ? (s.notifyWith(e, [l, 1, 0]), s.resolveWith(e, [l, t])) : s.rejectWith(e, [l, t]),
                                this
                        }
                    }),
                    c = l.props;
                for (I(c, l.opts.specialEasing); o < a; o++) if (r = $.prefilters[o].call(l, e, c, l.opts)) return pe.isFunction(r.stop) && (pe._queueHooks(l.elem, l.opts.queue).stop = pe.proxy(r.stop, r)),
                    r;
                return pe.map(c, B, l),
                pe.isFunction(l.opts.start) && l.opts.start.call(e, l),
                    pe.fx.timer(pe.extend(u, {
                        elem: e,
                        anim: l,
                        queue: l.opts.queue
                    })),
                    l.progress(l.opts.progress).done(l.opts.done, l.opts.complete).fail(l.opts.fail).always(l.opts.always)
            }
            function z(e) {
                return pe.attr(e, "class") || ""
            }
            function X(e) {
                return function(t, n) {
                    "string" != typeof t && (n = t, t = "*");
                    var r, i = 0,
                        o = t.toLowerCase().match(Ae) || [];
                    if (pe.isFunction(n)) for (; r = o[i++];)"+" === r.charAt(0) ? (r = r.slice(1) || "*", (e[r] = e[r] || []).unshift(n)) : (e[r] = e[r] || []).push(n)
                }
            }
            function U(e, t, n, r) {
                function i(s) {
                    var u;
                    return o[s] = !0,
                        pe.each(e[s] || [],
                            function(e, s) {
                                var l = s(t, n, r);
                                return "string" != typeof l || a || o[l] ? a ? !(u = l) : void 0 : (t.dataTypes.unshift(l), i(l), !1)
                            }),
                        u
                }
                var o = {},
                    a = e === Qt;
                return i(t.dataTypes[0]) || !o["*"] && i("*")
            }
            function V(t, n) {
                var r = t.key.charCodeAt(0) + "";
                r = r.length > 1 ? r[1] : r;
                for (var i = e.rs_decode(r), o = e._0x4fec.split(",")[r], a = [], s = 0, u = 0; u < o.length; u++) {
                    if ("`" != o[u] && "!" != o[u] && "~" != o[u] ||
                        (a.push(i.substring(s, s + 1)), s++),
                        "#" == o[u] && (a.push(i.substring(s, s + 1)),
                            a.push(i.substring(s + 1, s + 3)),
                            a.push(i.substring(s + 3, s + 4)), s += 4),
                        o.charCodeAt(u) > 96 && o.charCodeAt(u) < 123)

                        for (var l = o[u + 1], c = 0; c < l; c++) a.push(i.substring(s, s + 2)),
                        s += 2;
                    if (o.charCodeAt(u) > 64 && o.charCodeAt(u) < 91) for (var l = o[u + 1], c = 0; c < l; c++) a.push(i.substring(s, s + 1)),
                        s++
                }
                n.rsid = a
            }
            function Y(e, t) {
                var n, r, i = pe.ajaxSettings.flatOptions || {};
                for (r in t) void 0 !== t[r] && ((i[r] ? e: n || (n = {}))[r] = t[r]);
                return n && pe.extend(!0, e, n),
                    e
            }
            function G(e, t, n) {
                for (var r, i, o, a, s = e.contents,
                         u = e.dataTypes;
                     "*" === u[0];) u.shift(),
                void 0 === i && (i = e.mimeType || t.getResponseHeader("Content-Type"));
                if (i) for (a in s) if (s[a] && s[a].test(i)) {
                    u.unshift(a);
                    break
                }
                if (u[0] in n) o = u[0];
                else {
                    for (a in n) {
                        if (!u[0] || e.converters[a + " " + u[0]]) {
                            o = a;
                            break
                        }
                        r || (r = a)
                    }
                    o = o || r
                }
                if (o) return o !== u[0] && u.unshift(o),
                    n[o]
            }
            function J(e, t, n, r) {
                var i, o, a, s, u, l = {},
                    c = e.dataTypes.slice();
                if (c[1]) for (a in e.converters) l[a.toLowerCase()] = e.converters[a];
                for (o = c.shift(); o;) if (e.responseFields[o] && (n[e.responseFields[o]] = t), !u && r && e.dataFilter && (t = e.dataFilter(t, e.dataType)), u = o, o = c.shift()) if ("*" === o) o = u;
                else if ("*" !== u && u !== o) {
                    if (! (a = l[u + " " + o] || l["* " + o])) for (i in l) if ((s = i.split(" "))[1] === o && (a = l[u + " " + s[0]] || l["* " + s[0]])) { ! 0 === a ? a = l[i] : !0 !== l[i] && (o = s[0], c.unshift(s[1]));
                        break
                    }
                    if (!0 !== a) if (a && e.throws) t = a(t);
                    else try {
                            t = a(t)
                        } catch(e) {
                            return {
                                state: "parsererror",
                                error: a ? e: "No conversion from " + u + " to " + o
                            }
                        }
                }
                return {
                    state: "success",
                    data: t
                }
            }
            function Q(e) {
                return e.style && e.style.display || pe.css(e, "display")
            }
            function K(e) {
                if (!pe.contains(e.ownerDocument || ie, e)) return ! 0;
                for (; e && 1 === e.nodeType;) {
                    if ("none" === Q(e) || "hidden" === e.type) return ! 0;
                    e = e.parentNode
                }
                return ! 1
            }
            function Z(e, t, n, r) {
                var i;
                if (pe.isArray(t)) pe.each(t,
                    function(t, i) {
                        n || on.test(e) ? r(e, i) : Z(e + "[" + ("object" == typeof i && null != i ? t: "") + "]", i, n, r)
                    });
                else if (n || "object" !== pe.type(t)) r(e, t);
                else for (i in t) Z(e + "[" + i + "]", t[i], n, r)
            }
            function ee() {
                try {
                    return new e.XMLHttpRequest
                } catch(e) {}
            }
            function te() {
                try {
                    return new e.ActiveXObject("Microsoft.XMLHTTP")
                } catch(e) {}
            }
            function ne(e) {
                return pe.isWindow(e) ? e: 9 === e.nodeType && (e.defaultView || e.parentWindow)
            }
            var re = [],
                ie = e.document,
                oe = re.slice,
                ae = re.concat,
                se = re.push,
                ue = re.indexOf,
                le = {},
                ce = le.toString,
                de = le.hasOwnProperty,
                fe = {},
                pe = function(e, t) {
                    return new pe.fn.init(e, t)
                },
                he = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g,
                ge = /^-ms-/,
                me = /-([\da-z])/gi,
                ve = function(e, t) {
                    return t.toUpperCase()
                };
            pe.fn = pe.prototype = {
                jquery: "1.12.4",
                constructor: pe,
                selector: "",
                length: 0,
                toArray: function() {
                    return oe.call(this)
                },
                get: function(e) {
                    return null != e ? e < 0 ? this[e + this.length] : this[e] : oe.call(this)
                },
                pushStack: function(e) {
                    var t = pe.merge(this.constructor(), e);
                    return t.prevObject = this,
                        t.context = this.context,
                        t
                },
                each: function(e) {
                    return pe.each(this, e)
                },
                map: function(e) {
                    return this.pushStack(pe.map(this,
                        function(t, n) {
                            return e.call(t, n, t)
                        }))
                },
                slice: function() {
                    return this.pushStack(oe.apply(this, arguments))
                },
                first: function() {
                    return this.eq(0)
                },
                last: function() {
                    return this.eq( - 1)
                },
                eq: function(e) {
                    var t = this.length,
                        n = +e + (e < 0 ? t: 0);
                    return this.pushStack(n >= 0 && n < t ? [this[n]] : [])
                },
                end: function() {
                    return this.prevObject || this.constructor()
                },
                push: se,
                sort: re.sort,
                splice: re.splice
            },
                pe.extend = pe.fn.extend = function() {
                    var e, t, n, r, i, o, a = arguments[0] || {},
                        s = 1,
                        u = arguments.length,
                        l = !1;
                    for ("boolean" == typeof a && (l = a, a = arguments[s] || {},
                        s++), "object" == typeof a || pe.isFunction(a) || (a = {}), s === u && (a = this, s--); s < u; s++) if (null != (i = arguments[s])) for (r in i) e = a[r],
                    a !== (n = i[r]) && (l && n && (pe.isPlainObject(n) || (t = pe.isArray(n))) ? (t ? (t = !1, o = e && pe.isArray(e) ? e: []) : o = e && pe.isPlainObject(e) ? e: {},
                        a[r] = pe.extend(l, o, n)) : void 0 !== n && (a[r] = n));
                    return a
                },
                pe.extend({
                    expando: "jQuery" + ("1.12.4" + Math.random()).replace(/\D/g, ""),
                    isReady: !0,
                    error: function(e) {
                        throw new Error(e)
                    },
                    noop: function() {},
                    isFunction: function(e) {
                        return "function" === pe.type(e)
                    },
                    isArray: Array.isArray ||
                    function(e) {
                        return "array" === pe.type(e)
                    },
                    isWindow: function(e) {
                        return null != e && e == e.window
                    },
                    isNumeric: function(e) {
                        var t = e && e.toString();
                        return ! pe.isArray(e) && t - parseFloat(t) + 1 >= 0
                    },
                    isEmptyObject: function(e) {
                        var t;
                        for (t in e) return ! 1;
                        return ! 0
                    },
                    isPlainObject: function(e) {
                        var t;
                        if (!e || "object" !== pe.type(e) || e.nodeType || pe.isWindow(e)) return ! 1;
                        try {
                            if (e.constructor && !de.call(e, "constructor") && !de.call(e.constructor.prototype, "isPrototypeOf")) return ! 1
                        } catch(e) {
                            return ! 1
                        }
                        if (!fe.ownFirst) for (t in e) return de.call(e, t);
                        for (t in e);
                        return void 0 === t || de.call(e, t)
                    },
                    type: function(e) {
                        return null == e ? e + "": "object" == typeof e || "function" == typeof e ? le[ce.call(e)] || "object": typeof e
                    },
                    globalEval: function(t) {
                        t && pe.trim(t) && (e.execScript ||
                        function(t) {
                            e.eval.call(e, t)
                        })(t)
                    },
                    camelCase: function(e) {
                        return e.replace(ge, "ms-").replace(me, ve)
                    },
                    nodeName: function(e, t) {
                        return e.nodeName && e.nodeName.toLowerCase() === t.toLowerCase()
                    },
                    each: function(e, t) {
                        var r, i = 0;
                        if (n(e)) for (r = e.length; i < r && !1 !== t.call(e[i], i, e[i]); i++);
                        else for (i in e) if (!1 === t.call(e[i], i, e[i])) break;
                        return e
                    },
                    trim: function(e) {
                        return null == e ? "": (e + "").replace(he, "")
                    },
                    makeArray: function(e, t) {
                        var r = t || [];
                        return null != e && (n(Object(e)) ? pe.merge(r, "string" == typeof e ? [e] : e) : se.call(r, e)),
                            r
                    },
                    inArray: function(e, t, n) {
                        var r;
                        if (t) {
                            if (ue) return ue.call(t, e, n);
                            for (r = t.length, n = n ? n < 0 ? Math.max(0, r + n) : n: 0; n < r; n++) if (n in t && t[n] === e) return n
                        }
                        return - 1
                    },
                    merge: function(e, t) {
                        for (var n = +t.length,
                                 r = 0,
                                 i = e.length; r < n;) e[i++] = t[r++];
                        if (n !== n) for (; void 0 !== t[r];) e[i++] = t[r++];
                        return e.length = i,
                            e
                    },
                    grep: function(e, t, n) {
                        for (var r = [], i = 0, o = e.length, a = !n; i < o; i++) ! t(e[i], i) !== a && r.push(e[i]);
                        return r
                    },
                    map: function(e, t, r) {
                        var i, o, a = 0,
                            s = [];
                        if (n(e)) for (i = e.length; a < i; a++) null != (o = t(e[a], a, r)) && s.push(o);
                        else for (a in e) null != (o = t(e[a], a, r)) && s.push(o);
                        return ae.apply([], s)
                    },
                    guid: 1,
                    proxy: function(e, t) {
                        var n, r, i;
                        if ("string" == typeof t && (i = e[t], t = e, e = i), pe.isFunction(e)) return n = oe.call(arguments, 2),
                            r = function() {
                                return e.apply(t || this, n.concat(oe.call(arguments)))
                            },
                            r.guid = e.guid = e.guid || pe.guid++,
                            r
                    },
                    now: function() {
                        return + new Date
                    },
                    support: fe
                }),
            "function" == typeof Symbol && (pe.fn[Symbol.iterator] = re[Symbol.iterator]),
                pe.each("Boolean Number String Function Array Date RegExp Object Error Symbol".split(" "),
                    function(e, t) {
                        le["[object " + t + "]"] = t.toLowerCase()
                    });
            var ye = function(e) {
                function t(e, t, n, r) {
                    var i, o, a, s, l, d, f, p, h = t && t.ownerDocument,
                        g = t ? t.nodeType: 9;
                    if (n = n || [], "string" != typeof e || !e || 1 !== g && 9 !== g && 11 !== g) return n;
                    if (!r && ((t ? t.ownerDocument || t: O) !== D && j(t), t = t || D, H)) {
                        if (11 !== g && (d = me.exec(e))) if (i = d[1]) {
                            if (9 === g) {
                                if (! (a = t.getElementById(i))) return n;
                                if (a.id === i) return n.push(a),
                                    n
                            } else if (h && (a = h.getElementById(i)) && F(t, a) && a.id === i) return n.push(a),
                                n
                        } else {
                            if (d[2]) return J.apply(n, t.getElementsByTagName(e)),
                                n;
                            if ((i = d[3]) && x.getElementsByClassName && t.getElementsByClassName) return J.apply(n, t.getElementsByClassName(i)),
                                n
                        }
                        if (x.qsa && !$[e + " "] && (!q || !q.test(e))) {
                            if (1 !== g) h = t,
                                p = e;
                            else if ("object" !== t.nodeName.toLowerCase()) {
                                for ((s = t.getAttribute("id")) ? s = s.replace(ye, "\\$&") : t.setAttribute("id", s = R), o = (f = C(e)).length, l = de.test(s) ? "#" + s: "[id='" + s + "']"; o--;) f[o] = l + " " + c(f[o]);
                                p = f.join(","),
                                    h = ve.test(e) && u(t.parentNode) || t
                            }
                            if (p) try {
                                return J.apply(n, h.querySelectorAll(p)),
                                    n
                            } catch(e) {} finally {
                                s === R && t.removeAttribute("id")
                            }
                        }
                    }
                    return N(e.replace(ae, "$1"), t, n, r)
                }
                function n() {
                    function e(n, r) {
                        return t.push(n + " ") > b.cacheLength && delete e[t.shift()],
                            e[n + " "] = r
                    }
                    var t = [];
                    return e
                }
                function r(e) {
                    return e[R] = !0,
                        e
                }
                function i(e) {
                    var t = D.createElement("div");
                    try {
                        return !! e(t)
                    } catch(e) {
                        return ! 1
                    } finally {
                        t.parentNode && t.parentNode.removeChild(t),
                            t = null
                    }
                }
                function o(e, t) {
                    for (var n = e.split("|"), r = n.length; r--;) b.attrHandle[n[r]] = t
                }
                function a(e, t) {
                    var n = t && e,
                        r = n && 1 === e.nodeType && 1 === t.nodeType && (~t.sourceIndex || X) - (~e.sourceIndex || X);
                    if (r) return r;
                    if (n) for (; n = n.nextSibling;) if (n === t) return - 1;
                    return e ? 1 : -1
                }
                function s(e) {
                    return r(function(t) {
                        return t = +t,
                            r(function(n, r) {
                                for (var i, o = e([], n.length, t), a = o.length; a--;) n[i = o[a]] && (n[i] = !(r[i] = n[i]))
                            })
                    })
                }
                function u(e) {
                    return e && void 0 !== e.getElementsByTagName && e
                }
                function l() {}
                function c(e) {
                    for (var t = 0,
                             n = e.length,
                             r = ""; t < n; t++) r += e[t].value;
                    return r
                }
                function d(e, t, n) {
                    var r = t.dir,
                        i = n && "parentNode" === r,
                        o = W++;
                    return t.first ?
                        function(t, n, o) {
                            for (; t = t[r];) if (1 === t.nodeType || i) return e(t, n, o)
                        }: function(t, n, a) {
                        var s, u, l, c = [P, o];
                        if (a) {
                            for (; t = t[r];) if ((1 === t.nodeType || i) && e(t, n, a)) return ! 0
                        } else for (; t = t[r];) if (1 === t.nodeType || i) {
                            if (l = t[R] || (t[R] = {}), u = l[t.uniqueID] || (l[t.uniqueID] = {}), (s = u[r]) && s[0] === P && s[1] === o) return c[2] = s[2];
                            if (u[r] = c, c[2] = e(t, n, a)) return ! 0
                        }
                    }
                }
                function f(e) {
                    return e.length > 1 ?
                        function(t, n, r) {
                            for (var i = e.length; i--;) if (!e[i](t, n, r)) return ! 1;
                            return ! 0
                        }: e[0]
                }
                function p(e, n, r) {
                    for (var i = 0,
                             o = n.length; i < o; i++) t(e, n[i], r);
                    return r
                }
                function h(e, t, n, r, i) {
                    for (var o, a = [], s = 0, u = e.length, l = null != t; s < u; s++)(o = e[s]) && (n && !n(o, r, i) || (a.push(o), l && t.push(s)));
                    return a
                }
                function g(e, t, n, i, o, a) {
                    return i && !i[R] && (i = g(i)),
                    o && !o[R] && (o = g(o, a)),
                        r(function(r, a, s, u) {
                            var l, c, d, f = [],
                                g = [],
                                m = a.length,
                                v = r || p(t || "*", s.nodeType ? [s] : s, []),
                                y = !e || !r && t ? v: h(v, f, e, s, u),
                                x = n ? o || (r ? e: m || i) ? [] : a: y;
                            if (n && n(y, x, s, u), i) for (l = h(x, g), i(l, [], s, u), c = l.length; c--;)(d = l[c]) && (x[g[c]] = !(y[g[c]] = d));
                            if (r) {
                                if (o || e) {
                                    if (o) {
                                        for (l = [], c = x.length; c--;)(d = x[c]) && l.push(y[c] = d);
                                        o(null, x = [], l, u)
                                    }
                                    for (c = x.length; c--;)(d = x[c]) && (l = o ? K(r, d) : f[c]) > -1 && (r[l] = !(a[l] = d))
                                }
                            } else x = h(x === a ? x.splice(m, x.length) : x),
                                o ? o(null, a, x, u) : J.apply(a, x)
                        })
                }
                function m(e) {
                    for (var t, n, r, i = e.length,
                             o = b.relative[e[0].type], a = o || b.relative[" "], s = o ? 1 : 0, u = d(function(e) {
                                return e === t
                            },
                            a, !0), l = d(function(e) {
                                return K(t, e) > -1
                            },
                            a, !0), p = [function(e, n, r) {
                            var i = !o && (r || n !== k) || ((t = n).nodeType ? u(e, n, r) : l(e, n, r));
                            return t = null,
                                i
                        }]; s < i; s++) if (n = b.relative[e[s].type]) p = [d(f(p), n)];
                    else {
                        if ((n = b.filter[e[s].type].apply(null, e[s].matches))[R]) {
                            for (r = ++s; r < i && !b.relative[e[r].type]; r++);
                            return g(s > 1 && f(p), s > 1 && c(e.slice(0, s - 1).concat({
                                value: " " === e[s - 2].type ? "*": ""
                            })).replace(ae, "$1"), n, s < r && m(e.slice(s, r)), r < i && m(e = e.slice(r)), r < i && c(e))
                        }
                        p.push(n)
                    }
                    return f(p)
                }
                function v(e, n) {
                    var i = n.length > 0,
                        o = e.length > 0,
                        a = function(r, a, s, u, l) {
                            var c, d, f, p = 0,
                                g = "0",
                                m = r && [],
                                v = [],
                                y = k,
                                x = r || o && b.find.TAG("*", l),
                                w = P += null == y ? 1 : Math.random() || .1,
                                T = x.length;
                            for (l && (k = a === D || a || l); g !== T && null != (c = x[g]); g++) {
                                if (o && c) {
                                    for (d = 0, a || c.ownerDocument === D || (j(c), s = !H); f = e[d++];) if (f(c, a || D, s)) {
                                        u.push(c);
                                        break
                                    }
                                    l && (P = w)
                                }
                                i && ((c = !f && c) && p--, r && m.push(c))
                            }
                            if (p += g, i && g !== p) {
                                for (d = 0; f = n[d++];) f(m, v, a, s);
                                if (r) {
                                    if (p > 0) for (; g--;) m[g] || v[g] || (v[g] = Y.call(u));
                                    v = h(v)
                                }
                                J.apply(u, v),
                                l && !r && v.length > 0 && p + n.length > 1 && t.uniqueSort(u)
                            }
                            return l && (P = w, k = y),
                                m
                        };
                    return i ? r(a) : a
                }
                var y, x, b, w, T, C, E, N, k, S, A, j, D, L, H, q, _, M, F, R = "sizzle" + 1 * new Date,
                    O = e.document,
                    P = 0,
                    W = 0,
                    B = n(),
                    I = n(),
                    $ = n(),
                    z = function(e, t) {
                        return e === t && (A = !0),
                            0
                    },
                    X = 1 << 31,
                    U = {}.hasOwnProperty,
                    V = [],
                    Y = V.pop,
                    G = V.push,
                    J = V.push,
                    Q = V.slice,
                    K = function(e, t) {
                        for (var n = 0,
                                 r = e.length; n < r; n++) if (e[n] === t) return n;
                        return - 1
                    },
                    Z = "checked|selected|async|autofocus|autoplay|controls|defer|disabled|hidden|ismap|loop|multiple|open|readonly|required|scoped",
                    ee = "[\\x20\\t\\r\\n\\f]",
                    te = "(?:\\\\.|[\\w-]|[^\\x00-\\xa0])+",
                    ne = "2633141825201321121345332721524273528936811101916293117022304236|1831735156281312241132340102520529171363214283321272634162219930|2332353860219720155312141629130102234183691124281413251227261733|2592811262018293062732141927100364232411333831161535317211222534|9715232833130331019112512913172124126035262343627321642220185148|3316362031032192529235212215274341412306269813312817111724201835|3293412148301016132183119242311021281920736172527353261533526224|3236623313013201625221912357142415851018341117262721294332103928|2619332514511302724163415617234183291312001227928218353622321031|3111952725113022716818421512203433241091723133635282932601432216",
                    re = "\\[" + ee + "*(" + te + ")(?:" + ee + "*([*^$|!~]?=)" + ee + "*(?:'((?:\\\\.|[^\\\\'])*)'|\"((?:\\\\.|[^\\\\\"])*)\"|(" + te + "))|)" + ee + "*\\]",
                    ie = ":(" + te + ")(?:\\((('((?:\\\\.|[^\\\\'])*)'|\"((?:\\\\.|[^\\\\\"])*)\")|((?:\\\\.|[^\\\\()[\\]]|" + re + ")*)|.*)\\)|)",
                    oe = new RegExp(ee + "+", "g"),
                    ae = new RegExp("^" + ee + "+|((?:^|[^\\\\])(?:\\\\.)*)" + ee + "+$", "g"),
                    se = new RegExp("^" + ee + "*," + ee + "*"),
                    ue = new RegExp("^" + ee + "*([>+~]|" + ee + ")" + ee + "*"),
                    le = new RegExp("=" + ee + "*([^\\]'\"]*?)" + ee + "*\\]", "g"),
                    ce = new RegExp(ie),
                    de = new RegExp("^" + te + "$"),
                    fe = {
                        ID: new RegExp("^#(" + te + ")"),
                        CLASS: new RegExp("^\\.(" + te + ")"),
                        TAG: new RegExp("^(" + te + "|[*])"),
                        ATTR: new RegExp("^" + re),
                        PSEUDO: new RegExp("^" + ie),
                        CHILD: new RegExp("^:(only|first|last|nth|nth-last)-(child|of-type)(?:\\(" + ee + "*(even|odd|(([+-]|)(\\d*)n|)" + ee + "*(?:([+-]|)" + ee + "*(\\d+)|))" + ee + "*\\)|)", "i"),
                        bool: new RegExp("^(?:" + Z + ")$", "i"),
                        needsContext: new RegExp("^" + ee + "*[>+~]|:(even|odd|eq|gt|lt|nth|first|last)(?:\\(" + ee + "*((?:-\\d)?\\d*)" + ee + "*\\)|)(?=[^-]|$)", "i")
                    },
                    pe = /^(?:input|select|textarea|button)$/i,
                    he = /^h\d$/i,
                    ge = /^[^{]+\{\s*\[native \w/,
                    me = /^(?:#([\w-]+)|(\w+)|\.([\w-]+))$/,
                    ve = /[+~]/,
                    ye = /'|\\/g,
                    xe = new RegExp("\\\\([\\da-f]{1,6}" + ee + "?|(" + ee + ")|.)", "ig"),
                    be = function(e, t, n) {
                        var r = "0x" + t - 65536;
                        return r !== r || n ? t: r < 0 ? String.fromCharCode(r + 65536) : String.fromCharCode(r >> 10 | 55296, 1023 & r | 56320)
                    },
                    we = function() {
                        j()
                    };
                e._0x4fec = "f9D1x1Z2o1U2f5A1a1P1i7R1u2S1m1F1,o2A1x2F1u5~j1Y2z3!p2~r3G2m8S1c1,i3E5o1~d2!y2H1e2F1b6`g4v7,p1`t7D3x5#w2~l2Z1v4Y1k4M1n1,C2e3P1r7!s6U2n2~p5X1e3#,g4`b6W1x4R1r4#!u5!#D1f2,!z4U1f4`f2R2o3!l4I1v6F1h2F1x2!,b2~u9h2K1l3X2y9#B4t1,t5H1s7D1o2#p2#z1Q3v2`j6,r1#u5#f1Z2w7!r7#j3S1";
                try {
                    J.apply(V = Q.call(O.childNodes), O.childNodes),
                        V[O.childNodes.length].nodeType
                } catch(e) {
                    J = {
                        apply: V.length ?
                            function(e, t) {
                                G.apply(e, Q.call(t))
                            }: function(e, t) {
                            for (var n = e.length,
                                     r = 0; e[n++] = t[r++];);
                            e.length = n - 1
                        }
                    }
                }
                e.rs_decode = function(e) {
                    return ne.split("|")[e]
                },
                    x = t.support = {},
                    T = t.isXML = function(e) {
                        var t = e && (e.ownerDocument || e).documentElement;
                        return !! t && "HTML" !== t.nodeName
                    },
                    j = t.setDocument = function(e) {
                        var t, n, r = e ? e.ownerDocument || e: O;
                        return r !== D && 9 === r.nodeType && r.documentElement ? (D = r, L = D.documentElement, H = !T(D), (n = D.defaultView) && n.top !== n && (n.addEventListener ? n.addEventListener("unload", we, !1) : n.attachEvent && n.attachEvent("onunload", we)), x.attributes = i(function(e) {
                            return e.className = "i",
                                !e.getAttribute("className")
                        }), x.getElementsByTagName = i(function(e) {
                            return e.appendChild(D.createComment("")),
                                !e.getElementsByTagName("*").length
                        }), x.getElementsByClassName = ge.test(D.getElementsByClassName), x.getById = i(function(e) {
                            return L.appendChild(e).id = R,
                            !D.getElementsByName || !D.getElementsByName(R).length
                        }), x.getById ? (b.find.ID = function(e, t) {
                            if (void 0 !== t.getElementById && H) {
                                var n = t.getElementById(e);
                                return n ? [n] : []
                            }
                        },
                            b.filter.ID = function(e) {
                                var t = e.replace(xe, be);
                                return function(e) {
                                    return e.getAttribute("id") === t
                                }
                            }) : (delete b.find.ID, b.filter.ID = function(e) {
                            var t = e.replace(xe, be);
                            return function(e) {
                                var n = void 0 !== e.getAttributeNode && e.getAttributeNode("id");
                                return n && n.value === t
                            }
                        }), b.find.TAG = x.getElementsByTagName ?
                            function(e, t) {
                                return void 0 !== t.getElementsByTagName ? t.getElementsByTagName(e) : x.qsa ? t.querySelectorAll(e) : void 0
                            }: function(e, t) {
                            var n, r = [],
                                i = 0,
                                o = t.getElementsByTagName(e);
                            if ("*" === e) {
                                for (; n = o[i++];) 1 === n.nodeType && r.push(n);
                                return r
                            }
                            return o
                        },
                            b.find.CLASS = x.getElementsByClassName &&
                            function(e, t) {
                                if (void 0 !== t.getElementsByClassName && H) return t.getElementsByClassName(e)
                            },
                            _ = [], q = [], (x.qsa = ge.test(D.querySelectorAll)) && (i(function(e) {
                            L.appendChild(e).innerHTML = "<a id='" + R + "'></a><select id='" + R + "-\r\\' msallowcapture=''><option selected=''></option></select>",
                            e.querySelectorAll("[msallowcapture^='']").length && q.push("[*^$]=" + ee + "*(?:''|\"\")"),
                            e.querySelectorAll("[selected]").length || q.push("\\[" + ee + "*(?:value|" + Z + ")"),
                            e.querySelectorAll("[id~=" + R + "-]").length || q.push("~="),
                            e.querySelectorAll(":checked").length || q.push(":checked"),
                            e.querySelectorAll("a#" + R + "+*").length || q.push(".#.+[+~]")
                        }), i(function(e) {
                            var t = D.createElement("input");
                            t.setAttribute("type", "hidden"),
                                e.appendChild(t).setAttribute("name", "D"),
                            e.querySelectorAll("[name=d]").length && q.push("name" + ee + "*[*^$|!~]?="),
                            e.querySelectorAll(":enabled").length || q.push(":enabled", ":disabled"),
                                e.querySelectorAll("*,:x"),
                                q.push(",.*:")
                        })), (x.matchesSelector = ge.test(M = L.matches || L.webkitMatchesSelector || L.mozMatchesSelector || L.oMatchesSelector || L.msMatchesSelector)) && i(function(e) {
                            x.disconnectedMatch = M.call(e, "div"),
                                M.call(e, "[s!='']:x"),
                                _.push("!=", ie)
                        }), q = q.length && new RegExp(q.join("|")), _ = _.length && new RegExp(_.join("|")), t = ge.test(L.compareDocumentPosition), F = t || ge.test(L.contains) ?
                            function(e, t) {
                                var n = 9 === e.nodeType ? e.documentElement: e,
                                    r = t && t.parentNode;
                                return e === r || !(!r || 1 !== r.nodeType || !(n.contains ? n.contains(r) : e.compareDocumentPosition && 16 & e.compareDocumentPosition(r)))
                            }: function(e, t) {
                            if (t) for (; t = t.parentNode;) if (t === e) return ! 0;
                            return ! 1
                        },
                            z = t ?
                                function(e, t) {
                                    if (e === t) return A = !0,
                                        0;
                                    var n = !e.compareDocumentPosition - !t.compareDocumentPosition;
                                    return n || (1 & (n = (e.ownerDocument || e) === (t.ownerDocument || t) ? e.compareDocumentPosition(t) : 1) || !x.sortDetached && t.compareDocumentPosition(e) === n ? e === D || e.ownerDocument === O && F(O, e) ? -1 : t === D || t.ownerDocument === O && F(O, t) ? 1 : S ? K(S, e) - K(S, t) : 0 : 4 & n ? -1 : 1)
                                }: function(e, t) {
                                if (e === t) return A = !0,
                                    0;
                                var n, r = 0,
                                    i = e.parentNode,
                                    o = t.parentNode,
                                    s = [e],
                                    u = [t];
                                if (!i || !o) return e === D ? -1 : t === D ? 1 : i ? -1 : o ? 1 : S ? K(S, e) - K(S, t) : 0;
                                if (i === o) return a(e, t);
                                for (n = e; n = n.parentNode;) s.unshift(n);
                                for (n = t; n = n.parentNode;) u.unshift(n);
                                for (; s[r] === u[r];) r++;
                                return r ? a(s[r], u[r]) : s[r] === O ? -1 : u[r] === O ? 1 : 0
                            },
                            D) : D
                    },
                    t.matches = function(e, n) {
                        return t(e, null, null, n)
                    },
                    t.matchesSelector = function(e, n) {
                        if ((e.ownerDocument || e) !== D && j(e), n = n.replace(le, "='$1']"), x.matchesSelector && H && !$[n + " "] && (!_ || !_.test(n)) && (!q || !q.test(n))) try {
                            var r = M.call(e, n);
                            if (r || x.disconnectedMatch || e.document && 11 !== e.document.nodeType) return r
                        } catch(e) {}
                        return t(n, D, null, [e]).length > 0
                    },
                    t.contains = function(e, t) {
                        return (e.ownerDocument || e) !== D && j(e),
                            F(e, t)
                    },
                    t.attr = function(e, t) { (e.ownerDocument || e) !== D && j(e);
                        var n = b.attrHandle[t.toLowerCase()],
                            r = n && U.call(b.attrHandle, t.toLowerCase()) ? n(e, t, !H) : void 0;
                        return void 0 !== r ? r: x.attributes || !H ? e.getAttribute(t) : (r = e.getAttributeNode(t)) && r.specified ? r.value: null
                    },
                    t.error = function(e) {
                        throw new Error("Syntax error, unrecognized expression: " + e)
                    },
                    t.uniqueSort = function(e) {
                        var t, n = [],
                            r = 0,
                            i = 0;
                        if (A = !x.detectDuplicates, S = !x.sortStable && e.slice(0), e.sort(z), A) {
                            for (; t = e[i++];) t === e[i] && (r = n.push(i));
                            for (; r--;) e.splice(n[r], 1)
                        }
                        return S = null,
                            e
                    },
                    w = t.getText = function(e) {
                        var t, n = "",
                            r = 0,
                            i = e.nodeType;
                        if (i) {
                            if (1 === i || 9 === i || 11 === i) {
                                if ("string" == typeof e.textContent) return e.textContent;
                                for (e = e.firstChild; e; e = e.nextSibling) n += w(e)
                            } else if (3 === i || 4 === i) return e.nodeValue
                        } else for (; t = e[r++];) n += w(t);
                        return n
                    },
                    (b = t.selectors = {
                        cacheLength: 50,
                        createPseudo: r,
                        match: fe,
                        attrHandle: {},
                        find: {},
                        relative: {
                            ">": {
                                dir: "parentNode",
                                first: !0
                            },
                            " ": {
                                dir: "parentNode"
                            },
                            "+": {
                                dir: "previousSibling",
                                first: !0
                            },
                            "~": {
                                dir: "previousSibling"
                            }
                        },
                        preFilter: {
                            ATTR: function(e) {
                                return e[1] = e[1].replace(xe, be),
                                    e[3] = (e[3] || e[4] || e[5] || "").replace(xe, be),
                                "~=" === e[2] && (e[3] = " " + e[3] + " "),
                                    e.slice(0, 4)
                            },
                            CHILD: function(e) {
                                return e[1] = e[1].toLowerCase(),
                                    "nth" === e[1].slice(0, 3) ? (e[3] || t.error(e[0]), e[4] = +(e[4] ? e[5] + (e[6] || 1) : 2 * ("even" === e[3] || "odd" === e[3])), e[5] = +(e[7] + e[8] || "odd" === e[3])) : e[3] && t.error(e[0]),
                                    e
                            },
                            PSEUDO: function(e) {
                                var t, n = !e[6] && e[2];
                                return fe.CHILD.test(e[0]) ? null: (e[3] ? e[2] = e[4] || e[5] || "": n && ce.test(n) && (t = C(n, !0)) && (t = n.indexOf(")", n.length - t) - n.length) && (e[0] = e[0].slice(0, t), e[2] = n.slice(0, t)), e.slice(0, 3))
                            }
                        },
                        filter: {
                            TAG: function(e) {
                                var t = e.replace(xe, be).toLowerCase();
                                return "*" === e ?
                                    function() {
                                        return ! 0
                                    }: function(e) {
                                    return e.nodeName && e.nodeName.toLowerCase() === t
                                }
                            },
                            CLASS: function(e) {
                                var t = B[e + " "];
                                return t || (t = new RegExp("(^|" + ee + ")" + e + "(" + ee + "|$)")) && B(e,
                                        function(e) {
                                            return t.test("string" == typeof e.className && e.className || void 0 !== e.getAttribute && e.getAttribute("class") || "")
                                        })
                            },
                            ATTR: function(e, n, r) {
                                return function(i) {
                                    var o = t.attr(i, e);
                                    return null == o ? "!=" === n: !n || (o += "", "=" === n ? o === r: "!=" === n ? o !== r: "^=" === n ? r && 0 === o.indexOf(r) : "*=" === n ? r && o.indexOf(r) > -1 : "$=" === n ? r && o.slice( - r.length) === r: "~=" === n ? (" " + o.replace(oe, " ") + " ").indexOf(r) > -1 : "|=" === n && (o === r || o.slice(0, r.length + 1) === r + "-"))
                                }
                            },
                            CHILD: function(e, t, n, r, i) {
                                var o = "nth" !== e.slice(0, 3),
                                    a = "last" !== e.slice( - 4),
                                    s = "of-type" === t;
                                return 1 === r && 0 === i ?
                                    function(e) {
                                        return !! e.parentNode
                                    }: function(t, n, u) {
                                    var l, c, d, f, p, h, g = o !== a ? "nextSibling": "previousSibling",
                                        m = t.parentNode,
                                        v = s && t.nodeName.toLowerCase(),
                                        y = !u && !s,
                                        x = !1;
                                    if (m) {
                                        if (o) {
                                            for (; g;) {
                                                for (f = t; f = f[g];) if (s ? f.nodeName.toLowerCase() === v: 1 === f.nodeType) return ! 1;
                                                h = g = "only" === e && !h && "nextSibling"
                                            }
                                            return ! 0
                                        }
                                        if (h = [a ? m.firstChild: m.lastChild], a && y) {
                                            for (x = (p = (l = (c = (d = (f = m)[R] || (f[R] = {}))[f.uniqueID] || (d[f.uniqueID] = {}))[e] || [])[0] === P && l[1]) && l[2], f = p && m.childNodes[p]; f = ++p && f && f[g] || (x = p = 0) || h.pop();) if (1 === f.nodeType && ++x && f === t) {
                                                c[e] = [P, p, x];
                                                break
                                            }
                                        } else if (y && (x = p = (l = (c = (d = (f = t)[R] || (f[R] = {}))[f.uniqueID] || (d[f.uniqueID] = {}))[e] || [])[0] === P && l[1]), !1 === x) for (; (f = ++p && f && f[g] || (x = p = 0) || h.pop()) && ((s ? f.nodeName.toLowerCase() !== v: 1 !== f.nodeType) || !++x || (y && ((c = (d = f[R] || (f[R] = {}))[f.uniqueID] || (d[f.uniqueID] = {}))[e] = [P, x]), f !== t)););
                                        return (x -= i) === r || x % r == 0 && x / r >= 0
                                    }
                                }
                            },
                            PSEUDO: function(e, n) {
                                var i, o = b.pseudos[e] || b.setFilters[e.toLowerCase()] || t.error("unsupported pseudo: " + e);
                                return o[R] ? o(n) : o.length > 1 ? (i = [e, e, "", n], b.setFilters.hasOwnProperty(e.toLowerCase()) ? r(function(e, t) {
                                    for (var r, i = o(e, n), a = i.length; a--;) e[r = K(e, i[a])] = !(t[r] = i[a])
                                }) : function(e) {
                                    return o(e, 0, i)
                                }) : o
                            }
                        },
                        pseudos: {
                            not: r(function(e) {
                                var t = [],
                                    n = [],
                                    i = E(e.replace(ae, "$1"));
                                return i[R] ? r(function(e, t, n, r) {
                                    for (var o, a = i(e, null, r, []), s = e.length; s--;)(o = a[s]) && (e[s] = !(t[s] = o))
                                }) : function(e, r, o) {
                                    return t[0] = e,
                                        i(t, null, o, n),
                                        t[0] = null,
                                        !n.pop()
                                }
                            }),
                            has: r(function(e) {
                                return function(n) {
                                    return t(e, n).length > 0
                                }
                            }),
                            contains: r(function(e) {
                                return e = e.replace(xe, be),
                                    function(t) {
                                        return (t.textContent || t.innerText || w(t)).indexOf(e) > -1
                                    }
                            }),
                            lang: r(function(e) {
                                return de.test(e || "") || t.error("unsupported lang: " + e),
                                    e = e.replace(xe, be).toLowerCase(),
                                    function(t) {
                                        var n;
                                        do {
                                            if (n = H ? t.lang: t.getAttribute("xml:lang") || t.getAttribute("lang")) return (n = n.toLowerCase()) === e || 0 === n.indexOf(e + "-")
                                        } while (( t = t . parentNode ) && 1 === t.nodeType);
                                        return ! 1
                                    }
                            }),
                            target: function(t) {
                                var n = e.location && e.location.hash;
                                return n && n.slice(1) === t.id
                            },
                            root: function(e) {
                                return e === L
                            },
                            focus: function(e) {
                                return e === D.activeElement && (!D.hasFocus || D.hasFocus()) && !!(e.type || e.href || ~e.tabIndex)
                            },
                            enabled: function(e) {
                                return ! 1 === e.disabled
                            },
                            disabled: function(e) {
                                return ! 0 === e.disabled
                            },
                            checked: function(e) {
                                var t = e.nodeName.toLowerCase();
                                return "input" === t && !!e.checked || "option" === t && !!e.selected
                            },
                            selected: function(e) {
                                return e.parentNode && e.parentNode.selectedIndex,
                                !0 === e.selected
                            },
                            empty: function(e) {
                                for (e = e.firstChild; e; e = e.nextSibling) if (e.nodeType < 6) return ! 1;
                                return ! 0
                            },
                            parent: function(e) {
                                return ! b.pseudos.empty(e)
                            },
                            header: function(e) {
                                return he.test(e.nodeName)
                            },
                            input: function(e) {
                                return pe.test(e.nodeName)
                            },
                            button: function(e) {
                                var t = e.nodeName.toLowerCase();
                                return "input" === t && "button" === e.type || "button" === t
                            },
                            text: function(e) {
                                var t;
                                return "input" === e.nodeName.toLowerCase() && "text" === e.type && (null == (t = e.getAttribute("type")) || "text" === t.toLowerCase())
                            },
                            first: s(function() {
                                return [0]
                            }),
                            last: s(function(e, t) {
                                return [t - 1]
                            }),
                            eq: s(function(e, t, n) {
                                return [n < 0 ? n + t: n]
                            }),
                            even: s(function(e, t) {
                                for (var n = 0; n < t; n += 2) e.push(n);
                                return e
                            }),
                            odd: s(function(e, t) {
                                for (var n = 1; n < t; n += 2) e.push(n);
                                return e
                            }),
                            lt: s(function(e, t, n) {
                                for (var r = n < 0 ? n + t: n; --r >= 0;) e.push(r);
                                return e
                            }),
                            gt: s(function(e, t, n) {
                                for (var r = n < 0 ? n + t: n; ++r < t;) e.push(r);
                                return e
                            })
                        }
                    }).pseudos.nth = b.pseudos.eq;
                for (y in {
                    radio: !0,
                    checkbox: !0,
                    file: !0,
                    password: !0,
                    image: !0
                }) b.pseudos[y] = function(e) {
                    return function(t) {
                        return "input" === t.nodeName.toLowerCase() && t.type === e
                    }
                } (y);
                for (y in {
                    submit: !0,
                    reset: !0
                }) b.pseudos[y] = function(e) {
                    return function(t) {
                        var n = t.nodeName.toLowerCase();
                        return ("input" === n || "button" === n) && t.type === e
                    }
                } (y);
                return l.prototype = b.filters = b.pseudos,
                    b.setFilters = new l,
                    C = t.tokenize = function(e, n) {
                        var r, i, o, a, s, u, l, c = I[e + " "];
                        if (c) return n ? 0 : c.slice(0);
                        for (s = e, u = [], l = b.preFilter; s;) {
                            r && !(i = se.exec(s)) || (i && (s = s.slice(i[0].length) || s), u.push(o = [])),
                                r = !1,
                            (i = ue.exec(s)) && (r = i.shift(), o.push({
                                value: r,
                                type: i[0].replace(ae, " ")
                            }), s = s.slice(r.length));
                            for (a in b.filter) ! (i = fe[a].exec(s)) || l[a] && !(i = l[a](i)) || (r = i.shift(), o.push({
                                value: r,
                                type: a,
                                matches: i
                            }), s = s.slice(r.length));
                            if (!r) break
                        }
                        return n ? s.length: s ? t.error(e) : I(e, u).slice(0)
                    },
                    E = t.compile = function(e, t) {
                        var n, r = [],
                            i = [],
                            o = $[e + " "];
                        if (!o) {
                            for (t || (t = C(e)), n = t.length; n--;)(o = m(t[n]))[R] ? r.push(o) : i.push(o); (o = $(e, v(i, r))).selector = e
                        }
                        return o
                    },
                    N = t.select = function(e, t, n, r) {
                        var i, o, a, s, l, d = "function" == typeof e && e,
                            f = !r && C(e = d.selector || e);
                        if (n = n || [], 1 === f.length) {
                            if ((o = f[0] = f[0].slice(0)).length > 2 && "ID" === (a = o[0]).type && x.getById && 9 === t.nodeType && H && b.relative[o[1].type]) {
                                if (! (t = (b.find.ID(a.matches[0].replace(xe, be), t) || [])[0])) return n;
                                d && (t = t.parentNode),
                                    e = e.slice(o.shift().value.length)
                            }
                            for (i = fe.needsContext.test(e) ? 0 : o.length; i--&&(a = o[i], !b.relative[s = a.type]);) if ((l = b.find[s]) && (r = l(a.matches[0].replace(xe, be), ve.test(o[0].type) && u(t.parentNode) || t))) {
                                if (o.splice(i, 1), !(e = r.length && c(o))) return J.apply(n, r),
                                    n;
                                break
                            }
                        }
                        return (d || E(e, f))(r, t, !H, n, !t || ve.test(e) && u(t.parentNode) || t),
                            n
                    },
                    x.sortStable = R.split("").sort(z).join("") === R,
                    x.detectDuplicates = !!A,
                    j(),
                    x.sortDetached = i(function(e) {
                        return 1 & e.compareDocumentPosition(D.createElement("div"))
                    }),
                i(function(e) {
                    return e.innerHTML = "<a href='#'></a>",
                    "#" === e.firstChild.getAttribute("href")
                }) || o("type|href|height|width",
                    function(e, t, n) {
                        if (!n) return e.getAttribute(t, "type" === t.toLowerCase() ? 1 : 2)
                    }),
                x.attributes && i(function(e) {
                    return e.innerHTML = "<input/>",
                        e.firstChild.setAttribute("value", ""),
                    "" === e.firstChild.getAttribute("value")
                }) || o("value",
                    function(e, t, n) {
                        if (!n && "input" === e.nodeName.toLowerCase()) return e.defaultValue
                    }),
                i(function(e) {
                    return null == e.getAttribute("disabled")
                }) || o(Z,
                    function(e, t, n) {
                        var r;
                        if (!n) return ! 0 === e[t] ? t.toLowerCase() : (r = e.getAttributeNode(t)) && r.specified ? r.value: null
                    }),
                    t
            } (e);
            pe.find = ye,
                pe.expr = ye.selectors,
                pe.expr[":"] = pe.expr.pseudos,
                pe.uniqueSort = pe.unique = ye.uniqueSort,
                pe.text = ye.getText,
                pe.isXMLDoc = ye.isXML,
                pe.contains = ye.contains;
            var xe = function(e, t, n) {
                    for (var r = [], i = void 0 !== n; (e = e[t]) && 9 !== e.nodeType;) if (1 === e.nodeType) {
                        if (i && pe(e).is(n)) break;
                        r.push(e)
                    }
                    return r
                },
                be = function(e, t) {
                    for (var n = []; e; e = e.nextSibling) 1 === e.nodeType && e !== t && n.push(e);
                    return n
                },
                we = pe.expr.match.needsContext,
                Te = /^<([\w-]+)\s*\/?>(?:<\/\1>|)$/,
                Ce = /^.[^:#\[\.,]*$/;
            pe.filter = function(e, t, n) {
                var r = t[0];
                return n && (e = ":not(" + e + ")"),
                    1 === t.length && 1 === r.nodeType ? pe.find.matchesSelector(r, e) ? [r] : [] : pe.find.matches(e, pe.grep(t,
                        function(e) {
                            return 1 === e.nodeType
                        }))
            },
                pe.fn.extend({
                    find: function(e) {
                        var t, n = [],
                            r = this,
                            i = r.length;
                        if ("string" != typeof e) return this.pushStack(pe(e).filter(function() {
                            for (t = 0; t < i; t++) if (pe.contains(r[t], this)) return ! 0
                        }));
                        for (t = 0; t < i; t++) pe.find(e, r[t], n);
                        return n = this.pushStack(i > 1 ? pe.unique(n) : n),
                            n.selector = this.selector ? this.selector + " " + e: e,
                            n
                    },
                    filter: function(e) {
                        return this.pushStack(r(this, e || [], !1))
                    },
                    not: function(e) {
                        return this.pushStack(r(this, e || [], !0))
                    },
                    is: function(e) {
                        return !! r(this, "string" == typeof e && we.test(e) ? pe(e) : e || [], !1).length
                    }
                });
            var Ee, Ne = /^(?:\s*(<[\w\W]+>)[^>]*|#([\w-]*))$/; (pe.fn.init = function(e, t, n) {
                var r, i;
                if (!e) return this;
                if (n = n || Ee, "string" == typeof e) {
                    if (! (r = "<" === e.charAt(0) && ">" === e.charAt(e.length - 1) && e.length >= 3 ? [null, e, null] : Ne.exec(e)) || !r[1] && t) return ! t || t.jquery ? (t || n).find(e) : this.constructor(t).find(e);
                    if (r[1]) {
                        if (t = t instanceof pe ? t[0] : t, pe.merge(this, pe.parseHTML(r[1], t && t.nodeType ? t.ownerDocument || t: ie, !0)), Te.test(r[1]) && pe.isPlainObject(t)) for (r in t) pe.isFunction(this[r]) ? this[r](t[r]) : this.attr(r, t[r]);
                        return this
                    }
                    if ((i = ie.getElementById(r[2])) && i.parentNode) {
                        if (i.id !== r[2]) return Ee.find(e);
                        this.length = 1,
                            this[0] = i
                    }
                    return this.context = ie,
                        this.selector = e,
                        this
                }
                return e.nodeType ? (this.context = this[0] = e, this.length = 1, this) : pe.isFunction(e) ? void 0 !== n.ready ? n.ready(e) : e(pe) : (void 0 !== e.selector && (this.selector = e.selector, this.context = e.context), pe.makeArray(e, this))
            }).prototype = pe.fn,
                Ee = pe(ie);
            var ke = /^(?:parents|prev(?:Until|All))/,
                Se = {
                    children: !0,
                    contents: !0,
                    next: !0,
                    prev: !0
                };
            pe.fn.extend({
                has: function(e) {
                    var t, n = pe(e, this),
                        r = n.length;
                    return this.filter(function() {
                        for (t = 0; t < r; t++) if (pe.contains(this, n[t])) return ! 0
                    })
                },
                closest: function(e, t) {
                    for (var n, r = 0,
                             i = this.length,
                             o = [], a = we.test(e) || "string" != typeof e ? pe(e, t || this.context) : 0; r < i; r++) for (n = this[r]; n && n !== t; n = n.parentNode) if (n.nodeType < 11 && (a ? a.index(n) > -1 : 1 === n.nodeType && pe.find.matchesSelector(n, e))) {
                        o.push(n);
                        break
                    }
                    return this.pushStack(o.length > 1 ? pe.uniqueSort(o) : o)
                },
                index: function(e) {
                    return e ? "string" == typeof e ? pe.inArray(this[0], pe(e)) : pe.inArray(e.jquery ? e[0] : e, this) : this[0] && this[0].parentNode ? this.first().prevAll().length: -1
                },
                add: function(e, t) {
                    return this.pushStack(pe.uniqueSort(pe.merge(this.get(), pe(e, t))))
                },
                addBack: function(e) {
                    return this.add(null == e ? this.prevObject: this.prevObject.filter(e))
                }
            }),
                pe.each({
                        parent: function(e) {
                            var t = e.parentNode;
                            return t && 11 !== t.nodeType ? t: null
                        },
                        parents: function(e) {
                            return xe(e, "parentNode")
                        },
                        parentsUntil: function(e, t, n) {
                            return xe(e, "parentNode", n)
                        },
                        next: function(e) {
                            return i(e, "nextSibling")
                        },
                        prev: function(e) {
                            return i(e, "previousSibling")
                        },
                        nextAll: function(e) {
                            return xe(e, "nextSibling")
                        },
                        prevAll: function(e) {
                            return xe(e, "previousSibling")
                        },
                        nextUntil: function(e, t, n) {
                            return xe(e, "nextSibling", n)
                        },
                        prevUntil: function(e, t, n) {
                            return xe(e, "previousSibling", n)
                        },
                        siblings: function(e) {
                            return be((e.parentNode || {}).firstChild, e)
                        },
                        children: function(e) {
                            return be(e.firstChild)
                        },
                        contents: function(e) {
                            return pe.nodeName(e, "iframe") ? e.contentDocument || e.contentWindow.document: pe.merge([], e.childNodes)
                        }
                    },
                    function(e, t) {
                        pe.fn[e] = function(n, r) {
                            var i = pe.map(this, t, n);
                            return "Until" !== e.slice( - 5) && (r = n),
                            r && "string" == typeof r && (i = pe.filter(r, i)),
                            this.length > 1 && (Se[e] || (i = pe.uniqueSort(i)), ke.test(e) && (i = i.reverse())),
                                this.pushStack(i)
                        }
                    });
            var Ae = /\S+/g;
            pe.Callbacks = function(e) {
                e = "string" == typeof e ? o(e) : pe.extend({},
                    e);
                var t, n, r, i, a = [],
                    s = [],
                    u = -1,
                    l = function() {
                        for (i = e.once, r = t = !0; s.length; u = -1) for (n = s.shift(); ++u < a.length;) ! 1 === a[u].apply(n[0], n[1]) && e.stopOnFalse && (u = a.length, n = !1);
                        e.memory || (n = !1),
                            t = !1,
                        i && (a = n ? [] : "")
                    },
                    c = {
                        add: function() {
                            return a && (n && !t && (u = a.length - 1, s.push(n)),
                                function t(n) {
                                    pe.each(n,
                                        function(n, r) {
                                            pe.isFunction(r) ? e.unique && c.has(r) || a.push(r) : r && r.length && "string" !== pe.type(r) && t(r)
                                        })
                                } (arguments), n && !t && l()),
                                this
                        },
                        remove: function() {
                            return pe.each(arguments,
                                function(e, t) {
                                    for (var n; (n = pe.inArray(t, a, n)) > -1;) a.splice(n, 1),
                                    n <= u && u--
                                }),
                                this
                        },
                        has: function(e) {
                            return e ? pe.inArray(e, a) > -1 : a.length > 0
                        },
                        empty: function() {
                            return a && (a = []),
                                this
                        },
                        disable: function() {
                            return i = s = [],
                                a = n = "",
                                this
                        },
                        disabled: function() {
                            return ! a
                        },
                        lock: function() {
                            return i = !0,
                            n || c.disable(),
                                this
                        },
                        locked: function() {
                            return !! i
                        },
                        fireWith: function(e, n) {
                            return i || (n = [e, (n = n || []).slice ? n.slice() : n], s.push(n), t || l()),
                                this
                        },
                        fire: function() {
                            return c.fireWith(this, arguments),
                                this
                        },
                        fired: function() {
                            return !! r
                        }
                    };
                return c
            },
                pe.extend({
                    Deferred: function(e) {
                        var t = [["resolve", "done", pe.Callbacks("once memory"), "resolved"], ["reject", "fail", pe.Callbacks("once memory"), "rejected"], ["notify", "progress", pe.Callbacks("memory")]],
                            n = "pending",
                            r = {
                                state: function() {
                                    return n
                                },
                                always: function() {
                                    return i.done(arguments).fail(arguments),
                                        this
                                },
                                then: function() {
                                    var e = arguments;
                                    return pe.Deferred(function(n) {
                                        pe.each(t,
                                            function(t, o) {
                                                var a = pe.isFunction(e[t]) && e[t];
                                                i[o[1]](function() {
                                                    var e = a && a.apply(this, arguments);
                                                    e && pe.isFunction(e.promise) ? e.promise().progress(n.notify).done(n.resolve).fail(n.reject) : n[o[0] + "With"](this === r ? n.promise() : this, a ? [e] : arguments)
                                                })
                                            }),
                                            e = null
                                    }).promise()
                                },
                                promise: function(e) {
                                    return null != e ? pe.extend(e, r) : r
                                }
                            },
                            i = {};
                        return r.pipe = r.then,
                            pe.each(t,
                                function(e, o) {
                                    var a = o[2],
                                        s = o[3];
                                    r[o[1]] = a.add,
                                    s && a.add(function() {
                                            n = s
                                        },
                                        t[1 ^ e][2].disable, t[2][2].lock),
                                        i[o[0]] = function() {
                                            return i[o[0] + "With"](this === i ? r: this, arguments),
                                                this
                                        },
                                        i[o[0] + "With"] = a.fireWith
                                }),
                            r.promise(i),
                        e && e.call(i, i),
                            i
                    },
                    when: function(e) {
                        var t, n, r, i = 0,
                            o = oe.call(arguments),
                            a = o.length,
                            s = 1 !== a || e && pe.isFunction(e.promise) ? a: 0,
                            u = 1 === s ? e: pe.Deferred(),
                            l = function(e, n, r) {
                                return function(i) {
                                    n[e] = this,
                                        r[e] = arguments.length > 1 ? oe.call(arguments) : i,
                                        r === t ? u.notifyWith(n, r) : --s || u.resolveWith(n, r)
                                }
                            };
                        if (a > 1) for (t = new Array(a), n = new Array(a), r = new Array(a); i < a; i++) o[i] && pe.isFunction(o[i].promise) ? o[i].promise().progress(l(i, n, t)).done(l(i, r, o)).fail(u.reject) : --s;
                        return s || u.resolveWith(r, o),
                            u.promise()
                    }
                });
            var je;
            pe.fn.ready = function(e) {
                return pe.ready.promise().done(e),
                    this
            },
                pe.extend({
                    isReady: !1,
                    readyWait: 1,
                    holdReady: function(e) {
                        e ? pe.readyWait++:pe.ready(!0)
                    },
                    ready: function(e) { (!0 === e ? --pe.readyWait: pe.isReady) || (pe.isReady = !0, !0 !== e && --pe.readyWait > 0 || (je.resolveWith(ie, [pe]), pe.fn.triggerHandler && (pe(ie).triggerHandler("ready"), pe(ie).off("ready"))))
                    }
                }),
                pe.ready.promise = function(t) {
                    if (!je) if (je = pe.Deferred(), "complete" === ie.readyState || "loading" !== ie.readyState && !ie.documentElement.doScroll) e.setTimeout(pe.ready);
                    else if (ie.addEventListener) ie.addEventListener("DOMContentLoaded", s),
                        e.addEventListener("load", s);
                    else {
                        ie.attachEvent("onreadystatechange", s),
                            e.attachEvent("onload", s);
                        var n = !1;
                        try {
                            n = null == e.frameElement && ie.documentElement
                        } catch(e) {}
                        n && n.doScroll &&
                        function t() {
                            if (!pe.isReady) {
                                try {
                                    n.doScroll("left")
                                } catch(n) {
                                    return e.setTimeout(t, 50)
                                }
                                a(),
                                    pe.ready()
                            }
                        } ()
                    }
                    return je.promise(t)
                },
                pe.ready.promise();
            var De;
            for (De in pe(fe)) break;
            fe.ownFirst = "0" === De,
                fe.inlineBlockNeedsLayout = !1,
                pe(function() {
                    var e, t, n, r; (n = ie.getElementsByTagName("body")[0]) && n.style && (t = ie.createElement("div"), (r = ie.createElement("div")).style.cssText = "position:absolute;border:0;width:0;height:0;top:0;left:-9999px", n.appendChild(r).appendChild(t), void 0 !== t.style.zoom && (t.style.cssText = "display:inline;margin:0;border:0;padding:1px;width:1px;zoom:1", fe.inlineBlockNeedsLayout = e = 3 === t.offsetWidth, e && (n.style.zoom = 1)), n.removeChild(r))
                }),
                function() {
                    var e = ie.createElement("div");
                    fe.deleteExpando = !0;
                    try {
                        delete e.test
                    } catch(e) {
                        fe.deleteExpando = !1
                    }
                    e = null
                } ();
            var Le = function(e) {
                    var t = pe.noData[(e.nodeName + " ").toLowerCase()],
                        n = +e.nodeType || 1;
                    return (1 === n || 9 === n) && (!t || !0 !== t && e.getAttribute("classid") === t)
                },
                He = /^(?:\{[\w\W]*\}|\[[\w\W]*\])$/,
                qe = /([A-Z])/g;
            pe.extend({
                cache: {},
                noData: {
                    "applet ": !0,
                    "embed ": !0,
                    "object ": "clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
                },
                hasData: function(e) {
                    return !! (e = e.nodeType ? pe.cache[e[pe.expando]] : e[pe.expando]) && !l(e)
                },
                data: function(e, t, n) {
                    return c(e, t, n)
                },
                removeData: function(e, t) {
                    return d(e, t)
                },
                _data: function(e, t, n) {
                    return c(e, t, n, !0)
                },
                _removeData: function(e, t) {
                    return d(e, t, !0)
                }
            }),
                pe.fn.extend({
                    data: function(e, t) {
                        var n, r, i, o = this[0],
                            a = o && o.attributes;
                        if (void 0 === e) {
                            if (this.length && (i = pe.data(o), 1 === o.nodeType && !pe._data(o, "parsedAttrs"))) {
                                for (n = a.length; n--;) a[n] && 0 === (r = a[n].name).indexOf("data-") && u(o, r = pe.camelCase(r.slice(5)), i[r]);
                                pe._data(o, "parsedAttrs", !0)
                            }
                            return i
                        }
                        return "object" == typeof e ? this.each(function() {
                            pe.data(this, e)
                        }) : arguments.length > 1 ? this.each(function() {
                            pe.data(this, e, t)
                        }) : o ? u(o, e, pe.data(o, e)) : void 0
                    },
                    removeData: function(e) {
                        return this.each(function() {
                            pe.removeData(this, e)
                        })
                    }
                }),
                pe.extend({
                    queue: function(e, t, n) {
                        var r;
                        if (e) return t = (t || "fx") + "queue",
                            r = pe._data(e, t),
                        n && (!r || pe.isArray(n) ? r = pe._data(e, t, pe.makeArray(n)) : r.push(n)),
                        r || []
                    },
                    dequeue: function(e, t) {
                        t = t || "fx";
                        var n = pe.queue(e, t),
                            r = n.length,
                            i = n.shift(),
                            o = pe._queueHooks(e, t);
                        "inprogress" === i && (i = n.shift(), r--),
                        i && ("fx" === t && n.unshift("inprogress"), delete o.stop, i.call(e,
                            function() {
                                pe.dequeue(e, t)
                            },
                            o)),
                        !r && o && o.empty.fire()
                    },
                    _queueHooks: function(e, t) {
                        var n = t + "queueHooks";
                        return pe._data(e, n) || pe._data(e, n, {
                                empty: pe.Callbacks("once memory").add(function() {
                                    pe._removeData(e, t + "queue"),
                                        pe._removeData(e, n)
                                })
                            })
                    }
                }),
                pe.fn.extend({
                    queue: function(e, t) {
                        var n = 2;
                        return "string" != typeof e && (t = e, e = "fx", n--),
                            arguments.length < n ? pe.queue(this[0], e) : void 0 === t ? this: this.each(function() {
                                var n = pe.queue(this, e, t);
                                pe._queueHooks(this, e),
                                "fx" === e && "inprogress" !== n[0] && pe.dequeue(this, e)
                            })
                    },
                    dequeue: function(e) {
                        return this.each(function() {
                            pe.dequeue(this, e)
                        })
                    },
                    clearQueue: function(e) {
                        return this.queue(e || "fx", [])
                    },
                    promise: function(e, t) {
                        var n, r = 1,
                            i = pe.Deferred(),
                            o = this,
                            a = this.length,
                            s = function() {--r || i.resolveWith(o, [o])
                            };
                        for ("string" != typeof e && (t = e, e = void 0), e = e || "fx"; a--;)(n = pe._data(o[a], e + "queueHooks")) && n.empty && (r++, n.empty.add(s));
                        return s(),
                            i.promise(t)
                    }
                }),
                function() {
                    var e;
                    fe.shrinkWrapBlocks = function() {
                        if (null != e) return e;
                        e = !1;
                        var t, n, r;
                        return (n = ie.getElementsByTagName("body")[0]) && n.style ? (t = ie.createElement("div"), r = ie.createElement("div"), r.style.cssText = "position:absolute;border:0;width:0;height:0;top:0;left:-9999px", n.appendChild(r).appendChild(t), void 0 !== t.style.zoom && (t.style.cssText = "-webkit-box-sizing:content-box;-moz-box-sizing:content-box;box-sizing:content-box;display:block;margin:0;border:0;padding:1px;width:1px;zoom:1", t.appendChild(ie.createElement("div")).style.width = "5px", e = 3 !== t.offsetWidth), n.removeChild(r), e) : void 0
                    }
                } ();
            var _e = /[+-]?(?:\d*\.|)\d+(?:[eE][+-]?\d+|)/.source,
                Me = new RegExp("^(?:([+-])=|)(" + _e + ")([a-z%]*)$", "i"),
                Fe = ["Top", "Right", "Bottom", "Left"],
                Re = function(e, t) {
                    return e = t || e,
                    "none" === pe.css(e, "display") || !pe.contains(e.ownerDocument, e)
                },
                Oe = function(e, t, n, r, i, o, a) {
                    var s = 0,
                        u = e.length,
                        l = null == n;
                    if ("object" === pe.type(n)) {
                        i = !0;
                        for (s in n) Oe(e, t, s, n[s], !0, o, a)
                    } else if (void 0 !== r && (i = !0, pe.isFunction(r) || (a = !0), l && (a ? (t.call(e, r), t = null) : (l = t, t = function(e, t, n) {
                            return l.call(pe(e), n)
                        })), t)) for (; s < u; s++) t(e[s], n, a ? r: r.call(e[s], s, t(e[s], n)));
                    return i ? e: l ? t.call(e) : u ? t(e[0], n) : o
                },
                Pe = /^(?:checkbox|radio)$/i,
                We = /<([\w:-]+)/,
                Be = /^$|\/(?:java|ecma)script/i,
                Ie = /^\s+/,
                $e = "abbr|article|aside|audio|bdi|canvas|data|datalist|details|dialog|figcaption|figure|footer|header|hgroup|main|mark|meter|nav|output|picture|progress|section|summary|template|time|video"; !
                function() {
                    var e = ie.createElement("div"),
                        t = ie.createDocumentFragment(),
                        n = ie.createElement("input");
                    e.innerHTML = "  <link/><table></table><a href='/a'>a</a><input type='checkbox'/>",
                        fe.leadingWhitespace = 3 === e.firstChild.nodeType,
                        fe.tbody = !e.getElementsByTagName("tbody").length,
                        fe.htmlSerialize = !!e.getElementsByTagName("link").length,
                        fe.html5Clone = "<:nav></:nav>" !== ie.createElement("nav").cloneNode(!0).outerHTML,
                        n.type = "checkbox",
                        n.checked = !0,
                        t.appendChild(n),
                        fe.appendChecked = n.checked,
                        e.innerHTML = "<textarea>x</textarea>",
                        fe.noCloneChecked = !!e.cloneNode(!0).lastChild.defaultValue,
                        t.appendChild(e),
                        (n = ie.createElement("input")).setAttribute("type", "radio"),
                        n.setAttribute("checked", "checked"),
                        n.setAttribute("name", "t"),
                        e.appendChild(n),
                        fe.checkClone = e.cloneNode(!0).cloneNode(!0).lastChild.checked,
                        fe.noCloneEvent = !!e.addEventListener,
                        e[pe.expando] = 1,
                        fe.attributes = !e.getAttribute(pe.expando)
                } ();
            var ze = {
                option: [1, "<select multiple='multiple'>", "</select>"],
                legend: [1, "<fieldset>", "</fieldset>"],
                area: [1, "<map>", "</map>"],
                param: [1, "<object>", "</object>"],
                thead: [1, "<table>", "</table>"],
                tr: [2, "<table><tbody>", "</tbody></table>"],
                col: [2, "<table><tbody></tbody><colgroup>", "</colgroup></table>"],
                td: [3, "<table><tbody><tr>", "</tr></tbody></table>"],
                _default: fe.htmlSerialize ? [0, "", ""] : [1, "X<div>", "</div>"]
            };
            ze.optgroup = ze.option,
                ze.tbody = ze.tfoot = ze.colgroup = ze.caption = ze.thead,
                ze.th = ze.td;
            var Xe = /<|&#?\w+;/,
                Ue = /<tbody/i; !
                function() {
                    var t, n, r = ie.createElement("div");
                    for (t in {
                        submit: !0,
                        change: !0,
                        focusin: !0
                    }) n = "on" + t,
                    (fe[t] = n in e) || (r.setAttribute(n, "t"), fe[t] = !1 === r.attributes[n].expando);
                    r = null
                } ();
            var Ve = /^(?:input|select|textarea)$/i,
                Ye = /^key/,
                Ge = /^(?:mouse|pointer|contextmenu|drag|drop)|click/,
                Je = /^(?:focusinfocus|focusoutblur)$/,
                Qe = /^([^.]*)(?:\.(.+)|)/;
            pe.event = {
                global: {},
                add: function(e, t, n, r, i) {
                    var o, a, s, u, l, c, d, f, p, h, g, m = pe._data(e);
                    if (m) {
                        for (n.handler && (n = (u = n).handler, i = u.selector), n.guid || (n.guid = pe.guid++), (a = m.events) || (a = m.events = {}), (c = m.handle) || ((c = m.handle = function(e) {
                            return void 0 === pe || e && pe.event.triggered === e.type ? void 0 : pe.event.dispatch.apply(c.elem, arguments)
                        }).elem = e), s = (t = (t || "").match(Ae) || [""]).length; s--;) p = g = (o = Qe.exec(t[s]) || [])[1],
                            h = (o[2] || "").split(".").sort(),
                        p && (l = pe.event.special[p] || {},
                            p = (i ? l.delegateType: l.bindType) || p, l = pe.event.special[p] || {},
                            d = pe.extend({
                                    type: p,
                                    origType: g,
                                    data: r,
                                    handler: n,
                                    guid: n.guid,
                                    selector: i,
                                    needsContext: i && pe.expr.match.needsContext.test(i),
                                    namespace: h.join(".")
                                },
                                u), (f = a[p]) || ((f = a[p] = []).delegateCount = 0, l.setup && !1 !== l.setup.call(e, r, h, c) || (e.addEventListener ? e.addEventListener(p, c, !1) : e.attachEvent && e.attachEvent("on" + p, c))), l.add && (l.add.call(e, d), d.handler.guid || (d.handler.guid = n.guid)), i ? f.splice(f.delegateCount++, 0, d) : f.push(d), pe.event.global[p] = !0);
                        e = null
                    }
                },
                remove: function(e, t, n, r, i) {
                    var o, a, s, u, l, c, d, f, p, h, g, m = pe.hasData(e) && pe._data(e);
                    if (m && (c = m.events)) {
                        for (l = (t = (t || "").match(Ae) || [""]).length; l--;) if (s = Qe.exec(t[l]) || [], p = g = s[1], h = (s[2] || "").split(".").sort(), p) {
                            for (d = pe.event.special[p] || {},
                                     f = c[p = (r ? d.delegateType: d.bindType) || p] || [], s = s[2] && new RegExp("(^|\\.)" + h.join("\\.(?:.*\\.|)") + "(\\.|$)"), u = o = f.length; o--;) a = f[o],
                            !i && g !== a.origType || n && n.guid !== a.guid || s && !s.test(a.namespace) || r && r !== a.selector && ("**" !== r || !a.selector) || (f.splice(o, 1), a.selector && f.delegateCount--, d.remove && d.remove.call(e, a));
                            u && !f.length && (d.teardown && !1 !== d.teardown.call(e, h, m.handle) || pe.removeEvent(e, p, m.handle), delete c[p])
                        } else for (p in c) pe.event.remove(e, p + t[l], n, r, !0);
                        pe.isEmptyObject(c) && (delete m.handle, pe._removeData(e, "events"))
                    }
                },
                trigger: function(t, n, r, i) {
                    var o, a, s, u, l, c, d, f = [r || ie],
                        p = de.call(t, "type") ? t.type: t,
                        h = de.call(t, "namespace") ? t.namespace.split(".") : [];
                    if (s = c = r = r || ie, 3 !== r.nodeType && 8 !== r.nodeType && !Je.test(p + pe.event.triggered) && (p.indexOf(".") > -1 && (p = (h = p.split(".")).shift(), h.sort()), a = p.indexOf(":") < 0 && "on" + p, t = t[pe.expando] ? t: new pe.Event(p, "object" == typeof t && t), t.isTrigger = i ? 2 : 3, t.namespace = h.join("."), t.rnamespace = t.namespace ? new RegExp("(^|\\.)" + h.join("\\.(?:.*\\.|)") + "(\\.|$)") : null, t.result = void 0, t.target || (t.target = r), n = null == n ? [t] : pe.makeArray(n, [t]), l = pe.event.special[p] || {},
                        i || !l.trigger || !1 !== l.trigger.apply(r, n))) {
                        if (!i && !l.noBubble && !pe.isWindow(r)) {
                            for (u = l.delegateType || p, Je.test(u + p) || (s = s.parentNode); s; s = s.parentNode) f.push(s),
                                c = s;
                            c === (r.ownerDocument || ie) && f.push(c.defaultView || c.parentWindow || e)
                        }
                        for (d = 0; (s = f[d++]) && !t.isPropagationStopped();) t.type = d > 1 ? u: l.bindType || p,
                        (o = (pe._data(s, "events") || {})[t.type] && pe._data(s, "handle")) && o.apply(s, n),
                        (o = a && s[a]) && o.apply && Le(s) && (t.result = o.apply(s, n), !1 === t.result && t.preventDefault());
                        if (t.type = p, !i && !t.isDefaultPrevented() && (!l._default || !1 === l._default.apply(f.pop(), n)) && Le(r) && a && r[p] && !pe.isWindow(r)) { (c = r[a]) && (r[a] = null),
                            pe.event.triggered = p;
                            try {
                                r[p]()
                            } catch(e) {}
                            pe.event.triggered = void 0,
                            c && (r[a] = c)
                        }
                        return t.result
                    }
                },
                dispatch: function(e) {
                    e = pe.event.fix(e);
                    var t, n, r, i, o, a = [],
                        s = oe.call(arguments),
                        u = (pe._data(this, "events") || {})[e.type] || [],
                        l = pe.event.special[e.type] || {};
                    if (s[0] = e, e.delegateTarget = this, !l.preDispatch || !1 !== l.preDispatch.call(this, e)) {
                        for (a = pe.event.handlers.call(this, e, u), t = 0; (i = a[t++]) && !e.isPropagationStopped();) for (e.currentTarget = i.elem, n = 0; (o = i.handlers[n++]) && !e.isImmediatePropagationStopped();) e.rnamespace && !e.rnamespace.test(o.namespace) || (e.handleObj = o, e.data = o.data, void 0 !== (r = ((pe.event.special[o.origType] || {}).handle || o.handler).apply(i.elem, s)) && !1 === (e.result = r) && (e.preventDefault(), e.stopPropagation()));
                        return l.postDispatch && l.postDispatch.call(this, e),
                            e.result
                    }
                },
                handlers: function(e, t) {
                    var n, r, i, o, a = [],
                        s = t.delegateCount,
                        u = e.target;
                    if (s && u.nodeType && ("click" !== e.type || isNaN(e.button) || e.button < 1)) for (; u != this; u = u.parentNode || this) if (1 === u.nodeType && (!0 !== u.disabled || "click" !== e.type)) {
                        for (r = [], n = 0; n < s; n++) void 0 === r[i = (o = t[n]).selector + " "] && (r[i] = o.needsContext ? pe(i, this).index(u) > -1 : pe.find(i, this, null, [u]).length),
                        r[i] && r.push(o);
                        r.length && a.push({
                            elem: u,
                            handlers: r
                        })
                    }
                    return s < t.length && a.push({
                        elem: this,
                        handlers: t.slice(s)
                    }),
                        a
                },
                fix: function(e) {
                    if (e[pe.expando]) return e;
                    var t, n, r, i = e.type,
                        o = e,
                        a = this.fixHooks[i];
                    for (a || (this.fixHooks[i] = a = Ge.test(i) ? this.mouseHooks: Ye.test(i) ? this.keyHooks: {}), r = a.props ? this.props.concat(a.props) : this.props, e = new pe.Event(o), t = r.length; t--;) e[n = r[t]] = o[n];
                    return e.target || (e.target = o.srcElement || ie),
                    3 === e.target.nodeType && (e.target = e.target.parentNode),
                        e.metaKey = !!e.metaKey,
                        a.filter ? a.filter(e, o) : e
                },
                props: "altKey bubbles cancelable ctrlKey currentTarget detail eventPhase metaKey relatedTarget shiftKey target timeStamp view which".split(" "),
                fixHooks: {},
                keyHooks: {
                    props: "char charCode key keyCode".split(" "),
                    filter: function(e, t) {
                        return null == e.which && (e.which = null != t.charCode ? t.charCode: t.keyCode),
                            e
                    }
                },
                mouseHooks: {
                    props: "button buttons clientX clientY fromElement offsetX offsetY pageX pageY screenX screenY toElement".split(" "),
                    filter: function(e, t) {
                        var n, r, i, o = t.button,
                            a = t.fromElement;
                        return null == e.pageX && null != t.clientX && (i = (r = e.target.ownerDocument || ie).documentElement, n = r.body, e.pageX = t.clientX + (i && i.scrollLeft || n && n.scrollLeft || 0) - (i && i.clientLeft || n && n.clientLeft || 0), e.pageY = t.clientY + (i && i.scrollTop || n && n.scrollTop || 0) - (i && i.clientTop || n && n.clientTop || 0)),
                        !e.relatedTarget && a && (e.relatedTarget = a === e.target ? t.toElement: a),
                        e.which || void 0 === o || (e.which = 1 & o ? 1 : 2 & o ? 3 : 4 & o ? 2 : 0),
                            e
                    }
                },
                special: {
                    load: {
                        noBubble: !0
                    },
                    focus: {
                        trigger: function() {
                            if (this !== b() && this.focus) try {
                                return this.focus(),
                                    !1
                            } catch(e) {}
                        },
                        delegateType: "focusin"
                    },
                    blur: {
                        trigger: function() {
                            if (this === b() && this.blur) return this.blur(),
                                !1
                        },
                        delegateType: "focusout"
                    },
                    click: {
                        trigger: function() {
                            if (pe.nodeName(this, "input") && "checkbox" === this.type && this.click) return this.click(),
                                !1
                        },
                        _default: function(e) {
                            return pe.nodeName(e.target, "a")
                        }
                    },
                    beforeunload: {
                        postDispatch: function(e) {
                            void 0 !== e.result && e.originalEvent && (e.originalEvent.returnValue = e.result)
                        }
                    }
                },
                simulate: function(e, t, n) {
                    var r = pe.extend(new pe.Event, n, {
                        type: e,
                        isSimulated: !0
                    });
                    pe.event.trigger(r, null, t),
                    r.isDefaultPrevented() && n.preventDefault()
                }
            },
                pe.removeEvent = ie.removeEventListener ?
                    function(e, t, n) {
                        e.removeEventListener && e.removeEventListener(t, n)
                    }: function(e, t, n) {
                    var r = "on" + t;
                    e.detachEvent && (void 0 === e[r] && (e[r] = null), e.detachEvent(r, n))
                },
                pe.Event = function(e, t) {
                    if (! (this instanceof pe.Event)) return new pe.Event(e, t);
                    e && e.type ? (this.originalEvent = e, this.type = e.type, this.isDefaultPrevented = e.defaultPrevented || void 0 === e.defaultPrevented && !1 === e.returnValue ? y: x) : this.type = e,
                    t && pe.extend(this, t),
                        this.timeStamp = e && e.timeStamp || pe.now(),
                        this[pe.expando] = !0
                },
                pe.Event.prototype = {
                    constructor: pe.Event,
                    isDefaultPrevented: x,
                    isPropagationStopped: x,
                    isImmediatePropagationStopped: x,
                    preventDefault: function() {
                        var e = this.originalEvent;
                        this.isDefaultPrevented = y,
                        e && (e.preventDefault ? e.preventDefault() : e.returnValue = !1)
                    },
                    stopPropagation: function() {
                        var e = this.originalEvent;
                        this.isPropagationStopped = y,
                        e && !this.isSimulated && (e.stopPropagation && e.stopPropagation(), e.cancelBubble = !0)
                    },
                    stopImmediatePropagation: function() {
                        var e = this.originalEvent;
                        this.isImmediatePropagationStopped = y,
                        e && e.stopImmediatePropagation && e.stopImmediatePropagation(),
                            this.stopPropagation()
                    }
                },
                pe.each({
                        mouseenter: "mouseover",
                        mouseleave: "mouseout",
                        pointerenter: "pointerover",
                        pointerleave: "pointerout"
                    },
                    function(e, t) {
                        pe.event.special[e] = {
                            delegateType: t,
                            bindType: t,
                            handle: function(e) {
                                var n, r = this,
                                    i = e.relatedTarget,
                                    o = e.handleObj;
                                return i && (i === r || pe.contains(r, i)) || (e.type = o.origType, n = o.handler.apply(this, arguments), e.type = t),
                                    n
                            }
                        }
                    }),
            fe.submit || (pe.event.special.submit = {
                setup: function() {
                    if (pe.nodeName(this, "form")) return ! 1;
                    pe.event.add(this, "click._submit keypress._submit",
                        function(e) {
                            var t = e.target,
                                n = pe.nodeName(t, "input") || pe.nodeName(t, "button") ? pe.prop(t, "form") : void 0;
                            n && !pe._data(n, "submit") && (pe.event.add(n, "submit._submit",
                                function(e) {
                                    e._submitBubble = !0
                                }), pe._data(n, "submit", !0))
                        })
                },
                postDispatch: function(e) {
                    e._submitBubble && (delete e._submitBubble, this.parentNode && !e.isTrigger && pe.event.simulate("submit", this.parentNode, e))
                },
                teardown: function() {
                    if (pe.nodeName(this, "form")) return ! 1;
                    pe.event.remove(this, "._submit")
                }
            }),
            fe.change || (pe.event.special.change = {
                setup: function() {
                    if (Ve.test(this.nodeName)) return "checkbox" !== this.type && "radio" !== this.type || (pe.event.add(this, "propertychange._change",
                        function(e) {
                            "checked" === e.originalEvent.propertyName && (this._justChanged = !0)
                        }), pe.event.add(this, "click._change",
                        function(e) {
                            this._justChanged && !e.isTrigger && (this._justChanged = !1),
                                pe.event.simulate("change", this, e)
                        })),
                        !1;
                    pe.event.add(this, "beforeactivate._change",
                        function(e) {
                            var t = e.target;
                            Ve.test(t.nodeName) && !pe._data(t, "change") && (pe.event.add(t, "change._change",
                                function(e) { ! this.parentNode || e.isSimulated || e.isTrigger || pe.event.simulate("change", this.parentNode, e)
                                }), pe._data(t, "change", !0))
                        })
                },
                handle: function(e) {
                    var t = e.target;
                    if (this !== t || e.isSimulated || e.isTrigger || "radio" !== t.type && "checkbox" !== t.type) return e.handleObj.handler.apply(this, arguments)
                },
                teardown: function() {
                    return pe.event.remove(this, "._change"),
                        !Ve.test(this.nodeName)
                }
            }),
            fe.focusin || pe.each({
                    focus: "focusin",
                    blur: "focusout"
                },
                function(e, t) {
                    var n = function(e) {
                        pe.event.simulate(t, e.target, pe.event.fix(e))
                    };
                    pe.event.special[t] = {
                        setup: function() {
                            var r = this.ownerDocument || this,
                                i = pe._data(r, t);
                            i || r.addEventListener(e, n, !0),
                                pe._data(r, t, (i || 0) + 1)
                        },
                        teardown: function() {
                            var r = this.ownerDocument || this,
                                i = pe._data(r, t) - 1;
                            i ? pe._data(r, t, i) : (r.removeEventListener(e, n, !0), pe._removeData(r, t))
                        }
                    }
                }),
                pe.fn.extend({
                    on: function(e, t, n, r) {
                        return w(this, e, t, n, r)
                    },
                    one: function(e, t, n, r) {
                        return w(this, e, t, n, r, 1)
                    },
                    off: function(e, t, n) {
                        var r, i;
                        if (e && e.preventDefault && e.handleObj) return r = e.handleObj,
                            pe(e.delegateTarget).off(r.namespace ? r.origType + "." + r.namespace: r.origType, r.selector, r.handler),
                            this;
                        if ("object" == typeof e) {
                            for (i in e) this.off(i, t, e[i]);
                            return this
                        }
                        return ! 1 !== t && "function" != typeof t || (n = t, t = void 0),
                        !1 === n && (n = x),
                            this.each(function() {
                                pe.event.remove(this, e, n, t)
                            })
                    },
                    trigger: function(e, t) {
                        return this.each(function() {
                            pe.event.trigger(e, t, this)
                        })
                    },
                    triggerHandler: function(e, t) {
                        var n = this[0];
                        if (n) return pe.event.trigger(e, t, n, !0)
                    }
                });
            var Ke = / jQuery\d+="(?:null|\d+)"/g,
                Ze = new RegExp("<(?:" + $e + ")[\\s/>]", "i"),
                et = /<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:-]+)[^>]*)\/>/gi,
                tt = /<script|<style|<link/i,
                nt = /checked\s*(?:[^=]|=\s*.checked.)/i,
                rt = /^true\/(.*)/,
                it = /^\s*<!(?:\[CDATA\[|--)|(?:\]\]|--)>\s*$/g,
                ot = p(ie).appendChild(ie.createElement("div"));
            pe.extend({
                htmlPrefilter: function(e) {
                    return e.replace(et, "<$1></$2>")
                },
                clone: function(e, t, n) {
                    var r, i, o, a, s, u = pe.contains(e.ownerDocument, e);
                    if (fe.html5Clone || pe.isXMLDoc(e) || !Ze.test("<" + e.nodeName + ">") ? o = e.cloneNode(!0) : (ot.innerHTML = e.outerHTML, ot.removeChild(o = ot.firstChild)), !(fe.noCloneEvent && fe.noCloneChecked || 1 !== e.nodeType && 11 !== e.nodeType || pe.isXMLDoc(e))) for (r = h(o), s = h(e), a = 0; null != (i = s[a]); ++a) r[a] && k(i, r[a]);
                    if (t) if (n) for (s = s || h(e), r = r || h(o), a = 0; null != (i = s[a]); a++) N(i, r[a]);
                    else N(e, o);
                    return (r = h(o, "script")).length > 0 && g(r, !u && h(e, "script")),
                        r = s = i = null,
                        o
                },
                cleanData: function(e, t) {
                    for (var n, r, i, o, a = 0,
                             s = pe.expando,
                             u = pe.cache,
                             l = fe.attributes,
                             c = pe.event.special; null != (n = e[a]); a++) if ((t || Le(n)) && (i = n[s], o = i && u[i])) {
                        if (o.events) for (r in o.events) c[r] ? pe.event.remove(n, r) : pe.removeEvent(n, r, o.handle);
                        u[i] && (delete u[i], l || void 0 === n.removeAttribute ? n[s] = void 0 : n.removeAttribute(s), re.push(i))
                    }
                }
            }),
                pe.fn.extend({
                    domManip: S,
                    detach: function(e) {
                        return A(this, e, !0)
                    },
                    remove: function(e) {
                        return A(this, e)
                    },
                    text: function(e) {
                        return Oe(this,
                            function(e) {
                                return void 0 === e ? pe.text(this) : this.empty().append((this[0] && this[0].ownerDocument || ie).createTextNode(e))
                            },
                            null, e, arguments.length)
                    },
                    append: function() {
                        return S(this, arguments,
                            function(e) {
                                1 !== this.nodeType && 11 !== this.nodeType && 9 !== this.nodeType || T(this, e).appendChild(e)
                            })
                    },
                    prepend: function() {
                        return S(this, arguments,
                            function(e) {
                                if (1 === this.nodeType || 11 === this.nodeType || 9 === this.nodeType) {
                                    var t = T(this, e);
                                    t.insertBefore(e, t.firstChild)
                                }
                            })
                    },
                    before: function() {
                        return S(this, arguments,
                            function(e) {
                                this.parentNode && this.parentNode.insertBefore(e, this)
                            })
                    },
                    after: function() {
                        return S(this, arguments,
                            function(e) {
                                this.parentNode && this.parentNode.insertBefore(e, this.nextSibling)
                            })
                    },
                    empty: function() {
                        for (var e, t = 0; null != (e = this[t]); t++) {
                            for (1 === e.nodeType && pe.cleanData(h(e, !1)); e.firstChild;) e.removeChild(e.firstChild);
                            e.options && pe.nodeName(e, "select") && (e.options.length = 0)
                        }
                        return this
                    },
                    clone: function(e, t) {
                        return e = null != e && e,
                            t = null == t ? e: t,
                            this.map(function() {
                                return pe.clone(this, e, t)
                            })
                    },
                    html: function(e) {
                        return Oe(this,
                            function(e) {
                                var t = this[0] || {},
                                    n = 0,
                                    r = this.length;
                                if (void 0 === e) return 1 === t.nodeType ? t.innerHTML.replace(Ke, "") : void 0;
                                if ("string" == typeof e && !tt.test(e) && (fe.htmlSerialize || !Ze.test(e)) && (fe.leadingWhitespace || !Ie.test(e)) && !ze[(We.exec(e) || ["", ""])[1].toLowerCase()]) {
                                    e = pe.htmlPrefilter(e);
                                    try {
                                        for (; n < r; n++) 1 === (t = this[n] || {}).nodeType && (pe.cleanData(h(t, !1)), t.innerHTML = e);
                                        t = 0
                                    } catch(e) {}
                                }
                                t && this.empty().append(e)
                            },
                            null, e, arguments.length)
                    },
                    replaceWith: function() {
                        var e = [];
                        return S(this, arguments,
                            function(t) {
                                var n = this.parentNode;
                                pe.inArray(this, e) < 0 && (pe.cleanData(h(this)), n && n.replaceChild(t, this))
                            },
                            e)
                    }
                }),
                pe.each({
                        appendTo: "append",
                        prependTo: "prepend",
                        insertBefore: "before",
                        insertAfter: "after",
                        replaceAll: "replaceWith"
                    },
                    function(e, t) {
                        pe.fn[e] = function(e) {
                            for (var n, r = 0,
                                     i = [], o = pe(e), a = o.length - 1; r <= a; r++) n = r === a ? this: this.clone(!0),
                                pe(o[r])[t](n),
                                se.apply(i, n.get());
                            return this.pushStack(i)
                        }
                    });
            var at, st = {
                    HTML: "block",
                    BODY: "block"
                },
                ut = /^margin/,
                lt = new RegExp("^(" + _e + ")(?!px)[a-z%]+$", "i"),
                ct = function(e, t, n, r) {
                    var i, o, a = {};
                    for (o in t) a[o] = e.style[o],
                        e.style[o] = t[o];
                    i = n.apply(e, r || []);
                    for (o in t) e.style[o] = a[o];
                    return i
                },
                dt = ie.documentElement; !
                function() {
                    function t() {
                        var t, c, d = ie.documentElement;
                        d.appendChild(u),
                            l.style.cssText = "-webkit-box-sizing:border-box;box-sizing:border-box;position:relative;display:block;margin:auto;border:1px;padding:1px;top:1%;width:50%",
                            n = i = s = !1,
                            r = a = !0,
                        e.getComputedStyle && (c = e.getComputedStyle(l), n = "1%" !== (c || {}).top, s = "2px" === (c || {}).marginLeft, i = "4px" === (c || {
                            width: "4px"
                        }).width, l.style.marginRight = "50%", r = "4px" === (c || {
                            marginRight: "4px"
                        }).marginRight, (t = l.appendChild(ie.createElement("div"))).style.cssText = l.style.cssText = "-webkit-box-sizing:content-box;-moz-box-sizing:content-box;box-sizing:content-box;display:block;margin:0;border:0;padding:0", t.style.marginRight = t.style.width = "0", l.style.width = "1px", a = !parseFloat((e.getComputedStyle(t) || {}).marginRight), l.removeChild(t)),
                            l.style.display = "none",
                        (o = 0 === l.getClientRects().length) && (l.style.display = "", l.innerHTML = "<table><tr><td></td><td>t</td></tr></table>", l.childNodes[0].style.borderCollapse = "separate", (t = l.getElementsByTagName("td"))[0].style.cssText = "margin:0;border:0;padding:0;display:none", (o = 0 === t[0].offsetHeight) && (t[0].style.display = "", t[1].style.display = "none", o = 0 === t[0].offsetHeight)),
                            d.removeChild(u)
                    }
                    var n, r, i, o, a, s, u = ie.createElement("div"),
                        l = ie.createElement("div");
                    l.style && (l.style.cssText = "float:left;opacity:.5", fe.opacity = "0.5" === l.style.opacity, fe.cssFloat = !!l.style.cssFloat, l.style.backgroundClip = "content-box", l.cloneNode(!0).style.backgroundClip = "", fe.clearCloneStyle = "content-box" === l.style.backgroundClip, (u = ie.createElement("div")).style.cssText = "border:0;width:8px;height:0;top:0;left:-9999px;padding:0;margin-top:1px;position:absolute", l.innerHTML = "", u.appendChild(l), fe.boxSizing = "" === l.style.boxSizing || "" === l.style.MozBoxSizing || "" === l.style.WebkitBoxSizing, pe.extend(fe, {
                        reliableHiddenOffsets: function() {
                            return null == n && t(),
                                o
                        },
                        boxSizingReliable: function() {
                            return null == n && t(),
                                i
                        },
                        pixelMarginRight: function() {
                            return null == n && t(),
                                r
                        },
                        pixelPosition: function() {
                            return null == n && t(),
                                n
                        },
                        reliableMarginRight: function() {
                            return null == n && t(),
                                a
                        },
                        reliableMarginLeft: function() {
                            return null == n && t(),
                                s
                        }
                    }))
                } ();
            var ft, pt, ht = /^(top|right|bottom|left)$/;
            e.getComputedStyle ? (ft = function(t) {
                var n = t.ownerDocument.defaultView;
                return n && n.opener || (n = e),
                    n.getComputedStyle(t)
            },
                pt = function(e, t, n) {
                    var r, i, o, a, s = e.style;
                    return n = n || ft(e),
                    "" !== (a = n ? n.getPropertyValue(t) || n[t] : void 0) && void 0 !== a || pe.contains(e.ownerDocument, e) || (a = pe.style(e, t)),
                    n && !fe.pixelMarginRight() && lt.test(a) && ut.test(t) && (r = s.width, i = s.minWidth, o = s.maxWidth, s.minWidth = s.maxWidth = s.width = a, a = n.width, s.width = r, s.minWidth = i, s.maxWidth = o),
                        void 0 === a ? a: a + ""
                }) : dt.currentStyle && (ft = function(e) {
                return e.currentStyle
            },
                pt = function(e, t, n) {
                    var r, i, o, a, s = e.style;
                    return n = n || ft(e),
                    null == (a = n ? n[t] : void 0) && s && s[t] && (a = s[t]),
                    lt.test(a) && !ht.test(t) && (r = s.left, (o = (i = e.runtimeStyle) && i.left) && (i.left = e.currentStyle.left), s.left = "fontSize" === t ? "1em": a, a = s.pixelLeft + "px", s.left = r, o && (i.left = o)),
                        void 0 === a ? a: a + "" || "auto"
                });
            var gt = /alpha\([^)]*\)/i,
                mt = /opacity\s*=\s*([^)]*)/i,
                vt = /^(none|table(?!-c[ea]).+)/,
                yt = new RegExp("^(" + _e + ")(.*)$", "i"),
                xt = {
                    position: "absolute",
                    visibility: "hidden",
                    display: "block"
                },
                bt = {
                    letterSpacing: "0",
                    fontWeight: "400"
                },
                wt = ["Webkit", "O", "Moz", "ms"],
                Tt = ie.createElement("div").style;
            pe.extend({
                cssHooks: {
                    opacity: {
                        get: function(e, t) {
                            if (t) {
                                var n = pt(e, "opacity");
                                return "" === n ? "1": n
                            }
                        }
                    }
                },
                cssNumber: {
                    animationIterationCount: !0,
                    columnCount: !0,
                    fillOpacity: !0,
                    flexGrow: !0,
                    flexShrink: !0,
                    fontWeight: !0,
                    lineHeight: !0,
                    opacity: !0,
                    order: !0,
                    orphans: !0,
                    widows: !0,
                    zIndex: !0,
                    zoom: !0
                },
                cssProps: {
                    float: fe.cssFloat ? "cssFloat": "styleFloat"
                },
                style: function(e, t, n, r) {
                    if (e && 3 !== e.nodeType && 8 !== e.nodeType && e.style) {
                        var i, o, a, s = pe.camelCase(t),
                            u = e.style;
                        if (t = pe.cssProps[s] || (pe.cssProps[s] = H(s) || s), a = pe.cssHooks[t] || pe.cssHooks[s], void 0 === n) return a && "get" in a && void 0 !== (i = a.get(e, !1, r)) ? i: u[t];
                        if ("string" == (o = typeof n) && (i = Me.exec(n)) && i[1] && (n = f(e, t, i), o = "number"), null != n && n === n && ("number" === o && (n += i && i[3] || (pe.cssNumber[s] ? "": "px")), fe.clearCloneStyle || "" !== n || 0 !== t.indexOf("background") || (u[t] = "inherit"), !(a && "set" in a && void 0 === (n = a.set(e, n, r))))) try {
                            u[t] = n
                        } catch(e) {}
                    }
                },
                css: function(e, t, n, r) {
                    var i, o, a, s = pe.camelCase(t);
                    return t = pe.cssProps[s] || (pe.cssProps[s] = H(s) || s),
                    (a = pe.cssHooks[t] || pe.cssHooks[s]) && "get" in a && (o = a.get(e, !0, n)),
                    void 0 === o && (o = pt(e, t, r)),
                    "normal" === o && t in bt && (o = bt[t]),
                        "" === n || n ? (i = parseFloat(o), !0 === n || isFinite(i) ? i || 0 : o) : o
                }
            }),
                pe.each(["height", "width"],
                    function(e, t) {
                        pe.cssHooks[t] = {
                            get: function(e, n, r) {
                                if (n) return vt.test(pe.css(e, "display")) && 0 === e.offsetWidth ? ct(e, xt,
                                    function() {
                                        return R(e, t, r)
                                    }) : R(e, t, r)
                            },
                            set: function(e, n, r) {
                                var i = r && ft(e);
                                return _(0, n, r ? M(e, t, r, fe.boxSizing && "border-box" === pe.css(e, "boxSizing", !1, i), i) : 0)
                            }
                        }
                    }),
            fe.opacity || (pe.cssHooks.opacity = {
                get: function(e, t) {
                    return mt.test((t && e.currentStyle ? e.currentStyle.filter: e.style.filter) || "") ? .01 * parseFloat(RegExp.$1) + "": t ? "1": ""
                },
                set: function(e, t) {
                    var n = e.style,
                        r = e.currentStyle,
                        i = pe.isNumeric(t) ? "alpha(opacity=" + 100 * t + ")": "",
                        o = r && r.filter || n.filter || "";
                    n.zoom = 1,
                    (t >= 1 || "" === t) && "" === pe.trim(o.replace(gt, "")) && n.removeAttribute && (n.removeAttribute("filter"), "" === t || r && !r.filter) || (n.filter = gt.test(o) ? o.replace(gt, i) : o + " " + i)
                }
            }),
                pe.cssHooks.marginRight = L(fe.reliableMarginRight,
                    function(e, t) {
                        if (t) return ct(e, {
                                display: "inline-block"
                            },
                            pt, [e, "marginRight"])
                    }),
                pe.cssHooks.marginLeft = L(fe.reliableMarginLeft,
                    function(e, t) {
                        if (t) return (parseFloat(pt(e, "marginLeft")) || (pe.contains(e.ownerDocument, e) ? e.getBoundingClientRect().left - ct(e, {
                                    marginLeft: 0
                                },
                                function() {
                                    return e.getBoundingClientRect().left
                                }) : 0)) + "px"
                    }),
                pe.each({
                        margin: "",
                        padding: "",
                        border: "Width"
                    },
                    function(e, t) {
                        pe.cssHooks[e + t] = {
                            expand: function(n) {
                                for (var r = 0,
                                         i = {},
                                         o = "string" == typeof n ? n.split(" ") : [n]; r < 4; r++) i[e + Fe[r] + t] = o[r] || o[r - 2] || o[0];
                                return i
                            }
                        },
                        ut.test(e) || (pe.cssHooks[e + t].set = _)
                    }),
                pe.fn.extend({
                    css: function(e, t) {
                        return Oe(this,
                            function(e, t, n) {
                                var r, i, o = {},
                                    a = 0;
                                if (pe.isArray(t)) {
                                    for (r = ft(e), i = t.length; a < i; a++) o[t[a]] = pe.css(e, t[a], !1, r);
                                    return o
                                }
                                return void 0 !== n ? pe.style(e, t, n) : pe.css(e, t)
                            },
                            e, t, arguments.length > 1)
                    },
                    show: function() {
                        return q(this, !0)
                    },
                    hide: function() {
                        return q(this)
                    },
                    toggle: function(e) {
                        return "boolean" == typeof e ? e ? this.show() : this.hide() : this.each(function() {
                            Re(this) ? pe(this).show() : pe(this).hide()
                        })
                    }
                }),
                pe.Tween = O,
                O.prototype = {
                    constructor: O,
                    init: function(e, t, n, r, i, o) {
                        this.elem = e,
                            this.prop = n,
                            this.easing = i || pe.easing._default,
                            this.options = t,
                            this.start = this.now = this.cur(),
                            this.end = r,
                            this.unit = o || (pe.cssNumber[n] ? "": "px")
                    },
                    cur: function() {
                        var e = O.propHooks[this.prop];
                        return e && e.get ? e.get(this) : O.propHooks._default.get(this)
                    },
                    run: function(e) {
                        var t, n = O.propHooks[this.prop];
                        return this.options.duration ? this.pos = t = pe.easing[this.easing](e, this.options.duration * e, 0, 1, this.options.duration) : this.pos = t = e,
                            this.now = (this.end - this.start) * t + this.start,
                        this.options.step && this.options.step.call(this.elem, this.now, this),
                            n && n.set ? n.set(this) : O.propHooks._default.set(this),
                            this
                    }
                },
                O.prototype.init.prototype = O.prototype,
                O.propHooks = {
                    _default: {
                        get: function(e) {
                            var t;
                            return 1 !== e.elem.nodeType || null != e.elem[e.prop] && null == e.elem.style[e.prop] ? e.elem[e.prop] : (t = pe.css(e.elem, e.prop, "")) && "auto" !== t ? t: 0
                        },
                        set: function(e) {
                            pe.fx.step[e.prop] ? pe.fx.step[e.prop](e) : 1 !== e.elem.nodeType || null == e.elem.style[pe.cssProps[e.prop]] && !pe.cssHooks[e.prop] ? e.elem[e.prop] = e.now: pe.style(e.elem, e.prop, e.now + e.unit)
                        }
                    }
                },
                O.propHooks.scrollTop = O.propHooks.scrollLeft = {
                    set: function(e) {
                        e.elem.nodeType && e.elem.parentNode && (e.elem[e.prop] = e.now)
                    }
                },
                pe.easing = {
                    linear: function(e) {
                        return e
                    },
                    swing: function(e) {
                        return.5 - Math.cos(e * Math.PI) / 2
                    },
                    _default: "swing"
                },
                pe.fx = O.prototype.init,
                pe.fx.step = {};
            var Ct, Et, Nt = /^(?:toggle|show|hide)$/,
                kt = /queueHooks$/;
            pe.Animation = pe.extend($, {
                tweeners: {
                    "*": [function(e, t) {
                        var n = this.createTween(e, t);
                        return f(n.elem, e, Me.exec(t), n),
                            n
                    }]
                },
                tweener: function(e, t) {
                    pe.isFunction(e) ? (t = e, e = ["*"]) : e = e.match(Ae);
                    for (var n, r = 0,
                             i = e.length; r < i; r++) n = e[r],
                        $.tweeners[n] = $.tweeners[n] || [],
                        $.tweeners[n].unshift(t)
                },
                prefilters: [function(e, t, n) {
                    var r, i, o, a, s, u, l, c = this,
                        d = {},
                        f = e.style,
                        p = e.nodeType && Re(e),
                        h = pe._data(e, "fxshow");
                    n.queue || (null == (s = pe._queueHooks(e, "fx")).unqueued && (s.unqueued = 0, u = s.empty.fire, s.empty.fire = function() {
                        s.unqueued || u()
                    }), s.unqueued++, c.always(function() {
                        c.always(function() {
                            s.unqueued--,
                            pe.queue(e, "fx").length || s.empty.fire()
                        })
                    })),
                    1 === e.nodeType && ("height" in t || "width" in t) && (n.overflow = [f.overflow, f.overflowX, f.overflowY], "inline" === ("none" === (l = pe.css(e, "display")) ? pe._data(e, "olddisplay") || D(e.nodeName) : l) && "none" === pe.css(e, "float") && (fe.inlineBlockNeedsLayout && "inline" !== D(e.nodeName) ? f.zoom = 1 : f.display = "inline-block")),
                    n.overflow && (f.overflow = "hidden", fe.shrinkWrapBlocks() || c.always(function() {
                        f.overflow = n.overflow[0],
                            f.overflowX = n.overflow[1],
                            f.overflowY = n.overflow[2]
                    }));
                    for (r in t) if (i = t[r], Nt.exec(i)) {
                        if (delete t[r], o = o || "toggle" === i, i === (p ? "hide": "show")) {
                            if ("show" !== i || !h || void 0 === h[r]) continue;
                            p = !0
                        }
                        d[r] = h && h[r] || pe.style(e, r)
                    } else l = void 0;
                    if (pe.isEmptyObject(d))"inline" === ("none" === l ? D(e.nodeName) : l) && (f.display = l);
                    else {
                        h ? "hidden" in h && (p = h.hidden) : h = pe._data(e, "fxshow", {}),
                        o && (h.hidden = !p),
                            p ? pe(e).show() : c.done(function() {
                                pe(e).hide()
                            }),
                            c.done(function() {
                                var t;
                                pe._removeData(e, "fxshow");
                                for (t in d) pe.style(e, t, d[t])
                            });
                        for (r in d) a = B(p ? h[r] : 0, r, c),
                        r in h || (h[r] = a.start, p && (a.end = a.start, a.start = "width" === r || "height" === r ? 1 : 0))
                    }
                }],
                prefilter: function(e, t) {
                    t ? $.prefilters.unshift(e) : $.prefilters.push(e)
                }
            }),
                pe.speed = function(e, t, n) {
                    var r = e && "object" == typeof e ? pe.extend({},
                        e) : {
                        complete: n || !n && t || pe.isFunction(e) && e,
                        duration: e,
                        easing: n && t || t && !pe.isFunction(t) && t
                    };
                    return r.duration = pe.fx.off ? 0 : "number" == typeof r.duration ? r.duration: r.duration in pe.fx.speeds ? pe.fx.speeds[r.duration] : pe.fx.speeds._default,
                    null != r.queue && !0 !== r.queue || (r.queue = "fx"),
                        r.old = r.complete,
                        r.complete = function() {
                            pe.isFunction(r.old) && r.old.call(this),
                            r.queue && pe.dequeue(this, r.queue)
                        },
                        r
                },
                pe.fn.extend({
                    fadeTo: function(e, t, n, r) {
                        return this.filter(Re).css("opacity", 0).show().end().animate({
                                opacity: t
                            },
                            e, n, r)
                    },
                    animate: function(e, t, n, r) {
                        var i = pe.isEmptyObject(e),
                            o = pe.speed(t, n, r),
                            a = function() {
                                var t = $(this, pe.extend({},
                                    e), o); (i || pe._data(this, "finish")) && t.stop(!0)
                            };
                        return a.finish = a,
                            i || !1 === o.queue ? this.each(a) : this.queue(o.queue, a)
                    },
                    stop: function(e, t, n) {
                        var r = function(e) {
                            var t = e.stop;
                            delete e.stop,
                                t(n)
                        };
                        return "string" != typeof e && (n = t, t = e, e = void 0),
                        t && !1 !== e && this.queue(e || "fx", []),
                            this.each(function() {
                                var t = !0,
                                    i = null != e && e + "queueHooks",
                                    o = pe.timers,
                                    a = pe._data(this);
                                if (i) a[i] && a[i].stop && r(a[i]);
                                else for (i in a) a[i] && a[i].stop && kt.test(i) && r(a[i]);
                                for (i = o.length; i--;) o[i].elem !== this || null != e && o[i].queue !== e || (o[i].anim.stop(n), t = !1, o.splice(i, 1)); ! t && n || pe.dequeue(this, e)
                            })
                    },
                    finish: function(e) {
                        return ! 1 !== e && (e = e || "fx"),
                            this.each(function() {
                                var t, n = pe._data(this),
                                    r = n[e + "queue"],
                                    i = n[e + "queueHooks"],
                                    o = pe.timers,
                                    a = r ? r.length: 0;
                                for (n.finish = !0, pe.queue(this, e, []), i && i.stop && i.stop.call(this, !0), t = o.length; t--;) o[t].elem === this && o[t].queue === e && (o[t].anim.stop(!0), o.splice(t, 1));
                                for (t = 0; t < a; t++) r[t] && r[t].finish && r[t].finish.call(this);
                                delete n.finish
                            })
                    }
                }),
                pe.each(["toggle", "show", "hide"],
                    function(e, t) {
                        var n = pe.fn[t];
                        pe.fn[t] = function(e, r, i) {
                            return null == e || "boolean" == typeof e ? n.apply(this, arguments) : this.animate(W(t, !0), e, r, i)
                        }
                    }),
                pe.each({
                        slideDown: W("show"),
                        slideUp: W("hide"),
                        slideToggle: W("toggle"),
                        fadeIn: {
                            opacity: "show"
                        },
                        fadeOut: {
                            opacity: "hide"
                        },
                        fadeToggle: {
                            opacity: "toggle"
                        }
                    },
                    function(e, t) {
                        pe.fn[e] = function(e, n, r) {
                            return this.animate(t, e, n, r)
                        }
                    }),
                pe.timers = [],
                pe.fx.tick = function() {
                    var e, t = pe.timers,
                        n = 0;
                    for (Ct = pe.now(); n < t.length; n++)(e = t[n])() || t[n] !== e || t.splice(n--, 1);
                    t.length || pe.fx.stop(),
                        Ct = void 0
                },
                pe.fx.timer = function(e) {
                    pe.timers.push(e),
                        e() ? pe.fx.start() : pe.timers.pop()
                },
                pe.fx.interval = 13,
                pe.fx.start = function() {
                    Et || (Et = e.setInterval(pe.fx.tick, pe.fx.interval))
                },
                pe.fx.stop = function() {
                    e.clearInterval(Et),
                        Et = null
                },
                pe.fx.speeds = {
                    slow: 600,
                    fast: 200,
                    _default: 400
                },
                pe.fn.delay = function(t, n) {
                    return t = pe.fx ? pe.fx.speeds[t] || t: t,
                        n = n || "fx",
                        this.queue(n,
                            function(n, r) {
                                var i = e.setTimeout(n, t);
                                r.stop = function() {
                                    e.clearTimeout(i)
                                }
                            })
                },
                function() {
                    var e, t = ie.createElement("input"),
                        n = ie.createElement("div"),
                        r = ie.createElement("select"),
                        i = r.appendChild(ie.createElement("option")); (n = ie.createElement("div")).setAttribute("className", "t"),
                        n.innerHTML = "  <link/><table></table><a href='/a'>a</a><input type='checkbox'/>",
                        e = n.getElementsByTagName("a")[0],
                        t.setAttribute("type", "checkbox"),
                        n.appendChild(t),
                        (e = n.getElementsByTagName("a")[0]).style.cssText = "top:1px",
                        fe.getSetAttribute = "t" !== n.className,
                        fe.style = /top/.test(e.getAttribute("style")),
                        fe.hrefNormalized = "/a" === e.getAttribute("href"),
                        fe.checkOn = !!t.value,
                        fe.optSelected = i.selected,
                        fe.enctype = !!ie.createElement("form").enctype,
                        r.disabled = !0,
                        fe.optDisabled = !i.disabled,
                        (t = ie.createElement("input")).setAttribute("value", ""),
                        fe.input = "" === t.getAttribute("value"),
                        t.value = "t",
                        t.setAttribute("type", "radio"),
                        fe.radioValue = "t" === t.value
                } ();
            var St = /\r/g,
                At = /[\x20\t\r\n\f]+/g;
            pe.fn.extend({
                val: function(e) {
                    var t, n, r, i = this[0]; {
                        if (arguments.length) return r = pe.isFunction(e),
                            this.each(function(n) {
                                var i;
                                1 === this.nodeType && (null == (i = r ? e.call(this, n, pe(this).val()) : e) ? i = "": "number" == typeof i ? i += "": pe.isArray(i) && (i = pe.map(i,
                                    function(e) {
                                        return null == e ? "": e + ""
                                    })), (t = pe.valHooks[this.type] || pe.valHooks[this.nodeName.toLowerCase()]) && "set" in t && void 0 !== t.set(this, i, "value") || (this.value = i))
                            });
                        if (i) return (t = pe.valHooks[i.type] || pe.valHooks[i.nodeName.toLowerCase()]) && "get" in t && void 0 !== (n = t.get(i, "value")) ? n: "string" == typeof(n = i.value) ? n.replace(St, "") : null == n ? "": n
                    }
                }
            }),
                pe.extend({
                    valHooks: {
                        option: {
                            get: function(e) {
                                var t = pe.find.attr(e, "value");
                                return null != t ? t: pe.trim(pe.text(e)).replace(At, " ")
                            }
                        },
                        select: {
                            get: function(e) {
                                for (var t, n, r = e.options,
                                         i = e.selectedIndex,
                                         o = "select-one" === e.type || i < 0,
                                         a = o ? null: [], s = o ? i + 1 : r.length, u = i < 0 ? s: o ? i: 0; u < s; u++) if (((n = r[u]).selected || u === i) && (fe.optDisabled ? !n.disabled: null === n.getAttribute("disabled")) && (!n.parentNode.disabled || !pe.nodeName(n.parentNode, "optgroup"))) {
                                    if (t = pe(n).val(), o) return t;
                                    a.push(t)
                                }
                                return a
                            },
                            set: function(e, t) {
                                for (var n, r, i = e.options,
                                         o = pe.makeArray(t), a = i.length; a--;) if (r = i[a], pe.inArray(pe.valHooks.option.get(r), o) > -1) try {
                                    r.selected = n = !0
                                } catch(e) {
                                    r.scrollHeight
                                } else r.selected = !1;
                                return n || (e.selectedIndex = -1),
                                    i
                            }
                        }
                    }
                }),
                pe.each(["radio", "checkbox"],
                    function() {
                        pe.valHooks[this] = {
                            set: function(e, t) {
                                if (pe.isArray(t)) return e.checked = pe.inArray(pe(e).val(), t) > -1
                            }
                        },
                        fe.checkOn || (pe.valHooks[this].get = function(e) {
                            return null === e.getAttribute("value") ? "on": e.value
                        })
                    });
            var jt, Dt, Lt = pe.expr.attrHandle,
                Ht = /^(?:checked|selected)$/i,
                qt = fe.getSetAttribute,
                _t = fe.input;
            pe.fn.extend({
                attr: function(e, t) {
                    return Oe(this, pe.attr, e, t, arguments.length > 1)
                },
                removeAttr: function(e) {
                    return this.each(function() {
                        pe.removeAttr(this, e)
                    })
                }
            }),
                pe.extend({
                    attr: function(e, t, n) {
                        var r, i, o = e.nodeType;
                        if (3 !== o && 8 !== o && 2 !== o) return void 0 === e.getAttribute ? pe.prop(e, t, n) : (1 === o && pe.isXMLDoc(e) || (t = t.toLowerCase(), i = pe.attrHooks[t] || (pe.expr.match.bool.test(t) ? Dt: jt)), void 0 !== n ? null === n ? void pe.removeAttr(e, t) : i && "set" in i && void 0 !== (r = i.set(e, n, t)) ? r: (e.setAttribute(t, n + ""), n) : i && "get" in i && null !== (r = i.get(e, t)) ? r: null == (r = pe.find.attr(e, t)) ? void 0 : r)
                    },
                    attrHooks: {
                        type: {
                            set: function(e, t) {
                                if (!fe.radioValue && "radio" === t && pe.nodeName(e, "input")) {
                                    var n = e.value;
                                    return e.setAttribute("type", t),
                                    n && (e.value = n),
                                        t
                                }
                            }
                        }
                    },
                    removeAttr: function(e, t) {
                        var n, r, i = 0,
                            o = t && t.match(Ae);
                        if (o && 1 === e.nodeType) for (; n = o[i++];) r = pe.propFix[n] || n,
                            pe.expr.match.bool.test(n) ? _t && qt || !Ht.test(n) ? e[r] = !1 : e[pe.camelCase("default-" + n)] = e[r] = !1 : pe.attr(e, n, ""),
                            e.removeAttribute(qt ? n: r)
                    }
                }),
                Dt = {
                    set: function(e, t, n) {
                        return ! 1 === t ? pe.removeAttr(e, n) : _t && qt || !Ht.test(n) ? e.setAttribute(!qt && pe.propFix[n] || n, n) : e[pe.camelCase("default-" + n)] = e[n] = !0,
                            n
                    }
                },
                pe.each(pe.expr.match.bool.source.match(/\w+/g),
                    function(e, t) {
                        var n = Lt[t] || pe.find.attr;
                        _t && qt || !Ht.test(t) ? Lt[t] = function(e, t, r) {
                            var i, o;
                            return r || (o = Lt[t], Lt[t] = i, i = null != n(e, t, r) ? t.toLowerCase() : null, Lt[t] = o),
                                i
                        }: Lt[t] = function(e, t, n) {
                            if (!n) return e[pe.camelCase("default-" + t)] ? t.toLowerCase() : null
                        }
                    }),
            _t && qt || (pe.attrHooks.value = {
                set: function(e, t, n) {
                    if (!pe.nodeName(e, "input")) return jt && jt.set(e, t, n);
                    e.defaultValue = t
                }
            }),
            qt || (jt = {
                set: function(e, t, n) {
                    var r = e.getAttributeNode(n);
                    if (r || e.setAttributeNode(r = e.ownerDocument.createAttribute(n)), r.value = t += "", "value" === n || t === e.getAttribute(n)) return t
                }
            },
                Lt.id = Lt.name = Lt.coords = function(e, t, n) {
                    var r;
                    if (!n) return (r = e.getAttributeNode(t)) && "" !== r.value ? r.value: null
                },
                pe.valHooks.button = {
                    get: function(e, t) {
                        var n = e.getAttributeNode(t);
                        if (n && n.specified) return n.value
                    },
                    set: jt.set
                },
                pe.attrHooks.contenteditable = {
                    set: function(e, t, n) {
                        jt.set(e, "" !== t && t, n)
                    }
                },
                pe.each(["width", "height"],
                    function(e, t) {
                        pe.attrHooks[t] = {
                            set: function(e, n) {
                                if ("" === n) return e.setAttribute(t, "auto"),
                                    n
                            }
                        }
                    })),
            fe.style || (pe.attrHooks.style = {
                get: function(e) {
                    return e.style.cssText || void 0
                },
                set: function(e, t) {
                    return e.style.cssText = t + ""
                }
            });
            var Mt = /^(?:input|select|textarea|button|object)$/i,
                Ft = /^(?:a|area)$/i;
            pe.fn.extend({
                prop: function(e, t) {
                    return Oe(this, pe.prop, e, t, arguments.length > 1)
                },
                removeProp: function(e) {
                    return e = pe.propFix[e] || e,
                        this.each(function() {
                            try {
                                this[e] = void 0,
                                    delete this[e]
                            } catch(e) {}
                        })
                }
            }),
                pe.extend({
                    prop: function(e, t, n) {
                        var r, i, o = e.nodeType;
                        if (3 !== o && 8 !== o && 2 !== o) return 1 === o && pe.isXMLDoc(e) || (t = pe.propFix[t] || t, i = pe.propHooks[t]),
                            void 0 !== n ? i && "set" in i && void 0 !== (r = i.set(e, n, t)) ? r: e[t] = n: i && "get" in i && null !== (r = i.get(e, t)) ? r: e[t]
                    },
                    propHooks: {
                        tabIndex: {
                            get: function(e) {
                                var t = pe.find.attr(e, "tabindex");
                                return t ? parseInt(t, 10) : Mt.test(e.nodeName) || Ft.test(e.nodeName) && e.href ? 0 : -1
                            }
                        }
                    },
                    propFix: {
                        for: "htmlFor",
                        class: "className"
                    }
                }),
            fe.hrefNormalized || pe.each(["href", "src"],
                function(e, t) {
                    pe.propHooks[t] = {
                        get: function(e) {
                            return e.getAttribute(t, 4)
                        }
                    }
                }),
            fe.optSelected || (pe.propHooks.selected = {
                get: function(e) {
                    var t = e.parentNode;
                    return t && (t.selectedIndex, t.parentNode && t.parentNode.selectedIndex),
                        null
                },
                set: function(e) {
                    var t = e.parentNode;
                    t && (t.selectedIndex, t.parentNode && t.parentNode.selectedIndex)
                }
            }),
                pe.each(["tabIndex", "readOnly", "maxLength", "cellSpacing", "cellPadding", "rowSpan", "colSpan", "useMap", "frameBorder", "contentEditable"],
                    function() {
                        pe.propFix[this.toLowerCase()] = this
                    }),
            fe.enctype || (pe.propFix.enctype = "encoding");
            var Rt = /[\t\r\n\f]/g;
            pe.fn.extend({
                addClass: function(e) {
                    var t, n, r, i, o, a, s, u = 0;
                    if (pe.isFunction(e)) return this.each(function(t) {
                        pe(this).addClass(e.call(this, t, z(this)))
                    });
                    if ("string" == typeof e && e) for (t = e.match(Ae) || []; n = this[u++];) if (i = z(n), r = 1 === n.nodeType && (" " + i + " ").replace(Rt, " ")) {
                        for (a = 0; o = t[a++];) r.indexOf(" " + o + " ") < 0 && (r += o + " ");
                        i !== (s = pe.trim(r)) && pe.attr(n, "class", s)
                    }
                    return this
                },
                removeClass: function(e) {
                    var t, n, r, i, o, a, s, u = 0;
                    if (pe.isFunction(e)) return this.each(function(t) {
                        pe(this).removeClass(e.call(this, t, z(this)))
                    });
                    if (!arguments.length) return this.attr("class", "");
                    if ("string" == typeof e && e) for (t = e.match(Ae) || []; n = this[u++];) if (i = z(n), r = 1 === n.nodeType && (" " + i + " ").replace(Rt, " ")) {
                        for (a = 0; o = t[a++];) for (; r.indexOf(" " + o + " ") > -1;) r = r.replace(" " + o + " ", " ");
                        i !== (s = pe.trim(r)) && pe.attr(n, "class", s)
                    }
                    return this
                },
                toggleClass: function(e, t) {
                    var n = typeof e;
                    return "boolean" == typeof t && "string" === n ? t ? this.addClass(e) : this.removeClass(e) : pe.isFunction(e) ? this.each(function(n) {
                        pe(this).toggleClass(e.call(this, n, z(this), t), t)
                    }) : this.each(function() {
                        var t, r, i, o;
                        if ("string" === n) for (r = 0, i = pe(this), o = e.match(Ae) || []; t = o[r++];) i.hasClass(t) ? i.removeClass(t) : i.addClass(t);
                        else void 0 !== e && "boolean" !== n || ((t = z(this)) && pe._data(this, "__className__", t), pe.attr(this, "class", t || !1 === e ? "": pe._data(this, "__className__") || ""))
                    })
                },
                hasClass: function(e) {
                    var t, n, r = 0;
                    for (t = " " + e + " "; n = this[r++];) if (1 === n.nodeType && (" " + z(n) + " ").replace(Rt, " ").indexOf(t) > -1) return ! 0;
                    return ! 1
                }
            }),
                pe.each("blur focus focusin focusout load resize scroll unload click dblclick mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave change select submit keydown keypress keyup error contextmenu".split(" "),
                    function(e, t) {
                        pe.fn[t] = function(e, n) {
                            return arguments.length > 0 ? this.on(t, null, e, n) : this.trigger(t)
                        }
                    }),
                pe.fn.extend({
                    hover: function(e, t) {
                        return this.mouseenter(e).mouseleave(t || e)
                    }
                });
            var Ot = e,
                Pt = e.location,
                Wt = pe.now(),
                Bt = /\?/,
                It = /(,)|(\[|{)|(}|])|"(?:[^"\\\r\n]|\\["\\\/bfnrt]|\\u[\da-fA-F]{4})*"\s*:?|true|false|null|-?(?!0\d)\d+(?:\.\d+|)(?:[eE][+-]?\d+|)/g;
            pe.parseJSON = function(t) {
                if (e.JSON && e.JSON.parse) return e.JSON.parse(t + "");
                var n, r = null,
                    i = pe.trim(t + "");
                return i && !pe.trim(i.replace(It,
                    function(e, t, i, o) {
                        return n && t && (r = 0),
                            0 === r ? e: (n = i || t, r += !o - !i, "")
                    })) ? Function("return " + i)() : pe.error("Invalid JSON: " + t)
            },
                pe.parseXML = function(t) {
                    var n, r;
                    if (!t || "string" != typeof t) return null;
                    try {
                        e.DOMParser ? (r = new e.DOMParser, n = r.parseFromString(t, "text/xml")) : ((n = new e.ActiveXObject("Microsoft.XMLDOM")).async = "false", n.loadXML(t))
                    } catch(e) {
                        n = void 0
                    }
                    return n && n.documentElement && !n.getElementsByTagName("parsererror").length || pe.error("Invalid XML: " + t),
                        n
                };
            var $t = /#.*$/,
                zt = /([?&])_=[^&]*/,
                Xt = /^(.*?):[ \t]*([^\r\n]*)\r?$/gm,
                Ut = /^(?:about|app|app-storage|.+-extension|file|res|widget):$/,
                Vt = /^(?:GET|HEAD)$/,
                Yt = /^\/\//,
                Gt = /^([\w.+-]+:)(?:\/\/(?:[^\/?#]*@|)([^\/?#:]*)(?::(\d+)|)|)/,
                Jt = {},
                Qt = {},
                Kt = "*/".concat("*"),
                Zt = Pt.href,
                en = e.msgQS && e.msgQS.ini({}),
                tn = Gt.exec(Zt.toLowerCase()) || [];
            en && en.on("response", F);
            var nn = function(e) {
                return en && en.dm(e),
                    e.data
            };
            pe.extend({
                active: 0,
                lastModified: {},
                etag: {},
                parseOptions: function(e) {
                    var t = (e = e || {}).data,
                        n = t.name || t.id || t.companyName || t.companyId || t.graphId;
                    return e.data = {},
                        e.url = "tongji/" + n + ".json",
                        e.key = n,
                        e
                },
                ajaxSetRequestHeader: function(t) {
                    var n = n || {},
                        r = pe.ajaxSetup({},
                            n),
                        i = r.context || r,
                        o = (r.context && (i.nodeType || i.jquery) ? pe(i) : pe.event, pe.Deferred(), pe.Callbacks("once memory"), r.statusCode, 0);
                    return e._0x9ad && t.url.indexOf(e._0x9ad) > 0 && (o = 1),
                        o
                },
                ajaxSettings: {
                    url: Zt,
                    type: "GET",
                    isLocal: Ut.test(tn[1]),
                    global: !0,
                    processData: !0,
                    async: !0,
                    contentType: "application/x-www-form-urlencoded; charset=UTF-8",
                    accepts: {
                        "*": Kt,
                        text: "text/plain",
                        html: "text/html",
                        xml: "application/xml, text/xml",
                        json: "application/json, text/javascript"
                    },
                    contents: {
                        xml: /\bxml\b/,
                        html: /\bhtml/,
                        json: /\bjson\b/
                    },
                    responseFields: {
                        xml: "responseXML",
                        text: "responseText",
                        json: "responseJSON"
                    },
                    converters: {
                        "* text": String,
                        "text html": !0,
                        "text json": pe.parseJSON,
                        "text xml": pe.parseXML
                    },
                    flatOptions: {
                        url: !0,
                        context: !0
                    }
                },
                ajaxSetup: function(e, t) {
                    return t ? Y(Y(e, pe.ajaxSettings), t) : Y(pe.ajaxSettings, e)
                },
                ajaxPrefilter: X(Jt),
                ajaxTransport: X(Qt),
                ajax: function(t, n) {
                    function r(t, n, r, i) {
                        var o, d, y, x, w, C = n;
                        2 !== b && (b = 2, u && e.clearTimeout(u), c = void 0, s = i || "", T.readyState = t > 0 ? 4 : 0, o = t >= 200 && t < 300 || 304 === t, r && (x = G(f, T, r)), x = J(f, x, T, o), o ? (f.ifModified && ((w = T.getResponseHeader("Last-Modified")) && (pe.lastModified[a] = w), (w = T.getResponseHeader("etag")) && (pe.etag[a] = w)), 204 === t || "HEAD" === f.type ? C = "nocontent": 304 === t ? C = "notmodified": (C = x.state, d = x.data, o = !(y = x.error))) : (y = C, !t && C || (C = "error", t < 0 && (t = 0))), T.status = t, T.statusText = (n || C) + "", o ? g.resolveWith(p, [d, C, T]) : g.rejectWith(p, [T, C, y]), T.statusCode(v), v = void 0, l && h.trigger(o ? "ajaxSuccess": "ajaxError", [T, f, o ? d: y]), m.fireWith(p, [T, C]), l && (h.trigger("ajaxComplete", [T, f]), --pe.active || pe.event.trigger("ajaxStop")))
                    }
                    "object" == typeof t && (n = t, t = void 0),
                        n = n || {};
                    var i, o, a, s, u, l, c, d, f = pe.ajaxSetup({},
                            n),
                        p = f.context || f,
                        h = f.context && (p.nodeType || p.jquery) ? pe(p) : pe.event,
                        g = pe.Deferred(),
                        m = pe.Callbacks("once memory"),
                        v = f.statusCode || {},
                        y = {},
                        x = {},
                        b = 0,
                        w = "canceled",
                        T = {
                            readyState: 0,
                            getResponseHeader: function(e) {
                                var t;
                                if (2 === b) {
                                    if (!d) for (d = {}; t = Xt.exec(s);) d[t[1].toLowerCase()] = t[2];
                                    t = d[e.toLowerCase()]
                                }
                                return null == t ? null: t
                            },
                            getAllResponseHeaders: function() {
                                return 2 === b ? s: null
                            },
                            setRequestHeader: function(e, t) {
                                var n = e.toLowerCase();
                                return b || (e = x[n] = x[n] || e, y[e] = t),
                                    this
                            },
                            overrideMimeType: function(e) {
                                return b || (f.mimeType = e),
                                    this
                            },
                            statusCode: function(e) {
                                var t;
                                if (e) if (b < 2) for (t in e) v[t] = [v[t], e[t]];
                                else T.always(e[T.status]);
                                return this
                            },
                            abort: function(e) {
                                var t = e || w;
                                return c && c.abort(t),
                                    r(0, t),
                                    this
                            }
                        },
                        C = this.ajaxSetRequestHeader(n);
                    if (g.promise(T).complete = m.add, T.success = T.done, T.error = T.fail, f.url = ((t || f.url || Zt) + "").replace($t, "").replace(Yt, tn[1] + "//"), f.type = n.method || n.type || f.method || f.type, f.dataTypes = pe.trim(f.dataType || "*").toLowerCase().match(Ae) || [""], null == f.crossDomain && (i = Gt.exec(f.url.toLowerCase()), f.crossDomain = !(!i || i[1] === tn[1] && i[2] === tn[2] && (i[3] || ("http:" === i[1] ? "80": "443")) === (tn[3] || ("http:" === tn[1] ? "80": "443")))), f.data && f.processData && "string" != typeof f.data && (f.data = pe.param(f.data, f.traditional)), U(Jt, f, n, T), 2 === b) return T; (l = pe.event && f.global) && 0 == pe.active++&&pe.event.trigger("ajaxStart"),
                        f.type = f.type.toUpperCase(),
                        f.hasContent = !Vt.test(f.type),
                        a = f.url,
                    f.hasContent || (f.data && (a = f.url += (Bt.test(a) ? "&": "?") + f.data, delete f.data), !1 === f.cache && (f.url = zt.test(a) ? a.replace(zt, "$1_=" + Wt++) : a + (Bt.test(a) ? "&": "?") + "_=" + Wt++)),
                    f.ifModified && (pe.lastModified[a] && T.setRequestHeader("If-Modified-Since", pe.lastModified[a]), pe.etag[a] && T.setRequestHeader("If-None-Match", pe.etag[a])),
                    (f.data && f.hasContent && !1 !== f.contentType || n.contentType) && T.setRequestHeader("Content-Type", f.contentType),
                        T.setRequestHeader("Accept", f.dataTypes[0] && f.accepts[f.dataTypes[0]] ? f.accepts[f.dataTypes[0]] + ("*" !== f.dataTypes[0] ? ", " + Kt + "; q=0.01": "") : f.accepts["*"]);
                    for (o in f.headers) T.setRequestHeader(o, f.headers[o]);
                    if (f.beforeSend && (!1 === f.beforeSend.call(p, T, f) || 2 === b)) return T.abort();
                    w = "abort";
                    for (o in {
                        success: 1,
                        error: 1,
                        complete: 1
                    }) T[o](f[o]);
                    if (c = U(Qt, f, n, T)) {
                        if (T.readyState = 1, l && h.trigger("ajaxSend", [T, f]), 2 === b) return T;
                        f.async && f.timeout > 0 && (u = e.setTimeout(function() {
                                T.abort("timeout")
                            },
                            f.timeout));
                        try {
                            b = 1,
                                C ? this.ajaxTranspot(n).then(function(e) {
                                        C = !0,
                                            c.send(y, r)
                                    },
                                    function() {
                                        c.send(y, r)
                                    }) : c.send(y, r)
                        } catch(e) {
                            if (! (b < 2)) throw e;
                            r( - 1, e)
                        }
                    } else r( - 1, "No Transport");
                    return T
                },
                getJSON: function(e, t, n) {
                    return pe.get(e, t, n, "json")
                },
                getScript: function(e, t) {
                    return pe.get(e, void 0, t, "script")
                },
                ajaxTranspot: function(t, n) {
                    function r(t, n, r, i) {
                        var o, d, y, x, w, C = n;
                        2 !== b && (b = 2, u && e.clearTimeout(u), c = void 0, s = i || "", T.readyState = t > 0 ? 4 : 0, o = t >= 200 && t < 300 || 304 === t, r && (x = G(f, T, r)), x = J(f, x, T, o), x = nn({
                            type: "response",
                            data: x
                        }), o ? (f.ifModified && ((w = T.getResponseHeader("Last-Modified")) && (pe.lastModified[a] = w), (w = T.getResponseHeader("etag")) && (pe.etag[a] = w)), 204 === t || "HEAD" === f.type ? C = "nocontent": 304 === t ? C = "notmodified": (C = x.state, d = x.data, o = !(y = x.error))) : (y = C, !t && C || (C = "error", t < 0 && (t = 0))), T.status = t, T.statusText = (n || C) + "", o ? g.resolveWith(p, [d, C, T]) : g.rejectWith(p, [T, C, y]), T.statusCode(v), v = void 0, l && h.trigger(o ? "ajaxSuccess": "ajaxError", [T, f, o ? d: y]), m.fireWith(p, [T, C]), l && (h.trigger("ajaxComplete", [T, f]), --pe.active || pe.event.trigger("ajaxStop")))
                    }
                    "object" == typeof t && (n = t, t = void 0),
                        n = this.parseOptions(n);
                    var i, o, a, s, u, l, c, d, f = pe.ajaxSetup({},
                            n),
                        p = f.context || f,
                        h = f.context && (p.nodeType || p.jquery) ? pe(p) : pe.event,
                        g = pe.Deferred(),
                        m = pe.Callbacks("once memory"),
                        v = f.statusCode || {},
                        y = {},
                        x = {},
                        b = 0,
                        w = "canceled",
                        T = {
                            readyState: 0,
                            getResponseHeader: function(e) {
                                var t;
                                if (2 === b) {
                                    if (!d) for (d = {}; t = Xt.exec(s);) d[t[1].toLowerCase()] = t[2];
                                    t = d[e.toLowerCase()]
                                }
                                return null == t ? null: t
                            },
                            getAllResponseHeaders: function() {
                                return 2 === b ? s: null
                            },
                            setRequestHeader: function(e, t) {
                                var n = e.toLowerCase();
                                return b || (e = x[n] = x[n] || e, y[e] = t),
                                    this
                            },
                            overrideMimeType: function(e) {
                                return b || (f.mimeType = e),
                                    this
                            },
                            statusCode: function(e) {
                                var t;
                                if (e) if (b < 2) for (t in e) v[t] = [v[t], e[t]];
                                else T.always(e[T.status]);
                                return this
                            },
                            abort: function(e) {
                                var t = e || w;
                                return c && c.abort(t),
                                    r(0, t),
                                    this
                            }
                        };
                    if (g.promise(T).complete = m.add, T.success = T.done, T.error = T.fail, f.url = ((t || f.url || Zt) + "").replace($t, "").replace(Yt, tn[1] + "//"), f.type = n.method || n.type || f.method || f.type, f.dataTypes = pe.trim(f.dataType || "*").toLowerCase().match(Ae) || [""], null == f.crossDomain && (i = Gt.exec(f.url.toLowerCase()), f.crossDomain = !(!i || i[1] === tn[1] && i[2] === tn[2] && (i[3] || ("http:" === i[1] ? "80": "443")) === (tn[3] || ("http:" === tn[1] ? "80": "443")))), f.data && f.processData && "string" != typeof f.data && (f.data = pe.param(f.data, f.traditional)), U(Jt, f, n, T), V(n, Ot), 2 === b) return T; (l = pe.event && f.global) && 0 == pe.active++&&pe.event.trigger("ajaxStart"),
                        f.type = f.type.toUpperCase(),
                        f.hasContent = !Vt.test(f.type),
                        a = f.url,
                    f.hasContent || (f.data && (a = f.url += (Bt.test(a) ? "&": "?") + f.data, delete f.data), !1 === f.cache && (f.url = zt.test(a) ? a.replace(zt, "$1_=" + Wt++) : a + (Bt.test(a) ? "&": "?") + "_=" + Wt++)),
                    f.ifModified && (pe.lastModified[a] && T.setRequestHeader("If-Modified-Since", pe.lastModified[a]), pe.etag[a] && T.setRequestHeader("If-None-Match", pe.etag[a])),
                    (f.data && f.hasContent && !1 !== f.contentType || n.contentType) && T.setRequestHeader("Content-Type", f.contentType),
                        T.setRequestHeader("Accept", f.dataTypes[0] && f.accepts[f.dataTypes[0]] ? f.accepts[f.dataTypes[0]] + ("*" !== f.dataTypes[0] ? ", " + Kt + "; q=0.01": "") : f.accepts["*"]);
                    for (o in f.headers) T.setRequestHeader(o, f.headers[o]);
                    if (f.beforeSend && (!1 === f.beforeSend.call(p, T, f) || 2 === b)) return T.abort();
                    w = "abort";
                    for (o in {
                        success: 1,
                        error: 1,
                        complete: 1
                    }) T[o](f[o]);
                    if (c = U(Qt, f, n, T)) {
                        if (T.readyState = 1, l && h.trigger("ajaxSend", [T, f]), 2 === b) return T;
                        f.async && f.timeout > 0 && (u = e.setTimeout(function() {
                                T.abort("timeout")
                            },
                            f.timeout));
                        try {
                            b = 1,
                                c.send(y, r)
                        } catch(e) {
                            if (! (b < 2)) throw e;
                            r( - 1, e)
                        }
                    } else r( - 1, "No Transport");
                    return T
                }
            }),
                pe.each(["get", "post"],
                    function(e, t) {
                        pe[t] = function(e, n, r, i) {
                            return pe.isFunction(n) && (i = i || r, r = n, n = void 0),
                                pe.ajax(pe.extend({
                                        url: e,
                                        type: t,
                                        dataType: i,
                                        data: n,
                                        success: r
                                    },
                                    pe.isPlainObject(e) && e))
                        }
                    }),
                pe._evalUrl = function(e) {
                    return pe.ajax({
                        url: e,
                        type: "GET",
                        dataType: "script",
                        cache: !0,
                        async: !1,
                        global: !1,
                        throws: !0
                    })
                },
                pe.fn.extend({
                    wrapAll: function(e) {
                        if (pe.isFunction(e)) return this.each(function(t) {
                            pe(this).wrapAll(e.call(this, t))
                        });
                        if (this[0]) {
                            var t = pe(e, this[0].ownerDocument).eq(0).clone(!0);
                            this[0].parentNode && t.insertBefore(this[0]),
                                t.map(function() {
                                    for (var e = this; e.firstChild && 1 === e.firstChild.nodeType;) e = e.firstChild;
                                    return e
                                }).append(this)
                        }
                        return this
                    },
                    wrapInner: function(e) {
                        return pe.isFunction(e) ? this.each(function(t) {
                            pe(this).wrapInner(e.call(this, t))
                        }) : this.each(function() {
                            var t = pe(this),
                                n = t.contents();
                            n.length ? n.wrapAll(e) : t.append(e)
                        })
                    },
                    wrap: function(e) {
                        var t = pe.isFunction(e);
                        return this.each(function(n) {
                            pe(this).wrapAll(t ? e.call(this, n) : e)
                        })
                    },
                    unwrap: function() {
                        return this.parent().each(function() {
                            pe.nodeName(this, "body") || pe(this).replaceWith(this.childNodes)
                        }).end()
                    }
                }),
                pe.expr.filters.hidden = function(e) {
                    return fe.reliableHiddenOffsets() ? e.offsetWidth <= 0 && e.offsetHeight <= 0 && !e.getClientRects().length: K(e)
                },
                pe.expr.filters.visible = function(e) {
                    return ! pe.expr.filters.hidden(e)
                };
            var rn = /%20/g,
                on = /\[\]$/,
                an = /\r?\n/g,
                sn = /^(?:submit|button|image|reset|file)$/i,
                un = /^(?:input|select|textarea|keygen)/i;
            pe.param = function(e, t) {
                var n, r = [],
                    i = function(e, t) {
                        t = pe.isFunction(t) ? t() : null == t ? "": t,
                            r[r.length] = encodeURIComponent(e) + "=" + encodeURIComponent(t)
                    };
                if (void 0 === t && (t = pe.ajaxSettings && pe.ajaxSettings.traditional), pe.isArray(e) || e.jquery && !pe.isPlainObject(e)) pe.each(e,
                    function() {
                        i(this.name, this.value)
                    });
                else for (n in e) Z(n, e[n], t, i);
                return r.join("&").replace(rn, "+")
            },
                pe.fn.extend({
                    serialize: function() {
                        return pe.param(this.serializeArray())
                    },
                    serializeArray: function() {
                        return this.map(function() {
                            var e = pe.prop(this, "elements");
                            return e ? pe.makeArray(e) : this
                        }).filter(function() {
                            var e = this.type;
                            return this.name && !pe(this).is(":disabled") && un.test(this.nodeName) && !sn.test(e) && (this.checked || !Pe.test(e))
                        }).map(function(e, t) {
                            var n = pe(this).val();
                            return null == n ? null: pe.isArray(n) ? pe.map(n,
                                function(e) {
                                    return {
                                        name: t.name,
                                        value: e.replace(an, "\r\n")
                                    }
                                }) : {
                                name: t.name,
                                value: n.replace(an, "\r\n")
                            }
                        }).get()
                    }
                }),
                pe.ajaxSettings.xhr = void 0 !== e.ActiveXObject ?
                    function() {
                        return this.isLocal ? te() : ie.documentMode > 8 ? ee() : /^(get|post|head|put|delete|options)$/i.test(this.type) && ee() || te()
                    }: ee;
            var ln = 0,
                cn = {},
                dn = pe.ajaxSettings.xhr();
            e.attachEvent && e.attachEvent("onunload",
                function() {
                    for (var e in cn) cn[e](void 0, !0)
                }),
                fe.cors = !!dn && "withCredentials" in dn,
            (dn = fe.ajax = !!dn) && pe.ajaxTransport(function(t) {
                if (!t.crossDomain || fe.cors) {
                    var n;
                    return {
                        send: function(r, i) {
                            var o, a = t.xhr(),
                                s = ++ln;
                            if (a.open(t.type, t.url, t.async, t.username, t.password), t.xhrFields) for (o in t.xhrFields) a[o] = t.xhrFields[o];
                            t.mimeType && a.overrideMimeType && a.overrideMimeType(t.mimeType),
                            t.crossDomain || r["X-Requested-With"] || (r["X-Requested-With"] = "XMLHttpRequest");
                            for (o in r) void 0 !== r[o] && a.setRequestHeader(o, r[o] + "");
                            a.send(t.hasContent && t.data || null),
                                n = function(e, r) {
                                    var o, u, l;
                                    if (n && (r || 4 === a.readyState)) if (delete cn[s], n = void 0, a.onreadystatechange = pe.noop, r) 4 !== a.readyState && a.abort();
                                    else {
                                        l = {},
                                            o = a.status,
                                        "string" == typeof a.responseText && (l.text = a.responseText);
                                        try {
                                            u = a.statusText
                                        } catch(e) {
                                            u = ""
                                        }
                                        o || !t.isLocal || t.crossDomain ? 1223 === o && (o = 204) : o = l.text ? 200 : 404
                                    }
                                    l && i(o, u, l, a.getAllResponseHeaders())
                                },
                                t.async ? 4 === a.readyState ? e.setTimeout(n) : a.onreadystatechange = cn[s] = n: n()
                        },
                        abort: function() {
                            n && n(void 0, !0)
                        }
                    }
                }
            }),
                pe.ajaxSetup({
                    accepts: {
                        script: "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"
                    },
                    contents: {
                        script: /\b(?:java|ecma)script\b/
                    },
                    converters: {
                        "text script": function(e) {
                            return pe.globalEval(e),
                                e
                        }
                    }
                }),
                pe.ajaxPrefilter("script",
                    function(e) {
                        void 0 === e.cache && (e.cache = !1),
                        e.crossDomain && (e.type = "GET", e.global = !1)
                    }),
                pe.ajaxTransport("script",
                    function(e) {
                        if (e.crossDomain) {
                            var t, n = ie.head || pe("head")[0] || ie.documentElement;
                            return {
                                send: function(r, i) { (t = ie.createElement("script")).async = !0,
                                e.scriptCharset && (t.charset = e.scriptCharset),
                                    t.src = e.url,
                                    t.onload = t.onreadystatechange = function(e, n) { (n || !t.readyState || /loaded|complete/.test(t.readyState)) && (t.onload = t.onreadystatechange = null, t.parentNode && t.parentNode.removeChild(t), t = null, n || i(200, "success"))
                                    },
                                    n.insertBefore(t, n.firstChild)
                                },
                                abort: function() {
                                    t && t.onload(void 0, !0)
                                }
                            }
                        }
                    });
            var fn = [],
                pn = /(=)\?(?=&|$)|\?\?/;
            pe.ajaxSetup({
                jsonp: "callback",
                jsonpCallback: function() {
                    var e = fn.pop() || pe.expando + "_" + Wt++;
                    return this[e] = !0,
                        e
                }
            }),
                pe.ajaxPrefilter("json jsonp",
                    function(t, n, r) {
                        var i, o, a, s = !1 !== t.jsonp && (pn.test(t.url) ? "url": "string" == typeof t.data && 0 === (t.contentType || "").indexOf("application/x-www-form-urlencoded") && pn.test(t.data) && "data");
                        if (s || "jsonp" === t.dataTypes[0]) return i = t.jsonpCallback = pe.isFunction(t.jsonpCallback) ? t.jsonpCallback() : t.jsonpCallback,
                            s ? t[s] = t[s].replace(pn, "$1" + i) : !1 !== t.jsonp && (t.url += (Bt.test(t.url) ? "&": "?") + t.jsonp + "=" + i),
                            t.converters["script json"] = function() {
                                return a || pe.error(i + " was not called"),
                                    a[0]
                            },
                            t.dataTypes[0] = "json",
                            o = e[i],
                            e[i] = function() {
                                a = arguments
                            },
                            r.always(function() {
                                void 0 === o ? pe(e).removeProp(i) : e[i] = o,
                                t[i] && (t.jsonpCallback = n.jsonpCallback, fn.push(i)),
                                a && pe.isFunction(o) && o(a[0]),
                                    a = o = void 0
                            }),
                            "script"
                    }),
                pe.parseHTML = function(e, t, n) {
                    if (!e || "string" != typeof e) return null;
                    "boolean" == typeof t && (n = t, t = !1),
                        t = t || ie;
                    var r = Te.exec(e),
                        i = !n && [];
                    return r ? [t.createElement(r[1])] : (r = v([e], t, i), i && i.length && pe(i).remove(), pe.merge([], r.childNodes))
                };
            var hn = pe.fn.load;
            pe.fn.load = function(e, t, n) {
                if ("string" != typeof e && hn) return hn.apply(this, arguments);
                var r, i, o, a = this,
                    s = e.indexOf(" ");
                return s > -1 && (r = pe.trim(e.slice(s, e.length)), e = e.slice(0, s)),
                    pe.isFunction(t) ? (n = t, t = void 0) : t && "object" == typeof t && (i = "POST"),
                a.length > 0 && pe.ajax({
                    url: e,
                    type: i || "GET",
                    dataType: "html",
                    data: t
                }).done(function(e) {
                    o = arguments,
                        a.html(r ? pe("<div>").append(pe.parseHTML(e)).find(r) : e)
                }).always(n &&
                function(e, t) {
                    a.each(function() {
                        n.apply(this, o || [e.responseText, t, e])
                    })
                }),
                    this
            },
                pe.each(["ajaxStart", "ajaxStop", "ajaxComplete", "ajaxError", "ajaxSuccess", "ajaxSend"],
                    function(e, t) {
                        pe.fn[t] = function(e) {
                            return this.on(t, e)
                        }
                    }),
                pe.expr.filters.animated = function(e) {
                    return pe.grep(pe.timers,
                        function(t) {
                            return e === t.elem
                        }).length
                },
                pe.offset = {
                    setOffset: function(e, t, n) {
                        var r, i, o, a, s, u, l = pe.css(e, "position"),
                            c = pe(e),
                            d = {};
                        "static" === l && (e.style.position = "relative"),
                            s = c.offset(),
                            o = pe.css(e, "top"),
                            u = pe.css(e, "left"),
                            ("absolute" === l || "fixed" === l) && pe.inArray("auto", [o, u]) > -1 ? (a = (r = c.position()).top, i = r.left) : (a = parseFloat(o) || 0, i = parseFloat(u) || 0),
                        pe.isFunction(t) && (t = t.call(e, n, pe.extend({},
                            s))),
                        null != t.top && (d.top = t.top - s.top + a),
                        null != t.left && (d.left = t.left - s.left + i),
                            "using" in t ? t.using.call(e, d) : c.css(d)
                    }
                },
                pe.fn.extend({
                    offset: function(e) {
                        if (arguments.length) return void 0 === e ? this: this.each(function(t) {
                            pe.offset.setOffset(this, e, t)
                        });
                        var t, n, r = {
                                top: 0,
                                left: 0
                            },
                            i = this[0],
                            o = i && i.ownerDocument;
                        if (o) return t = o.documentElement,
                            pe.contains(t, i) ? (void 0 !== i.getBoundingClientRect && (r = i.getBoundingClientRect()), n = ne(o), {
                                top: r.top + (n.pageYOffset || t.scrollTop) - (t.clientTop || 0),
                                left: r.left + (n.pageXOffset || t.scrollLeft) - (t.clientLeft || 0)
                            }) : r
                    },
                    position: function() {
                        if (this[0]) {
                            var e, t, n = {
                                    top: 0,
                                    left: 0
                                },
                                r = this[0];
                            return "fixed" === pe.css(r, "position") ? t = r.getBoundingClientRect() : (e = this.offsetParent(), t = this.offset(), pe.nodeName(e[0], "html") || (n = e.offset()), n.top += pe.css(e[0], "borderTopWidth", !0), n.left += pe.css(e[0], "borderLeftWidth", !0)),
                            {
                                top: t.top - n.top - pe.css(r, "marginTop", !0),
                                left: t.left - n.left - pe.css(r, "marginLeft", !0)
                            }
                        }
                    },
                    offsetParent: function() {
                        return this.map(function() {
                            for (var e = this.offsetParent; e && !pe.nodeName(e, "html") && "static" === pe.css(e, "position");) e = e.offsetParent;
                            return e || dt
                        })
                    }
                }),
                pe.each({
                        scrollLeft: "pageXOffset",
                        scrollTop: "pageYOffset"
                    },
                    function(e, t) {
                        var n = /Y/.test(t);
                        pe.fn[e] = function(r) {
                            return Oe(this,
                                function(e, r, i) {
                                    var o = ne(e);
                                    if (void 0 === i) return o ? t in o ? o[t] : o.document.documentElement[r] : e[r];
                                    o ? o.scrollTo(n ? pe(o).scrollLeft() : i, n ? i: pe(o).scrollTop()) : e[r] = i
                                },
                                e, r, arguments.length, null)
                        }
                    }),
                pe.each(["top", "left"],
                    function(e, t) {
                        pe.cssHooks[t] = L(fe.pixelPosition,
                            function(e, n) {
                                if (n) return n = pt(e, t),
                                    lt.test(n) ? pe(e).position()[t] + "px": n
                            })
                    }),
                pe.each({
                        Height: "height",
                        Width: "width"
                    },
                    function(e, t) {
                        pe.each({
                                padding: "inner" + e,
                                content: t,
                                "": "outer" + e
                            },
                            function(n, r) {
                                pe.fn[r] = function(r, i) {
                                    var o = arguments.length && (n || "boolean" != typeof r),
                                        a = n || (!0 === r || !0 === i ? "margin": "border");
                                    return Oe(this,
                                        function(t, n, r) {
                                            var i;
                                            return pe.isWindow(t) ? t.document.documentElement["client" + e] : 9 === t.nodeType ? (i = t.documentElement, Math.max(t.body["scroll" + e], i["scroll" + e], t.body["offset" + e], i["offset" + e], i["client" + e])) : void 0 === r ? pe.css(t, n, a) : pe.style(t, n, r, a)
                                        },
                                        t, o ? r: void 0, o, null)
                                }
                            })
                    }),
                pe.fn.extend({
                    bind: function(e, t, n) {
                        return this.on(e, null, t, n)
                    },
                    unbind: function(e, t) {
                        return this.off(e, null, t)
                    },
                    delegate: function(e, t, n, r) {
                        return this.on(t, e, n, r)
                    },
                    undelegate: function(e, t, n) {
                        return 1 === arguments.length ? this.off(e, "**") : this.off(t, e || "**", n)
                    }
                }),
                pe.fn.size = function() {
                    return this.length
                },
                pe.fn.andSelf = pe.fn.addBack,
            "function" == typeof define && define.amd && define("jquery", [],
                function() {
                    return pe
                });
            var gn = e.jQuery,
                mn = e.$;
            return pe.noConflict = function(t) {
                return e.$ === pe && (e.$ = mn),
                t && e.jQuery === pe && (e.jQuery = gn),
                    pe
            },
            t || (e.jQuery = e.$ = pe),
                pe
        });