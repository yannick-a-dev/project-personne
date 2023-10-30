package com.gestiondepersonne.gestiondepersonne.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Personnes", description = "Opérations de gestion des personnes")
public class PersonneController {
    private final PersonneService personneService;

    @PostMapping("/enregistrer")
    @Operation(
        summary = "Enregistrer une nouvelle personne",
        responses = {
            @ApiResponse(responseCode = "200", description = "Personne enregistrée avec succès"),
            @ApiResponse(responseCode = "400", description = "L'âge de la personne dépasse 150 ans")
        }
    )
    public ResponseEntity<String> enregistrerNouvellePersonne(@RequestBody Personne personne) {
        try {
            personneService.enregistrerPersonne(personne);
            return ResponseEntity.ok("Personne enregistrée avec succès.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("L'âge de la personne dépasse 150 ans.");
        }
    }

    @PostMapping("/{personneId}/ajouter-emploi")
    @Operation(
        summary = "Ajouter un emploi à une personne",
        responses = {
            @ApiResponse(responseCode = "201", description = "Emploi ajouté avec succès"),
            @ApiResponse(responseCode = "400", description = "Erreur lors de l'ajout de l'emploi")
        }
    )
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
    @Operation(
        summary = "Obtenir toutes les personnes avec des détails"
    )
    public ResponseEntity<List<PersonneDetailsDTO> > obtenirToutesLesPersonnesAvecDetails() {
        List<PersonneDetailsDTO> personnesDetails = personneService.obtenirToutesLesPersonnesAvecDetails();
        return ResponseEntity.ok(personnesDetails);
    }

    @GetMapping("/par-entreprise")
    @Operation(
        summary = "Obtenir les personnes par entreprise",
        responses = {
            @ApiResponse(responseCode = "200", description = "Personnes dans l'entreprise trouvées"),
            @ApiResponse(responseCode = "404", description = "Aucune personne trouvée pour l'entreprise spécifiée")
        }
    )
    public ResponseEntity<List<Personne>> obtenirPersonnesParEntreprise(@RequestParam String entreprise) {
        List<Personne> personnesDansEntreprise = personneService.obtenirPersonnesParEntreprise(entreprise);
        return ResponseEntity.ok(personnesDansEntreprise);
    }

    @GetMapping("/{personneId}/emplois-entre-dates")
    @Operation(
        summary = "Obtenir les emplois d'une personne entre deux dates",
        responses = {
            @ApiResponse(responseCode = "200", description = "Emplois trouvés"),
            @ApiResponse(responseCode = "404", description = "Aucun emploi trouvé pour la personne et les dates spécifiées")
        }
    )
    public ResponseEntity<List<Emploi> > obtenirEmploisEntreDeuxDates(
            @PathVariable Long personneId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<Emploi> emplois = personneService.obtenirEmploisEntreDeuxDates(personneId, dateDebut, dateFin);
        return ResponseEntity.ok(emplois);
    }
    
    @GetMapping("/{personneId}/emplois")
    public ResponseEntity<List<Emploi>> getEmploisByPersonneId(@PathVariable Long personneId) {
        List<Emploi> emplois = personneService.getEmploisByPersonneId(personneId);
        return new ResponseEntity<>(emplois, HttpStatus.OK);
    }
}
