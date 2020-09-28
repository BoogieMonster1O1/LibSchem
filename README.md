# LibSchem

## Usage
Add the repository
```gradle
 repositories {
     maven {
         url = 'https://dl.bintray.com/boogiemonster1o1/cool-mods/'
     }
 }
 ```

Add the dependency 
```gradle
dependencies {
    modImplementation "io.github.boogiemonster1o1:LibSchem:${project.libschem_version}"

    // Optional: includes LibSchem as a Jar-in-Jar dependency
    include "io.github.boogiemonster1o1:LibSchem:${project.libschem_version}"
}
```


Add the property to `gradle.properties`. Skip this step if you decide to put the version in the dependency. 
```properties
libschem_version = <the latest version>
```
[ ![Download](https://api.bintray.com/packages/boogiemonster1o1/cool-mods/LibSchem/images/download.svg) ](https://bintray.com/boogiemonster1o1/cool-mods/LibSchem/_latestVersion)
Find all versions [here](https://bintray.com/beta/#/boogiemonster1o1/cool-mods/LibSchem?tab=overview)

## License
This mod is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.