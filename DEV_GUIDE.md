#### Quick guide for developers

Follow the steps to get the project working on your IDE

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
