package ca.albertlockett.atlanticchip;

import java.text.DecimalFormat;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
    	DecimalFormat formatter = new DecimalFormat("0.########E0");
    	for(int i = 0; i < Math.pow(10.0, 9.0); i++){
    		if(i%10 == 0) {
    			System.out.print("\n" + formatter.format(i) + " ");
    		}
    		System.out.print("Albert ");
    		
    	}
    }
}
