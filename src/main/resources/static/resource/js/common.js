function showTips(icon, css, message) {
    if ($('#GTip').length == 0) {
        var tip = "<div id='GTip' style='display:none;'><span class='glyphicon " + icon + "' style='top:2px;' ></span> " + message + "</label></div>";
        var w = $(document).width();
        $('body').append($(tip));
        $('#GTip').css({
            top: 72,
            left: (w - 300) / 2
        })
    } else {
        $('#GTip').removeAttr('class')
    }
    $('#GTip').addClass(css);
    $('#GTip').fadeIn(200,
        function() {
            window.setTimeout(function() {
                    $('#GTip').fadeOut(300,
                        function() {
                            $('#GTip').remove()
                        })
                },
                2000)
        })
}
function prepareShowProcess(css, message) {
    if ($('#ProcessDialog').length == 0) {
        var tip = "<div id='ProcessDialog' style=''><a id='ProcessIcon'/><label style='margin-left:10px;'>" + message + "</label></div>";
        var h = $(document).height();
        var w = $(document).width();
        $('body').append($(tip));
        $('#ProcessDialog').addClass(css.tip);
        $('#ProcessIcon').addClass(css.icon);
        $('#ProcessDialog').css({
            top: 72,
            left: (w - 300) / 2
        });
        $('#ProcessDialog').fadeIn("slow")
    } else {
        $('#ProcessDialog').find('label').text(message);
        $('#ProcessDialog').fadeIn("slow")
    }
    $('.panel').css('z-index', '997')
}
function hideProcess() {
    $('#ProcessDialog').hide();
    if ($('.gdialog:visible').length == 0) {
        $('#global_mask').fadeOut("slow")
    }
    $('.panel').css('z-index', '1000')
}
function showSTip(message) {
    showTips("glyphicon-ok-sign", "tip_green", message)
}
function showHTip(message) {
    showTips("glyphicon-info-sign", "tip_blue", message)
}
function showProcess(message) {
    prepareShowProcess({
            tip: 'tip_process',
            icon: 'icon_loading_small'
        },
        message);
    var h = $(document).height();
    $('#global_mask').css('height', h);
    $('#global_mask').fadeIn("slow")
}
function showETip(message) {
    showTips("glyphicon-remove-sign", "tip_red", message)
}

function isEmail(str) {
    var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    return reg.test(str)
}
function isImageFile(filename) {
    var postf = filename.substring(filename.lastIndexOf('.') + 1, filename.length).toLowerCase();
    return postf == 'jpg' || postf == 'png' || postf == 'jpeg' || postf == 'gif'
}
function isCsvTemplateFile(filename) {
    var postf = filename.substring(filename.lastIndexOf('.') + 1, filename.length).toLowerCase();
    return postf == 'csv'
}
function doShowDialog(dialogId) {
    $('#' + dialogId).modal({
        show: true
    })
}
function doHideDialog(dialogId) {
    $('#' + dialogId).modal('hide')
}

function doShowDialogV5(dialogId) {
    var myModal = new bootstrap.Modal(document.getElementById(dialogId), {
        keyboard: false
    });
    myModal.show();
}
function doHideDialogV5(dialogId) {
    var myModal = new bootstrap.Modal(document.getElementById(dialogId), {
        keyboard: false
    });
    myModal.hide();
}

function doShowConfirm(setting) {
    if ($('#ConfirmDialog').length == 0) {
        var dialog = "<div class='modal fade' id='ConfirmDialog' tabindex='-1' role='dialog'><div class='modal-dialog' style='width:400px;z-index:100000;' ><div id='ConfirmDialogContent'   class='modal-content'></div></div></div>";
        var head = "<div class='modal-header'><button type='button' class='close' data-dismiss='modal' aria-label='Close'> <span aria-hidden='true'>×</span></button><h4 class='modal-title' id='ConfirmDialogTitle'>提示</h4></div>";
        var body = "<div class='modal-body' style='padding:25px;'></div>";
        var foot = "<div class='modal-footer' style='padding:5px 10px;'><div  style='text-align: right;'><button type='button' class='btn btn-default confirm-cancel' ><span class='glyphicon glyphicon-remove-circle' style='top:2px;'></span>取消</button><button type='button' class='btn btn-success confirm-ok' style='margin-left: 15px;' ><span style='top:2px;' class='glyphicon glyphicon-ok-circle'></span>确定</button></div></div>";
        $('body').append($(dialog));
        $(head).appendTo($('#ConfirmDialogContent'));
        $(body).appendTo($('#ConfirmDialogContent'));
        $(foot).appendTo($('#ConfirmDialogContent'))
    }
    $('#ConfirmDialogContent').find('.modal-body').html(setting.hint);
    if (setting.onCancel != undefined && jQuery.isFunction(setting.onCancel)) {
        $('#ConfirmDialogContent').find('.modal-footer').find(".confirm-cancel").unbind("click").click(setting.onCancel)
    } else {
        $('#ConfirmDialogContent').find('.modal-footer').find(".confirm-cancel").unbind("click").click(doHideConfirm)
    }
    $('#ConfirmDialogContent').find('.modal-footer').find(".confirm-ok").unbind("click").click(setting.onConfirm);
    doShowDialog('ConfirmDialog')
}
function doHideConfirm() {
    doHideDialog('ConfirmDialog')
}
function gotoPage(number) {
    var intreg = /^\d+$/;
    if (intreg.test(number)) {
        $('#currentPage').val(number);
        FormValidator.filterEmptyParamter($("#searchForm"));
        document.getElementById("searchForm").submit()
    }
}

var FormValidator = {};
FormValidator.autoCheckAndAsyncPost = function(form) {
    var verify = true;
    var params = {};
    form.find('input,select,textarea').each(function() {
        var input = $(this);
        var value = input.val();
        var name = input.attr("name");
        var required = input.attr("required") == 'required';
        if (required && $.trim(value) == '') {
            verify = false;
            input.addClass("input-required");
            setTimeout(function() {input.removeClass("input-required")},2000);
            return false
        }
        if ($.trim(value) != '') {
            params[name] = value
        }
    });
    if (verify) {
        var action = form.attr("action");
        var loading = form.attr("loading");
        var method = form.attr("method").toUpperCase();
        var onsuccess = form.attr("onsuccess");
        if (loading != undefined && $.trim(loading) != '') {
            showProcess(loading)
        }

        $.ajax({url:action,type: method, data:params,success:function(data){
            eval(onsuccess).call(this,data);
        }});
    }
};

FormValidator.filterEmptyParamter = function(form) {
    form.find('input,select').each(function() {
        if ($.trim($(this).val()) == ''){
            $(this).removeAttr("name")
        }
    });
};

$(document).ready(function() {
    $(".smart-form").submit(function(e) {
        FormValidator.autoCheckAndAsyncPost($(this));
        return false
    })
    $("#searchForm").submit(function(e) {
        FormValidator.filterEmptyParamter($(this));
        return true
    })
});

function toDatetime(timestamp) {
    var date = new Date(timestamp );
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    var D = date.getDate() + ' ';
    var h = date.getHours() + ':';
    var m = date.getMinutes() + ':';
    var s = date.getSeconds();
    return Y+M+D+h+m+s;
}

function generateUUID() {
    let d = new Date().getTime();
    let uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        let r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c === 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
    return uuid.replace(/-/g, '');
}