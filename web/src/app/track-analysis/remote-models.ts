export interface RemoteTrack {
    readonly id: string
    readonly name: string
    readonly disc_number: number
    readonly track_number: number
    readonly duration: number
    readonly popularity: number
    readonly album: RemoteAlbum
    readonly artists: Array<RemoteArtist>
}

export interface RemoteArtist {
    readonly id: string
    readonly name: string
}

export interface RemoteAlbum {
    readonly id: string
    readonly name: string
    readonly release_date: string
    readonly release_date_precision: "day" | "month" | "year"
    readonly images: Array<ImageSpec>
}

export interface ImageSpec {
    /** The source URL of the image. */
    readonly url: string
    /** The image width in pixels. Not present if not known. */
    readonly width?: number
    /** The image height in pixels. Not present if not known. */
    readonly height?: number
}

/**
 * The offset-based paging object is a container for a set of objects.
 * It contains key called `items` (whose value is an array of the requested objects)
 * along with other keys like previous, next and limit that can be useful in future calls.
 */
export interface Paging<T extends object> {
    /** The requested data. */
    readonly items: Array<T>
    /** The maximum number of items in the response. */
    readonly limit: number
    /** URL to the next page of items (null if none). */
    readonly next?: string
    /** The offset of the items returned. */
    readonly offset: number
    /** URL to the previous page of items (null if none). */
    readonly previous?: string
    /** The maximum number of items available. */
    readonly total: number
}

/**
 * Enumeration of musical keys.
 */
export const enum Pitch {
    C = 0,
    C_SHARP = 1,
    D = 2,
    D_SHARP = 3,
    E = 4,
    F = 5,
    F_SHARP = 6,
    G = 7,
    G_SHARP = 8,
    A = 9,
    A_SHARP = 10,
    B = 11
}

/**
 * Enumeration of musical modalities.
 */
export const enum MusicalMode {
    MINOR = 0,
    MAJOR = 1
}

/**
 * Audio features describes physical properties of a musical piece.
 */
export interface AudioFeature {

    /**
     * The Spotify ID for the track.
     */
    readonly id: string

    /**
     * The estimated overall key of the track.
     * Value is `undefined` if the key is unknown.
     */
    readonly key?: Pitch

    /**
     * Mode indicates the modality (major or minor) of a track,
     * the type of scale from which its melodic content is derived.
     */
    readonly mode: MusicalMode

    /**
     * The overall estimated tempo of a track in beats per minute (BPM).
     * In musical terminology, tempo is the speed or pace of a given piece
     * and derives directly from the average beat duration.
     */
    readonly tempo: number

    /**
     * An estimated overall time signature of a track.
     * The time signature (meter) is a notational convention to specify how many beats are in each bar (or measure).
     */
    readonly time_signature: number

    /**
     * The overall loudness of a track in decibels (dB).
     * Loudness values are averaged across the entire track and are useful for comparing relative loudness of tracks.
     * Loudness is the quality of a sound that is the primary psychological correlate of physical strength (amplitude).
     * Values typical range between `-60` and `0` dB.
     */
    readonly loudness: number

    /**
     * A confidence measure from `0.0` to `1.0` of whether the track is acoustic.
     * `1.0` represents high confidence the track is acoustic.
     */
    readonly acousticness: number

    /**
     * Danceability describes how suitable a track is for dancing based on a combination of musical elements
     * including tempo, rhythm stability, beat strength, and overall regularity.
     * A value of `0.0` is least danceable and `1.0` is most danceable.
     */
    readonly danceability: number

    /**
     * Energy is a measure from `0.0` to `1.0` and represents a perceptual measure of intensity and activity.
     * Typically, energetic tracks feel fast, loud, and noisy.
     *
     * For example, death metal has high energy, while a Bach prelude scores low on the scale.
     * Perceptual features contributing to this attribute include dynamic range, perceived loudness, timbre, onset rate,
     * and general entropy.
     */
    readonly energy: number

    /**
     * Predicts whether a track contains no vocals.
     * “Ooh” and “aah” sounds are treated as instrumental in this context.
     * Rap or spoken word tracks are clearly “vocal”.
     * The closer the instrumentalness value is to `1.0`, the greater likelihood the track contains no vocal content.
     * Values above 0.5 are intended to represent instrumental tracks, but confidence is higher as the value approaches `1.0`.
     */
    readonly instrumentalness: number

    /**
     * Detects the presence of an audience in the recording.
     * Higher liveness values represent an increased probability that the track was performed live.
     * A value above `0.8` provides strong likelihood that the track is live.
     */
    readonly liveness: number

    /**
     * Speechiness detects the presence of spoken words in a track.
     * The more exclusively speech-like the recording (e.g. talk show, audio book, poetry),
     * the closer to `1.0` the attribute value.
     * Values above `0.66` describe tracks that are probably made entirely of spoken words.
     * Values between `0.33` and `0.66` describe tracks that may contain both music and speech,
     * either in sections or layered, including such cases as rap music.
     * Values below `0.33` most likely represent music and other non-speech-like tracks.
     */
    readonly speechiness: number

    /**
     * A measure from `0.0` to `1.0` describing the musical positiveness conveyed by a track.
     * Tracks with high valence sound more positive (e.g. happy, cheerful, euphoric),
     * while tracks with low valence sound more negative (e.g. sad, depressed, angry).
     */
    readonly valence: number
}
