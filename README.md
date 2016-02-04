# TourGuide
TourGuide is an Android library. It lets you add pointer, overlay and tooltip easily, guiding users on how to use your app. Refer to the example below(this is a trivial example for demo purpose):

Let's say you have a button on your home screen that you want your users to click on:

![A button](https://raw.githubusercontent.com/worker8/all_my_media_files/695d9a2/2015-07-01_screenshot1.png)

Using TourGuide, the end result will look as below. A pointer, overlay and tooltip are added to the page to clearly notify user to tap on the "Get Started" button. Once user tap on the "Get Started" button, the overlay, pointer and tooltip will disappear.

![TourGuide at work](https://raw.githubusercontent.com/worker8/all_my_media_files/695d9a2/2015-07-01_screenshot.png)

The reason for having Overlay, Pointer and a Tooltip:
- Overlay: Darken other UI elements on the screen, so that user can focus on one single UI element.
- Tooltip: To give a text explanation
- Pointer: An animated clicking gesture to indicate the clickable UI element

# Demo
![Demo](https://raw.githubusercontent.com/worker8/all_my_media_files/25b3208/device-2015-07-01-114155.gif)

# How to setup
Add the below dependencies into your gradle file:

    repositories {
        mavenCentral()
        maven(){
            url "https://oss.sonatype.org/content/repositories/snapshots"
        }
    }
    compile ('com.github.worker8:tourguide:1.0.16-SNAPSHOT@aar'){
        transitive=true
    }
# MinSDK Version
The minimum SDK version required by TourGuide is `API Level 11+ (Android 3.0.x, HONEYCOMB)`.

# How to use
## Basic
Let's say you have a button like this where you want user to click on:

    Button button = (Button)findViewById(R.id.button);

You can add the tutorial pointer on top of it by:

    TourGuide mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(new ToolTip().setTitle("Welcome!").setDescription("Click on Get Started to begin..."))
                .setOverlay(new Overlay())
                .playOn(button);

- `setPointer()` - This describe how the Pointer will look like, refer to [Pointer Customization Guide](#pointer_customization) on how to change the appearance, `null` can be passed in if a Pointer is not wanted.
- `setToolTip` - This describe how the ToolTip will look like, refer to [ToolTip Customization Guide](#tooltip_customization) on how to change the appearance, `null` can be passed in if a ToolTip is not wanted.
- `setOverlay` - This describe how the Overlay will look like, refer to [Overlay Customization Guide](#overlay_customization) on how to change the appearance, `null` can be passed in if an Overlay is not wanted.
- `with` - Use TourGuide.Technique.Click for the moment, this will be removed in the future.
- `mTourGuideHandler` - The return type is a handler to be used for clean up purpose.

When the user is done, you can dismiss the tutorial by calling:

    mTourGuideHandler.cleanUp();

## <a name="tooltip_customization"></a>ToolTip Customization Guide
Tooltip is the box of text that gives further explanation of a UI element. In the basic example above, the ToolTip not customized, so the default style is used. However, you can customize it if you wish to.

        Animation animation = new TranslateAnimation(0f, 0f, 200f, 0f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setInterpolator(new BounceInterpolator());

        ToolTip toolTip = new ToolTip()
                            .setTitle("Next Button")
                            .setDescription("Click on Next button to proceed...")
                            .setTextColor(Color.parseColor("#bdc3c7"))
                            .setBackgroundColor(Color.parseColor("#e74c3c"))
                            .setShadow(true)
                            .setGravity(Gravity.TOP | Gravity.LEFT)
                            .setEnterAnimation(animation);

    TourGuide mTourGuideHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .setPointer(new Pointer())
                .setToolTip(toolTip)
                .setOverlay(new Overlay())
                .playOn(button);

Most of the customization methods/parameters are self-explanatory, except `gravity` that deserves a mention. `gravity` is relative to targetted button where TourGuide `playOn()`. For example `.setGravity(Gravity.TOP | Gravity.LEFT)` will produce the following:

![ToolTip gravity](https://raw.githubusercontent.com/worker8/all_my_media_files/d0b17ba/2015-07-01_screenshot2.png)

## <a name="pointer_customization"></a>Pointer Customization Guide
Pointer is the round button that is animating to indicate the clickable UI element. The default color is white and the default gravity is center. You can customize it by:

    new Pointer().setColor(Color.RED).setGravity(Gravity.BOTTOM|Gravity.RIGHT);

This is a comparison with and without the customization:

![Pointer Customization](https://raw.githubusercontent.com/worker8/all_my_media_files/64b8a3c/2015-07-01_screenshot5.png)

## <a name="overlay_customization"></a>Overlay Customization Guide
Overlay is the semi-transparent background that is used to cover up other UI elements so that users can take focus on what to click on. The color and shape can be customized by:

     Overlay overlay = new Overlay()
                .setBackgroundColor(Color.parseColor("#AAFF0000"))
                .disableClick(true)
                .setStyle(Overlay.Style.Rectangle);

- `disableClick(true)` will make elements covered by the overlay to become unclickable. Refer to Overlay Customization Activity in the example.
- `.setStyle()` Currently only 2 styles are available: `Overlay.Style.Rectangle` and `Overlay.Style.Circle`

# Running TourGuide in Sequence
Running TourGuide in sequence is a very common use case where you want to show a few buttons in a row instead of just one. Running in sequence can be subdivided into 2 use cases:

- Case 1: When you want user to click on the button itself to proceed to next TourGuide
- Refer to [ManualSequenceActivity.java in the demo](https://github.com/worker8/TourGuide/blob/master/app/src/main/java/tourguide/tourguidedemo/ManualSequenceActivity.java)

![](https://github.com/worker8/all_my_media_files/raw/master/button_itself.gif)

- Case 2: When you don't want user to actually click on the button itself, but the Overlay to proceed to next TourGuide
- Refer to [OverlaySequenceTourActivity.java in the demo](https://github.com/worker8/TourGuide/blob/master/app/src/main/java/tourguide/tourguidedemo/OverlaySequenceTourActivity.java)

![](https://github.com/worker8/all_my_media_files/raw/master/click_overlay.gif)


# Source code of Example
Refer to this repo!

# Demo App
[Playstore Link](https://play.google.com/store/apps/details?id=tourguide.tourguide)

# Roadmap
[Refer to The Roadmap for tentative plans of TourGuide](https://github.com/worker8/TourGuide/wiki/Roadmap)

# Limitations
[Features that are commonly asked for, but is still not working goes under Limitations](https://github.com/worker8/TourGuide/wiki/Limitations)

# Contributing
You are very welcome to contribute to this project!

Before sending pull request, have a look at this: [Contributing Guidelines](https://github.com/worker8/TourGuide/wiki/Contributing-Guidelines)

Refer to [The Roadmap](https://github.com/worker8/TourGuide/wiki/Roadmap) if you're interested in developing a new feature or write some tests for TourGuide.

# License

    The MIT License (MIT)
    
    Copyright (c) 2015 Tan Jun Rong
    
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

