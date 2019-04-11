package Modelos;

public class Usuario {
    private long id;
    private String username;
    private String password;
    private boolean administrator;
    private boolean autor;

    public Usuario(long id, String username, String password, boolean adminstrator, boolean autor) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.administrator = adminstrator;
        this.autor = autor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public boolean isAdminstrator() {
        return administrator;
    }

    public void setAdminstrator(boolean adminstrator) {
        this.administrator = adminstrator;
    }

    public boolean isAutor() {
        return autor;
    }

    public void setAutor(boolean autor) {
        this.autor = autor;
    }
}


