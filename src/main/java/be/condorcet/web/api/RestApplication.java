package be.condorcet.web.api;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

// C'est l'URL de base de toute l'API.
// Exemple : http://localhost:8180/api/...
@ApplicationPath("/api")
public class RestApplication extends Application {
    // On laisse vide. Cette classe sert juste de configuration.
}