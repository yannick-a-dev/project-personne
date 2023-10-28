package com.gestiondepersonne.gestiondepersonne.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gestiondepersonne.gestiondepersonne.DTO.PersonneDetailsDTO;
import com.gestiondepersonne.gestiondepersonne.entities.Emploi;
import com.gestiondepersonne.gestiondepersonne.entities.Personne;
import com.gestiondepersonne.gestiondepersonne.service.PersonneService;

@RestController
@AllArgsConstructor
@RequestMapping("/personnes")
@ApiOperation(value = "Opérations de festion des personnes")
public class PersonneController {
    private final PersonneService personneService;

    @PostMapping("/enregistrer")
    @ApiOperation(value = "Enregistrer une nouvelle personne")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Personne enregistrée avec succès"),
        @ApiResponse(code = 400, message = "L'âge de la personne dépasse 150 ans")
    })
    public ResponseEntity<String> enregistrerNouvellePersonne(@RequestBody Personne personne) {
        try {
            personneService.enregistrerPersonne(personne);
            return ResponseEntity.ok("Personne enregistrée avec succès.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("L'âge de la personne dépasse 150 ans.");
        }
    }

    @PostMapping("/{personneId}/ajouter-emploi")
    @ApiOperation(value = "Ajouter un emploi à une personne")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Emploi ajouté avec succès"),
        @ApiResponse(code = 400, message = "Erreur lors de l'ajout de l'emploi")
    })
    public ResponseEntity<String> ajouterEmploi(
            @PathVariable Long personneId,
            @RequestBody Emploi emploi) {
        try {
            personneService.ajouterEmploi(personneId, emploi);
            return ResponseEntity.status(HttpStatus.CREATED).body("Emploi ajouté avec succès.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/toutes-les-personnes-details")
    @ApiOperation(value = "Obtenir toutes les personnes avec des détails")
    public ResponseEntity<List<PersonneDetailsDTO>> obtenirToutesLesPersonnesAvecDetails() {
        List<PersonneDetailsDTO> personnesDetails = personneService.obtenirToutesLesPersonnesAvecDetails();
        return ResponseEntity.ok(personnesDetails);
    }

    @GetMapping("/par-entreprise")
    @ApiOperation(value = "Obtenir les personnes par entreprise")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Personnes dans l'entreprise trouvées"),
        @ApiResponse(code = 404, message = "Aucune personne trouvée pour l'entreprise spécifiée")
    })
    public ResponseEntity<List<Personne>> obtenirPersonnesParEntreprise(@RequestParam String entreprise) {
        List<Personne> personnesDansEntreprise = personneService.obtenirPersonnesParEntreprise(entreprise);
        return ResponseEntity.ok(personnesDansEntreprise);
    }

    @GetMapping("/{personneId}/emplois-entre-dates")
    @ApiOperation(value = "Obtenir les emplois d'une personne entre deux dates")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Emplois trouvés"),
        @ApiResponse(code = 404, message = "Aucun emploi trouvé pour la personne et les dates spécifiées")
    })
    public ResponseEntity<List<Emploi>> obtenirEmploisEntreDeuxDates(
            @PathVariable Long personneId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<Emploi> emplois = personneService.obtenirEmploisEntreDeuxDates(personneId, dateDebut, dateFin);
        return ResponseEntity.ok(emplois);
    }
}
