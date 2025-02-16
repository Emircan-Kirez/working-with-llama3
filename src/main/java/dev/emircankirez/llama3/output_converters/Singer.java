package dev.emircankirez.llama3.output_converters;

import java.util.List;

public record Singer(
        String name,
        List<Song> songList
) {
}

record Song(
        String title,
        String releaseDate
) {
}
