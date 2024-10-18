package com.revature.DAOs;

import com.revature.models.Ability;
import com.revature.models.DnD_Class;
import com.revature.utils.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;

public class AbilityDAO implements AbilityDAOInterface {

    @Override
    public Ability getAbilityById(int id) {
        //try to open a Connection to DB
        try(Connection conn = ConnectionUtil.getConnection()){
            //represents our SQL query
            //? = wildcard variable to fill in
            String sql = "SELECT * FROM abilities WHERE ability_id = ?";

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
                Ability ability = new Ability(
                    rs.getInt("ability_id"),
                    rs.getString("ability_name")
                );
                //Return the Role
                return ability;
            }

        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("couldn't get Role by ID");
        }
        //if the SQL query never gets injected we will return null so we can still have a return for this method
        return null;
    }

    @Override
    public String updateAbilityName(int id, String newName) {

        //try to open a connection to the DB
        try(Connection conn = ConnectionUtil.getConnection()){
            //sql statement
            String sql = "UPDATE abilities SET ability_name = ? WHERE ability_id = ?";

            //Create a PreparedStatement ot fill in the variables
            PreparedStatement ps = conn.prepareStatement(sql);

            //set the variable values in the sql statement
            ps.setString(1,newName);
            ps.setInt(2, id);

            //execute the update
            ps.executeUpdate();

            //return new salary
            return newName;
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("couldn't update role salary");
        }
        //catch all if the update fails, we'll return 0
        return null;
    }

    public ArrayList<Ability> getAllAbilities() {

        //
        try(Connection conn = ConnectionUtil.getConnection()){

            //create SQL statement
            String sql = "SELECT * FROM abilities ORDER BY ability_id";

            //create the statement obj since we have no ? wildcards in SQL statement
            Statement s = conn.createStatement();

            //execute query
            ResultSet rs = s.executeQuery(sql);

            //Create ArrayList to store our DnDClasses
            ArrayList<Ability> abilities = new ArrayList<>();


            //loop through ResultSet
            //rs.next will return false when there are no more rows in the ResultSet
            while(rs.next()){
                //For every DnD_Class found, add it to the ArrayList
                //instantiate a new DnD_Class for each row in the ResultSet
                //we can get each piece of DnD_Class Data with rs.get methods
                Ability ability = new Ability(
                        rs.getInt("ability_id"),
                        rs.getString("ability_name")
                );


                abilities.add(ability);
            }
            return abilities;


        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("couldn't get all DnD Classes");
        }
        return null;


    }

    @Override
    public Ability insertAbility(Ability ability) {
        //try to connect to the database
        try(Connection conn = ConnectionUtil.getConnection()){
            //create SQL statement
            String sql = "INSERT INTO abilities(ability_name) " + "VALUES (?);";
            //use PreparedStatement to fill in the values of our variables
            PreparedStatement ps = conn.prepareStatement(sql);
            //use set() to fill in values
            ps.setString(1, ability.getAbility_name());
            //Send the SQL command to the DB
            //execute update is used for inserts, updates, and deletes
            ps.executeUpdate();
            //return the new DnD_Class object
            String idsql = "SELECT ability_id FROM abilities WHERE ability_name = '" + ability.getAbility_name() +"'";
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(idsql);
            rs.next();
            ability.setAbility_id(rs.getInt("ability_id"));
            return ability;

        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Couldn't insert DnD Class");
        }
        return null;
    }

    @Override
    public Ability deleteAbility(int id) {
        try(Connection conn = ConnectionUtil.getConnection()){
            //represents our SQL query
            //? = wildcard variable to fill in
            String sql = "DELETE from abilities where ability_id = ?";

            //PreparedStatement to fill in variable (id)
            PreparedStatement ps = conn.prepareStatement(sql);

            //use id param to set the wildcard variable, parameterIndex = index of ?
            ps.setInt(1,id);

            Ability out = getAbilityById(id);
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
