(function ($) {
    $.fn.load = function (options) {
        options.container = this.selector;
        this.load.options = $.extend({}, this.load.options, {
            pages: 1,
            pageNumber: 1
        }, {dataLoad: false}, options || {});
        $(this.selector).html("");
        var loadingHtml = '<div id="list_cloading" style="text-align:center;display:none;"><img style="width: 30px; margin: 10px auto;" src="../images/loading.gif" /></div>';
        $(options.container).append(loadingHtml);
        try {
            var loadingImage = new Image();
            loadingImage.src = '../images/loading.gif';
            loadingImage.onload = function () {
                if (loadingImage.complete == true) {
                    if (loadingImage)
                        $.fn.load.options.getMore();
                }
            }
        } catch (e) {
            this.getMore();
        }
    };

    $.fn.load.options = {
        pageNumber: 1,
        pageSize: 12,
        pages: 1,
        priceFlag: null,
        saleFlag: null,
        rowCount: 0,
        url: "",
        dataLoad: false, // 数据是否正在加载
        datas: {},
        container: "",
        async: true,
        callback: function () {
        },
        complete: function () {
        },
        getMore: function () {
            if (this.dataLoad) {
                return false;
            }
            $.fn.load.options.dataLoad = true;
            if (this.pageNumber > this.pages) {
                return;
            }
            try {
                $.ajax({
                    url: this.url,
                    type: "POST",
                   /* dataType: "JSON",*/
                    async: $.fn.load.options.async,
                    timeout: 60000,
                    data: $.extend(true, this.datas, {
                        pageNo: this.pageNumber,
                        pageSize: this.pageSize,
                        priceFlag: this.priceFlag,
                        saleFlag: this.saleFlag
                    }),
                    beforeSend: function (xmlHttpRequest) {
                        $("#list_cloading").show();
                        MaskUtil.mask();
                    },
                    success: function (data) {
                        if (data) {
                            if (typeof(data.responseCode) != 'undefined' && data.responseCode == 'err999') {
                                alert(data.responseMsg);
                                return false;
                            }
                            $.fn.load.options.pages = data.totalpage;
                            $.fn.load.options.rowCount = data.count;
                            var dataList = data.dataList;
                            var htmls = $.fn.load.options.callback(dataList);
                            $($.fn.load.options.container + " #list_cloading").before(htmls);
                            // 当没有返回pages的时候，将最后一次加载出的数据和pageSize比较，得出pages
                            if (!$.fn.load.options.pages && dataList && dataList.length < $.fn.load.options.pageSize) {
                                $.fn.load.options.pages = $.fn.load.options.pageNumber;
                            }
                            $.fn.load.options.pageNumber++;
                        }
                    },
                    // XMLHttpRequest, textStatus
                    complete: function (XMLHttpReq, status) {
                        MaskUtil.unmask();
                        $("#list_cloading").hide();
                        if ($.fn.load.options.complete) {
                            $.fn.load.options.complete(XMLHttpReq, status);
                        }
                        $.fn.load.options.dataLoad = false;
                    }
                });
            } catch (e) {
                $.fn.load.options.dataLoad = false;
            }
        }
    };

})(jQuery);