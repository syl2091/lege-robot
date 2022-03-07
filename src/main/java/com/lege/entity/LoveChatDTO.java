package com.lege.entity;

/**
 * @author lege
 * @Description
 * @create 2022-03-04 15:48
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yinfeng
 * @description 话术实体
 * @since 2022/1/1 20:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoveChatDTO {
    private String female;
    private List<String> male;
}
