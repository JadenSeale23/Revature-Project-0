package com.revature;

import com.revature.DAOs.AbilityDAO;
import com.revature.controllers.AbilityController;
import com.revature.controllers.DnDClassController;
import io.javalin.Javalin;

public class LauncherNEW {
    public static void main(String[] args) {

        //typical Javalin setup Syntax
        var app = Javalin.create().start(7000);

        //very basic callable resource
        app.get("/",ctx -> ctx.result("Hello Javalin and Postman!"));

        //instantiate a controller to access Handlers
        DnDClassController dc = new DnDClassController();
        AbilityController ac = new AbilityController();

        //get all DnDClasses
        app.get("/dnd_classes", dc.getDnDClassesHandler); //call to a controller

        //get all Abilities
        app.get("/abilities", ac.getAbilitiesHandler);

        //insert new DnDClass
        app.post("/dnd_classes", dc.insertDnDClassHandler);

        //insert new Ability
        app.post("/abilities", ac.insertAbilityHandler);

        //get DnDClass by ID
        app.get("/dnd_classes/{id}", dc.getDnDClassByIdHandler);

        //get Ability by ID
        app.get("/abilities/{id}", ac.getAbilityByIdHandler);

        //update ability name
        app.patch("/abilities/{id}", ac.updateAbilityNameHandler);

        //update DnD Class variables
        app.patch("/dnd_classes", ctx -> {
            //get the updateType from query parameters
            String updateType = ctx.queryParam("updateType");
            if(updateType == null){
                ctx.status(400);
                ctx.result("Missing query parameter 'updateType'");
            }else {
                switch (updateType) {
                    case "name":
                        //Handle name updates
                        dc.updateDnDClassNameHandler.handle(ctx);
                        break;
                    case "hit_die":
                        //Handle hit die updates
                        dc.updateDnDClassHitDieHandler.handle(ctx);
                        break;
                    case "primary_ability":
                        //Handle primary ability updates
                        dc.updateDnDClassPrimaryAbilityHandler.handle(ctx);
                        break;
                    case "archetype":
                        //Handle archetype updates
                        dc.updateDnDClassArchetypeHandler.handle(ctx);
                        break;
                    default:
                        ctx.status(400);
                        ctx.result("Invalid query parameter value for 'updateType'" + " updateType passed:" + updateType);
                }
            }
        });

        //delete Ability
        app.delete("/abilities/{id}", ac.deleteAbilityHandler);

        //delete DnD Class
        app.delete("/dnd_classes/{id}", dc.deleteDnDClassHandler);

        app.get("/dnd_classes/abilityid/{id}", dc.getDnDClassesByAbilityHandler);
    }
}
