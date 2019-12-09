name := "scala_with_cats"

version := "0.1"

scalaVersion := "2.12.4"


// https://mvnrepository.com/artifact/org.typelevel/cats-core
libraryDependencies += "org.typelevel" %% "cats-core" % "1.0.0"

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")

// scala编译器相关参数的使用
scalacOptions += "-Ypartial-unification" // 添加这个选项的目的在于在function这样的functor中要使用map这个方法
scalacOptions += "-language:higherKinds"  //启用高阶类型
