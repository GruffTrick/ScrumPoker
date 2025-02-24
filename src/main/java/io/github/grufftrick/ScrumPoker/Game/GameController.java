package io.github.grufftrick.ScrumPoker.Game;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class GameController {
    private final List<Session> sessions;

    public GameController() {
        this.sessions = new ArrayList<>();
    }


    @PostMapping("/api/session/create")
    public ResponseEntity<Session> createSession(@RequestBody int maxPlayers) {
        // check duplicate ids
        String id = generateId();
        if (!sessions.isEmpty()) {
            while (hasDuplicateId(id)) id = generateId();
        }

        Session session;
        try {
            session = new Session(id, maxPlayers);
            sessions.add(session);
        } catch (Exception e) {
            // TODO Exceptions
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(sessions.get(sessions.indexOf(session)));
    }

    @PostMapping("api/session/{sessionId}/player/add")
    public ResponseEntity<Player> addPlayer(@PathVariable String sessionId, @RequestBody String name) {
        if (sessions.isEmpty()) return ResponseEntity.noContent().build();
        Session session = null;
        try {
            session = findMatchingSession(sessionId);
            if (session == null) return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        if (session.getPlayers().size() == session.getMaxPlayers()) return ResponseEntity.status(507).build(); // 507 insufficient storage if maxplayers in session
        Player player = new Player(generateId(), name);
        session.getPlayers().add(player);
        return ResponseEntity.ok(player);
    }

    @GetMapping("/api/session/{sessionId}")
    public ResponseEntity<Session> getSession(@PathVariable String sessionId) {
        if (sessions.isEmpty()) return ResponseEntity.noContent().build();
        Session session = null;
        try {
            session = findMatchingSession(sessionId);
            if (session == null) return ResponseEntity.badRequest().build();
            return ResponseEntity.ok(session);
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/api/session/all")
    public ResponseEntity<List<Session>> getSession() {
        if (sessions.isEmpty()) return ResponseEntity.noContent().build();
        Session session = null;
        try {
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            System.err.println(e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/api/session/{sessionId}/player/all")
    public ResponseEntity<List<Player>> getPlayers(@PathVariable String sessionId) {
        if (sessions.isEmpty()) return ResponseEntity.noContent().build();
        List<Player> players = new ArrayList<>();

        try {
            Session session = findMatchingSession(sessionId);
            if (session == null) return ResponseEntity.badRequest().build();
            players = session.getPlayers();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(players);
    }

    @GetMapping("/api/session/{sessionId}/reveal")
    public ResponseEntity<List<Player>> getSelections(@PathVariable String sessionId) {
        if (sessions.isEmpty()) return ResponseEntity.noContent().build();
        List<Player> players = new ArrayList<>();
        try {
            Session session = findMatchingSession(sessionId);
            if (session == null) return ResponseEntity.badRequest().build();
            if (session.getPlayers().isEmpty()) ResponseEntity.badRequest().build();
            for (Player player : session.getPlayers()) {
                if (player.hasSelected() && player.getSelection() != null) {
                    players.add(player);
                }
            }
            return ResponseEntity.ok(players);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    //Select a value for selection, by player ID
    @PatchMapping("/api/session/{sessionId}/player/{playerId}/selection")
    public ResponseEntity<Player> updateSelection(@PathVariable String sessionId, @PathVariable String playerId,
                                                  @RequestBody int selection) {
        if (sessions.isEmpty()) return ResponseEntity.badRequest().build();
        Session session = findMatchingSession(sessionId);
        if (session == null) return ResponseEntity.badRequest().build();

        for (Player player: session.getPlayers()) {
            if (player.getId().equals(playerId)) {
                player.setSelection(selection);
                player.setHasSelected(true);
                return ResponseEntity.ok(player);
            }
        }
        return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping("/api/session/delete/{sessionId}")
    public ResponseEntity<Session> deleteSession(@PathVariable String sessionId) {
        if (sessions.isEmpty()) return ResponseEntity.badRequest().build();
        Session session = null;
        try {
            session = findMatchingSession(sessionId);
            if (session == null) return ResponseEntity.badRequest().build();
            sessions.remove(session);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(session);
    }



    /**
     * Generates a random alphanumeric string
     *
     * @author User 'Suresh Atta' on Stack Overflow
     * @return id key of random alphanumeric characters
     */
    private static String generateId() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    private boolean hasDuplicateId(String id) {
        for (Session sesh: sessions) {
            if (sesh.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    private Session findMatchingSession(String sessionId) {
        Session session;
        for (Session sesh : sessions) {
            if (sesh.getId().equals(sessionId)) {
                session = sesh;
                return session;
            }
        }
        return null;
    }
}
