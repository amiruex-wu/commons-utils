package top.itoolbox.commons.model;


import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class PersonD implements Serializable {

    private String id;

    private String username;

    private PersonC personB;

    private PersonB personC;

    private List<String> roles;

}
