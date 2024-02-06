package me.nateweisz.protocol;

/*
 * Manages data about the ages of survey respondents
 */
public class MusicSurvey {

    private final int[] ages;    // The ages of respondents to the survey

    /*
     * Initializes ages to the parameter ages
     */
    public MusicSurvey(int[] ages) {
        this.ages = ages;
    }

    /*
     * Returns a String containing the number of respondents in the 1D array ages
     */
    public String toString() {
        return "Number of Respondents: " + ages.length;
    }
}