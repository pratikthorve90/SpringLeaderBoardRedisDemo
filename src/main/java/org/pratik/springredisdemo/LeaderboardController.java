package org.pratik.springredisdemo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LeaderboardController {

    private final LeaderBoardService leaderBoardService;

    public LeaderboardController(LeaderBoardService leaderBoardService) {
        this.leaderBoardService = leaderBoardService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello World!");
    }

    @GetMapping("/{username}/{incrementBy}") // passing inputs -> username, score
    public ResponseEntity<List<LeaderBoardEntryDTO>> addScore(@PathVariable String username, @PathVariable Long incrementBy) {
        return ResponseEntity.ok(leaderBoardService.incrementScore(username, incrementBy));
    }
}
