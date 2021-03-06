import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar;
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	java
	maven
	kotlin("jvm") version "1.4.21"
	id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.ruthlessjailer.plugin.onechunk"
version = "1.0.0"

val finalName = "${project.name}.jar"
val copyDir = "D:/Gaming/Minecraft/Server/paper 1.16/plugins"
val mainClass = "com.ruthlessjailer.plugin.onechunk.OneChunk"
val javaVersion = "11"

repositories {
	mavenLocal()
	mavenCentral()
	jcenter()
	maven {
		name = "spigotmc-repo"
		url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

		content {
			includeGroup("org.spigotmc")
		}
	}

	maven {
		name = "codemc-repo"
		url = uri("https://repo.codemc.org/repository/maven-public/")
	}

	maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
	maven { url = uri("https://oss.sonatype.org/content/repositories/central") }
	maven { url = uri("https://jitpack.io") }

}

dependencies {
	api(group = "com.ruthlessjailer.api.theseus", name = "Theseus", version = "1.2.4")
	compileOnly("org.spigotmc:spigot-api:1.16.4-R0.1-SNAPSHOT")
	compileOnly("org.projectlombok:lombok:1.18.8")
	compileOnly("org.apache.commons:commons-lang3:3.11")
	compileOnly("commons-io:commons-io:2.8.0")
	annotationProcessor("org.projectlombok:lombok:1.18.8")
	implementation(kotlin("stdlib-jdk8"))
//	implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.21")
	api("com.nesaak:NoReflection:0.1-SNAPSHOT")

}

tasks {
	named<ShadowJar>("shadowJar") {
		archiveFileName.set(finalName)
		minimize()
		mergeServiceFiles()
		manifest {
			attributes(mapOf("Main-Class" to mainClass))
		}
	}

	register<Copy>("copyReport") {
		from(file("$buildDir/libs/$finalName"))
		into(file(copyDir))
	}

	build {
		dependsOn(getByName("shadowJar"))
	}

	processResources {
		expand("version" to project.version, "name" to project.name, "mainClass" to mainClass)
	}

	jar {
		archiveFileName.set("${project.name}-${project.version}-unshaded.jar")
	}
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
	jvmTarget = javaVersion
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
	jvmTarget = javaVersion
}