package com.gestiondepersonne.gestiondepersonne.DTO;

import java.util.List;
import lombok.Data;

@Data
public class PersonneDetailsDTO {
	private Long id;
    private String nom;
    private String prenom;
    private int age;
    private List<String> emploisActuels;
}
