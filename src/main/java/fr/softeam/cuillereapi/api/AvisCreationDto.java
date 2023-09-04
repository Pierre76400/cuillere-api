package fr.softeam.cuillereapi.api;

import fr.softeam.cuillereapi.model.Restaurant;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

public class AvisCreationDto {
	private String commentaire;

	private String auteur;

	private Long note;

	private Long idRestaurant;

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public String getAuteur() {
		return auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	public Long getNote() {
		return note;
	}

	public void setNote(Long note) {
		this.note = note;
	}

	public Long getIdRestaurant() {
		return idRestaurant;
	}

	public void setIdRestaurant(Long idRestaurant) {
		this.idRestaurant = idRestaurant;
	}
}
