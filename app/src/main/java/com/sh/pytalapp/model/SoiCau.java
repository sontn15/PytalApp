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
public class SoiCau implements Serializable {
    private int id;
    private String keyDate;
    private String createdDate;
    private String bachThuLo;
    private String songThuLo1;
    private String songThuLo2;
    private String listDanDe6X;
    private String mien;
    private String nhaDai;
}
