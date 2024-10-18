package com.revature.models;

public class DnD_Class {

    private int class_id;
    private String name;
    private String hit_die;
    private String archetype;
    //foreign keys will usually just be the
    private Ability ability;
    //this will be useful when inserting new dnd_classes
    private int primary_ability;

    public DnD_Class() {
    }
    //no fk field for all-args constructor
    public DnD_Class(int class_id, String name, String hit_die, Ability ability, String archetype) {
        this.class_id = class_id;
        this.name = name;
        this.hit_die = hit_die;
        this.ability = ability;
        this.archetype = archetype;
    }

    //we will use this constructor to insert Employees only
    public DnD_Class(String name, String hit_die, int primary_ability, String archetype) {
        this.name = name;
        this.hit_die = hit_die;
        this.primary_ability = primary_ability;
        this.archetype = archetype;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHit_die() {
        return hit_die;
    }

    public void setHit_die(String hit_die) {
        this.hit_die = hit_die;
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    public int getPrimary_ability() {
        return primary_ability;
    }

    public void setPrimary_ability(int primary_ability) {
        this.primary_ability = primary_ability;
    }

    public String getArchetype() {
        return archetype;
    }

    public void setArchetype(String archetype) {
        this.archetype = archetype;
    }

    @Override
    public String toString() {
        return "DnD_Class{" +
                "class_id=" + class_id +
                ", name='" + name + '\'' +
                ", hit_die='" + hit_die + '\'' +
                ", ability=" + ability +
                ", archetype=" + archetype +
                '}';
    }
}
