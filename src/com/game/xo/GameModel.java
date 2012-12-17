package com.game.xo;

import java.util.*;
import java.util.Random;

public class GameModel {
    public enum Winner {WIN_X, WIN_O, WIN_NONE, DEAD_HEAT};
    public enum Cell {CellX, CellO, CellNone};
    
    static final int xo_size = 3;
    static final int cells_to_win = 3;
    Cell xo_matrix[][] = new Cell[xo_size][xo_size];
    int movesCount = 0;

    public void fillArray() {
        for (int i = 0; i < this.xo_size; i++)
            for (int j = 0; j < this.xo_size; j++)
                this.xo_matrix[i][j] = Cell.CellNone;
    }

    public boolean checkDeadHeat() {
        for (int i = 0; i < this.xo_size; i++)
            for (int j = 0; j < this.xo_size; j++)
                if (this.xo_matrix[i][j] == Cell.CellNone) return false;
        return true;
    }

    public Winner checkWinnings() {
        int dlx_cnt = 0, drx_cnt = 0, dlo_cnt = 0, dro_cnt = 0;

        for (int i = 0; i < this.xo_size; i++) {
            int x_cnt = 0, o_cnt = 0, vx_cnt = 0, vo_cnt = 0;

            for (int j = 0; j < this.xo_size; j++) {
                if (xo_matrix[i][j] == Cell.CellX) x_cnt++;
                if (xo_matrix[i][j] == Cell.CellO) o_cnt++;
                if (xo_matrix[j][i] == Cell.CellX) vx_cnt++;
                if (xo_matrix[j][i] == Cell.CellO) vo_cnt++;
            }

            if (x_cnt == cells_to_win || vx_cnt == cells_to_win) return Winner.WIN_X;
            if (o_cnt == cells_to_win || vo_cnt == cells_to_win) return Winner.WIN_O;

            if (xo_matrix[i][i] == Cell.CellX) dlx_cnt++;
            if (xo_matrix[i][i] == Cell.CellO) dlo_cnt++;
            if (xo_matrix[i][this.xo_size - i - 1] == Cell.CellX) drx_cnt++;
            if (xo_matrix[i][this.xo_size - i - 1] == Cell.CellO) dro_cnt++;
        }
        if (dlx_cnt == cells_to_win || drx_cnt == cells_to_win) return Winner.WIN_X;
        if (dlo_cnt == cells_to_win || dro_cnt == cells_to_win) return Winner.WIN_O;

        return Winner.WIN_NONE;
    }
    
    public String oneLineMatrix() {
        String result = "";
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (xo_matrix[i][j] == Cell.CellX) result += "X";
                else if (xo_matrix[i][j] == Cell.CellO) result += "O";
                else result += " ";
            }
        }
        return result;
    }

    public boolean makePlayerMove(int x, int y) {
        if (xo_matrix[x][y] != Cell.CellNone) {
            return false;
        }

        this.xo_matrix[x][y] = Cell.CellX;
        movesCount++;
        if (this.checkWinnings() != Winner.WIN_NONE) return true;

        return this.makeOpponentMove();
    }

    boolean checkPreWinningSituation() {
        int dlx_cnt = 0, dlo_cnt = 0, drx_cnt = 0, dro_cnt = 0;
        int dl_pos = -1, dr_pos = -1;

        for (int i = 0; i < 3; i++) {
            int x_cnt = 0, o_cnt = 0, n_pos = -1, xv_cnt = 0, ov_cnt = 0, nv_pos = -1;
            for (int j = 0; j < 3; j++) {
                if (xo_matrix[i][j] == Cell.CellX) x_cnt++;
                else if (xo_matrix[i][j] == Cell.CellO) o_cnt++;
                else n_pos = j;

                if (xo_matrix[j][i] == Cell.CellX) xv_cnt++;
                else if (xo_matrix[j][i] == Cell.CellO) ov_cnt++;
                else nv_pos = j;
            }

            if ((x_cnt == 2 && o_cnt == 0) || (x_cnt == 0 && o_cnt == 2)) {
                xo_matrix[i][n_pos] = Cell.CellO;
                movesCount++;
                return true;
            }

            if ((xv_cnt == 2 && ov_cnt == 0) || (xv_cnt == 0 && ov_cnt == 2)) {
                xo_matrix[nv_pos][i] = Cell.CellO;
                movesCount++;
                return true;
            }

            if (xo_matrix[i][i] == Cell.CellX) dlx_cnt++;
            else if (xo_matrix[i][i] == Cell.CellO) dlo_cnt++;
            else dl_pos = i;

            if (xo_matrix[i][2-i] == Cell.CellX) drx_cnt++;
            else if (xo_matrix[i][2-i] == Cell.CellO) dro_cnt++;
            else dr_pos = i;
        }

        if ((dlx_cnt == 2 && dlo_cnt == 0) || (dlx_cnt == 0 && dlo_cnt == 2)) {
            xo_matrix[dl_pos][dl_pos] = Cell.CellO;
            movesCount++;
            return true;
        }

        if ((drx_cnt == 2 && dro_cnt == 0) || (drx_cnt == 0 && dro_cnt == 2)) {
            xo_matrix[dr_pos][dr_pos] = Cell.CellO;
            movesCount++;
            return true;
        }

        return false;
    }

    boolean makeOpponentMove() {
        if (movesCount == 1 && xo_matrix[1][1] == Cell.CellNone) {
            xo_matrix[1][1] = Cell.CellO;
            movesCount++;
            return true;
        }

        if (checkDeadHeat()) return false;
        if (checkPreWinningSituation()) return true;

        if (movesCount == 3 && xo_matrix[1][1] == Cell.CellO) {
            if (xo_matrix[0][0] == Cell.CellX) xo_matrix[2][2] = Cell.CellO;
            else if (xo_matrix[2][2] == Cell.CellX) xo_matrix[0][0] = Cell.CellO;
            else if (xo_matrix[0][2] == Cell.CellX) xo_matrix[2][0] = Cell.CellO;
            else if (xo_matrix[2][0] == Cell.CellX) xo_matrix[0][2] = Cell.CellO;
            movesCount++;
            return true;
        }

        if (xo_matrix[0][0] == Cell.CellNone) {
            xo_matrix[0][0] = Cell.CellO;
            movesCount++;
            return true;
        }
        if (xo_matrix[0][2] == Cell.CellNone) {
            xo_matrix[0][2] = Cell.CellO;
            movesCount++;
            return true;
        }
        if (xo_matrix[2][0] == Cell.CellNone) {
            xo_matrix[2][0] = Cell.CellO;
            movesCount++;
            return true;
        }
        if (xo_matrix[2][2] == Cell.CellNone) {
            xo_matrix[2][2] = Cell.CellO;
            movesCount++;
            return true;
        }

        while (true) {
            int n = (int)(Math.random() * 9);
            if (xo_matrix[n/3][n%3] == Cell.CellNone) {
                xo_matrix[n/3][n%3] = Cell.CellO;
                movesCount++;
                return true;
            }
        }
    }
}
