package model;

public class Tester extends Entity<String> {

    String name;
    String username;
    String password;

    public Tester(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        setId(username);
    }

    public Tester(){}


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}