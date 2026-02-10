package org.pratik.springredisdemo;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class LeaderBoardService {

    private static final String LEADERBOARD_KEY = "leaderboard";

    private final UserRepository userRepository;
    private final StringRedisTemplate stringRedisTemplate;

    public LeaderBoardService(UserRepository userRepository, StringRedisTemplate stringRedisTemplate) {
        this.userRepository = userRepository;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    // inputs from API is passed here -> userName, incrementBy
    public List<LeaderBoardEntryDTO> incrementScore(String username, long incrementBy){
        // find the user with the username, if user found return, else create a new user with 0 score
        UserScore userScore = userRepository.findByUsername(username)
                .orElseGet(() -> new UserScore(username, 0L));

        // newScore = user's current score + increment
        long newScore = userScore.getScore() + incrementBy;
        // update score
        userScore.setScore(newScore);
        // save in DB
        userRepository.save(userScore);

        // Create a  Redis sorted set -- ZSET
        ZSetOperations<String, String> zSetOps = stringRedisTemplate.opsForZSet();
        // create a sorted set called leaderboard, insert -> username, incrementBy
        // sorted set value for the username is updated by incrementBy
        // for example - user1, 10 => incrementby 20 => new entry -> user1, 30
        zSetOps.incrementScore(LEADERBOARD_KEY, username, incrementBy);

        /**
         * Instead of using redis, if we would have used SQL
         * Fetch all entries from SQL
         * Sort them by score
         * Choose top 10
         * return


        127.0.0.1:6379> ZREVRANGE leaderboard 0 -1 WITHSCORES
        1) "reva"
        2) "2000"
        3) "pratiksha"
        4) "2000"
        5) "pratik"
        6) "110"
        7) "ritesh"
        8) "100"

         127.0.0.1:6379> ZSCORE leaderboard reva
         "2000


         */

        // 3. Get top 10 from Redis
        Set<ZSetOperations.TypedTuple<String>> top =
                zSetOps.reverseRangeWithScores(LEADERBOARD_KEY, 0, 9); // fetch top 10 items index 0 to index 9

        // convert the redis entry into a DTO object -> UI understandable object
        List<LeaderBoardEntryDTO> result = new ArrayList<>();
        if (top != null) {
            for (ZSetOperations.TypedTuple<String> tuple : top) {
                if (tuple.getValue() != null && tuple.getScore() != null) {
                    result.add(new LeaderBoardEntryDTO(
                            tuple.getValue(),
                            tuple.getScore().longValue()
                    ));
                }
            }
        }
        // return the result
        return result;
    }
}