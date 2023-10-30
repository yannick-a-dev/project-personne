package com.gestiondepersonne.gestiondepersonne.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.gestiondepersonne.gestiondepersonne.entities.Emploi;
import com.gestiondepersonne.gestiondepersonne.entities.Personne;

public interface EmploiRepository extends JpaRepository<Emploi, Long> {

	List<Emploi> findByNomEntreprise(String nomEntreprise);

	@Query("SELECT e FROM Emploi e WHERE e.personne = :personne "
			+ "AND e.dateDebut >= :dateDebut AND (e.dateFin IS NULL OR e.dateFin <= :dateFin)")
	List<Emploi> findEmploisBetweenDates(@Param("personne") Personne personne, @Param("dateDebut") LocalDate dateDebut,
			@Param("dateFin") LocalDate dateFin);

	List<Emploi> findByPersonneId(Long personneId);

}
