package cell;

import core.*;
import ui.models.Settings;
import ui.views.GameWindow;

import java.awt.*;

public class CellManager {

    private final Cell[][] _countries;
    private final Statistics _statistics;
    private final Settings _settings;
    private final GameWindow _window;
    private final Game _game;

    public CellManager(Statistics stats, Settings conf, GameWindow window, Game game) {
        this._game = game;
        this._window = window;
        this._settings = conf;
        this._statistics = stats;
        _countries = new Cell[Game.WIDTH][Game.HEIGHT];

        for (int i = 0; i < Game.HEIGHT; ++i) {
            for (int j = 0; j < Game.WIDTH; ++j) {
                _countries[j][i] = CellFactory.empty();
            }
        }
    }

    public void reset() {
        for (int i = 0; i < Game.HEIGHT; ++i) {
            for (int j = 0; j < Game.WIDTH; ++j) {
                _countries[j][i].setInactive();
            }
        }
    }

    public void setCountry(int x, int y, Color col, String t, int damage, boolean diseased, int age, int reproduction) {
        _countries[x][y].set(col, t, damage, diseased, age, reproduction);
        _game.world_population++;
    }

    public int length() {
        return _countries.length;
    }

    public int length(int x) {
        return _countries[x].length;
    }

    public Cell get(int x, int y) {
        return _countries[x][y];
    }

    public void kill(int x, int y, GameWindow gameWindow, Settings gameSettings, Statistics gameStatistics, boolean addToStats) {
        if (_countries[x][y].diseased() && addToStats) gameStatistics.incDiedToDisease();
        else if (_countries[x][y].age() > gameSettings.MAX_AGE && addToStats) gameStatistics.incDiedToAging();
        _countries[x][y].setInactive();
        gameWindow.setPixel(x, y, gameSettings.GREEN);
        _game.world_population--;
    }

    public void update() {
        for (int i = 0; i < this.length(); ++i) {
            for (int j = 0; j < this.length(i); ++j) {
                Cell currentCell = this.get(i, j);
                currentCell.update(_settings);
                if (this.get(i, j).active()) {
                    if (currentCell.shouldDie(_settings)) {
                        this.kill(i, j, _window, _settings, _statistics, true);
                        continue;
                    } else {
                        _window.setPixel(i, j, currentCell.color().getRGB());
                        _statistics.addToPopulationStatistics(currentCell);
                    }
                    int x = Rand.randomInt(1 - -1 + 1) + -1, y = Rand.randomInt(1 - -1 + 1) + -1;
                    //if the location we're trying to move to is grass
                    if (_window.getPixel(i + x, j + y) == _settings.GREEN) {
                        //if we should reproduce
                        if (currentCell.reproduction() > _settings.REPRODUCTION_THRESHOLD) {
                            if (_game.world_population < _settings.MAX_POPULATION) {
                                //we do
                                currentCell.setReproduction(0);
                                _window.setPixel(i + x, j + y, currentCell.color().getRGB());
                                this.setCountry((i + x), (j + y), currentCell.color(), currentCell.tribe(), currentCell.childDamage(_settings), currentCell.childDiseased(_settings), 0, 0);
                            } else {
                                if (Rand.randomInt(1000) < 5) {
                                    System.out.println("POPULATION MAXED OUT!");
                                    System.out.println();
                                }

                            }
                        } else {
                            //otherwise we just move
                            _window.setPixel(i, j, _settings.GREEN);
                            _window.setPixel(i + x, j + y, currentCell.color().getRGB());
                            this.setCountry((i + x), (j + y), currentCell.color(), currentCell.tribe(), currentCell.damage(), currentCell.childDiseased(_settings), currentCell.age(), currentCell.reproduction());
                            this.kill(i, j, _window, _settings, _statistics, false);
                        }
                    }
                    //if its not green, is it a player? (if not its (presumably) water so we will do nothing)
                    else if (this.get(i + x, j + y).active()) {
                        //is this player nit in our tribe?
                        if (!this.get(i + x, j + y).tribe().equals(currentCell.tribe())) {
                            //fight
                            if (this.get(i + x, j + y).damage() > currentCell.damage() && (Rand.randomInt(100) < _settings.STRONGER_WINS_CHANCE)) {
                                this.kill(i, j, _window, _settings, _statistics, false);
                                _window.setPixel(i, j, _settings.GREEN);
                                _statistics.incDiedToWar();
                            } else {
                                this.setCountry(i + x, j + y, currentCell.color(), currentCell.tribe(), currentCell.damage(), currentCell.diseased(), currentCell.age(), currentCell.reproduction());
                                _game.world_population--;
                                this.kill(i, j, _window, _settings, _statistics, false);
                                _window.setPixel(i, j, _settings.GREEN);
                                _statistics.incDiedToWar();
                            }
                        }
                        //its a player in our tribe
                        else {
                            if (currentCell.diseased()) {
                                if (Rand.randomInt(100) < _settings.DISEASE_INFECTIVITY)
                                    this.get(x + i, j + y).infect();
                            }
                        }
                    }
                }
            }
        }
    }
}
