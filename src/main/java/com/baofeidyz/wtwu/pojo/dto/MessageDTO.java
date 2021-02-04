package com.baofeidyz.wtwu.pojo.dto;

import com.baofeidyz.wtwu.constant.MessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private MessageType type;
    private String message;
    private Long currentTime;

}
