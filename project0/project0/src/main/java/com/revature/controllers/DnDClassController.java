package com.revature.controllers;

import com.revature.DAOs.AbilityDAO;
import com.revature.DAOs.DnDClassDAO;
import com.revature.models.Ability;
import com.revature.models.DnD_Class;
import io.javalin.http.Handler;

import java.lang.reflect.Array;
import java.util.ArrayList;

//controller layer is where HTTP requests get sent after Javalin directs them from main()
//JSON comes in and gets translated to Java and vise versa
//Take in HTTP requests and sending back HTTP Responses
public class DnDClassController {

    //We need a DnDClassDAO to use JDBC methods
    DnDClassDAO dDAO = new DnDClassDAO();

    //this handler will handle GET requests to get all DnDClasses
    public Handler getDnDClassesHandler = ctx -> {

        //populate an ArrayList of DnDClasses
        ArrayList<DnD_Class> dndclass = dDAO.getAllDnDClasses();

        //HTTP request is expecting a JSON instead of Java so we have to translate it into a JSON
        ctx.json(dndclass);

        //set the status code
        ctx.status(200); //OK

    };

    //This Handler will handle POST requests to insert a new DnD Class
    public Handler insertDnDClassHandler = ctx -> {

        //We have JSON coming in (Sending a DnDClass through Postman)
        //convert JSON int Java object before we can send it to the DB
        DnD_Class newClass = ctx.bodyAsClass(DnD_Class.class);
        //initialize a new ArrayList to hold all class names
        ArrayList<String> classNames = new ArrayList<>();
        //fill classNames with all the class names in the database
        dDAO.getAllDnDClasses().forEach(dclass -> classNames.add(dclass.getName()));
        AbilityDAO aDAO = new AbilityDAO();
        ArrayList<Ability> abilities = aDAO.getAllAbilities();
        //show off error handling make sure there is a name and hitdie
        if(newClass.getName() == null || newClass.getName().isBlank()){
            ctx.result("name is required!");
            ctx.status(400); //Bad Request
        }
        else if(newClass.getHit_die() == null || newClass.getHit_die().isBlank()) {
            ctx.result("hit_die is required!");
            ctx.status(400); //Bad Request
        }
        else if(newClass.getArchetype() == null || newClass.getArchetype().isBlank()) {
            ctx.result("archetype is required!");
            ctx.status(400); //Bad Request
        }else if (classNames.contains(newClass.getName())) {//checks to see if user supplied a duplicate name
            ctx.result("DnD Class name must be unique!");
            ctx.status(400); //Bad result
        } else if (aDAO.getAbilityById(newClass.getPrimary_ability()) == null) {
            ctx.result("No matching ability found for id:" + newClass.getPrimary_ability());
            ctx.status(404);
        } else {
            //then the inputted DnDClass is good!
            DnD_Class insertedClass = dDAO.insertDnD_Class(newClass);
            insertedClass.setAbility(aDAO.getAbilityById(insertedClass.getPrimary_ability()));
            ctx.status(201); //Created
            ctx.json(insertedClass); //Send DnDClass back to Client
        }
    };

    public Handler getDnDClassByIdHandler = ctx ->{
        int dndClass_id = Integer.parseInt(ctx.pathParam("id"));

        DnD_Class dndClass = dDAO.getClassByID(dndClass_id);
        if(dndClass_id <= 0){
            ctx.result("id must be greater than 0");
            ctx.status(400); //Bad result
        }else if(dndClass == null){
            ctx.result("DnD Class at id:" + dndClass_id + " was not found.");
            ctx.status(404); //Not Found
        }else {
            ctx.json(dndClass);
            ctx.status(200); //OK
        }
    };

    public Handler deleteDnDClassHandler = ctx -> {
        int class_id = Integer.parseInt(ctx.pathParam("id"));
        DnD_Class deletedClass = dDAO.getClassByID(class_id);

        if(class_id <= 0){
            ctx.result("id must be greater than 0");
            ctx.status(400); //Bad result
        }else if(deletedClass == null){
            ctx.result("DnD Class at id:" + class_id + " was not found.");
            ctx.status(404); //Not Found
        }else {
            dDAO.deleteDnDClass(class_id);
            ctx.result("Deleted " + deletedClass);
            ctx.status(200);
        }
    };

    public Handler updateDnDClassNameHandler = ctx -> {
        //id in query parameter
        String id = ctx.queryParam("id");
        //newName in request body
        String newName = ctx.body();
        //initialize a new ArrayList to hold all class names
        ArrayList<String> classNames = new ArrayList<>();
        //fill classNames with all the class names in the database
        dDAO.getAllDnDClasses().forEach(dclass -> classNames.add(dclass.getName()));
        if(id == null){//checks if user supplied an id in query parameter
            ctx.status(400); //Bad Result
            ctx.result("Missing query parameter 'id'");
        }else {//class_id is not null
            int class_id = Integer.parseInt(id);
            DnD_Class dnDClass = dDAO.getClassByID(class_id);
            //Error Handling
            if (class_id <= 0) {// checks if user is trying to update index 0 or negative index
                ctx.result("id must be greater than 0");
                ctx.status(400); //Bad result
            }else if (dnDClass == null) {//checks if user is trying to update a nonexistent DnDClass
                ctx.result("No DnD Class found at id: " + class_id);
                ctx.status(404); //Not Found
            }else if (newName.isBlank()) {//checks to see if user didn't supply a newName
                ctx.result("DnD Class name is required!");
                ctx.status(400);
            }else if (classNames.contains(newName)) {//checks to see if user supplied a duplicate name
                ctx.result("DnD Class name must be unique!");
                ctx.status(400); //Bad result
            }else {//user supplied a satisfactory dnd class id and new name, update class name
                String newClassName = dDAO.updateClassName(class_id, newName);
                ctx.result("DnD Class at id:" + class_id + " name updated to " + newClassName);
                ctx.status(200);
            }
        }
    };

    public Handler updateDnDClassHitDieHandler = ctx -> {
        //id in query parameter
        String id = ctx.queryParam("id");
        //newHitDie in request body
        String newHitDie = ctx.body();
        if(id == null){//checks if id supplied was nonexistent
            ctx.status(400); //Bad Result
            ctx.result("Missing query parameter 'id'");
        }else {//class_id is not null
            int class_id = Integer.parseInt(id);
            DnD_Class dnDClass = dDAO.getClassByID(class_id);
            //Error Handling
            if (class_id <= 0) {//checks if user is trying to update a negative id or id 0
                ctx.result("id must be greater than 0");
                ctx.status(400); //Bad result
            } else if (dnDClass == null) {//checks if user is trying to update a nonexistent id
                ctx.result("DnD Class at id:" + class_id + " was not found.");
                ctx.status(404); //Not Found
            } else if (newHitDie.isBlank()) {//checks if user supplied an empty hit die
                ctx.result("Hit die is required!");
                ctx.status(400);
            } else {//user supplied a satisfactory dnd class id and hit die, update hit die
                String newClassHitDie = dDAO.updateHitDie(class_id, newHitDie);
                ctx.result("DnD Class at id:" + class_id + " hit die has been updated to " + newClassHitDie);
                ctx.status(200);
            }
        }
    };

    public Handler updateDnDClassPrimaryAbilityHandler = ctx -> {
        //id in query parameter
        String id = ctx.queryParam("id");
        //newPrimaryAbility in request body
        int newPrimaryAbility = Integer.parseInt(ctx.body());
        AbilityDAO aDAO = new AbilityDAO();
        ArrayList<Ability> abilities = aDAO.getAllAbilities();
        if(id == null){//checks if id was supplied
            ctx.status(400); //Bad Result
            ctx.result("Missing query parameter 'id'");
        }else {//class_id is not null
            int class_id = Integer.parseInt(id);
            DnD_Class dnDClass = dDAO.getClassByID(class_id);
            //Error Handling
            if (class_id <= 0) {//checks if user is trying to update a negative id or id 0
                ctx.result("id must be greater than 0");
                ctx.status(400); //Bad result
            }else if (dnDClass == null) {//checks if user is trying to update a nonexistent id
                ctx.result("DnD Class at id:" + class_id + " not found.");
                ctx.status(404); //Not Found
            }else if (aDAO.getAbilityById(newPrimaryAbility) == null) {//checks if user supplied a correct id for primaryAbility
                ctx.result("No matching ability found for id:" + class_id);
                ctx.status(404);//Not Found
            }else {//user supplied a satisfactory dnd class id and ability id for primary id, update primary ability
                Ability newClassAbility = dDAO.updateAbility(class_id, newPrimaryAbility);
                ctx.result("DnD Class at id:" + class_id + " primary ability was updated to " + newClassAbility);
                ctx.status(200);
            }
        }
    };

    public Handler updateDnDClassArchetypeHandler = ctx -> {
        //id in query parameter
        String id = ctx.queryParam("id");
        //newHitDie in request body
        String newArchetype = ctx.body();
        if(id == null){//checks if user supplied an id
            ctx.status(400); //Bad Result
            ctx.result("Missing query parameter 'id'");
        }else{//class_id is not null
            int class_id = Integer.parseInt(id);
            DnD_Class dnDClass = dDAO.getClassByID(class_id);
            //Error Handling
            if (class_id <= 0) {
                ctx.result("id must be greater than 0");
                ctx.status(400); //Bad result
            }else if(dnDClass == null) {
                ctx.result("DnD Class at id:" + class_id + " was not found.");
                ctx.status(404); //Not Found
            }else if(newArchetype.isBlank()) {
                ctx.result("Archetype is required!");
                ctx.status(400);
            }else{//user supplied a satisfactory dnd class id and archetype, update archetype
                String newClassArchetype = dDAO.updateArchetype(class_id, newArchetype);
                ctx.result("DnD Class at id:" + class_id + " archetype was updated to " + newClassArchetype);
                ctx.status(200);
            }
        }
    };

    public Handler getDnDClassesByAbilityHandler = ctx -> {
        //ability id in the path parameter
        int ability_id = Integer.parseInt(ctx.pathParam("id"));
        ArrayList<DnD_Class> classes = dDAO.getAllClassesByAbilityID(ability_id);
        AbilityDAO aDAO = new AbilityDAO();
        if(ability_id <= 0){
            ctx.result("Ability id is required");
            ctx.status(400);
        }else if(aDAO.getAbilityById(ability_id) == null){
            ctx.result("Ability at id:" + ability_id + " was not found");
            ctx.status(404);
        }else if(classes.isEmpty()){
            ctx.result("There are no DnD Classes whose primary ability is " + aDAO.getAbilityById(ability_id).getAbility_name());
            ctx.status(200);
        }
        else {
            ctx.json(classes);
            ctx.status(200);
        }
    };

}
