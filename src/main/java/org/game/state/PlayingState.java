/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.game.state;

import org.game.Main;

/**
 *
 * @author farah
 */

public class PlayingState implements GameState {

    @Override
    public void handleState(Main game) {
        // Logique du jeu en cours
        game.updateGame();
    }
}
