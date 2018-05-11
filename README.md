![asdf](https://api.travis-ci.org/worker8/TourGuide.svg?branch=master) ![](https://camo.githubusercontent.com/cf76db379873b010c163f9cf1b5de4f5730b5a67/68747470733a2f2f6261646765732e66726170736f66742e636f6d2f6f732f6d69742f6d69742e7376673f763d313032)

# TourGuide
TourGuide is an Android library. It lets you add pointer, overlay, and tooltip easily, guiding users on how to use your app. Refer to the example below (this is a trivial example for demo purpose):

Let's say you have a button on your home screen that you want your users to click on:

![A button](https://raw.githubusercontent.com/worker8/all_my_media_files/695d9a2/2015-07-01_screenshot1.png)

Using TourGuide, the end result will look as below. A **pointer**, **overlay** and **tooltip** are added to the page to clearly notify the user to tap on the "Get Started" button. Once user tap on the "Get Started" button, the overlay, pointer, and tooltip will disappear.

![TourGuide at work](https://raw.githubusercontent.com/worker8/all_my_media_files/695d9a2/2015-07-01_screenshot.png)

Below is the code for achieving the above :point_up_2:
```kotlin
val tourGuide: TourGuide = TourGuide.create(activity) {
        pointer {}

        toolTip {
            title { "Welcome!" }
            description { "Click on Get Started to begin..." }
        }

        overlay {
            backgroundColor { Color.parseColor("#66FF0000") }
        }
    }.playOn(button)
```

Explanation of each components:
- **Overlay**: Darken other UI elements on the screen, so that user can focus on one single UI element.
- **Tooltip**: To give a text explanation
- **Pointer**: An animated clicking gesture to indicate the clickable UI element
- **tourGuide**: The return type will be a handler to be used for cleaning up. To dismiss TourGuide, just call `tourGuide.cleanUP()`

# Demo
<details>
<img src="https://raw.githubusercontent.com/worker8/all_my_media_files/25b3208/device-2015-07-01-114155.gif" width="200px" />
</details>

# How to setup
Add the below dependencies into your app level `build.gradle` file:

```json
repositories {
    mavenCentral()
    maven() {
        url "https://oss.sonatype.org/content/repositories/snapshots"
    }
}
dependencies {
    ...
    compile ('com.github.worker8:tourguide:1.0.19-SNAPSHOT@aar') {
        transitive=true
    }
}
```

# MinSDK Version
The minimum SDK version required by TourGuide is `API Level 11+ (Android 3.0.x, HONEYCOMB)`.

# Customization

## ToolTip Customization
`Tooltip` is the box of text that gives further explanation of a UI element. In the basic example above, the `ToolTip` not customized, so the default style is used. However, you can customize it if you wish to.

```kotlin
val tourGuide = TourGuide.create(this) {
            toolTip {
                title { "Next Button" }
                description { "Click on Next button to proceed..." }
                textColor { Color.parseColor("#bdc3c7") }
                backgroundColor { Color.parseColor("#e74c3c") }
                shadow { true }
                gravity { Gravity.TOP or Gravity.LEFT }
                enterAnimation {
                    TranslateAnimation(0f, 0f, 200f, 0f).apply {
                        duration = 1000
                        fillAfter = true
                        interpolator = BounceInterpolator()
                    }
                }
            }
        }.playOn(button)
```

Most of the customization methods/parameters are self-explanatory, except `gravity` that deserves a mention. `gravity` is relative to targeted button where TourGuide `playOn()`. For example `Gravity.TOP or Gravity.LEFT` will produce the following:

![ToolTip gravity](https://raw.githubusercontent.com/worker8/all_my_media_files/d0b17ba/2015-07-01_screenshot2.png)

## Pointer Customization
Pointer is the round button that is animating to indicate the clickable UI element. The default color is white and the default gravity is center. You can customize it by:
```java
new Pointer().setColor(Color.RED).setGravity(Gravity.BOTTOM|Gravity.RIGHT);
```
This is a comparison with and without the customization:

![Pointer Customization](https://raw.githubusercontent.com/worker8/all_my_media_files/64b8a3c/2015-07-01_screenshot5.png)

## Overlay Customization
Overlay is the semi-transparent background that is used to cover up other UI elements so that users can take focus on what to click on. The color and shape can be customized by:
```java
Overlay overlay = new Overlay()
            .setBackgroundColor(Color.parseColor("#AAFF0000"))
            .disableClick(true)
            .setStyle(Overlay.Style.Rectangle);
```
- `disableClick(true)` will make elements covered by the overlay to become unclickable. Refer to Overlay Customization Activity in the example.
- `.setStyle()` Currently only 2 styles are available: `Overlay.Style.Rectangle` and `Overlay.Style.Circle`

# Running TourGuide in Sequence
Running TourGuide in sequence is a very common use case where you want to show a few buttons in a row instead of just one:

When you want user to click on the button itself to proceed to next TourGuide
- Refer to [ManualSequenceActivity.java in the demo](https://github.com/worker8/TourGuide/blob/master/app/src/main/java/tourguide/tourguidedemo/ManualSequenceActivity.kt)

![](https://github.com/worker8/all_my_media_files/raw/master/button_itself.gif)

# Source code of Example
Refer to this repo!

# Demo App
[Playstore Link](https://play.google.com/store/apps/details?id=tourguide.tourguide)

# Roadmap
[Refer to The Roadmap for tentative plans of TourGuide](https://github.com/worker8/TourGuide/wiki/Roadmap)

# Limitations
[Features that are commonly asked for, but is still not working goes under Limitations](https://github.com/worker8/TourGuide/wiki/Limitations)

# Contributing
I only maintain this project when I have some free time, so merging PR might be delayed when I'm busy :bow:.

# License
<details>
<summary>
click here to show License
</summary>
    The MIT License (MIT)

    Copyright (c) 2016 Tan Jun Rong

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
</details>
