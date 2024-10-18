package com.revature.models;

public class Ability {

    private int ability_id;
    private String ability_name;

    public Ability() {
    }

    public Ability(int ability_id, String ability_name) {
        this.ability_id = ability_id;
        this.ability_name = ability_name;
    }

    public Ability(String ability_name) {
        this.ability_name = ability_name;
    }



    public int getAbility_id() {
        return ability_id;
    }

    public void setAbility_id(int ability_id) {
        this.ability_id = ability_id;
    }

    public String getAbility_name() {
        return ability_name;
    }

    public void setAbility_name(String ability_name) {
        this.ability_name = ability_name;
    }

    @Override
    public String toString() {
        return "Ability{" +
                "ability_id=" + ability_id +
                ", ability_name='" + ability_name + "'" +
                "}";
    }
}
