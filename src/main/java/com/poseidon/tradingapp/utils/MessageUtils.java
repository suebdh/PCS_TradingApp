package com.poseidon.tradingapp.utils;
/**
 * Classe utilitaire centralisant les messages utilisateurs (flash / validation)
 * Cette approche favorise la cohérence entre les contrôleurs et simplifie la maintenance (modification d'un message à un seul endroit)
 */
public final class MessageUtils {

    private MessageUtils() {
        // Constructeur privé : empêche l'instanciation de la classe.
        // new MessageUtils() provoquerait une erreur de compilation, car tout est statique.
    }

    // === RÈGLE (Rule) ===
    public static final String RULE_DUPLICATE = "Une règle portant ce nom existe déjà !";
    // --- AJOUT ---
    public static final String RULE_ADD_SUCCESS = "La règle a été ajoutée avec succès !";
    // --- MISE À JOUR / ÉDITION ---
    public static final String RULE_UPDATE_SUCCESS = "La règle a été mise à jour avec succès !";
    public static final String RULE_UPDATE_NOT_FOUND = "Mise à jour impossible : la règle n'existe pas ou a été supprimée !";
    public static final String RULE_EDIT_NOT_FOUND = "Impossible d'ouvrir le formulaire : la règle n'existe pas ou a été supprimée !";
    // --- SUPPRESSION ---
    public static final String RULE_DELETE_SUCCESS = "La règle a été supprimée avec succès !";
    public static final String RULE_DELETE_NOT_FOUND = "Suppression impossible : la règle n'existe pas ou a déjà été supprimée !";

    // ========================================================================
    // === TRADE (Transaction) ===

    // --- AJOUT ---
    public static final String TRADE_ADD_SUCCESS = "Le trade a été ajouté avec succès !";

    // --- MISE À JOUR / ÉDITION ---
    public static final String TRADE_UPDATE_SUCCESS = "Le trade a été mis à jour avec succès !";
    public static final String TRADE_UPDATE_NOT_FOUND = "Mise à jour impossible : le trade n'existe pas ou a été supprimé !";
    public static final String TRADE_EDIT_NOT_FOUND = "Impossible d'ouvrir le formulaire : le trade n'existe pas ou a été supprimé !";

    // --- SUPPRESSION ---
    public static final String TRADE_DELETE_SUCCESS = "Le trade a été supprimé avec succès !";
    public static final String TRADE_DELETE_NOT_FOUND = "Suppression impossible : le trade n'existe pas ou a déjà été supprimé !";

    // ========================================================================
    // === BIDLIST (Offres) ===

    // --- AJOUT ---
    public static final String BID_ADD_SUCCESS = "L'offre (Bid) a été ajoutée avec succès !";

    // --- MISE À JOUR / ÉDITION ---
    public static final String BID_UPDATE_SUCCESS = "L'offre (Bid) a été mise à jour avec succès !";
    public static final String BID_UPDATE_NOT_FOUND = "Mise à jour impossible : l'offre n'existe pas ou a été supprimée !";
    public static final String BID_EDIT_NOT_FOUND = "Impossible d'ouvrir le formulaire : l'offre n'existe pas ou a été supprimée !";

    // --- SUPPRESSION ---
    public static final String BID_DELETE_SUCCESS = "L'offre (Bid) a été supprimée avec succès !";
    public static final String BID_DELETE_NOT_FOUND = "Suppression impossible : l'offre n'existe pas ou a déjà été supprimée !";

    // ========================================================================
// === CURVEPOINT (Points de courbe) ===

    // --- AJOUT ---
    public static final String CURVE_ADD_SUCCESS = "Le point de courbe a été ajouté avec succès !";

    // --- MISE À JOUR / ÉDITION ---
    public static final String CURVE_UPDATE_SUCCESS = "Le point de courbe a été mis à jour avec succès !";
    public static final String CURVE_UPDATE_NOT_FOUND = "Mise à jour impossible : le point de courbe n'existe pas ou a été supprimé !";
    public static final String CURVE_EDIT_NOT_FOUND = "Impossible d'ouvrir le formulaire : le point de courbe n'existe pas ou a été supprimé !";

    // --- SUPPRESSION ---
    public static final String CURVE_DELETE_SUCCESS = "Le point de courbe a été supprimé avec succès !";
    public static final String CURVE_DELETE_NOT_FOUND = "Suppression impossible : le point de courbe n'existe pas ou a déjà été supprimé !";

    // ========================================================================
// === Rating (Notation) ===
    // --- AJOUT ---
    public static final String RATING_ADD_SUCCESS = "Le rating a été ajouté avec succès !";

    // --- MISE À JOUR / ÉDITION ---
    public static final String RATING_EDIT_NOT_FOUND = "Impossible d'ouvrir le formulaire : le rating n'existe pas ou a été supprimé !";

    public static final String RATING_UPDATE_SUCCESS = "Le rating a été mis à jour avec succès !";
    public static final String RATING_UPDATE_NOT_FOUND = "Mise à jour impossible : le rating n'existe pas ou a été supprimé !";

    // --- SUPPRESSION ---
    public static final String RATING_DELETE_SUCCESS = "Le rating a été supprimé avec succès !";
    public static final String RATING_DELETE_NOT_FOUND = "Suppression impossible : le rating n'existe pas ou a déjà été supprimé !";
}
