package net.yslibrary.monotweety

object AppInitializerProvider {
    fun get(): AppInitializer = AppInitializerImpl()
}
