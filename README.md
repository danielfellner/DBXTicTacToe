DBX Kft feladat:
--------------------------

Tic-Tac-Toe

Egy Tic-Tac-Toe táblát egy kétdimenziós vektorban (vagy listában vagy tömbben) ábrázolunk. 
Az első játékos korongjait az “X” string reprezentálja, a második játékos korongjait az “O” string. Ha egy mezőn nincsen korong, azt a szóköz karakter jelzi: “ ”.
Írj egy olyan programot, ami standard bemenetről (vagy fájlból) beolvas egy Tic-Tac-Toe táblát és eldönti, hogy melyik játékos nyert.
[1] https://hu.wikipedia.org/wiki/Tic-tac-toe

Bonyolítás: Írd meg úgy a programot, hogy tetszőleges méretű táblán működjön és paraméterként megadható legyen az is, hogy a győzelemhez hány szomszédos korong szükséges.

Elkészült alkalmazás:
---------------------------
1. mvn build
--------------------------
futtatni parancssorból: mvn clean install
ennek hatására lefutnak a tesztek és a project library target könyvtárában létrejön a project fájlokat
tartalmazó DBX_TicTacToe-1.0.-SNAPSHOT.jar
Az összes függőség a project libray target/libs/ könyvtárában található meg.

2. telepített java 11 kompatibilis virtuális géppel való gyors futtatás
---------------------------
a.) input argumentumok nélkül: 
java -cp target/DBXTicTacToe-1.0-SNAPSHOT.jar:target/libs/* hu.dbx.homework.TicTacToeEndGameAnalyserApplicationMain

Ez esetben a terminálban érkezik az adatbakérés a táblára vonatkozólag a standard inputról.
Az analyser ilyenkor az alapértelemezett legkisebb 3x3 -as tábla esetén vett maximális markerek számának a 3-as értéket veszi.  
Az játéktábla ettől eltérhet, a rendszer a felolvasott adatok alapján kalkulálja a tábla méretet. 

b.) input argumentumokkal 
-f | -filename ${magának az adatájlnak a neve}
illetve 
-m : -markers ${az egymás melletti markerek száma}

pl.
~/IdeaProjects/DBXTicTacToe$ java -cp target/DBXTicTacToe-1.0-SNAPSHOT.jar:target/libs/* hu.dbx.homework.TicTacToeEndGameAnalyserApplicationMain -m 3 -f target/test-classes/testfile.txt

A program a standard inputra loggolja az eredményt, amennyiben a végállás alapján kalkulálható a játékosok győzelme, vagy a döntetlen állapot
Amennyiben nem a standard error-ra fogja loggolni az észrevételeket.

pl.
~/IdeaProjects/DBXTicTacToe$ java -cp target/DBXTicTacToe-1.0-SNAPSHOT.jar:target/libs/* hu.dbx.homework.TicTacToeEndGameAnalyserApplicationMain -m 300 -f target/test-classes/testfile.txt

