package jobagency.entities;

public class User {
    private String name;
    private String prefix;
    private String username;
    private String email;

    public User(String name, String prefix, String username, String email) {
        this.name = name;
        this.prefix = prefix;
        this.username = username;
        this.email = email;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

