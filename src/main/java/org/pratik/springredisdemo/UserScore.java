package org.pratik.springredisdemo;

import jakarta.persistence.*;


@Entity
@Table(name = "user_score")
public class UserScore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String username;
    private Long score = 0L;

    public UserScore(String username, Long score) {
        this.score = score;
        this.username = username;
    }

    public UserScore() {
    }

    public UserScore(int id, String username, Long score) {
        this.id = id;
        this.username = username;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }
}
