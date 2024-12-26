/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.game.state;

/**
 *
 * @author farah
 */
import java.util.Observable;

public class Score extends Observable {

    private int score = 0;

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        score++;
        setChanged();
        notifyObservers();
    }
}