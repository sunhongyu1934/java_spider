$(function () {
    function t(t, a) {
        var e = $("ul[data-type=" + t + "]");
        return e = 0 == e.length ? $("div[data-type=" + t + "] > ul") : e,
            e.find(">li>a[data-value=" + a + "]").html()
    }

    function a() {
        var a = js.getQueryString("sub_scope");
        if (a) {
            o.sub_scope = a;
            var e = t("sub_scope", a)
                ,
                n = '<span data-type="sub_scope" data-value="' + a + '" data-text="' + e + '">' + e + '<i class="iconfont icon-shanchu"></i></span>';
            $(".invest-filter-tag").append(n),
                edward.getData(o, c, 1, r)
        }
    }

    function e() {
        0 == $(".invest-filter-tag>span").length ? $(".tag-clear-btn").removeClass("active-clear") : $(".tag-clear-btn").addClass("active-clear")
    }

    function n() {
        var t = $(".invest-list-table>tbody input:checkbox")
            , a = $(".invest-list-table>tbody input:checked");
        t.length == a.length ? $("#selectAll").iCheck("check") : $("#selectAll").iCheck("uncheck")
    }

    function i() {
        p = [],
            $("#export-btn").find(">span").html("全部")
    }

    function s(t, a, e, n) {
        faralert({
            kind: "closed",
            boxWidth: 440,
            boxClass: "export-all-box " + t,
            html: !0,
            closedText: "导出数据",
            htmlStr: '<div class="export-all-content"></div>',
            confirmButtonText: a
        }, function (t) {
            var a = $(".export-all-box").hasClass("export-oversize-box")
                , e = $(".export-all-box").hasClass("export-success")
                , i = $(".export-all-box").hasClass("export-contact");
            if (a)
                return void faralert.closed();
            if (i)
                return faralert.closed(),
                    void _MEIQIA("showPanel");
            if (n)
                return void window.open("/order", "_blank");
            if (e)
                return void window.open("/export", "_self");
            var o, c, r = "e";
            if (-1 !== document.location.pathname.indexOf("merg") && (r = "m"),
                0 == p.length) {
                var d = "&dump=0"
                    , h = "in";
                $.each(l, function (t, a) {
                    d += "&" + t + "=" + encodeURIComponent(a)
                }),
                    o = "/export/exportinvesteventbyparams",
                "m" == r && (o = "/export/exportmergerbyparams"),
                -1 !== document.location.pathname.indexOf("out") && (h = "out"),
                    o += "?location=" + h + "&m=" + r + d
            } else
                o = "/export/exportinvestevent",
                "m" == r && (o = "/export/exportmerger"),
                    o += "?m=" + r + "&invselist=" + p.join(","),
                    c = [],
                    $.each($(".invest-filter-tag span"), function (t, a) {
                        c.push($(this).data("text"))
                    }),
                    c = c.join(","),
                    o += "&filters=" + c;
            u || (u = !0,
                $.get(o, function (t) {
                    if (1 == t.status) {
                        faralert.closed(),
                            u = !1;
                        s("export-success", "查看下载历史", '<p>已为您生成文件 <span data-url="' + t.data.url + '" class="exported download-excel">点此下载</span></p>')
                    }
                }))
        }),
            $(".export-all-content").html(e)
    }

    var o = {}
        , c = "def"
        , r = []
        , l = {}
        , d = 0
        , p = [];
    !function (t, a, e, i) {
        function s(a, e, i, s) {
            var h = {
                orderby: e,
                page: i,
                keyword: s
            };
            for (var f in a)
                h[f] = a[f];
            l = h,
                t.ajax({
                    url: _API_INFO,
                    type: "GET",
                    dataType: "json",
                    data: h
                }).done(function (a) {
                    if (2 === a.status) {
                        t("#total").text(0);
                        return t(".invest-list-table").after('<p class="invest-msg">无相关结果</p>'),
                            t(".invest-list-table tbody").empty(),
                            t(":checkbox").iCheck("uncheck"),
                            void t("#page-selection").bootpag({
                                total: 1,
                                page: 1
                            })
                    }
                    t("p.company-msg").remove();
                    var e = a.data.total
                        , i = a.data.page_num;
                    d = e,
                        u ? (t("#page-selection").bootpag({
                            total: e,
                            maxVisible: 3,
                            page: i,
                            next: "下一页",
                            prev: "上一页",
                            first: "首页",
                            last: "最后一页",
                            leaps: !1,
                            firstLastUse: !0,
                            wrapClass: "pagination xia"
                        }).on("page", function (a, e) {
                            Object.keys(l).length <= 3 && !s && e > 1 && !js.checkPermission(".invest-list-box") || e > 2 && !js.checkPermission(".invest-list-box") || (t(".invest-list-box").removeClass("no-permission"),
                                edward.getData(o, c, e, r))
                        }),
                            u = !u) : t("#page-selection").bootpag({
                            total: a.data.page_total,
                            page: a.data.page_num
                        }),
                        t("#total").text(a.data.total),
                        t(".invest-list-table tbody").empty(),
                        t(":checkbox").iCheck("uncheck");
                    t("html, body").animate({
                        scrollTop: 0
                    }, "slow");
                    var h = a.data
                        , f = template("invest-tpl", h);
                    t(".invest-list-table tbody").find("tr").remove(),
                        t(".invest-list-table").show().find("tbody").append(f),
                        t(".no-data").hide(),
                        t.each(t(":checkbox"), function (a, e) {
                            var n = t(this).data().checkbox;
                            p.indexOf(n) < 0 ? t(this).iCheck({
                                checkboxClass: "icheckbox_flat-red"
                            }).iCheck("uncheck") : t(this).iCheck({
                                checkboxClass: "icheckbox_flat-red"
                            }).iCheck("check")
                        }),
                        n(),
                        t('[data-toggle="popover"]').popover()
                }).fail(function () {
                    console.log("error")
                }).always(function () {
                })
        }

        var u = !0;
        a.edward = {
            getData: s
        }
    }(jQuery, window, document),
        $("#goto_page_btn").click(function () {
            var t = $("#goto_page_num").val();
            t > 2 && !js.checkPermission(".invest-list-box") || ($(".invest-list-box").removeClass("no-permission"),
                edward.getData(o, c, t, r))
        }),
    "serch" !== js.getQueryString("type") && edward.getData(o, c, 1, []),
        function () {
            var e = js.getQueryString("serchkeyword");
            e && (r.push(e),
                $(".invest-filter-tag").append('<span class="keyword" data-type="keyword" data-value=' + e + ">" + e + '<i class="iconfont icon-shanchu"></i></span>'));
            var n = js.getQueryString("scope");
            if (n) {
                o.scope = n,
                    js.getMore({
                        url: _API_chart,
                        type: "GET",
                        data: {
                            cat_id: n
                        }
                    }, function (t) {
                        var e = "";
                        $.each(t, function (t, a) {
                            e += '<li><a href="javascript:;" data-value="' + a.cat_id + '">' + a.cat_name + "</a></li>"
                        }),
                            $(".ajax-subsectors").append(e),
                            a()
                    });
                var i = t("scope", n)
                    ,
                    s = '<span data-type="scope" data-value="' + n + '" data-text="' + i + '">' + i + '<i class="iconfont icon-shanchu"></i></span>';
                $(".invest-filter-tag").append(s)
            }
            var l = js.getQueryString("date");
            if (l) {
                o.date = l;
                var d = t("date", l)
                    ,
                    p = '<span data-type="date" data-value="' + l + '" data-text="' + d + '">' + d + '<i class="iconfont icon-shanchu"></i></span>';
                $(".invest-filter-tag").append(p)
            }
            var u = js.getQueryString("prov");
            if (u) {
                o.prov = u;
                var h = t("prov", u)
                    ,
                    f = '<span data-type="prov" data-value="' + u + '" data-text="' + h + '">' + h + '<i class="iconfont icon-shanchu"></i></span>';
                $(".invest-filter-tag").append(f)
            }
            var v = js.getQueryString("round");
            if (v) {
                o.round = v;
                var g = t("round", v)
                    ,
                    x = '<span data-type="round" data-value="' + v + '" data-text="' + g + '">' + g + '<i class="iconfont icon-shanchu"></i></span>';
                $(".invest-filter-tag").append(x)
            }
            var m = js.getQueryString("currency");
            if (m) {
                o.currency = m;
                var b = t("currency", m)
                    ,
                    k = '<span data-type="currency" data-value="' + m + '" data-text="' + b + '">' + b + '<i class="iconfont icon-shanchu"></i></span>';
                $(".invest-filter-tag").append(k)
            }
            var y = js.getQueryString("invest_id");
            if (y) {
                o.invst_id = y;
                var w = js.getQueryString("invest_name")
                    ,
                    _ = '<span data-type="invst_id" data-value="' + y + '" data-text="' + w + '">' + w + '<i class="iconfont icon-shanchu"></i></span>';
                $(".invest-filter-tag").append(_)
            }
            edward.getData(o, c, 1, r)
        }(),
        $(document).on("click", 'i[data-fn= "note"]', function (t) {
            function a() {
                var t = $("textarea.popup-textarea").val();
                $.ajax({
                    url: "/note/addnote",
                    type: "POST",
                    dataType: "json",
                    data: {
                        com_id: n,
                        note: t
                    }
                }).done(function (a) {
                    a.status && (swal(a.msg, "", "success"),
                        $(".invest-list-table").find("i[data-id=" + n + "]").data("notecon", t),
                    i || $("i[data-id=" + n + "]").parent().html('<i data-note="' + i + '" data-id="' + n + '" data-fn="note" data-notecon="' + t + '" class="iconfont">&#xe605;</i>'))
                }).fail(function () {
                    console.log("error")
                }).always(function () {
                })
            }

            t.preventDefault();
            var e = $(this).data("notecon")
                , n = $(this).data("id")
                , i = $(this).data("note");
            js.swalModal(js.notesContent, a),
                $("textarea.popup-textarea").val(e)
        }),
        $(":checkbox").iCheck({
            checkboxClass: "icheckbox_flat-red"
        }),
        $(".show-btn").click(function (t) {
            if (!js.checkNeedUpdate()) {
                var a = !0;
                $(this).hasClass("show") ? ($(this).removeClass("show"),
                    $(this).find("b").text("投资事件统计")) : (a && (a = !1,
                    $.post(_API_chart, function (t) {
                        var a = t.data;
                        $("#main").highcharts({
                            chart: {
                                backgroundColor: "#f6f8fa",
                                type: "column"
                            },
                            colors: ["#f98859", "#fcc049", "#86d2df ", "#55a4b0", "#278b8d"],
                            title: {
                                text: ""
                            },
                            xAxis: {
                                categories: a.scope
                            },
                            yAxis: {
                                allowDecimals: !1,
                                min: 0,
                                title: {
                                    text: ""
                                }
                            },
                            tooltip: {
                                formatter: function () {
                                    return "<b>" + this.x + "</b><br/>" + this.series.name + ": " + this.y + "<br/>Total: " + this.point.stackTotal
                                }
                            },
                            plotOptions: {
                                column: {
                                    stacking: "normal"
                                }
                            },
                            credits: {
                                enabled: !1
                            },
                            legend: {
                                align: "right",
                                verticalAlign: "top",
                                x: 0,
                                y: 100
                            },
                            series: [{
                                name: a.series[1].name,
                                data: a.series[1].total,
                                stack: "male"
                            }, {
                                name: a.series[0].name,
                                data: a.series[0].total,
                                stack: "male"
                            }]
                        })
                    })),
                    $(this).addClass("show"),
                    $(this).find("b").text("投资事件统计")),
                    $("#main").slideToggle()
            }
        }),
        $("#list-seach").on("keydown", function (t) {
            if (13 !== t.keyCode)
                return !0;
            if (!js.checkNeedUpdate()) {
                var a = $(this).val()
                    , n = !1;
                if ("" === a)
                    return void setTimeout(function () {
                        swal("不能为空！", "", "warning")
                    }, 0);
                var s = '<span class="keyword" data-type="keyword" data-value=' + a + ">" + a + '<i class="iconfont icon-shanchu"></i></span>';
                if (0 === r.length)
                    return r.push(a),
                        $(".invest-filter-tag").append(s),
                        i(),
                        e(),
                        void edward.getData(o, c, 1, r);
                $.each(r, function (t, e) {
                    if (e == a)
                        return setTimeout(function () {
                            swal("重复添加关键字！", "", "warning")
                        }, 0),
                            n = !0,
                            !1
                }),
                n || (r.push(a),
                    $(".invest-filter-tag").append(s),
                    i(),
                    e(),
                    edward.getData(o, c, 1, r))
            }
        }),
        $(document).on("click", ".invest-filter-box .dropdown-menu a", function (t) {
            if (t.preventDefault(),
                    !js.checkNeedUpdate()) {
                var a = $(this).closest(".dropdown-menu")
                    , n = a.data("type")
                    , s = $(this).data("value")
                    , l = $(this).text();
                o[n] = s;
                var d = $(".invest-filter-tag span[data-type=" + n + "]").length;
                if ($(this).parents("ul").is(".ajax") && $.ajax({
                        url: "/company/getsubscope?cat_id=" + s,
                        type: "GET",
                        dataType: "json"
                    }).done(function (t) {
                        var a = "";
                        $.each(t.data, function (t, e) {
                            a += '<li><a href="javascript:;" data-value="' + e.cat_id + '">' + e.cat_name + "</a></li>"
                        }),
                            $(".ajax-subsectors").empty().append(a)
                    }),
                    1 === d)
                    $(".invest-filter-tag span[data-type=" + n + "]").data("value", s).html(l + '<i class="iconfont icon-shanchu"></i>'),
                    "scope" == n && $(".invest-filter-tag span[data-type=sub_scope]").remove();
                else if (0 === d) {
                    var p = '<span data-type="' + n + '" data-value="' + s + '" data-text="' + l + '">' + l + '<i class="iconfont icon-shanchu"></i></span>';
                    $(".invest-filter-tag").append(p)
                }
                i(),
                    e(),
                    edward.getData(o, c, 1, r)
            }
        }),
        $(document).on("click", ".invest-filter-tag>span>.iconfont", function (t) {
            t.preventDefault();
            var a = $(this).parent().data("type");
            if ("keyword" === a) {
                var n = $(this).parent().data("value");
                $.each(r, function (t, a) {
                    if (a == n)
                        return void r.splice(t, 1)
                })
            }
            delete o[a],
                $(this).parent().remove(),
                i(),
                e(),
                edward.getData(o, c, 1, r)
        }),
        $(".tag-clear-btn").click(function (t) {
            if ($(this).hasClass("active-clear")) {
                for (var a in o)
                    delete o[a];
                r.length = 0,
                    $(".invest-filter-tag").empty(),
                    $("#list-seach").val(""),
                    i(),
                    e(),
                    edward.getData(o, c, 1, r)
            }
        }),
        $("#invest-list-sort span").on("click", function (t) {
            t.preventDefault(),
                $(this).addClass("active").siblings().removeClass("active");
            var a = $(this).data("sort");
            c = a,
                edward.getData(o, c, 1, r)
        }),
        $(document).on("ifClicked", "#selectAll", function (t) {
            $(this).prop("checked") ? $(":checkbox").iCheck("uncheck") : $(":checkbox").iCheck("check")
        }),
        $(document).on("ifChanged", ":checkbox", function (t) {
            var a, e = $(this).data().checkbox, i = $(this).prop("checked");
            e && i && -1 == p.indexOf(e) && (p.push(e),
                n()),
            e && !i && (p.splice(p.indexOf(e), 1),
                $("#selectAll").iCheck("uncheck")),
                a = p.length > 0 ? "(" + p.length + ")" : "全部",
                $("#export-btn>span").html(a)
        }),
        $("#export-btn").click(js.limiter("invest_excel", function () {
            $.get("/export/check", {
                m: "e"
            }).done(function (t) {
                if (1 !== t.status)
                    return faralert.closed(),
                        void faralert({
                            kind: "default",
                            type: "warning",
                            title: t.msg
                        });
                $(".export-all-content").find(".exported").text(t.data.exported),
                    $(".export-all-content").find(".remain").text(t.data.remain);
                var a = ""
                    , e = (t.data.remain,
                p.length || d);
                if (3 == t.data.is_identity)
                    return a = '<p>您当前会员账号<span class="star">已到期</span> ， 请<a href="/order" target="_blank">升级会员</a> </p>',
                        void s("export-update no-cancel", "马上升级", a, !0);
                if (a = '<p></span>剩余下载量：<span class="remain">' + t.data.remain + '</span></p><p></span>本次下载所需下载量：<span class="remain">' + e + "</span></p>",
                    e > t.data.remain)
                    switch (t.data.is_identity) {
                        case 2:
                            a += '<p>您当前剩余下载量不足，请<a href="/order" target="_blank">升级会员</a></p>',
                                s("export-update no-cancel", "马上升级", a, !0);
                            break;
                        case 1:
                            a += "<p>您当月剩余下载量不足</p><p>小桔妹建议您：联系我们购买数据包</p>",
                                s("export-contact no-cancel", "联系我们", a, !0)
                    }
                else
                    a += "<p>您确定要导出此次数据吗？</p>",
                        s("", "确定", a)
            })
        }, 1e3));
    var u = !1;
    $(document).on("click", ".export-success span.download-excel", function () {
        var t = $(this).data().url;
        window.open(t),
            faralert.closed()
    });
    var h = 0;
    $(".drop-input .search>input").on("input", function (t) {
        if (t.preventDefault(),
                !js.checkNeedUpdate()) {
            var a = $(".drop-input>ul")
                , e = $(this).val();
            "" !== $.trim(e) && (e = $.trim(e),
                clearTimeout(h),
                h = setTimeout(function () {
                    $.get("/investevent/getInvestmentByKeyword?keyword=" + e, function (t) {
                        a.empty(),
                            1 === t.status && t.data && t.data.length > 0 ? $.each(t.data, function (t, e) {
                                var n = '<li><a href="javascript:;" data-value="' + e.invst_id + '">' + e.invst_name + "</a></li>";
                                a.append(n)
                            }) : a.append('<li><a class="no-result" href="javascript:;">暂无相关数据</a></li>')
                    })
                }, 500))
        }
    })
});
//# sourceMappingURL=invest_list.js.map
