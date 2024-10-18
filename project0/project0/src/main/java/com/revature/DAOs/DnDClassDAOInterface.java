package com.revature.DAOs;

import com.revature.models.*;

import java.util.ArrayList;

public interface DnDClassDAOInterface {

    DnD_Class insertDnD_Class(DnD_Class dndclass);

    ArrayList<DnD_Class> getAllDnDClasses();

    DnD_Class getClassByID(int id);

    ArrayList<DnD_Class> getAllClassesByAbilityID(int abilityID);

    String updateClassName(int id, String newName);

    String updateHitDie(int id, String newHitDie);

    String updateArchetype(int id, String newArchetype);

    Ability updateAbility(int id, int abilityID);

    DnD_Class deleteDnDClass(int id);

}
