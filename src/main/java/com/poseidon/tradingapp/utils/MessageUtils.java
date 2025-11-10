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
}
