package org.wch.commons.model;


import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class PersonA implements Serializable {

//    private String name;
//
//    private String id;

    private PersonB personB;

    private List<String> addrs;

}
