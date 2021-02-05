package com.sh.pytalapp.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettingModel implements Serializable {
    private int id;
    private String phoneNumber;
    private String huongDanTaiXiu;
    private String huongDanXocDia;
    private String huongDanBaccarat;
}
