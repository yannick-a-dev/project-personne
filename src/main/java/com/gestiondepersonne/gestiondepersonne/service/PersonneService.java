package com.gestiondepersonne.gestiondepersonne.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.gestiondepersonne.gestiondepersonne.DTO.PersonneDetailsDTO;
import com.gestiondepersonne.gestiondepersonne.entities.Emploi;
import com.gestiondepersonne.gestiondepersonne.entities.Personne;
import com.gestiondepersonne.gestiondepersonne.repository.EmploiRepository;
import com.gestiondepersonne.gestiondepersonne.repository.PersonneRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PersonneService {
	private final PersonneRepository personneRepository;
	private final EmploiRepository emploiRepository;

	public Personne enregistrerPersonne(Personne personne) {
	    LocalDate dateNaissance = personne.getDateNaissance();

	    if (dateNaissance == null) {
	        throw new IllegalArgumentException("La date de naissance de la personne est manquante.");
	    }

	    LocalDate dateLimite = LocalDate.now().minusYears(150);
	    Period age = Period.between(dateNaissance, LocalDate.now());

	    if (dateNaissance.isBefore(dateLimite)) {
	        throw new IllegalArgumentException("L'âge de la personne dépasse 150 ans.");
	    }

	    return personneRepository.save(personne);
	}

	public void ajouterEmploi(Long personneId, Emploi emploi) {
        Personne personne = personneRepository.findById(personneId)
                .orElseThrow(() -> new IllegalArgumentException("Personne non trouvée"));

        LocalDate dateDebut = emploi.getDateDebut();
        LocalDate dateFin = emploi.getDateFin();

        if (dateDebut.isAfter(dateFin)) {
            throw new IllegalArgumentException("La date de début ne peut pas être après la date de fin.");
        }

        List<Emploi> emploisExistant = personne.getEmplois();
        for (Emploi emploiExistant : emploisExistant) {
            if (dateDebut.isBefore(emploiExistant.getDateFin()) && dateFin.isAfter(emploiExistant.getDateDebut())) {
                throw new IllegalArgumentException("Les dates d'emploi se chevauchent avec un emploi existant.");
            }
        }
        emploi.setPersonne(personne);
        emploiRepository.save(emploi);
    }

	public List<PersonneDetailsDTO> obtenirToutesLesPersonnesAvecDetails() {
        List<Personne> personnes = personneRepository.findAllByOrderByNomAscPrenomAsc();
        List<PersonneDetailsDTO> personnesDetails = new ArrayList<>();

        LocalDate aujourdHui = LocalDate.now();

        for (Personne personne : personnes) {
            PersonneDetailsDTO personneDetails = new PersonneDetailsDTO();
            personneDetails.setId(personne.getId());
            personneDetails.setNom(personne.getNom());
            personneDetails.setPrenom(personne.getPrenom());

            LocalDate dateNaissance = personne.getDateNaissance();
            int age = Period.between(dateNaissance, aujourdHui).getYears();
            personneDetails.setAge(age);

            List<String> emploisActuels = new ArrayList<>();
            for (Emploi emploi : personne.getEmplois()) {
                if (emploi.getDateFin() == null || emploi.getDateFin().isAfter(aujourdHui)) {
                    emploisActuels.add(emploi.getPosteOccupe());
                }
            }
            personneDetails.setEmploisActuels(emploisActuels);

            personnesDetails.add(personneDetails);
        }

        return personnesDetails;
    }

	 public List<Personne> obtenirPersonnesParEntreprise(String nomEntreprise) {
		 
	        List<Emploi> emploisDansEntreprise = emploiRepository.findByNomEntreprise(nomEntreprise);
	        List<Personne> personnesDansEntreprise = new ArrayList<>();

	        for (Emploi emploi : emploisDansEntreprise) {
	            Personne personne = emploi.getPersonne();
	            personnesDansEntreprise.add(personne);
	        }

	        return personnesDansEntreprise;
	    }

	 public List<Emploi> obtenirEmploisEntreDeuxDates(Long personneId, LocalDate dateDebut, LocalDate dateFin) {
	        Personne personne = personneRepository.findById(personneId)
	                .orElseThrow(() -> new IllegalArgumentException("Personne non trouvée"));

	        return emploiRepository.findEmploisBetweenDates(personne, dateDebut, dateFin);
	    }


	    public List<Emploi> getEmploisByPersonneId(Long personneId) {
	        return emploiRepository.findByPersonneId(personneId);
	    }
}
