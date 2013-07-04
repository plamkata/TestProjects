package gameLogic.players.computers;

import constantsAndTypes.Name;
import constantsAndTypes.Weapon;
import gameLogic.combination.Combination;

import java.io.*;
import java.util.Vector;

/**
 * ComputerIntermidiate is created in Game when
 * Settings.level is set to INTERMIDIATE.
 * @see constantsAndTypes.Level
 */
public class ComputerIntermidiate extends ComputerBeginner {

      /**
       * The file not found Exception is thrown here
       * and goes up to the Game's constructor.
       * @param weapon
       * @param name
       * @param listener
       * @throws FileNotFoundException
       */
      public ComputerIntermidiate(Weapon weapon, Name name, BattleField listener)
              throws FileNotFoundException {
          super(weapon, name, listener);
          // load the initial memory from some external file
          loadInitialMemory();
      }

      /**
       * Loads some initial memory from
       * file attached to the project.
       * @throws FileNotFoundException
       */
      private void loadInitialMemory()
              throws FileNotFoundException {
          memory = new Vector();
          InputStream in;
          if (weapon == Weapon.OO) {
              in = this.getClass().getClassLoader().
			   		  getResourceAsStream("newCombinationsO.txt");
			  if (in == null) throw new FileNotFoundException();
          } else {
              in = this.getClass().getClassLoader().
	   		  		getResourceAsStream("newCombinationsX.txt");
	          if (in == null) throw new FileNotFoundException();
          }

          BufferedReader buff = new BufferedReader(
                  new InputStreamReader(in));
          try {
              String str = buff.readLine();
              while(str != null) {
                  Combination cmb = convertToCombination(str);
                  if (cmb != null) {
					  memory.add(cmb);
				  }
                  str = buff.readLine();
              }
			  in.close();
              buff.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }

}
