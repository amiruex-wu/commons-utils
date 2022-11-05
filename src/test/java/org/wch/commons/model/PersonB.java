package org.wch.commons.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonB implements Serializable {
    private static final long serialVersionUID = 2652938498316489612L;

    private String username;

    private String password;

    private PersonC personC;

    private PersonC personB;

    private List<String> roles;

    public PersonB(String username, String password, PersonC personC) {
        this.username = username;
        this.username = password;
        this.personC = personC;
    }
}
