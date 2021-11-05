package cell;

import core.*;
import ui.models.Settings;

import java.awt.*;

public class Cell {

    private Color _color;
    private boolean _active = false;
    private int _damage;
    private boolean _diseased;
    private int _age;
    private int _reproduction;
    private String _tribe;


    Cell() {
        _age = 0;
        _reproduction = 0;
        _diseased = false;
        _tribe = "";
    }

    public void set(Color color, String tribe, int damage, boolean isDiseased, int age, int reproduction) {
        this._color = color;
        this._tribe = tribe;
        this._age = age;
        this._reproduction = reproduction;
        this._damage = damage;
        this._diseased = isDiseased;
        this._active = true;
    }

    public void update(Settings gc) {
        handleAging(gc);
        attenptCure(gc);
    }

    private void attenptCure(Settings gameSettings) {
        if (Rand.randomInt(100) < gameSettings.DISEASE_CURE_RATE) cure();
    }

    public int childDamage(Settings gameSettings) {
        if (Rand.randomInt(1000) < gameSettings.MUTATION_CHANCE) {
            int temp;
            if (_diseased) {
                temp = ((_damage - gameSettings.DISEASE_DAMAGE_HARM) + Rand.randomInt(gameSettings.MUTATION_AMOUNT) - gameSettings.MUTATION_SUBTRACT);
            } else {
                temp = _damage + (Rand.randomInt(gameSettings.MUTATION_AMOUNT) - gameSettings.MUTATION_SUBTRACT);
            }
            if (temp > 0) return temp;
            return 1;
        }
        if (_diseased && _damage - gameSettings.DISEASE_DAMAGE_HARM > 0)
            return _damage - gameSettings.DISEASE_DAMAGE_HARM;
        else if (_diseased) return 1;

        return _damage;
    }


    private void cure() {
        _diseased = false;
    }

    public void infect() {
        _diseased = true;
    }

    public void handleAging(Settings gameSettings) {
        _reproduction++;
        if (_diseased) {
            _age += gameSettings.DISEASE_MULTIPLIER;
        } else {
            _age++;
        }
    }

    public boolean shouldDie(Settings gameSettings) {
        return _age > gameSettings.MAX_AGE;
    }

    public boolean childDiseased(Settings gameSettings) {
        if (!_diseased) {
            return Rand.randomInt(10000) < gameSettings.DISEASE_SPREAD_RATE;
        }
        return true;
    }

    public Color color() {
        return _color;
    }

    public int damage() {
        return _damage;
    }

    public boolean diseased() {
        return _diseased;
    }

    public boolean active() {
        return _active;
    }

    public int reproduction() {
        return _reproduction;
    }

    public int age() {
        return _age;
    }

    public void setInactive() {
        _active = false;
    }

    public void setReproduction(int reproduction) {
        _reproduction = reproduction;
    }

    public String tribe() {
        return _tribe;
    }
}
