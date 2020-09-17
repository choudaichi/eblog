layui.use('layim', function (layim) {

    var $ = layui.jquery;

    var tiows = new tio.ws($, layim)

    layim.config({
        brief: true //是否简约模式（如果true则不显示主面板）
        , voice: false
        , chatLog: layui.cache.dir + 'css/modules/layim/html/chatlog.html'
    });

    // 打开窗口
    tiows.openChatWindow();

    // 建立连接ws
    tiows.connect();

    // 发送消息
    layim.on('sendMessage', function (res) {
        tiows.sendChatMessage(res);
    });

    // 历史聊天信息回显
    tiows.initHistoryMess();


});