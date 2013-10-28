
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
 
/* Eric Ma
 * 6/3/12
 * This class's purpose is to be able to load each of the images in the project
 * The class contains 4 methods: 
 * 		3 are used to get 1 image each, and the 4th is used to get one of the 9 number images
 */
public class LoadImage extends Component {
	//private data is a BufferedImage that is initialized in the getter methods
    private static BufferedImage img; 
    
    /*initializes the BufferedImage img to the file falseflag.JPG
     *catches an exception and returns the img
     */
    public static BufferedImage getFalseFlagImage(){
		try {
			img = ImageIO.read(new File("falseflag.JPG"));
		} catch (IOException e) {
		}
		return img;    	  	
    }
    
    /*initializes the BufferedImage img to the file bomb.JPG
     *catches an exception and returns the img
     */
    public static BufferedImage getBombImage(){
		try {
			img = ImageIO.read(new File("bomb.JPG"));
		} catch (IOException e) {
		}
		return img;    	
    }
    
    /*initializes the BufferedImage img to the file flag.JPG
     *catches an exception and returns the img
     */
    public static BufferedImage getFlagImage(){
		try {
			img = ImageIO.read(new File("flag.JPG"));
		} catch (IOException e) {
		}
		return img;
    }
   
    /*takes in an int of the corresponding int image returned
     *initializes the BufferedImage img to that number's JPG image file
     *catches an exception and returns the img
     */
    public static BufferedImage getImg(int number){
    	try {
            img = ImageIO.read(new File(number + ".JPG"));
        } catch (IOException e) {
   	    }
       	return img;
    }
}