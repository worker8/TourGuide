# TourGuide
TourGuide is an Android library. It lets you add a pointer easily to teach users how to use your app.

# Demo
![Demo](https://raw.githubusercontent.com/worker8/all_my_media_files/master/device-2015-06-17-180522.gif)


# How to setup
Add the below dependencies into your gradle file:

    compile 'com.github.worker8:tourguide:1.0.1-SNAPSHOT@aar'
    compile 'com.melnykov:floatingactionbutton:1.3.0'

`com.melnykov:floatingactionbutton` is needed for the use of the FloatingActionButton.

# How to use
## Basic
Let's say you have a button like this where you want user to click on:

    Button button = (Button)findViewById(R.id.button);

You can add the tutorial pointer on top of it by:

    AnimateTutorial mTutorialHandler = AnimateTutorial.init(this).with(AnimateTutorial.Technique.Click)
         .duration(700)
         .disableClick(true)
         .gravity(Gravity.CENTER)
         .motionType(AnimateTutorial.MotionType.ClickOnly)
         .title("Welcome!")
         .description("Click on the start button to begin")
         .playOn(button);

- `duration` - this is not working yet (to be completed)
- `disableClick` - boolean (true - if you want to restrict user to only click on the button you ask them to; false - there is not restriction)
- `gravity` - this decides where the tutorial pointer will be aligned to (Gravity.CENTER, GRAVITY.TOP, GRAVITY.BOTTOM, etc)
- `motionType` - currently onlyAnimateTutorial.MotionType.ClickOnly is supported (future: swipe, pinch zoom, double tapping)
- `title` & `description` - String that will appear on the bottom to teach users how to use (both can be blank)
- `playOn` - argument should be the button you want user to click on
- `mTutorialHandler` - this variable is a handler of the tutorial views, use it for cleaning up

When the user is done, you can dismiss the tutorial by:

    mTutorialHandler.cleanUp();

## Customize tooltip
Tooltip is the box of text that appear to teach the users. In the above example, it's not customized, so the default style is used. You can customize it if you want to:

        Animation animation = new TranslateAnimation(0f, 0f, 200f, 0f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setInterpolator(new BounceInterpolator());

        ToolTip toolTip = new ToolTip().
                            title("Welcome!").
                            description("Click on Get Started to begin...").
                            backgroundColor(Color.parseColor("#27ae60")).
                            textColor(Color.parseColor("#FFFFFF")).
                            gravity(Gravity.CENTER).
                            enterAnimation(animation);

        mTutorialHandler = TourGuide.init(this).with(TourGuide.Technique.Click)
                .duration(700)
                .disableClick(disable_click)
                .gravity(Gravity.CENTER)
                .motionType(TourGuide.MotionType.ClickOnly)
                .toolTip(toolTip)
                .playOn(button);
# Example
Refer to this repo!

# Demo App
Coming soon..! I'll upload to playstore once I feel comfortable.

# License

    The MIT License (MIT)
    
    Copyright (c) [2015] [Tan Jun Rong]
    
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

