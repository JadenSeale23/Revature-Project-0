package com.revature.controllers;

import com.revature.DAOs.AbilityDAO;
import com.revature.models.Ability;
import com.revature.models.DnD_Class;
import io.javalin.http.Handler;

import java.util.ArrayList;

public class AbilityController {
    AbilityDAO aDAO = new AbilityDAO();

    //this Handler will handle GET requests to /abilities
    public Handler getAbilityByIdHandler = ctx -> {
        int ability_id = Integer.parseInt(ctx.pathParam("id"));

        Ability ability = aDAO.getAbilityById(ability_id);
        if(ability_id <= 0){
            ctx.result("id must be greater than 0");
            ctx.status(400); //Bad result
        }else if(ability == null){
            ctx.result("Ability at id:" + ability_id + " was not found.");
            ctx.status(404); //Not Found
        }else {
            //send the Ability back
            ctx.json(ability);
            ctx.status(200); //OK
        }
    };

    public Handler updateAbilityNameHandler = ctx -> {
        //ability ID in path param
        //new name in req body
        int ability_id = Integer.parseInt(ctx.pathParam("id"));
        String newName = ctx.body();
        Ability ability = aDAO.getAbilityById(ability_id);
        ArrayList<String> abilityNames = new ArrayList<>();
        aDAO.getAllAbilities().forEach(abilitya -> abilityNames.add(abilitya.getAbility_name()));
        if(ability_id <= 0){
            ctx.result("id must be greater than 0");
            ctx.status(400); //Bad result
        }else if(ability == null){
            ctx.result("Ability at id:" + ability_id + " was not found.");
            ctx.status(404); //Not Found
        }else if(newName.isBlank()){
            ctx.result("Ability name is required!");
            ctx.status(400);
        }else if(abilityNames.contains(newName)){
            ctx.result("Ability name must be unique!");
            ctx.status(400);
        }
        else {
            String newAbilityName = aDAO.updateAbilityName(ability_id, newName);
            ctx.result("Ability ID " + ability_id + " name updated to " + newAbilityName);
            ctx.status(200);
        }
    };

    //this handler will handle GET requests to get all DnDClasses
    public Handler getAbilitiesHandler = ctx -> {

        //populate an ArrayList of Abilities
        ArrayList<Ability> abilities = aDAO.getAllAbilities();

        //HTTP request is expecting a JSON instead of Java so we have to translate it into a JSON
        ctx.json(abilities);

        //set the status code
        ctx.status(200); //OK
    };

    //This Handler will handle POST requests to insert a new DnD Class
    public Handler insertAbilityHandler = ctx -> {
        //We have JSON coming in (Sending a DnDClass through Postman)
        //convert JSON int Java object before we can send it to the DB
        Ability newAbility = ctx.bodyAsClass(Ability.class);
        ArrayList<String> abilityNames = new ArrayList<>();
        aDAO.getAllAbilities().forEach(ability1 -> abilityNames.add(ability1.getAbility_name()));
        //show off error handling make sure there is a name
        if(newAbility.getAbility_name() == null || newAbility.getAbility_name().isBlank()){
            ctx.result("Ability name is required!");
            ctx.status(400); //Bad Request
        }else if(abilityNames.contains(newAbility.getAbility_name())){
            ctx.result("ability_name must be unique!");
            ctx.status(400);
        }
        else {
            //then the inputted Ability is good!
            Ability insertedAbility = aDAO.insertAbility(newAbility);
            ctx.status(201); //Created
            ctx.json(insertedAbility); //Send DnDClass back to Client
        }
    };

    public Handler deleteAbilityHandler = ctx -> {
        int ability_id = Integer.parseInt(ctx.pathParam("id"));
        Ability deletedAbility = aDAO.getAbilityById(ability_id);

        if(ability_id <= 0){
            ctx.result("id must be greater than 0");
            ctx.status(400); //Bad result
        }else if(deletedAbility == null){
            ctx.result("Ability at id:" + ability_id + " was not found.");
            ctx.status(404); //Not Found
        }else {
            aDAO.deleteAbility(ability_id);
            ctx.result("Deleted " + deletedAbility);
            ctx.status(200);
        }

    };




}



