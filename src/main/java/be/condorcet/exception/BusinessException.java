package be.condorcet.exception;

//Exception personnalisée pour les rêgles métier de l'appli. Utilisée quand une operation est invalide du point de vue métier
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
