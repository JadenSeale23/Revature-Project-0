package com.revature.DAOs;

import com.revature.models.Ability;
import com.revature.models.DnD_Class;

import java.util.ArrayList;

//This interface will lay out all the methods roleDAO must implement
public interface AbilityDAOInterface {

    //this method will get Abilities by their id
    Ability getAbilityById(int id);

    //this method will update an Ability's name
    String updateAbilityName(int id, String newName);

    //this method will get all Abilities
    public ArrayList<Ability> getAllAbilities();

    //this method will insert a new Ability
    Ability insertAbility(Ability ability);

    Ability deleteAbility(int id);




}
