package com.pyx.crowd.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressVO implements Serializable {

    private static final long serialVersionUID = -7262046030559817995L;
    private Integer id;

    private String receiveName;

    private String phoneNum;

    private String address;

    private Integer memberId;
}
