Parallax Navigation Drawer
==========================

Parallax Navigation Drawer is a custom Native Android navigation drawer that supports sliding from the left and right ends with parallax effect.

[<img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/IODevBlue/ParallaxNavigationDrawer?color=0109B6&style=for-the-badge&label=Current Version">](https://github.com/IODevBlue/ParallaxNavigationDrawer/releases) <img alt="Repository Size" src="https://img.shields.io/github/repo-size/IODevBlue/ParallaxNavigationDrawer?color=0109B6&style=for-the-badge"> [<img alt="License" src="https://img.shields.io/github/license/IODevBlue/ParallaxNavigationDrawer?color=0109B6&style=for-the-badge">](http://www.apache.org/licenses/LICENSE-2.0) [<img alt="GitHub Repository stars" src="https://img.shields.io/github/stars/IODevBlue/ParallaxNavigationDrawer?color=0109B6&style=for-the-badge">](https://github.com/IODevBlue/ParallaxNavigationDrawer/stargazers)
<img alt="GitHub watchers" src="https://img.shields.io/github/watchers/IODevBlue/ParallaxNavigationDrawer?label=Repository Watchers&color=0109B6&style=for-the-badge"> [<img alt="Gradle version" src="https://img.shields.io/static/v1?label=Gradle version&message=8.0&color=0109B6&style=for-the-badge">](https://docs.gradle.org/8.0/release-notes) [<img alt="Kotlin version" src="https://img.shields.io/static/v1?label=Kotlin version&message=1.9.20&color=0109B6&style=for-the-badge">](https://kotlinlang.org/docs/whatsnew1920.html)

Uses
----
Parallax Navigation Drawer can be used to provide left and right navigation drawers. Each drawer can contain dynamic content.

<p align="center"><img src="/art/demo1.gif" alt="Parallax Navigation Drawer"></p>

Installation
------------
There are several ways to install this library.

1. Grab a JAR artefact from the Maven Central Repository:
- On Gradle
```GROOVY
implementation 'io.github.iodevblue:parallaxnavigationdrawer:1.0.0'
```
- On Apache Maven
```XML
<dependency>
  <groudId> io.github.iodevblue </groudId>
  <artifactId> parallaxnavigationdrawer </artifactId>
  <version> 1.0.0 </version>
</dependency>
```
If it is a snapshot version, add the snapshot Maven Nexus OSS repository:
```GROOVY
maven {   
  url 'https://s01.oss.sonatype.org/content/repositories/snapshots/'
}
```
Then retrieve a copy:
```GROOVY
implementation 'io.github.iodevblue:parallaxnavigationdrawer:1.0.0-SNAPSHOT'
```

2. Grab a JAR or AAR artifact from the [release](https://github.com/IODevBlue/ParallaxNavigationDrawer/releases) section.
- Place it in `libs` folder in your project module and install in your project.
```GROOVY
implementation fileTree(dir:' libs', include:'*jar')
```

If you do not prefer the compiled JAR and want access to the source files directly:

3. [Download](https://github.com/IODevBlue/ParallaxNavigationDrawer/archive/refs/heads/main.zip) the project zip file.
- Create a new module with name `parallaxnavigationdrawer` in your project.
- Copy the contents of the `library` module from the downloaded project zip file to the new module `parallaxnavigationdrawer`.
- This method makes the source code accessible. If you do make major or minor improvements to the source code, consider making a pull request or an issue to make a contribution.

Check the [Contributing](https://github.com/IODevBlue/ParallaxNavigationDrawer/blob/development/CONTRIBUTING.md) for more information.

4. If creating a new module is not preferable for your project and you want to have access to the source codes while tightly coupling it to your project, then follow this process:
- [Download](https://github.com/IODevBlue/ParallaxNavigationDrawer/archive/refs/heads/main.zip) the project zip file.
- Create a new package with name `parallaxnavigationdrawer` in your project.
- Copy the subpackages and class files from the `com.blueiobase.api.android.parallaxnavigationdrawer` package from the downloaded project zip file into the new `parallaxnavigationdrawer` package.
- Edit the package declaration in each class file accordingly.
- Copy the contents of the res folder in the project zip into your module's res folder. 
- Alternatively for convenience and arrangement in an ordered manner, you could create a `sourceSet` dedicated to 3rd party libraries like so:
```GROOVY
android {
  sourceSets {
    main {
      res {
        srcDirs file("src/main/thirdpartyres/").listFiles(),
                'src/main/thirdpartyres'
      }
      java {
        srcDirs 'src/main/thirdpartylibraries'
      }
    }
  }
}
```
- Then sync project with Gradle.
- This creates a specialized Gradle source set `thirdpartylibraries` for 3rd party library source files and `thirdpartyres` for 3rd party resource files in the `main` directory.
- Create a `parallaxnavigationdrawer` subfolder in the `thirdpartyres` folder and copy the resource contents from the library module from the downloaded project zip file into the new subfolder.
- Copy the contents of the `com.blueiobase.api.android.parallaxnavigationdrawer` package from the downloaded project zip file into the `thirdpartylibraries` source set.
- Again, this method makes the source code accessible. If you do make major or minor improvements to the source code, consider making a pull request or an issue to make a contribution.

Check the [Contributing](https://github.com/IODevBlue/ParallaxNavigationDrawer/blob/development/CONTRIBUTING.md) for more information.


Usage
-----
To use `ParallaxNavigationDrawer`, you will need three layout files
- One representing the left navigation drawer.
- One representing the right navigation drawer.
- One representing the main User Interface content.

Suppose these are the layouts...
- The Left drawer content layout representing the left navigation drawer (`drawer_left.xml`)
```XML
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    >

    <ImageView/>

</RelativeLayout>
```
- The Right drawer content layout representing the right navigation drawer (`drawer_right.xml`)
```XML
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ImageView/>
    
</LinearLayout>
```
- Main content layout representing the Main User Interface (`drawer_main.xml`)
```XML
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.google.android.material.appbar.AppBarLayout/>

    <ImageView/>

    <TextView/>

    <ImageButton/>

</RelativeLayout>
```

Navigate to your `Activity` or `Fragment` XML layout file (e.g `activity_main.xml`), add a `ParallaxNavigationDrawer` widget and include the layouts in the following order
- Left navigation drawer layout
- Right navigation drawer layout
- Main content layout
```XML
<com.blueiobase.api.android.parallaxnavigationdrawer.ParallaxNavigationDrawer
        android:id="@+id/pnd"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/white"
        app:mainContentShadowAlpha="0.5"
        app:mainContentCloseOnTouch="true"
        app:drawerMode="both"
        app:parallax="true"
        >

        <include layout="@layout/drawer_left"/>

        <include layout="@layout/drawer_right"/>

        <include layout="@layout/drawer_main"/>

</com.blueiobase.api.android.parallaxnavigationdrawer.ParallaxNavigationDrawer>
```
***NOTE:*** The layouts ***MUST*** be in the specified order (Left, Right, Main) and there ***MUST*** be no more than ***3*** layout files included in the `ParallaxNavigationDrawer` widget unless
an `IllegalStateException` would be thrown.

To utilize one of either drawers, add the `drawerMode` attribute to the `ParallaxNavigationDrawer` widget specifying either `left` or `right` enum value.
Then <include/> at least two layouts. 
- One layout representing the navigation drawer.
- The other representing the main content User Interface.
***NOTE:*** The layout included last would be considered the main User Interface content.
```XML
<com.blueiobase.api.android.parallaxnavigationdrawer.ParallaxNavigationDrawer
        android:id="@+id/pnd"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/white"
        app:mainContentShadowAlpha="0.5"
        app:mainContentCloseOnTouch="true"
        app:drawerMode="right"
        app:parallax="true"
        >
  
        <include layout="@layout/drawer_right"/>

        <include layout="@layout/drawer_main"/>

</com.blueiobase.api.android.parallaxnavigationdrawer.ParallaxNavigationDrawer>
```
Specifying `none` indicates that drawers are deactivated.

***NOTE:*** By default, `ParallaxNavigationDrawer` has the `drawerMode` attribute set to `none`. This means it must be explicitly set either in the XML layout file or in the class file.

To open the left drawer:
```KOTLIN
parallaxNavigationDrawer.openLeftDrawer()
```
To close the close drawer:
```KOTLIN
parallaxNavigationDrawer.closeLeftDrawer()
```
To toggle between open and close states of the left drawer:
```KOTLIN
parallaxNavigationDrawer.toggleLeftDrawer()
```

You can utilize the `onBackPressed()` function which closes either the left or right drawer and returns a boolean indicating the closed state of both drawers.
This can be implemented in the overridden `onBackPressed()` of an `Activity` class.
```KOTLIN
override fun onBackPressed() {
  if(parallaxNavigationDrawer.onBackPressed()) super.onBackPressed()
}
```

Listen to drawer open and close events by using either one or both of these depending on your implementation:
- `OnDrawerStateChangedListener`: Listens for open and close events for both drawers.
- `OnLeftDrawerStateChangedListener`: Listens for open and close events for only the left drawer.
- `OnRightDrawerStateChangedListener`: Listens for open and close events for only the right drawer.
```KOTLIN
private val parallaxNavigationDrawer: ParallaxNavigationDrawer by lazy { findViewById(R.id.pnd) }

parallaxNavigationDrawer.apply {
    setOnLeftDrawerStateChangedListener {
        if(it)
            Toast.makeText(this@MainActivity, "Left Drawer Open!", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this@MainActivity, "Left Drawer Close!", Toast.LENGTH_SHORT).show()
    }
    setOnRightDrawerStateChangedListener {
        if(it)
            Toast.makeText(this@MainActivity, "Right Drawer Open!", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this@MainActivity, "Right Drawer Close!", Toast.LENGTH_SHORT).show()
    }
}
```
Alternatively, you can specify a drawer state listener using Kotlin DSL:
```KOTLIN
parallaxNavigationDrawer.setOnLeftDrawerStateChangedListener {
    if(it)
        Toast.makeText(this@MainActivity, "Left Drawer Open!", Toast.LENGTH_SHORT).show()
    else
        Toast.makeText(this@MainActivity, "Left Drawer Close!", Toast.LENGTH_SHORT).show()
}

parallaxNavigationDrawer.setOnRightDrawerStateChangedListener {
    if(it)
        Toast.makeText(this@MainActivity, "Right Drawer Open!", Toast.LENGTH_SHORT).show()
    else
        Toast.makeText(this@MainActivity, "Right Drawer Close!", Toast.LENGTH_SHORT).show()
}
```

To use `ParallaxNavigationDrawer` in a declarative manner:
- Create a `ParallaxNavigationDrawer` instance using a `Context`:
```KOTLIN
private val parallaxNavigationDrawer = ParallaxNavigationDrawer(context)
```
- Inflate the layouts representing the drawers and main User Interface content to `View` objects and add them dynamically to the `ParallaxNavigationDrawer` instance:
```KOTLIN 
val layoutInflater = LayoutInflater.from(context)
val leftDrawer = layoutInflater.inflate(R.layout.drawer_left, parallaxNavigationDrawer)
val rightDrawer = layoutInflater.inflate(R.layout.drawer_right, parallaxNavigationDrawer)
val mainContent = layoutInflater.inflate(R.layout.drawer_main, parallaxNavigationDrawer)
```
Or if the `View` objects representing the drawers already exist, then each can be added to the `ParallaxNavigationDrawer` instance.
***NOTE:*** `ParallaxNavigationDrawer` can only host a maximum 3 `View` objects. 
One for the left drawer, one for the right drawer and one for the main User Interface content.
```KOTLIN
parallaxNavigationDrawer.apply {
  addView(leftDrawerView)
  addView(rightDrawerView)
  addView(mainContentView)
}
```
The `index` in the `addView()` method signifies the position of each `View` added to the `ParallaxNavigationDrawer`.
- 0 = Left Drawer
- 1 = Right Drawer
- 2 = Main User Interface content

***NOTE:*** The `View` objects ***MUST*** be inflated with the `ParallaxNavigationDrawer` instance or added to it in this manner 
=> Left, Right, Main.
The order must be maintained for conformity.

Then you can set the `ParallaxNavigationDrawer` as the content view of your `Activity`
```KOTLIN
setContentView(parallaxNavigationDrawer)
```

A shorter syntax with Kotlin DSL involves creating an instance using an extension function on a `Context` class and performing the necessary preliminary setup.
```KOTLIN
private val parallaxNavigationDrawer = parallaxNavigationDrawer {
  drawerMode = ParallaxNavigationDrawer.DRAWER_MODE_BOTH
  addView(leftDrawerView)
  addView(rightDrawerView)
  addView(mainContentView)
}
```
See sample implementation for more details.

Java Interoperability
---------------------
`ParallaxNavigationDrawer` is completely interoperable with Java projects.

Retrieve a `ParallaxNavigationDrawer` from an inflated layout:
```JAVA
ParallaxNavigationDrawer parallaxNavigationDrawer = findViewById(R.id.pnd);
```
Or create using an instance using a `Context`:
```JAVA
ParallaxNavigationDrawer parallaxNavigationDrawer = new ParallaxNavigationDrawer(context);
```

Make preliminary changes to the `ParallaxNavigationDrawer` instance:
```JAVA
parallaxNavigationDrawer.setDuration(400);
parallaxNavigationDrawer.setEnableSwiping(false);
parallaxNavigationDrawer.setParallax(true);
```

To implement a drawer state listener, say an `OnLeftDrawerStateChangedListener`:
```JAVA
parallaxNavigationDrawer.setOnLeftDrawerStateChangedListener(new ParallaxNavigationDrawer.OnLeftDrawerStateChangedListener() {
  @Override
  public void onDrawerStateChanged (boolean isOpen) {
	if(isOpen){
	  Toast.makeText(context,"Left Drawer Open!",Toast.LENGTH_SHORT).show();
	} else {
	  Toast.makeText(context, "Left Drawer Close!", Toast.LENGTH_SHORT).show();
	}
  }
});
```
Or you can default to using a lambda invocation:
```JAVA
parallaxNavigationDrawer.setOnLeftDrawerStateChangedListener(isOpen -> {
  if(isOpen){
	Toast.makeText(cont,"Left Drawer Open!",Toast.LENGTH_SHORT).show();
  } else {
	Toast.makeText(cont, "Left Drawer Close!", Toast.LENGTH_SHORT).show();
  }
});
```

A drawer state listener can also be implemented using a `Function1` receiver function from the `kotlin.jvm.functions` package like so:
```JAVA
Function1<Boolean, Unit> function1 = (bool) -> {
  if (bool) {
	Toast.makeText(cont,"Left Drawer Open!",Toast.LENGTH_SHORT).show();
  } else {
	Toast.makeText(cont, "Left Drawer Close!", Toast.LENGTH_SHORT).show();
  }
  return null;
};

parallaxNavigationDrawer.setOnLeftDrawerStateChangedListener(function1);
```

Configurations:
|Variable |Default |Use |
|:---|:---:|:---:|
|`drawerMode` |DRAWER_MODE_NONE |Sets and retrieves the drawer mode. |
|`duration` |700 |The time it takes for the `ParallaxNavigationDrawer` to complete its open and close animation. |
|`enableSwiping`|true |Enables swiping and dragging on the main content `View` to reveal either drawers. |
|`mainContentPadding` |1/4th of the screen's total width | The padding applied to the main content `View` when the `ParallaxNavigationDrawer` is open. |
|`mainContentShadowAlpha` |0.5F |The alpha value of the shadow applied to the main content `View` when the `ParallaxNavigationDrawer` is open. |
|`mainContentShadowColor` |BLACK |The color of the shadow applied to the main content `View` when the `ParallaxNavigationDrawer` is open.
|`mainContentCloseOnTouch` |false |Enables the close-on-touch feature which closes the `ParallaxNavigationDrawer` when a widget on the main content `View` is touched.
|`parallax` |true |Enables or disables the parallax feature. |

Contributions
-------------
Contributors are welcome!

***NOTE:** This repository is split into two branches:
- [main](https://github.com/IODevBlue/ParallaxNavigationDrawer/tree/main) branch
- [development](https://github.com/IODevBlue/ParallaxNavigationDrawer/tree/development) branch

All developing implementations and proposed changes are pushed to the [development](https://github.com/IODevBlue/ParallaxNavigationDrawer/tree/development) branch 
and finalized updates are pushed to the [main](https://github.com/IODevBlue/ParallaxNavigationDrawer/tree/main) branch.

To note if current developments are being made, there would be more commits in the [development](https://github.com/IODevBlue/ParallaxNavigationDrawer/tree/development) branch than in the [main](https://github.com/IODevBlue/ParallaxNavigationDrawer/tree/main) branch.

Check the [Contributing](https://github.com/IODevBlue/ParallaxNavigationDrawer/blob/development/CONTRIBUTING.md) for more information.

Changelog
---------
* **1.0.0**
    * Initial release

More version history can be gotten from the [Change log](https://github.com/IODevBlue/ParallaxNavigationDrawer/blob/main/CHANGELOG.md) file.

License
=======
```
    Copyright 2022 IO DevBlue

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
```