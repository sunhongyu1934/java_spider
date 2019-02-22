function formSubmit(url, formId) {
//	alert("*******enter formSubmit");
    var curForm = document.getElementById("form" + formId);
    commitForECMA(callback, url, curForm);
}

function commitForECMA(callback, url, curForm) {
    //alert("*******enter commitForECMA"+url);
    request = createXMLHttp();
    request.onreadystatechange = callback;
    if (curForm == null) {
        request.open("GET", url);
        request.setRequestHeader("Content-Type", "text/html;encoding=gbk");
    }
    else {
        var fromEle = "";
        var myElements = curForm.elements;
        var myLength = myElements.length;
        for (var i = 0; i < myLength; i++) {
            var myEle = myElements[i];
            if (myEle.type != "submit" && myEle.value != "") {
                if (fromEle.length > 0) {
                    fromEle += "&" + myEle.name + "=" + myEle.value;
                }
                else {
                    fromEle += myEle.name + "=" + myEle.value;
                }

                fromEle += "&State=1";
            }
        }
        request.open("POST", url);
        fromEle = encodeURI(fromEle);
        fromEle = encodeURI(fromEle);
        request.setRequestHeader("cache-control", "no-cache");
        request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    }
//	alert('FAFA44');
    request.send(fromEle);
//alert('AAA22');
    if (curForm != null) {
        curForm.reset();
    }
}

function  geturl(myElements) {
    var fromEle = "";
    var myLength = myElements.length;
    for (var i = 0; i < myLength; i++) {
        var myEle = myElements[i];
        if (myEle.type != "submit" && myEle.value != "") {
            if (fromEle.length > 0) {
                fromEle += "&" + myEle.name + "=" + myEle.value;
            }
            else {
                fromEle += myEle.name + "=" + myEle.value;
            }

            fromEle += "&State=1";
        }
    }
    return fromEle;
}

function createXMLHttp()
{
//	alert("*******enter createXMLHttp");
    if(window.XMLHttpRequest)
    {
        return new XMLHttpRequest();
    }
    else if(window.ActiveXObject)
    {
        var aVersions = ["MSXML2.XMLHttp.5.0",
            "MSXML2.XMLHttp.4.0",
            "MSXML2.XMLHttp.3.0",
            "MSXML2.XMLHttp",
            "Microsoft.XMLHttp"];
        for(var i=0;i<aVersions.length;i++)
        {
            try
            {
                return new ActiveXObject(aVersions[i]);
            }
            catch (e)
            {
            }
        }
        throw new Error("您的浏览器不支持访问此网页");
    }
}
