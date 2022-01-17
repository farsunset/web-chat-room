/*CIM服务器IP*/
const CIM_HOST = window.location.hostname;
/*
 *服务端 websocket端口
 */
const CIM_PORT = 45678;
const CIM_URI = "ws://" + CIM_HOST + ":" + CIM_PORT;

/*
 *特殊的消息类型，代表被服务端强制下线
 */
const ACTION_999 = "999";
const DATA_HEADER_LENGTH = 1;

const MESSAGE = 2;
const REPLY_BODY = 4;
const SENT_BODY = 3;
const PING = 1;
const PONG = 0;
/**
 * PONG字符串转换后
 * @type {Uint8Array}
 */
const PONG_BODY = new Uint8Array([80,79,78,71]);

let socket;
let manualStop = false;
const CIMPushManager = {};
let roomId;
CIMPushManager.connect = function (id) {
    roomId = id;
    let isJoined = window.sessionStorage.getItem(roomId);
    if (isJoined !== 'true'){
        quitRoom();
        return;
    }

    manualStop = false;
    socket = new WebSocket(CIM_URI);
    socket.cookieEnabled = false;
    socket.binaryType = 'arraybuffer';
    socket.onopen = CIMPushManager.innerOnConnectFinished;
    socket.onmessage = CIMPushManager.innerOnMessageReceived;
    socket.onclose = CIMPushManager.innerOnConnectionClosed;
};

CIMPushManager.bind = function (roomId) {

    let body = new proto.com.farsunset.cim.sdk.web.model.SentBody();
    body.setKey("client_bind");
    body.setTimestamp(new Date().getTime());
    body.getDataMap().set("uid", window.localStorage.uid);
    body.getDataMap().set("roomId", roomId);
    body.getDataMap().set("name", window.localStorage.name);
    CIMPushManager.sendRequest(body);
};

CIMPushManager.setChatting = function (value) {
    isChatting = value;
};

CIMPushManager.stop = function () {
    manualStop = true;
    socket.close();
};

CIMPushManager.resume = function () {
    manualStop = false;
    CIMPushManager.connect(roomId);
};


CIMPushManager.innerOnConnectFinished = function () {
    onConnectFinished();
};


CIMPushManager.innerOnMessageReceived = function (e) {
    let data = new Uint8Array(e.data);
    let type = data[0];
    let body = data.subarray(DATA_HEADER_LENGTH, data.length);

    if (type === PING) {
        CIMPushManager.pong();
        return;
    }

    if (type === MESSAGE) {
        let message = proto.com.farsunset.cim.sdk.web.model.Message.deserializeBinary(body);
        onInterceptMessageReceived(message.toObject(false));
        return;
    }

    if (type === REPLY_BODY) {
        let message = proto.com.farsunset.cim.sdk.web.model.ReplyBody.deserializeBinary(body);
        /**
         * 将proto对象转换成json对象，去除无用信息
         */
        let reply = {};
        reply.code = message.getCode();
        reply.key = message.getKey();
        reply.message = message.getMessage();
        reply.timestamp = message.getTimestamp();
        reply.data = {};

        /**
         * 注意，遍历map这里的参数 value在前key在后
         */
        message.getDataMap().forEach(function (v, k) {
            reply.data[k] = v;
        });

        onReplyReceived(reply);
    }
};

CIMPushManager.innerOnConnectionClosed = function (e) {
    if (!manualStop) {
        setTimeout(function () {
            CIMPushManager.connect(roomId);
        }, 1);
    }
};

CIMPushManager.sendRequest = function (body) {
    let data = body.serializeBinary();
    let protobuf = new Uint8Array(data.length + 1);
    protobuf[0] = SENT_BODY;
    protobuf.set(data, 1);
    socket.send(protobuf);
};

CIMPushManager.pong = function () {
    let pong =  new Uint8Array(PONG_BODY.byteLength + 1);
    pong[0] = PONG;
    pong.set(PONG_BODY,1);
    socket.send(pong);
};

function onInterceptMessageReceived(message) {

    /*
     *收到消息后，将消息发送给页面
     */
    if (onMessageReceived instanceof Function) {
        onMessageReceived(message);
    }
}