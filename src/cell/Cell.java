package cell;

import core.*;

import java.awt.*;
import java.util.Random;
public class Cell {

    private Color _color;
    private boolean _active = false;
    private int _damage;
    private boolean _diseased;
    private int _age;
    private int _reproduction;
    private String _tribe;
    private int _xBias, _yBias;


    Cell(){
        _age = 0;
        _reproduction = 0;
        _diseased = false;
        _tribe = "";
        _xBias = 0;
        _yBias = 0;
    }
    public void set(Color color, String tribe, int damage, boolean isDiseased, int xbias, int ybias, int age) {
        this._color = color;
        this._tribe = tribe;
        this._age = age;
        this._reproduction = 0;
        this._damage = damage;
        this._diseased = isDiseased;
        this._xBias = xbias;
        this._yBias = ybias;
        this._active = true;
    }

    public int childDamage(Config gameConfig) {
        if (Rand.randomInt(1000) < gameConfig.MUTATION_CHANCE) {
            if (_diseased) {
                int temp = ((_damage - gameConfig.DISEASE_DAMAGE_HARM) + Rand.randomInt(gameConfig.MUTATION_AMOUNT) - gameConfig.MUTATION_SUBTRACT);
                if (temp > 0) return temp;
                return 1;
            } else {
                int temp = _damage + (Rand.randomInt(gameConfig.MUTATION_AMOUNT) - gameConfig.MUTATION_SUBTRACT);
                if(temp > 0)return temp;
                return 1;
            }
        }
        if (_diseased && _damage - gameConfig.DISEASE_DAMAGE_HARM > 0)
            return _damage - gameConfig.DISEASE_DAMAGE_HARM;
        else if(_diseased) return 1;

        return _damage;
    }


    private void cure() {
        _diseased = false;
    }
    public void infect() {
        _diseased = true;
    }
    public int handleAging(Config gameConfig) {
        _reproduction++;
        if (_diseased) {
            if (Rand.randomInt(100) < gameConfig.DISEASE_CURE_RATE) cure();
            _age += gameConfig.DISEASE_MULTIPLIER;
            return 1;
        } else {
            _age++;
            return 0;
        }
    }
    public boolean shouldDie(Config gameConfig) { return _age > gameConfig.MAX_AGE; }
    public boolean childDiseased(Config gameConfig) {
        if (!_diseased) {
            if (Rand.randomInt(10000) < gameConfig.DISEASE_SPREAD_RATE) {
                return true;
            }else{
                return false;
            }
        }
        return true;
    }
    public Color color() { return _color; }
    public int damage() { return _damage; }
    public boolean diseased(){ return _diseased; }
    public boolean active(){
        return _active;
    }
    public int reproduction() { return _reproduction; }
    public int age() { return _age; }
    public void setInactive(){
        _active = false;
    }
    public void setReproduction(int reproduction){
        _reproduction = reproduction;
    }
    public String tribe(){ return _tribe; }
    public int xBias(){ return _xBias; }
    public int yBias(){ return _yBias; }
}
