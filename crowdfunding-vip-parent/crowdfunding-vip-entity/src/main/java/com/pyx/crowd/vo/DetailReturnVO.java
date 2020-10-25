package com.pyx.crowd.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品详情页面的回报信息模块实体类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailReturnVO {

    // 回报信息主键
    private Integer returnId;

    // 当前档位需支持的金额
    private Integer supportMoney;

    // 是否限制单笔购买数量，0 表示不限购，1 表示限购
    private Integer signalPurchase;

    // 具体限额数量
    private Integer purchase;

    // 当前该档位的支持者
    private Integer supporterCount;

    // 运费，“0”为包邮
    private Integer freight;

    // 众筹结束后返还回报物品天数
    private Integer returnDate;

    // 回报内容
    private String content;
}
