package com.gestiondepersonne.gestiondepersonne.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gestiondepersonne.gestiondepersonne.entities.Personne;

public interface PersonneRepository extends JpaRepository<Personne, Long> {

	List<Personne> findAllByOrderByNomAscPrenomAsc();

}
