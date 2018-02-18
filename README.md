# IL2CPP Patcher

A tool for Android modders who are too lazy to mod multiple versions of an app over and over again.
*For **libil2cpp.so** modding only. If you're modding a different file, such as **Assembly-CSharp.dll**,
please look for other tools.*

## Getting Started

1. [For developers](#for-developers)
2. [For users](#for-users)
3. [What is the mod file?](#mod-file)

### Prerequisites
+ A computer
+ [Il2CppDumper](https://github.com/Perfare/Il2CppDumper): please Google how to use.
You will need this to get a `dump.cs` file
+ JDK 8 for developers, JRE / JDK 8 for users (both are ok)
  - JDK 9 should work but I had a lot of troubles with JDK 9 during development
  so it's better to use 8

#### For developers

1. Clone everything to your computer or download directly as a ZIP file from github: 
    `git clone https://github.com/kokonguyen191/Il2cppPatcher.git` 
2. Import to your preferred IDE (Eclipse or Intellij IDEA recommended)
3. Import `JUnit 4` to your IDE
    1. For Eclipse:
        1. Right-click the project, select `Build Path > Configure Build Path...`
        2. Click on `Libraries` tab
        3. Click on `Add Library...` button
        4. Select `JUnit`, click `Next >`
        5. Select `JUnit 4` for `JUnit library version`
        6. Click `Finish`
        7. Click `Apply and Close`
    2. For Intellij IDEA
        1. Open any test file in `\test`
        2. Click on any `@Test` string, press <kbd>ALT</kbd>+<kbd>ENTER</kbd>
        3. Select `Add 'JUnit4' to classpath`
        4. Select `Use 'JUnit4' from IntelliJ IDEA distribution`
        5. Click `OK`
        6. `Run > Edit Configurations...`
        7. `Defaults > JUnit`
        8. Change the `Working directory` to the root folder of the project, for example, `C:\Users\John\IdeaProjects\Il2cppPatcher`
4. You're good to go, just run directly from the **Main** class. The shortcut is usually
<kbd>CTRL</kbd>+<kbd>SHIFT</kbd>+<kbd>F10</kbd> or <kbd>CTRL</kbd>+<kbd>SHIFT</kbd>+<kbd>F11</kbd>

#### For users

1. Download the [latest release](https://github.com/kokonguyen191/Il2cppPatcher/releases)

2. [Open a terminal](https://www.lifewire.com/how-to-open-command-prompt-2618089), `cd` to the directory where you put the `.JAR` file
    + For example, if the `.JAR` file is in `E:/Download/JAR/Ill2cppPatcher.jar`, you type this into the terminal
    
    > E:<br>
    cd E:/Download/JAR
    
3. Type this in
    > java -jar Il2cppPatcher.jar <FULL_PATH_TO_IL2CPP.SO_FILE> <FULL_PATH_TO_DUMP.CS_FILE> <FULL_PATH_TO_MOD_FILE>
    
    So if your files are `E:/Folder1/libil2cpp.so`,  `D:/Folder2/dump.cs`, `C:/Folder3/mod.txt`, you type in
    
    > java -jar Il2cppPatcher.jar "E:/Folder1/libil2cpp.so"  "D:/Folder2/dump.cs" "C:/Folder3/mod.txt"

4. **Do make a backup of the libil2cpp.so file beforehand. The application will not create
a backup for you**

### Mod file

So what exactly is the mod file that is the 3rd argument you need to provide to the app?
A mod file contains all the changes needed to create a mod. I will assume that you know how to do
mods using `IDA`. A mod file contains the **method names** and the **hex changes** that will be
applied to the **libil2cpp.so** file.

For example, you need to mod a method called `public int get_HP(); // 0x123456`, and in that method,
the modification you do is to change the bytes values starting at address `0x123460` from `0x0011` to 
`0xAABB`. So the mod file will be something like this

> \# <br>
Get HP<br>
public int get_HP(); // 0x123456<br>
0x123460 0x2 00 11 AA BB

Let's see what each line does:

> \# 

This is the delimiter. Every mod option needs a `#` character at the start. So if you have two options
in your mod, let's say, _Godmode_ and _High damage_, then you need two `#` at the start of each.

> Get HP

This is the name of the mod. It has absolutely no meaning to the application, it's just there
so that you know which option is which.

> public int get_HP(); // 0x123456

This is the method. You need to copy this fully from `dump.cs` and every method **must** be in this format:
 `something functionName(); // 0xsomeNumber`
 
> 0x123460 0x2 00 11 AA BB

This means the modification starts at address `0x123460` and has length of `0x2` (it's `2`), and changes 
the bytes `0x0011` to `0xAABB`. You can get this format very quickly by pressing <kbd>CTRL</kbd>+<kbd>ALT</kbd>+<kbd>P</kbd> 
in IDA and copy paste from there.

You can see a more in-depth example [here](https://github.com/kokonguyen191/Il2cppPatcher/tree/master/example).
 It's a mod for the game アリス・ギア・アイギス.
 
### What to do when the Android app has an update?

This application works on an assumption that the more core methods will not change between small updates. 
And that is the fact with many Android app. So you only need to mod the first time and note your modifications 
down. For all future updates, you only need to extract the **libil2cpp.so** file and generate 
**dump.cs**. Then pass these two files, together with the mod file to the application, and you have a 
newly-modded **libil2cpp.so** file! In case there are changes between updates, the application will print 
out which mod options are not working correctly so that you can pinpoint on only the outdated options.