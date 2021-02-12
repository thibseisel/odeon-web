import { MusicalMode, Pitch } from './remote-models'
import { TonePipe } from './tone.pipe'

describe("TonePipe", () => {
  const pipe = new TonePipe()

  it("create an instance", () => {
    expect(pipe).toBeTruthy()
  })

  it("returns '?' for null and undefined", () => {
    expect(pipe.transform(null)).toBe("?")
    expect(pipe.transform(undefined)).toBe("?")
  })

  it("converts a pitch key to its enharmonic standard pitch class notation", () => {
    expect(pipe.transform(Pitch.C)).toBe("C")
    expect(pipe.transform(Pitch.D_FLAT)).toBe("Db")
    expect(pipe.transform(Pitch.D)).toBe("D")
    expect(pipe.transform(Pitch.E_FLAT)).toBe("Eb")
    expect(pipe.transform(Pitch.E)).toBe("E")
    expect(pipe.transform(Pitch.F)).toBe("F")
    expect(pipe.transform(Pitch.F_SHARP)).toBe("F#")
    expect(pipe.transform(Pitch.G)).toBe("G")
    expect(pipe.transform(Pitch.A_FLAT)).toBe("Ab")
    expect(pipe.transform(Pitch.A)).toBe("A")
    expect(pipe.transform(Pitch.B_FLAT)).toBe("Bb")
    expect(pipe.transform(Pitch.B)).toBe("B")
  })

  it("appends representation of modality when specified", () => {
    expect(pipe.transform(Pitch.C, MusicalMode.MAJOR)).toBe("CM")
    expect(pipe.transform(Pitch.C, MusicalMode.MINOR)).toBe("Cm")
  })
})
