package com.example.tennisscoretracker.match.match_play;

import com.example.tennisscoretracker.match.match_team.TennisTeam;

public class TennisMatch {

    private static final int FORMAT_SINGLES = 1;
    private static final int FORMAT_DOUBLES = 2;

    private int format;

    private TennisTeam tennisTeam1;
    private TennisTeam tennisTeam2;

    private int bestOfSets;

    private int setsWon_Team1;
    private int setsWon_Team2;

    private int currentSetNumber;

    private TennisSet currentTennisSet;

    //TODO: implement methods to update the state of our TennisMatch

}
