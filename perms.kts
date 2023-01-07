
LocationPermissionTracker.observeStatus { status ->
    when (status) {
        NOT_REQUESTED -> {
            ask()
        }
        NEEDS_EXPLANATION -> {
            show()
        }
        PERMANENTLY_DENIED -> {
            leadToSettings()
        }
        GRANTED -> {
            doStuff()
        }
    }
}

