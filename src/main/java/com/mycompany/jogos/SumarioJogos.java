package com.mycompany.jogos;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author rogerio, carlos
 */
public class SumarioJogos {

    private static class Info {

        public int reviwes;
        public double totalScore;
        public int countMediocre;
        public String bestTitle;
        public String worstTitle;
        public double bestScore;
        public double worstScore;
        public ArrayList<Double> scores = new ArrayList<>();

        public Info(int reviwes, double totalScore, int countMediocre, String bestTitle, String worstTitle, double bestScore, double worstScore) {
            this.reviwes = reviwes;
            this.totalScore = totalScore;
            this.countMediocre = countMediocre;
            this.bestTitle = bestTitle;
            this.worstTitle = worstTitle;
            this.bestScore = bestScore;
            this.worstScore = worstScore;

        }

        public double desvio() {
            double desv;
            double sum = 0;
            for (Double score : scores) {
                desv = score - (totalScore / (double) reviwes);
                sum += Math.pow(desv, 2);
            }
            return Math.sqrt(sum / reviwes);
        }

        @Override
        public String toString() {
            return "Reviews: " + reviwes + ",\nMediocre Reviews %: " + ((double) countMediocre / reviwes * 100)
                    + ",\nAverage Score: " + (totalScore / (double) reviwes)
                    + ",\ndes. pad. pop. scores: " + desvio()
                    + ",\nBest Scored Title: " + bestScore + "p., " + bestTitle
                    + ",\nWorst Scored Title: " + worstScore + "p., " + worstTitle + "\n";
        }

    }

    public static void main(String[] args) {

        Map<String, Info> map = new TreeMap<String, Info>();
        Map<String, Integer> yearAction = new HashMap<>();

        SimpleReader file = new SimpleReader("C:\\Users\\roger\\Documents\\NetBeansProjects\\Jogos\\src\\main\\java\\com\\mycompany\\jogos\\game-reviews.csv");

        String line = file.readLine();
        line = file.readLine();

        while (line != null) {
            String[] col = line.split(";");
            String title = col[0];
            String scorePhrase = col[2];
            double score = Double.parseDouble(col[3]);
            String genre = col[4];
            String year = col[6];

            Info i;
            if (!map.containsKey(year)) { // contagem
                int med = 0;
                if (scorePhrase.equals("Mediocre")) {
                    med++;
                }
                i = new Info(1, score, med, title, title, score, score);
                map.put(year, i);
            } else {
                i = map.get(year);
                i.reviwes++;
                i.totalScore = i.totalScore + score;
                if (scorePhrase.equals("Mediocre")) {
                    i.countMediocre++;
                }
                if (i.bestScore < score) {
                    i.bestScore = score;
                    i.bestTitle = title;
                }
                if (i.worstScore > score) {
                    i.worstScore = score;
                    i.worstTitle = title;
                }
                map.put(year, i);
                if (genre.contains("Action")) {
                    Integer genreCount = yearAction.get(year);
                    if (genreCount == null) {
                        genreCount = 0;
                    }
                    yearAction.put(year, ++genreCount);
                }
            }
            i.scores.add(score);
            line = file.readLine();
        }

        file.close();

        for (String w : map.keySet()) {
            System.out.println(w + ": " + map.get(w));
        }

        Integer maxGenre = null;
        String actionGenre = "";

        for (Map.Entry<String, Integer> genreEntry : yearAction.entrySet()) {
            if (maxGenre == null || maxGenre < genreEntry.getValue()) {
                maxGenre = genreEntry.getValue();
                actionGenre = genreEntry.getKey();
            }
        }

        System.out.println("The year with the most games released under the 'Action' genre is: " + actionGenre + " (" + maxGenre + " games)");
    }
}
