# [Atum 2]

Download on [CurseForge](https://minecraft.curseforge.com/projects/atum)

How to get Atum 2 through maven
---
Add to your build.gradle:
```gradle
repositories {
  maven {
    // url of the maven that hosts Atum 2 files
    url "http://girafi.dk/maven/"
  }
}

dependencies {
  // compile against Atum 2
  deobfCompile "com.teammetallurgy.atum:atum2_${mc_version}:${mc_version}-${atum2_version}"
}
```

`${mc_version}` & `${atum2_version}` can be found [here](http://girafi.dk/maven/com/teammetallurgy/atum/), check the file name of the version you want.