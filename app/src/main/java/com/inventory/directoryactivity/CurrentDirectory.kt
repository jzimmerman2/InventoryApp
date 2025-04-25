package com.inventory.directoryactivity

class CurrentDirectory {

    private var path: MutableList<String> = mutableListOf()

    fun pathToString() : String {
        if (path.isEmpty()) return "/"

        var pathString = ""
        for (cat in path) {
            pathString += "/"
            pathString += cat
        }
        return pathString
    }

    fun addToPath(newCat: String) {
        if (newCat == "..") {
            popOffPath()
        }
        else {
            path.add(newCat)
        }
    }

    fun popOffPath() {
        path.removeAt(path.lastIndex)
    }
}