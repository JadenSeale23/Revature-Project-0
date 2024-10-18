package com.revature.DAOs;

import com.revature.models.DnD_Class;
import com.revature.models.Ability;
import com.revature.utils.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;

public class DnDClassDAO implements DnDClassDAOInterface {
    AbilityDAO aDAO = new AbilityDAO();

    @Override
    public DnD_Class insertDnD_Class(DnD_Class dndclass) {
        //try to connect to the database
        try(Connection conn = ConnectionUtil.getConnection()){

            //create SQL statement
            String sql = "INSERT INTO dnd_classes(name,hit_die,primary_ability,archetype) " +
                    "VALUES (?,?,?,?);";

            //use PreparedStatement to fill in the values of our variables
            PreparedStatement ps = conn.prepareStatement(sql);

            //use set() to fill in values
            ps.setString(1, dndclass.getName());
            ps.setString(2, dndclass.getHit_die());
            ps.setInt(3, dndclass.getPrimary_ability());
            ps.setString(4, dndclass.getArchetype());

            //Send the SQL command to the DB
            //execute update is used for inserts, updates, and deletes
            ps.executeUpdate();

            String idsql = "SELECT class_id FROM dnd_classes WHERE name = '" + dndclass.getName() +"'";
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(idsql);
            rs.next();
            dndclass.setClass_id(rs.getInt("class_id"));
            //return the new DnD_Class object
            return dndclass;

        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Couldn't insert DnD Class");
        }
        return null;
    }

    @Override
    public ArrayList<DnD_Class> getAllDnDClasses() {

        //
        try(Connection conn = ConnectionUtil.getConnection()){

            //create SQL statement
            String sql = "SELECT * FROM dnd_classes ORDER BY class_id";

            //create the statement obj since we have no ? wildcards in SQL statement
            Statement s = conn.createStatement();

            //execute query
            ResultSet rs = s.executeQuery(sql);

            //Create ArrayList to store our DnDClasses
            ArrayList<DnD_Class> dnDClasses = new ArrayList<>();


            //loop through ResultSet
            //rs.next will return false when there are no more rows in the ResultSet
            while(rs.next()){
                //For every DnD_Class found, add it to the ArrayList
                //instantiate a new DnD_Class for each row in the ResultSet
                //we can get each piece of DnD_Class Data with rs.get methods
                DnD_Class dndClass = new DnD_Class(
                  rs.getInt("class_id"),
                  rs.getString("name"),
                  rs.getString("hit_die"),
                  null,//TODO: we can use our getAbilityByID method to fill this Ability object
                  rs.getString("archetype")
                );
                //gets the primary ability through rDAO.getRoleByID, then sets the role in our DnD_Class object
                Ability primaryAbility = aDAO.getAbilityById(rs.getInt("primary_ability"));
                dndClass.setAbility(primaryAbility);
                dndClass.setPrimary_ability(primaryAbility.getAbility_id());

                dnDClasses.add(dndClass);
            }
            return dnDClasses;


        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("couldn't get all DnD Classes");
        }
        return null;


    }

    @Override
    public DnD_Class getClassByID(int id) {
        try(Connection conn = ConnectionUtil.getConnection()){
            //represents our SQL query
            //? = wildcard variable to fill in
            String sql = "SELECT * FROM dnd_classes WHERE class_id = ?";

            //PreparedStatement to fill in variable (id)
            PreparedStatement ps = conn.prepareStatement(sql);

            //use id param to set the wildcard variable, parameterIndex = index of ?
            ps.setInt(1,id);

            //Execute the query, save the result in resultSet
            //executeQuery is used with inserts
            ResultSet rs = ps.executeQuery();
            //if there is a value in the next index of ResultSet
            if(rs.next()){
                //Then extract the data into Role object
                DnD_Class dndClass = new DnD_Class(
                        rs.getInt("class_id"),
                        rs.getString("name"),
                        rs.getString("hit_die"),
                        null,
                        rs.getString("archetype")
                );
                Ability ability = aDAO.getAbilityById(rs.getInt("primary_ability"));
                dndClass.setAbility(ability);
                //Return the Role
                return dndClass;
            }

        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("couldn't get Class by ID");
        }
        //if the SQL query never gets injected we will return null so we can still have a return for this method
        return null;
    }

    @Override
    public ArrayList<DnD_Class> getAllClassesByAbilityID(int abilityID) {
        try(Connection conn = ConnectionUtil.getConnection()){

            //create SQL statement
            String sql = "SELECT * FROM dnd_classes WHERE primary_ability = ?";


            //PreparedStatement to fill in variable (id)
            PreparedStatement ps = conn.prepareStatement(sql);

            //use id param to set the wildcard variable, parameterIndex = index of ?
            ps.setInt(1,abilityID);

            //Execute the query, save the result in resultSet
            //executeQuery is used with inserts
            ResultSet rs = ps.executeQuery();

            //Create ArrayList to store our DnDClasses
            ArrayList<DnD_Class> dnDClasses = new ArrayList<>();


            //loop through ResultSet
            //rs.next will return false when there are no more rows in the ResultSet
            while(rs.next()){
                //For every DnD_Class found, add it to the ArrayList
                //instantiate a new DnD_Class for each row in the ResultSet
                //we can get each piece of DnD_Class Data with rs.get methods
                DnD_Class dndClass = new DnD_Class(
                        rs.getInt("class_id"),
                        rs.getString("name"),
                        rs.getString("hit_die"),
                        null,//TODO: we can use our getAbilityByID method to fill this Ability object
                        rs.getString("archetype")
                );
                //gets the primary ability through rDAO.getRoleByID, then sets the role in our DnD_Class object
                Ability primaryAbility = aDAO.getAbilityById(rs.getInt("primary_ability"));
                dndClass.setAbility(primaryAbility);
                dndClass.setPrimary_ability(primaryAbility.getAbility_id());

                dnDClasses.add(dndClass);
            }
            return dnDClasses;


        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("couldn't get all DnD Classes");
        }
        return null;
    }

    @Override
    public String updateClassName(int id, String newName) {
        try(Connection conn = ConnectionUtil.getConnection()){
            //sql statement
            String sql = "UPDATE dnd_classes SET name = ? WHERE class_id = ?";

            //Create a PreparedStatement to fill in the variables
            PreparedStatement ps = conn.prepareStatement(sql);

            //set the variable values in the sql statement
            ps.setString(1,newName);
            ps.setInt(2, id);

            //execute the update
            ps.executeUpdate();

            //return new archetype
            return newName;
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("couldn't update class name");
        }
        //catch all if the update fails, we'll return 0
        return null;
    }

    @Override
    public String updateHitDie(int id, String newHitDie) {
        try(Connection conn = ConnectionUtil.getConnection()){
            //sql statement
            String sql = "UPDATE dnd_classes SET hit_die = ? WHERE class_id = ?";

            //Create a PreparedStatement to fill in the variables
            PreparedStatement ps = conn.prepareStatement(sql);

            //set the variable values in the sql statement
            ps.setString(1,newHitDie);
            ps.setInt(2, id);

            //execute the update
            ps.executeUpdate();

            //return new archetype
            return newHitDie;
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("couldn't update hit die");
        }
        //catch all if the update fails, we'll return 0
        return null;
    }

    @Override
    public String updateArchetype(int id, String newArchetype) {
        //try to open a connection to the DB
        try(Connection conn = ConnectionUtil.getConnection()){
            //sql statement
            String sql = "UPDATE dnd_classes SET archetype = ? WHERE class_id = ?";

            //Create a PreparedStatement to fill in the variables
            PreparedStatement ps = conn.prepareStatement(sql);

            //set the variable values in the sql statement
            ps.setString(1,newArchetype);
            ps.setInt(2, id);

            //execute the update
            ps.executeUpdate();

            //return new archetype
            return newArchetype;
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("couldn't update archetype");
        }
        //catch all if the update fails, we'll return 0
        return null;

    }

    @Override
    public Ability updateAbility(int id, int newAbilityID) {
        //try to open a connection to the DB
        try(Connection conn = ConnectionUtil.getConnection()){
            //sql statement
            String sql = "UPDATE dnd_classes SET primary_ability = ? WHERE class_id = ?";

            //Create a PreparedStatement to fill in the variables
            PreparedStatement ps = conn.prepareStatement(sql);

            //set the variable values in the sql statement
            ps.setInt(1,newAbilityID);
            ps.setInt(2, id);

            //execute the update
            ps.executeUpdate();

            //return new ability
            return aDAO.getAbilityById(newAbilityID);
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Couldn't update ability.");
        }
        //catch all if the update fails, we'll return 0
        return null;

    }

    @Override
    public DnD_Class deleteDnDClass(int id) {
        try(Connection conn = ConnectionUtil.getConnection()){
            //represents our SQL query
            //? = wildcard variable to fill in
            String sql = "DELETE from dnd_classes where class_id = ?";

            //PreparedStatement to fill in variable (id)
            PreparedStatement ps = conn.prepareStatement(sql);

            //use id param to set the wildcard variable, parameterIndex = index of ?
            ps.setInt(1,id);

            DnD_Class out = getClassByID(id);
            //Execute the update
            ps.executeUpdate();

            return out;


        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("couldn't delete DnD Class at id:" + id);
        }
        //if the SQL query never gets injected we will return null so we can still have a return for this method
        return null;
    }

}