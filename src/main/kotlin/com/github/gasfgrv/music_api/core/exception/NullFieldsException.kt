package com.github.gasfgrv.music_api.core.exception

class NullFieldsException:
    RuntimeException("Please enter at least one of the following parameters: 'album', 'produced_by', 'released_in', 'written_by'")
