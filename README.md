# Meteorite Attack
<p align="center">
<img src="/images/menu_logo.png">
</p>

[LATEST VERSION APK FILE](https://github.com/Nekobitlz/Meteorite-Attack-Android/blob/master/apks/Meteorite_Attack_1.06.apk)

## Technology stack
The source code of the game was written in Java. Canvas and Bitmap used to draw on SurfaceView. All data is saved in the SharedPreferences. Runnable was used as streams. To work with the background music used MediaPlayer with the Service and the SoundPool. For testing, Espresso and JUnit were used.

## Game description
**The essence of the game:** The user controls the spaceship, he needs to destroy meteorites in order to go as far as possible and score maximum points when the player’s health reaches zero - the game ends

**Purpose of the game:** To score the most points

**The game consists of 6 Activity:**
   1. ___Main menu___, on which the buttons for switching to other activity are located
   2. ___The playing field___ on which the whole game process takes place
   3. ___Shop___ where the user can improve the current ship, or buy a new one
      *Upgrades:*
         1) Increased health
         2) Increase bullet power
         3) Increase account multiplier
   All upgrades are bought for coins, coins are given for the destruction of meteorites, as well as for the distance traveled.
   4. ___Settings___, on which the user can adjust the volume of sounds and music, as well as select the control
   5. ___High Score___, which displays the highest scores, as well as the number of enemies destroyed


## Screenshots
<img src="/images/screenshots/MainMenu.jpg" width="360" height="640"> <img src="/images/screenshots/GameField.jpg" width="360" height="640"> <img src="/images/screenshots/Shop.jpg" width="360" height="640"> <img src="/images/screenshots/Settings.jpg" width="360" height="640"> <img src="/images/screenshots/HighScore.jpg" width="360" height="640">

**Objects:**
   1. <img src="/images/spaceship_1_game.png" align="middle" width="57" height="41"/> *Spaceship (Player)*
      - User controlled
      - Automatically shoots bullets that, when hit by an enemy, cause damage equal to the power of the bullet
      - Health count drawn on sprite
   2. *Enemies:*
      1) <img src="/images/meteor_1.png" align="middle" width="45" height="45"> ___Normal meteorite___
         - Has a random size
         - Can move strictly along the Y axis at random speed, or stand still
         - Has a random health count from a range that rises every 1000 points
         - Health count drawn on sprite
         - When confronted with a player, he takes away the amount of health equal to the health of the enemy ship
         - The reward for the murder: the number of points equal to the level * 10 + number of coins equal to the number of health meteorite
      2) <img src="/images/enemy_black_1.png" align="middle" width="46" height="42"> ___Enemy ship___
         - It can move at random speed strictly along the X axis, or diagonally, starting from the walls of the playing field
         - Has a random health count from a range that rises every 1000 points
         - Health count drawn on sprite
         - When confronted with a player, he takes away the amount of health equal to the health of the enemy ship
         - The reward for the murder: the number of points equal to the level * 20 + number of coins equal to the number of health of the enemy ship
      3) <img src="/images/meteor_brown_big_1.png" align="middle" width="50" height="42"> ___"Border destroyer"___
         - Characteristics of a conventional meteorite, but can only fly along the Y axis
         + If he flies over the lower boundary of the playing field - the game ends regardless of the amount of player health
         + Reward for killing: number of points equal to the level * 15 + number of coins equal to the number of meteorite health * 1.5
      4) <img src="/images/meteor_grey_big_3.png" align="middle" width="45" height="41"> ___"Exploder"___
         - Characteristics of an ordinary meteorite
         + After the destruction of the leaves the krator, falling into which the player receives damage in 1 health unit
   3. *Bonuses:*
      - Generated randomly after destroying enemies
      - Have a limited duration
      1) ___Positive___
         - <img src="/images/bonus_speed_up.png" align="middle" width="35" height="35"> "Shot speed up" - increases the frequency of bullets
         - <img src="/images/bonus_health.png" align="middle" width="35" height="35"> "Health" - increases the amount of health
         - <img src="/images/bonus_triple_shot.png" align="middle" width="35" height="35"> "Triple shot" - the ship starts to release 3 bullets for 1 shot
      2) ___Negative___
         - <img src="/images/bonus_slow_up.png" align="middle" width="35" height="35"> "Shot slow up" - reduces the frequency of bullet shots, but increases the damage by 1.5 times
         - <img src="/images/bonus_destroyer.png" align="middle" width="35" height="35"> "Destroyer" - reduces the amount of health to 1 unit, if health is already equal to 1, then the game ends
but increases damage by 2 times

## Setup for Developers
    1. Make sure you have downloaded the latest version of [Android Studio](https://developer.android.com/sdk/index.html). It works on Linux, Windows and Mac. Download the correct version for your OS.
    2. Go to [the project repo](https://github.com/Nekobitlz/Meteorite-Attack-Android) and fork it by clicking "Fork"
    3. If you are working on Windows, download [Git Bash for Windows](https://git-for-windows.github.io/) to get a full Unix bash with Git functionality
    4. Clone the repo to your desktop `git clone https://github.com/YOUR_USERNAME/Meteorite-Attack-Android.git`
    5. Open the project with Android Studio
    6. Build a 'app' application which is inside the base directory.

## License

Meteorite Attack is licensed under the [MIT license](https://github.com/Nekobitlz/Meteorite-Attack-Android/blob/master/LICENSE).