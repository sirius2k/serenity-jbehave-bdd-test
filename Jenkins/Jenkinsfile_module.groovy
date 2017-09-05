def printCausesRecursively(cause) {
    if (cause.class.toString().contains("UpstreamCause")) {
        println "This job was caused by " + cause.toString()
        for (upCause in cause.upstreamCauses) {
            printCausesRecursively(upCause)
        }
    } else {
        println "Root cause : " + cause.toString()
    }
}

return this
