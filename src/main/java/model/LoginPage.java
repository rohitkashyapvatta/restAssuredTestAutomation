package model;

public class LoginPage {
    private String email;
    private String password;

    public LoginPage(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public static class Builder {
        private String email;
        private String password;

        public Builder() {

        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public LoginPage build() {
            return new LoginPage(email, password);
        }

    }


}
