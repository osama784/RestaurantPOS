package GUI;
 

public enum usersStatus{
        admin(false),
        user(false),
        out(true); 
        
        private boolean value;

        private usersStatus(boolean value) {
            this.value = value ;
        }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
        
    }
