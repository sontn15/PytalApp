package com.sh.pytalapp.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SoiCauMbDTO {

    private int id;
    private String bachThuLo;
    private String songThuLo1;
    private String songThuLo2;
    private List<String> listDanDe6X;


}
