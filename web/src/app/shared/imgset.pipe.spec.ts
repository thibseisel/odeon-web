import { ImgSetPipe } from "./imgset.pipe"

describe("The Srcset pipe", () => {
  // Because it's a pure pipe we can declare it only once.
  const pipe = new ImgSetPipe()

  it("should transform empty image specs into undefined", () => {
    const result = pipe.transform([])
    expect(result).toBeUndefined()
  })

  it("should transform single ImageSpec into single source", () => {
    const images = [{
      url: "https://domain.xyz/image.png",
      width: 320,
      height: 320
    }]

    expect(pipe.transform(images)).toBe("https://domain.xyz/image.png 320w")
  })

  it("should transform multiple ImageSpec into multiple sources", () => {
    const images = [{
      url: "https://domain.xyz/other.png",
      width: 640,
      height: 640
    }, {
      url: "https://domain.xyz/image.png",
      width: 320,
      height: 320
    }]

    expect(pipe.transform(images)).toBe("https://domain.xyz/image.png 320w,https://domain.xyz/other.png 640w")
  })

  it("should workaround ImageSpec with unknown width", () => {
    const images = [{
      url: "https://domain.xyz/image.png"
    }]

    expect(pipe.transform(images)).toBe("https://domain.xyz/image.png 1x")
  })
})
