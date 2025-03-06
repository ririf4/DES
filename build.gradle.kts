import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
	alias(libs.plugins.kotlin)
	alias(libs.plugins.runPaper)
	alias(libs.plugins.paperYAML)
	alias(libs.plugins.paperWeight)
}

group = "net.ririfa"
version = "0.1.0"
description = "A original plugin for DES server."

repositories {
	mavenCentral()
	maven("https://repo.papermc.io/repository/maven-public/")
	maven("https://jitpack.io")
}

dependencies {
	libs.apply {
		// > == Paper == < \\
		paperweight.paperDevBundle(versions.paper.get())
		implementation(paper.api)
		// > == Exposed == < \\
		library(exposed.core)
		library(exposed.jdbc)
		library(exposed.dao)
		library(exposed.json)
		library(exposed.kotlin.datetime)
		library(postgre)
		// > == Other == < \\
		library(igf)
		compileOnly(vault)
	}
}

paper {
	name = "${project.name}-${project.version}"
	description = project.description.toString()
	version = project.version.toString()
	apiVersion = "1.21"
	main = "net.ririfa.des.DES"
	generateLibrariesJson = true
	foliaSupported = false
	contributors = listOf("RiriFa")
	load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD

	serverDependencies {
		register("Kotlin") {
			required = true
			load = PaperPluginDescription.RelativeLoadOrder.BEFORE
		}
	}
}