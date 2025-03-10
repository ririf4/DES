package net.ririfa.des

import net.ririfa.des.config.ConfigManager
import net.ririfa.des.manager.CacheManager
import net.ririfa.des.manager.DataManager

val DP by lazy { DES.get }
val Logger by lazy { DES.logger }
val Server by lazy { DES.get.server }
val DF by lazy { DES.get.dataFile }
val Config by lazy { ConfigManager.config }

typealias DB = DataManager
typealias DC = CacheManager