package com.baofeidyz.wtwu.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.baofeidyz.wtwu.constant.MessageType;
import com.baofeidyz.wtwu.pojo.dto.MessageDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ServerEndpoint("/room/{id}")
public class RoomController {

    private static final Map<String, Set<Session>> map = new ConcurrentHashMap<>();
    private static final Map<Session, String> sessionAndRoomIdMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        if (map.containsKey(id)) {
            map.get(id).add(session);
        } else {
            Set<Session> sessionSet = new HashSet<>();
            sessionSet.add(session);
            map.put(id, sessionSet);
        }
        sessionAndRoomIdMap.put(session, id);
        try {
            sendMessage(MessageDTO.builder().type(MessageType.FIRST).message("已连接").build(), session);
        } catch (IOException e) {
            log.error("消息发送失败！", e);
        }
    }

    private void sendMessage(MessageDTO message, Session session) throws IOException {
        session.getBasicRemote().sendText(JSON.toJSONString(message));
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.error(message);
        if (StringUtils.isEmpty(message) || !sessionAndRoomIdMap.containsKey(session)) {
            return;
        }
        String roomId = sessionAndRoomIdMap.get(session);
        if (!map.containsKey(roomId)) {
            return;
        }
        Set<Session> sessionSet = map.get(roomId);
        MessageDTO messageDTO = JSON.parseObject(message, MessageDTO.class);
        for (Session tempSession : sessionSet) {
            if (session.equals(tempSession)) {
                continue;
            }
            try {
                sendMessage(messageDTO, tempSession);
            } catch (IOException e) {
                log.error("消息发送失败！messageDataDTO:{}, tempSession:{}", messageDTO, tempSession, e);
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        String roomId = sessionAndRoomIdMap.get(session);
        sessionAndRoomIdMap.remove(session);
        if (null != roomId && map.containsKey(roomId)) {
            map.get(roomId).remove(session);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        String roomId = sessionAndRoomIdMap.get(session);
        sessionAndRoomIdMap.remove(session);
        if (null != roomId && map.containsKey(roomId)) {
            map.get(roomId).remove(session);
        }
        log.error("", error);
    }
}
