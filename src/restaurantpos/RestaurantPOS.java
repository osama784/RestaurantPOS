package restaurantpos;

import javax.swing.SwingUtilities;


public class RestaurantPOS {

    public static void main(String[] args) {
        Thread t = new Thread(new system.Restaurant());
        t.run();
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
              new GUI.GUISystem();
            }
        });
    }
    
}
