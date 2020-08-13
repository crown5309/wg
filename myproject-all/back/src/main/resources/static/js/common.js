// 非空判断
function isEmpty(str) {
    if (str == "") {
        return true;
    }
    if (str == null) {
        return true;
    }
    if (typeof(str) == undefined) {
        return true;
    }
    return false;
}

function to(url) {
    window.location.href = url;
}

function getRandomNumberByRange(start, end) {
    return Math.floor(Math.random() * (end - start) + start);
}

window.alert = function (name) {
    var iframe = document.createElement("IFRAME");
    iframe.style.display = "none";
    iframe.setAttribute("src", 'data:text/plain,');
    document.documentElement.appendChild(iframe);
    window.frames[0].window.alert(name);
    iframe.parentNode.removeChild(iframe);
};

window.confirm = function (message) {
    var iframe = document.createElement("IFRAME");
    iframe.style.display = "none";
    iframe.setAttribute("src", 'data:text/plain,');
    document.documentElement.appendChild(iframe);
    var alertFrame = window.frames[0];
    var result = alertFrame.window.confirm(message);
    iframe.parentNode.removeChild(iframe);
    return result;
};

// 正则校验 严格
function validateInputStrict(str) {
    var pattern = /[`~!@#$%^&*()_\-+=<>?:"{}|,.\/;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’，。、]/im;
    // 如果包含特殊字符返回false
    if (!pattern.test(str)) {
        return false;
    }
    return true;
}

function validateInputSearch(str) {
    var pattern = /[`~!@$%^*\<>"{}|,;'\\[\]~！@￥%……*（）\+{}|《》？：“”【】、；‘’，。、]/im;
    // 如果包含特殊字符返回false
    if (!pattern.test(str)) {
        return false;
    }
    return true;
}

function validateNum(str) {
    var pattern = /^[0-9]*$/im;
    // 如果包含特殊字符返回false
    if (!pattern.test(str)) {
        return false;
    }
    return true;
}

// 正则校验
function validateInput(str) {
    var pattern = /[`~!@$%^*()\-+<>"{},;'\\[\]·~！@#￥%……*（）——《》？：“”【】、；‘’，。、]/im;
    // 如果包含特殊字符返回false
    if (!pattern.test(str)) {
        return false;
    }
    return true;
}


function showPassWord(obj, passwordId) {
    var id = obj.id;
    var currentClass = $("#" + id).attr("class");
    // 隐藏 > 显示
    if (currentClass == "btn btn-primary btn-sm") {
        $("#" + id).addClass("btn-outline-primary");
        $("#" + passwordId).addClass("show");
        $("#" + passwordId).attr("type", "text");
    } else if (currentClass = "btn btn-primary btn-sm btn-outline-primary") {
        $("#" + id).removeClass("btn-outline-primary");
        $("#" + passwordId).removeClass("show");
        $("#" + passwordId).attr("type", "password");
    }
}


// 获取URL参数
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) {
        return decodeURI(r[2]);
    }
    return null; //返回参数值decodeURI(r[2])
}

/**
 * 使用方法:
 * 开启:MaskUtil.mask();
 * 关闭:MaskUtil.unmask();
 *
 * MaskUtil.mask('其它提示文字...');
 */
// MaskUtil Start
var MaskUtil = (function () {

    //var $mask, $maskMsg;
    var $mask;

    //var defMsg = '正在处理，请稍待。。。';

    function init() {
        if (!$mask) {
            $mask = $('<div><img style="width: 30px;margin-top:14px;" src="/images/loading_new.gif"></div>')
                .css({
                    'position': 'absolute'
                    ,'border-radius': '8px'
                    ,'width': '30%'
                    ,'height': '60px'
                    ,'top': '50%'
                    ,'margin-left': '35%'
                    ,'text-align': 'center'
                })
                .appendTo("body");
        }
        // if (!$maskMsg) {
        //     $maskMsg = $("<div></div>")
        //         .css({
        //             'position': 'absolute'
        //             ,'top': '50%'
        //             ,'margin-top': '-20px'
        //             ,'padding': '5px 20px 5px 20px'
        //             ,'width': 'auto'
        //             ,'border-width': '1px'
        //             ,'border-style': 'solid'
        //             ,'display': 'none'
        //             ,'background-color': '#ffffff'
        //             ,'font-size':'14px'
        //         })
        //         .appendTo("body");
        // }

        //$mask.css({width: "100%", height: $(document).height()});

        //var scrollTop = $(document.body).scrollTop();

        $mask.css({
            top:( ($(window).height()) / 2 ) + $(document).scrollTop()
        });

    }

    return {
        mask: function () {
            init();
            $mask.show();
            //$maskMsg.html(msg || defMsg).show();
        }
        , unmask: function () {
            $mask.hide();
            //$maskMsg.hide();
        }
    }

}());
// MaskUtil End