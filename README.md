# Meteorite Attack
Small 2D space shooter on Android

**The essence of the game:** The user controls the spaceship, he needs to destroy meteorites in order to go as far as possible and score maximum points when the player’s health reaches zero - the game ends

**Purpose of the game:** To score the most points

**The game consists of 6 Activity:**
   1. ___Main menu___, on which the buttons for switching to other activity are located
   2. ___The playing field___ on which the whole game process takes place
   3. ___A shop___ where the user can improve the current ship, or buy a new one  
      *Upgrades:* 
         1) Increased health 
         2) Increase bullet power
         3) Increase account multiplier  
   All upgrades are bought for coins, coins are given for the destruction of meteorites, as well as for the distance traveled.
   4. ___Settings___, on which the user can adjust the volume of sounds and music, as well as select the control
   5. ___High Score___, which displays the highest scores, as well as the number of enemies destroyed
   6. ___Help___, which contains all the information about the rules of the game

**Objects:**
   1. *Spaceship (Player)*
      - User controlled
      - Automatically shoots bullets that, when hit by an enemy, cause damage equal to the power of the bullet
      - Health count drawn on sprite
   2. *Enemies:*
      1) ___Normal meteorite___
         - Has a random size
         - Can move strictly along the Y axis at random speed, or stand still
         - Has a random health count from a range that rises every 1000 points
         - Health count drawn on sprite
         - When confronted with a player, he takes away the amount of health equal to the health of the enemy ship
         - The reward for the murder: the number of points equal to the level * 10 + number of coins equal to the number of health meteorite
      2) ___Enemy ship___
         - It can move at random speed strictly along the X axis, or diagonally, starting from the walls of the playing field
         - Has a random health count from a range that rises every 1000 points
         - Health count drawn on sprite  
         - When confronted with a player, he takes away the amount of health equal to the health of the enemy ship
         - The reward for the murder: the number of points equal to the level * 20 + number of coins equal to the number of health of the enemy ship
      3) ___"Border destroyer"___
         - Characteristics of a conventional meteorite, but can only fly along the Y axis
         + If he flies over the lower boundary of the playing field - the game ends regardless of the amount of player health
         + Reward for killing: number of points equal to the level * 15 + number of coins equal to the number of meteorite health * 1.5
      4) ___"Exploder"___
         - Characteristics of an ordinary meteorite
         + After the destruction of the leaves the krator, falling into which the player receives damage in 1 health unit
   3. *Bonuses:*
      - Generated randomly after destroying enemies
      - Have a limited duration
      1) ___Positive___
         - "Shot Accelerator" - increases the frequency of bullets
         - "Health" - increases the amount of health
         - "Triple shot" - the ship starts to release 3 bullets for 1 shot
      2) ___Negative___
         - "Shot Retarder" - reduces the frequency of bullet shots, but increases the damage by 1.5 times
         - "Destroyer" - reduces the amount of health to 1 unit, if health is already equal to 1, then the game ends
but increases damage by 2 times
