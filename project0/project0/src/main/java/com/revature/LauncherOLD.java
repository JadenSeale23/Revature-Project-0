package com.revature;

import com.revature.DAOs.*;
import com.revature.controllers.DnDClassController;
import com.revature.models.*;
import com.revature.utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class LauncherOLD {
    public static void main(String[] args) {
        //this is a try with resources block A resource is opened up within the () of the try block
        //If the resource successfully creates the rest of the try block will run,
        //benefit: the resource will close after the try block finishes
        try(Connection conn = ConnectionUtil.getConnection()){
            System.out.println("CONNECTION SUCCESSFUL!");
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("CONNECTION FAILED!");
        }


        //Test Ability
        Ability str = new Ability(1, "Strength");
        System.out.println("I made this strength: " + str);

        //Test DnD_Class
        DnD_Class artificer = new DnD_Class(1,"Artificer","d8",str, "Half-caster");
        System.out.println("I made this Artificer: " + artificer);
        System.out.println("The artificer's primary ability is: " + artificer.getPrimary_ability());
        //Testing DAO methods
        AbilityDAO aDAO = new AbilityDAO();
        DnDClassDAO dDAO = new DnDClassDAO();
        str = aDAO.getAbilityById(1);
        System.out.println("This is the Strength in the database: " + str);
        //System.out.println(aDAO.updateAbilityName(1,"Dummy Name")); works!
        //DnD_Class monk = new DnD_Class("Monk", "d8", 2, "Martial");
        //System.out.println("I have inserted this to the database: " + dDAO.insertDnD_Class(monk)); works!
        System.out.println(dDAO.getAllDnDClasses());
//        System.out.println("The DnD Class at id: 8 is: " + dDAO.getClassByID(8));
//        System.out.println("The Paladin Class's new primary ability is " + dDAO.updateAbility(8, 1));
//        System.out.println("Reverting Paladin change, Paladin's primary ability is now " + dDAO.updateAbility(8,6));
//        System.out.println("New Archetype: " + dDAO.updateArchetype(8,"dummy") + " New Class Name " + dDAO.updateClassName(8,"dummy") + " New Hit Die " + dDAO.updateHitDie(8,"d1"));
//        System.out.println(dDAO.getClassByID(8));
//        System.out.println(dDAO.updateArchetype(8, "Half-caster") + dDAO.updateHitDie(8, "d10") + dDAO.updateClassName(8,"Paladin"));
//        System.out.println(dDAO.getClassByID(8));
        //DnD_Class test = new DnD_Class("test","d1",1, "test");
        //dDAO.insertDnD_Class(test);
        //System.out.println(dDAO.deleteDnDClass(13));
        System.out.println(dDAO.getAllClassesByAbilityID(6));
        System.out.println(aDAO.getAllAbilities());
        //System.out.println(aDAO.deleteAbility(7));


    }
}
